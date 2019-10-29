/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.apsadmin.portal;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.*;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.portal.helper.IPageActionReferencesHelper;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseActionHelper;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.apsadmin.portal.PageActionConstants;
import org.entando.entando.apsadmin.portal.rs.model.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import java.util.*;

/**
 * Main action for pages handling
 *
 * @author E.Santoboni
 */
public class PageAction extends AbstractPortalAction implements ServletResponseAware {

    private static final Logger logger = LoggerFactory.getLogger(PageAction.class);

    private static final int PAGE_TITLE_MAX_LENGTH = 70;
    
    private String pageCode;
    private String parentPageCode;
    private String copyPageCode;
    private String group;
    private boolean groupSelectLock;
    private Set<String> extraGroups = new TreeSet<>();
    private String model;
    private boolean defaultShowlet = false;
    private ApsProperties titles = new ApsProperties();
    private boolean showable = false;
    private boolean useExtraTitles;
    private int strutsAction;

    private String mimeType;
    private String charset;

    private String nodeToBeDelete;

    private String extraGroupNameToRemove;

    private IPage pageToShow;

    private Map references;

    private String _allowedMimeTypesCSV;
    private String _allowedCharsetsCSV;

    private String targetNode;
    private Set<String> treeNodesToOpen = new HashSet<>();
    private String treeNodeActionMarkerCode;

    private HttpServletResponse response;

    private IPageModelManager pageModelManager;
    private IPageActionHelper pageActionHelper;
    private IPageActionReferencesHelper pageActionReferencesHelper;

    @Override
    public void validate() {
        super.validate();
        if (this.getStrutsAction() == ApsAdminSystemConstants.ADD || this.getStrutsAction() == ApsAdminSystemConstants.PASTE) {
            this.checkParentNode(this.getParentPageCode());
        }
        this.checkCode();
        this.checkTitles();
        if (this.getAuthorizationManager().isAuthOnGroup(this.getCurrentUser(), Group.ADMINS_GROUP_NAME)) {
            try {
                IPage currentPage = (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) ? this.getUpdatedPage() : this.buildNewPage();
                boolean check = this.getPageActionHelper().checkPageGroup(currentPage, this);
                if (!check) {
                    logger.error("Error checking page group");
                    this.addActionError(this.getText("error.page.scanGroup"));
                }
            } catch (Exception e) {
                logger.error("Error validation groups", e);
                throw new RuntimeException("Error validation groups", e);
            }
        }
    }

    protected String checkParentNode(String selectedNode) {
        if (null == selectedNode || selectedNode.trim().length() == 0) {
            this.addFieldError("parentPageCode", this.getText("error.parentPage.noSelection"));
            return "pageTree";
        }
        if (VIRTUAL_ROOT_CODE.equals(selectedNode)) {
            this.addFieldError("parentPageCode", this.getText("error.parentPage.virtualRootSelected"));
            return "pageTree";
        }
        IPage selectedPage = this.getPage(selectedNode);
        if (null == selectedPage) {
            this.addFieldError("parentPageCode", this.getText("error.parentPage.selectedPage.null"));
            return "pageTree";
        }
        if (!this.isUserAllowed(selectedPage)) {
            this.addFieldError("parentPageCode", this.getText("error.parentPage.userNotAllowed"));
            return "pageTree";
        }
        return null;
    }

    protected void checkTitles() {
        this.updateTitles();
        Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
        while (langsIter.hasNext()) {
            Lang lang = langsIter.next();
            String title = (String) this.getTitles().get(lang.getCode());
            String titleKey = "lang" + lang.getCode();
            if (null == title || title.trim().length() == 0) {
                String[] args = {lang.getDescr()};
                this.addFieldError(titleKey, this.getText("error.page.insertTitle", args));
            } else if (title.length() > PAGE_TITLE_MAX_LENGTH) {
                String[] args = {lang.getCode(), String.valueOf(PAGE_TITLE_MAX_LENGTH)};
                this.addFieldError(titleKey, this.getText("error.page.wrongTitleMaxLength", args));
            }
        }
    }

    protected void updateTitles() {
        Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
        while (langsIter.hasNext()) {
            Lang lang = (Lang) langsIter.next();
            String titleKey = "lang" + lang.getCode();
            String title = this.getRequest().getParameter(titleKey);
            if (null != title) {
                this.getTitles().put(lang.getCode(), title.trim());
            }
        }
    }

    protected void checkCode() {
        String code = this.getPageCode();
        if ((this.getStrutsAction() == ApsAdminSystemConstants.ADD || this.getStrutsAction() == ApsAdminSystemConstants.PASTE)
                && null != code && code.trim().length() > 0) {
            String currectCode = BaseActionHelper.purgeString(code.trim());
            if (currectCode.length() > 0 && null != this.getPage(currectCode)) {
                String[] args = {currectCode};
                this.addFieldError("pageCode", this.getText("error.page.duplicateCode", args));
            }
            this.setPageCode(currectCode);
        }
    }

    public String newPage() {
        String selectedNode = this.getParentPageCode();
        try {
            if (StringUtils.isEmpty(selectedNode)) {
                selectedNode = this.getSelectedNode();
            }
            IPage parentPage = null;
            if (StringUtils.isNotEmpty(selectedNode)) {
                String check = this.checkSelectedNode(selectedNode);
                if (null != check) {
                    return check;
                }
                parentPage = this.getPage(selectedNode);
            }
            this.valueFormForNew(parentPage);
            this.setCharset("utf-8");
            this.setMimeType("text/html");
        } catch (Throwable t) {
            logger.error("error in newPage", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String settingsPage() {
        return SUCCESS;
    }

    protected void valueFormForNew(IPage parentPage) {
        this.setStrutsAction(ApsAdminSystemConstants.ADD);
        if (parentPage != null) {
            this.setParentPageCode(parentPage.getCode());
            String groupName = parentPage.getGroup();
            this.setGroup(groupName);
        }
        this.setGroupSelectLock(false);
        this.setDefaultShowlet(true);
        this.setShowable(true);
    }

    public String edit() {
        String selectedPageCode = this.getSelectedNode();
        try {
            String check = this.checkSelectedNode(selectedPageCode);
            if (null != check) {
                return check;
            }
            IPage page = this.getPage(selectedPageCode);
            this.valueFormForEdit(page);
        } catch (Throwable t) {
            logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String entryEdit() {
        try {
            this.updateTitles();
        } catch (Throwable t) {
            logger.error("error in entry edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String joinExtraGroup() {
        try {
            this.updateTitles();
            String[] groupNameList = super.getParameters().get("extraGroupNameToAdd");
            if (groupNameList != null) {
                for (String groupName : groupNameList) {
                    this.getExtraGroups().add(groupName);
                }
            }
        } catch (Throwable t) {
            logger.error("error in joinExtraGroup", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String removeExtraGroup() {
        try {
            this.updateTitles();
            this.getExtraGroups().remove(this.getExtraGroupNameToRemove());
        } catch (Throwable t) {
            logger.error("error in removeExtraGroup", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String showDetail() {
        String selectedPageCode = this.getSelectedNode();
        try {
            String check = this.checkSelectedNode(selectedPageCode);
            if (null != check) {
                return check;
            }
            IPage page = this.getPage(selectedPageCode);
            Map extractedReferences = this.getPageActionHelper().getReferencingObjects(page, this.getRequest());
            if (null != extractedReferences) {
                this.setReferences(extractedReferences);
            }
            this.setPageToShow(page);
        } catch (Throwable t) {
            logger.error("error in showDetail", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected void valueFormForEdit(IPage pageToEdit) throws CloneNotSupportedException {
        this.setStrutsAction(ApsAdminSystemConstants.EDIT);
        this.setParentPageCode(pageToEdit.getParentCode());
        this.setPageCode(pageToEdit.getCode());
        this.setGroup(pageToEdit.getGroup());
        PageMetadata draftMetadata = pageToEdit.getMetadata();
        this.copyMetadataToForm(draftMetadata);
        boolean isAdmin = this.getAuthorizationManager().isAuthOnGroup(this.getCurrentUser(), Group.ADMINS_GROUP_NAME);
        IPage parentPage = this.getPageManager().getDraftPage(pageToEdit.getParentCode());
        boolean isParentFree = (pageToEdit.isRoot() || parentPage.getGroup().equals(Group.FREE_GROUP_NAME));
        this.setGroupSelectLock(!(isAdmin && isParentFree));
    }

    public String copy() {
        String selectedNode = this.getSelectedNode();
        try {
            String check = this.checkSelectedNode(selectedNode);
            if (null != check) {
                return check;
            }
            IPage pageToCopy = this.getPage(selectedNode);
            IPage publicToCopy = this.getOnlinePage(selectedNode);
            this.setStrutsAction(ApsAdminSystemConstants.PASTE);
            this.setCopyPageCode(selectedNode);
            this.setGroup(pageToCopy.getGroup());
            PageMetadata metadata = (null != publicToCopy) ? publicToCopy.getMetadata() : pageToCopy.getMetadata();
            this.copyMetadataToForm(metadata);
            this.getTitles().clear();
            this.setParentPageCode(selectedNode);
        } catch (Throwable t) {
            logger.error("error in paste", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    private void copyMetadataToForm(PageMetadata metadata) throws CloneNotSupportedException {
        if (metadata != null) {
            metadata = metadata.clone();
            this.setTitles(metadata.getTitles());
            this.setExtraGroups(metadata.getExtraGroups());
            this.setModel(metadata.getModel().getCode());
            this.setShowable(metadata.isShowable());
            this.setUseExtraTitles(metadata.isUseExtraTitles());
            if (StringUtils.isNotBlank(metadata.getCharset())) {
                this.setCharset(metadata.getCharset());
            } else {
                this.setCharset("utf-8");
            }
            if (StringUtils.isNotBlank(metadata.getMimeType())) {
                this.setMimeType(metadata.getMimeType());
            } else {
                this.setMimeType("text/html");
            }
        }
    }

    public String save() {
        IPage page = null;
        try {
            if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
                page = this.getUpdatedPage();
                this.getPageManager().updatePage(page);
                this.addActionMessage(this.getText("message.page.info.updated", new String[]{this.getTitle(page.getCode(), page
                    .getTitles())}));
                logger.debug("Updating page " + page.getCode());
            } else {
                page = this.buildNewPage();
                this.getPageManager().addPage(page);
                this.addActionMessage(this.getText("message.page.info.added", new String[]{this.getTitle(page.getCode(), page
                    .getTitles())}));
                logger.debug("Adding new page");
            }
            this.addActivityStreamInfo(page, this.getStrutsAction(), true);
        } catch (Throwable t) {
            logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String saveAndConfigure() {
        IPage page = null;
        try {
            if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
                page = this.getUpdatedPage();
                this.getPageManager().updatePage(page);
                this.addActionMessage(this.getText("message.page.info.updated", new String[]{this.getTitle(page.getCode(), page
                    .getTitles())}));
                logger.debug("Updating page " + page.getCode());
            } else {
                page = this.buildNewPage();
                this.getPageManager().addPage(page);
                this.addActionMessage(this.getText("message.page.info.added", new String[]{this.getTitle(page.getCode(), page
                    .getTitles())}));
                logger.debug("Adding new page");
            }
            this.addActivityStreamInfo(page, this.getStrutsAction(), true);
            this.setPageCode(page.getCode());
            this.setSelectedNode(page.getCode());
        } catch (Throwable t) {
            logger.error("error in save and configure", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String pageDetails() {
        return SUCCESS;
    }

    public PageResponse getPageDetailResponse() {
        PageResponse pageResponse = null;
        try {
            IPage draftPage = null;
            IPage onlinePage = null;
            String check = this.checkSelectedNode(this.getPageCode());
            if (null == check) {
                draftPage = this.getPage(this.getPageCode());
                onlinePage = this.getOnlinePage(this.getPageCode());
            }
            pageResponse = new PageResponse(this, draftPage, onlinePage);
        } catch (Throwable t) {
            logger.error("error in getPageJsonResponse", t);
            this.getServletResponse().setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
            return null;
        }
        return pageResponse;
    }

    protected IPage buildNewPage() throws ApsSystemException {
        Page page = new Page();
        try {
            page.setParentCode(this.getParentPageCode());
            if (this.getStrutsAction() == ApsAdminSystemConstants.PASTE) {
                IPage copyPage = this.getPage(this.getCopyPageCode());
                IPage publicPage = this.getOnlinePage(this.getCopyPageCode());
                page.setWidgets(null != publicPage ? publicPage.getWidgets() : copyPage.getWidgets());
                PageMetadata metadata = (null != publicPage) ? publicPage.getMetadata() : copyPage.getMetadata();
                if (metadata != null) {
                    metadata = metadata.clone();
                    metadata.setTitles(this.getTitles());
                    page.setMetadata(metadata);
                }
                page.setGroup(copyPage.getGroup());
            } else {
                page.setGroup(this.getGroup());
                PageMetadata metadata = page.getMetadata();
                this.valueMetadataFromForm(metadata);
                if (null != metadata.getModel()) {
                    page.setWidgets(new Widget[metadata.getModel().getFrames().length]);
                }
            }
            // ricava il codice
            page.setCode(this.buildNewPageCode(page.getMetadata()));
        } catch (Throwable t) {
            logger.error("Error building new page", t);
            throw new ApsSystemException("Error building new page", t);
        }
        return page;
    }

    private String buildNewPageCode(PageMetadata metadata) throws ApsSystemException {
        String newPageCode = this.getPageCode();
        if (StringUtils.isNotBlank(newPageCode)) {
            newPageCode = newPageCode.trim();
        } else {
            String defaultLangCode = this.getLangManager().getDefaultLang().getCode();
            newPageCode = this.getPageActionHelper().buildCode(metadata.getTitle(defaultLangCode), "page", 25);
        }
        return newPageCode;
    }

    protected IPage getUpdatedPage() throws ApsSystemException {
        Page page = null;
        try {
            page = (Page) this.getPage(this.getPageCode());
            page.setGroup(this.getGroup());
            PageMetadata metadata = page.getMetadata();
            if (metadata == null) {
                metadata = new PageMetadata();
                page.setMetadata(metadata);
            }
            PageModel oldModel = metadata.getModel();
            this.valueMetadataFromForm(metadata);
            if (oldModel == null || !oldModel.getCode().equals(this.getModel())) {
                // The model is changed, so I drop all the previous widgets
                page.setWidgets(new Widget[metadata.getModel().getFrames().length]);
            }
        } catch (Throwable t) {
            logger.error("Error updating page", t);
            throw new ApsSystemException("Error updating page", t);
        }
        return page;
    }

    private void valueMetadataFromForm(PageMetadata metadata) {
        if (metadata.getModel() == null || !metadata.getModel().getCode().equals(this.getModel())) {
            // Ho cambiato modello e allora cancello tutte le showlets
            // Precedenti
            PageModel model = this.getPageModelManager().getPageModel(this.getModel());
            metadata.setModel(model);
        }
        metadata.setShowable(this.isShowable());
        metadata.setUseExtraTitles(this.isUseExtraTitles());
        metadata.setTitles(this.getTitles());
        metadata.setExtraGroups(this.getExtraGroups());
        String charset = this.getCharset();
        metadata.setCharset(StringUtils.isNotBlank(charset) ? charset : null);
        String mimetype = this.getMimeType();
        metadata.setMimeType(StringUtils.isNotBlank(mimetype) ? mimetype : null);
        metadata.setUpdatedAt(new Date());
    }

    public String setDefaultWidgets() {
        Page page = null;
        try {
            page = (Page) this.getPage(this.getPageCode());
            PageModel model = page.getMetadata().getModel();
            Widget[] defaultWidgets = model.getDefaultWidget();
            if (null == defaultWidgets) {
                logger.info("No default Widget found for pagemodel '{}' of page '{}'", model.getCode(), page.getCode());
                return SUCCESS;
            }
            Widget[] widgets = new Widget[defaultWidgets.length];
            for (int i = 0; i < defaultWidgets.length; i++) {
                Widget defaultWidget = defaultWidgets[i];
                if (null != defaultWidget) {
                    if (null == defaultWidget.getType()) {
                        logger.info("Widget Type null when adding defaulWidget (of pagemodel '{}') on frame '{}' of page '{}'", model
                                .getCode(), i, page.getCode());
                        continue;
                    }
                    widgets[i] = defaultWidget;
                }
            }
            page.setWidgets(widgets);
            this.getPageManager().updatePage(page);
        } catch (Throwable t) {
            logger.error("Error setting default widget to page {}", this.getPageCode(), t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String checkSetOffline() {
        String selectedPageCode = this.getPageCode();
        try {
            if (StringUtils.isEmpty(selectedPageCode)) {
                selectedPageCode = this.getSelectedNode();
                this.setPageCode(selectedPageCode);
            }
            String check = this.checkSetOffline(selectedPageCode);
            if (null != check) {
                return check;
            }
            IPage currentPage = this.getPage(selectedPageCode);
            this.setSelectedNode(currentPage.getCode());
        } catch (Throwable t) {
            logger.error("error checking before putting page {} offline", selectedPageCode, t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String doSetOffline() {
        String selectedPageCode = this.getPageCode();
        try {
            if (StringUtils.isEmpty(selectedPageCode)) {
                selectedPageCode = this.getSelectedNode();
                this.setPageCode(selectedPageCode);
            }
            IPageManager pageManager = this.getPageManager();
            String check = this.checkSetOffline(selectedPageCode);
            if (null != check) {
                return check;
            }
            pageManager.setPageOffline(selectedPageCode);
            IPage page = this.getPage(selectedPageCode);
            this.addActionMessage(this.getText("message.page.set.offline", new String[]{this.getTitle(page.getCode(), page
                .getTitles())}));
            // TODO Define a new strutsAction to map "offline" operation
            this.addActivityStreamInfo(page, PageActionConstants.UNPUBLISH, true);
        } catch (Throwable t) {
            logger.error("error setting page {} offline", selectedPageCode, t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String checkSetOnline() {
        String selectedPageCode = this.getPageCode();
        try {
            if (StringUtils.isEmpty(selectedPageCode)) {
                selectedPageCode = this.getSelectedNode();
                this.setPageCode(selectedPageCode);
            }
            String check = this.checkSelectedNode(selectedPageCode);
            if (null != check) {
                return check;
            }
        } catch (Throwable t) {
            logger.error("error checking before putting page {} online", selectedPageCode, t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String doSetOnline() {
        String selectedPageCode = this.getPageCode();
        try {
            if (StringUtils.isEmpty(selectedPageCode)) {
                selectedPageCode = this.getSelectedNode();
                this.setPageCode(selectedPageCode);
            }
            IPageManager pageManager = this.getPageManager();
            String check = this.checkSelectedNode(selectedPageCode);
            if (null != check) {
                return check;
            }
            IPage page = this.getPage(selectedPageCode);
            IPage parentPage = this.getPageManager().getDraftPage(page.getParentCode());
            if (!parentPage.isOnline()) {
                this.addActionError(this.getText("error.page.parentDraft"));
                return "pageTree";
            }
            boolean success = this.getPageActionReferencesHelper().checkForSetOnline(page, this);
            if (!success) {
                this.addActionError(this.getText("error.page.setOnline.scanContentRefs"));
                return "pageTree";
            }
            if (this.hasErrors()) {
                return "pageTree";
            }
            pageManager.setPageOnline(selectedPageCode);
            this.addActionMessage(this.getText("message.page.set.online", new String[]{this.getTitle(page.getCode(), page
                .getTitles())}));
            // TODO Define a new strutsAction to map "offline" operation
            this.addActivityStreamInfo(page, PageActionConstants.PUBLISH, true);
        } catch (Throwable t) {
            logger.error("error setting page {} online", selectedPageCode, t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String trash() {
        String selectedNode = this.getSelectedNode();
        try {
            String check = this.checkDelete(selectedNode);
            if (null != check) {
                return check;
            }
            IPage currentPage = this.getPage(selectedNode);
            this.setNodeToBeDelete(selectedNode);
            this.setSelectedNode(currentPage.getParentCode());
        } catch (Throwable t) {
            logger.error("error in trash", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String delete() {
        String selectedNode = this.getNodeToBeDelete();
        try {
            IPageManager pageManager = this.getPageManager();
            String check = this.checkDelete(selectedNode);
            if (null != check) {
                return check;
            }
            IPage pageToDelete = this.getPage(selectedNode);
            pageManager.deletePage(selectedNode);
            this.setSelectedNode(pageToDelete.getParentCode());
            this.addActivityStreamInfo(pageToDelete, ApsAdminSystemConstants.DELETE, false);
        } catch (Throwable t) {
            logger.error("error in delete", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected String checkSetOffline(String selectedNode) throws ApsSystemException {
        String check = this.checkSelectedNode(selectedNode);
        if (null != check) {
            return check;
        }
        IPage currentPage = this.getPage(selectedNode);
        String[] children = currentPage.getChildrenCodes();
        IPage root = this.getPageManager().getOnlineRoot();
        if (root.getCode().equals(currentPage.getCode())) {
            this.addActionError(this.getText("error.page.offlineHome.notAllowed"));
            return "pageTree";
        } else if (null != children && children.length != 0) {
            boolean hasReferences = false;
            boolean isOnline = currentPage.isOnline();
            for (int i = 0; i < children.length; i++) {
                IPage child = PageUtils.getPage(this.getPageManager(), isOnline, children[i]);
                if (null != child && child.isOnline()) {
                    this.addActionError(this.getText("error.page.offline.notAllowed"));
                    hasReferences = true;
                    break;
                }
            }
            if (hasReferences) {
                return "pageTree";
            }
        }
        Map extractedReferences = this.getPageActionHelper().getReferencingObjects(currentPage, this.getRequest());
        if (extractedReferences.size() > 0) {
            this.addActionError(this.getText("error.page.offline.references", new String[]{this.getTitle(currentPage.getCode(),
                currentPage.getTitles())}));
            this.setReferences(extractedReferences);
            return "references";
        }
        return null;
    }

    protected String checkDelete(String selectedNode) throws ApsSystemException {
        String check = this.checkSelectedNode(selectedNode);
        if (null != check) {
            return check;
        }
        IPage currentPage = this.getPage(selectedNode);
        IPage root = this.getPageManager().getOnlineRoot();
        IPage parentPage = this.getPageManager().getDraftPage(currentPage.getParentCode());
        if (root.getCode().equals(currentPage.getCode())) {
            this.addActionError(this.getText("error.page.removeHome.notAllowed"));
            return "pageTree";
        } else if (!isUserAllowed(currentPage) || !isUserAllowed(parentPage)) {
            this.addActionError(this.getText("error.page.remove.notAllowed"));
            return "pageTree";
        } else if (null != currentPage.getChildrenCodes() && currentPage.getChildrenCodes().length != 0) {
            this.addActionError(this.getText("error.page.remove.notAllowed2"));
            return "pageTree";
        } else if (null != this.getOnlinePage(selectedNode)) {
            this.addActionError(this.getText("error.page.remove.notAllowed3"));
            return "pageTree";
        } else {
            Map extractedReferences = this.getPageActionHelper().getReferencingObjects(currentPage, this.getRequest());
            if (extractedReferences.size() > 0) {
                this.setReferences(extractedReferences);
                return "references";
            }
        }
        return null;
    }

    protected void addActivityStreamInfo(IPage page, int strutsAction, boolean addLink) {
        ActivityStreamInfo asi = this.getPageActionHelper().createActivityStreamInfo(page, strutsAction, addLink, "edit");
        super.addActivityStreamInfo(asi);
    }

    public PageResponse getPageResponse() {
        IPage draftPage = null;
        IPage onlinePage = null;
        if (StringUtils.isNotBlank(this.getPageCode())) {
            draftPage = this.getPage(this.getPageCode());
            onlinePage = this.getOnlinePage(this.getPageCode());
        }
        PageResponse pageResponse = new PageResponse(this, draftPage, onlinePage);
        pageResponse.setReferences(this.getReferences());
        return pageResponse;
    }

    /**
     * Return the list of allowed groups.
     *
     * @return The list of allowed groups.
     */
    public List<Group> getAllowedGroups() {
        return super.getActualAllowedGroups();
    }

    /**
     * Return the list of system groups.
     *
     * @return The list of system groups.
     */
    public List<Group> getGroups() {
        List<Group> groups = this.getGroupManager().getGroups();
        BeanComparator c = new BeanComparator("description");
        Collections.sort(groups, c);
        return groups;
    }

    public List<PageModel> getPageModels() {
        return new ArrayList<>(this.getPageModelManager().getPageModels());
    }

    public PageModel getPageModel(String code) {
        return this.getPageModelManager().getPageModel(code);
    }

    public String getCopyPageCode() {
        return copyPageCode;
    }

    public void setCopyPageCode(String copyPageCode) {
        this.copyPageCode = copyPageCode;
    }

    public boolean isDefaultShowlet() {
        return defaultShowlet;
    }

    public void setDefaultShowlet(boolean defaultShowlet) {
        this.defaultShowlet = defaultShowlet;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isGroupSelectLock() {
        return groupSelectLock;
    }

    public void setGroupSelectLock(boolean groupSelectLock) {
        this.groupSelectLock = groupSelectLock;
    }

    public void setExtraGroups(Set<String> extraGroups) {
        this.extraGroups = extraGroups;
    }

    public Set<String> getExtraGroups() {
        return extraGroups;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getParentPageCode() {
        return parentPageCode;
    }

    public void setParentPageCode(String parentPageCode) {
        this.parentPageCode = parentPageCode;
    }

    public boolean isShowable() {
        return showable;
    }

    public void setShowable(boolean showable) {
        this.showable = showable;
    }

    public boolean isUseExtraTitles() {
        return useExtraTitles;
    }

    public void setUseExtraTitles(boolean useExtraTitles) {
        this.useExtraTitles = useExtraTitles;
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public ApsProperties getTitles() {
        return titles;
    }

    public void setTitles(ApsProperties titles) {
        this.titles = titles;
    }

    public String getNodeToBeDelete() {
        return nodeToBeDelete;
    }

    public void setNodeToBeDelete(String nodeToBeDelete) {
        this.nodeToBeDelete = nodeToBeDelete;
    }

    public IPage getPageToShow() {
        return pageToShow;
    }

    protected void setPageToShow(IPage pageToShow) {
        this.pageToShow = pageToShow;
    }

    public Map getReferences() {
        return references;
    }

    protected void setReferences(Map references) {
        this.references = references;
    }

    public String[] getAllowedCharsets() {
        if (null == this.getAllowedCharsetsCSV()) {
            return new String[0];
        }
        return this.getAllowedCharsetsCSV().split(",");
    }

    protected String getAllowedCharsetsCSV() {
        return _allowedCharsetsCSV;
    }

    public void setAllowedCharsetsCSV(String allowedCharsetsCSV) {
        this._allowedCharsetsCSV = allowedCharsetsCSV;
    }

    public String[] getAllowedMimeTypes() {
        if (null == this.getAllowedMimeTypesCSV()) {
            return new String[0];
        }
        return this.getAllowedMimeTypesCSV().split(",");
    }

    protected String getAllowedMimeTypesCSV() {
        return _allowedMimeTypesCSV;
    }

    public void setAllowedMimeTypesCSV(String allowedMimeTypesCSV) {
        this._allowedMimeTypesCSV = allowedMimeTypesCSV;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public Set<String> getTreeNodesToOpen() {
        return treeNodesToOpen;
    }

    public void setTreeNodesToOpen(Set<String> treeNodesToOpen) {
        this.treeNodesToOpen = treeNodesToOpen;
    }

    public String getTreeNodeActionMarkerCode() {
        return treeNodeActionMarkerCode;
    }

    public void setTreeNodeActionMarkerCode(String treeNodeActionMarkerCode) {
        this.treeNodeActionMarkerCode = treeNodeActionMarkerCode;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getServletResponse() {
        return this.response;
    }

    protected IPageActionHelper getPageActionHelper() {
        return pageActionHelper;
    }

    public void setPageActionHelper(IPageActionHelper pageActionHelper) {
        this.pageActionHelper = pageActionHelper;
    }

    protected IPageModelManager getPageModelManager() {
        return pageModelManager;
    }

    public void setPageModelManager(IPageModelManager pageModelManager) {
        this.pageModelManager = pageModelManager;
    }

    protected IPageActionReferencesHelper getPageActionReferencesHelper() {
        return pageActionReferencesHelper;
    }

    public void setPageActionReferencesHelper(IPageActionReferencesHelper pageActionReferencesHelper) {
        this.pageActionReferencesHelper = pageActionReferencesHelper;
    }

    public String getExtraGroupNameToRemove() {
        return extraGroupNameToRemove;
    }

    public void setExtraGroupNameToRemove(String extraGroupNameToRemove) {
        this.extraGroupNameToRemove = extraGroupNameToRemove;
    }

}
