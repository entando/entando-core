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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.plugins.jacms.apsadmin.content.rs.model.ContentJO;
import org.entando.entando.plugins.jacms.apsadmin.content.rs.model.ContentsStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;
import com.agiletec.apsadmin.tags.util.AdminPagerTagHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentsStatus;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;

/**
 * Action per la ricerca contenuti.
 *
 * @author E.Santoboni
 */
public class ContentFinderAction extends AbstractApsEntityFinderAction {

    private static final Logger logger = LoggerFactory.getLogger(ContentFinderAction.class);

    private static final int DEFAULT_LASTUPDATE_RESPONSE_SIZE = 5;

    private String state = "";
    private String text = "";
    private String onLineState = "";
    private String contentIdToken = "";
    private String ownerGroupName;
    private String categoryCode;

    private String lastOrder;
    private String lastGroupBy;
    private String groupBy;

    private boolean viewCode;
    private boolean viewGroup;
    private boolean viewStatus;
    private boolean viewTypeDescr;
    private boolean viewCreationDate;

    private boolean allContentsSelected;
    private int strutsAction;

    private Set<String> contentIds;
    private Set<String> selectedIds;// Used in bulk actions
    private int lastUpdateResponseSize;

    private String actionCode = null;

    private String pagerName = AdminPagerTagHelper.DEFAULT_PAGER_NAME;

    private boolean openCollapsed = false;

    private IContentManager contentManager;
    private IGroupManager groupManager;
    private ICategoryManager categoryManager;

    public boolean isOpenCollapsed() {
        boolean hasFilterByCat = false;
        if (null != this.getCategoryCode()) {
            Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
            hasFilterByCat = (null != category && !category.isRoot());
        }
        return (this.openCollapsed
                || (super.isAddedAttributeFilter())
                || !StringUtils.isBlank(this.getContentType())
                || hasFilterByCat
                || !StringUtils.isBlank(this.getState())
                || !StringUtils.isBlank(this.getOwnerGroupName()));
    }

    public void setOpenCollapsed(boolean openCollapsed) {
        this.openCollapsed = openCollapsed;
    }

    public ContentsStatusResponse getContentsStatusResponse() {
        ContentsStatusResponse response = null;
        try {
            ContentsStatus pagesStatus = this.getContentManager().getContentsStatus();
            response = new ContentsStatusResponse(pagesStatus);
        } catch (Throwable t) {
            logger.error("Error loading contents status", t);
            throw new RuntimeException("Error loading contents status", t);
        }
        return response;
    }

    public String getLastUpdated() {
        if (this.getLastUpdateResponseSize() < 1) {
            this.setLastUpdateResponseSize(DEFAULT_LASTUPDATE_RESPONSE_SIZE);
        }
        return SUCCESS;
    }

    @SuppressWarnings("rawtypes")
    public List<ContentJO> getLastUpdateContentResponse() {
        List<ContentJO> response = null;
        try {

            EntitySearchFilter modifyOrder = new EntitySearchFilter(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY,
                    false);
            modifyOrder.setOrder(EntitySearchFilter.DESC_ORDER);

            List<String> ids = this.getContentManager().searchId(new EntitySearchFilter[]{modifyOrder});
            if (null != ids && !ids.isEmpty()) {
                if (this.getLastUpdateResponseSize() > ids.size() - 1) {
                    this.setLastUpdateResponseSize(ids.size() - 1);
                }
                List<String> subList = ids.subList(0, this.getLastUpdateResponseSize());
                response = new ArrayList<ContentJO>();
                Iterator<String> sublist = subList.iterator();
                while (sublist.hasNext()) {
                    String contentId = sublist.next();
                    Content content = this.getContentManager().loadContent(contentId, false);
                    ContentRecordVO vo = this.getContentManager().loadContentVO(contentId);
                    ContentJO contentJO = new ContentJO(content, vo);
                    response.add(contentJO);
                }
            }

        } catch (Throwable t) {
            logger.error("Error loading last updated content response", t);
            throw new RuntimeException("Error loading last updated content response", t);
        }
        return response;
    }

    @Override
    public String execute() {
        try {
            ContentFinderSearchInfo searchInfo = this.getContentSearchInfo();
            if (null == searchInfo) {
                searchInfo = new ContentFinderSearchInfo();
                this.getRequest().getSession().setAttribute(ContentFinderSearchInfo.SESSION_NAME, searchInfo);
            }
            this.createFilters();
        } catch (Throwable t) {
            logger.error("error in execute", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String list() {
        try {
            this.getRequest().getSession().setAttribute(ContentFinderSearchInfo.SESSION_NAME,
                    new ContentFinderSearchInfo());
            this.createFilters();
        } catch (Throwable t) {
            logger.error("error in execute", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String results() {
        try {
            ContentFinderSearchInfo searchInfo = this.getContentSearchInfo();
            if (null == searchInfo) {
                searchInfo = new ContentFinderSearchInfo();
                this.getRequest().getSession().setAttribute(ContentFinderSearchInfo.SESSION_NAME, searchInfo);
            }
            this.restoreCommonSearchState(searchInfo);
            this.restoreCategorySearchState(searchInfo);
            this.restoreEntitySearchState(searchInfo);
            this.restorePagerSearchState(searchInfo);

            this.addFilter(searchInfo.getFilter(ContentFinderSearchInfo.ORDER_FILTER));
        } catch (Throwable t) {
            logger.error("error in results", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    /**
     * Restituisce la lista identificativi di contenuti che deve essere erogata
     * dall'interfaccia di visualizzazione dei contenuti. La lista deve essere
     * filtrata secondo i parametri di ricerca impostati.
     *
     * @return La lista di contenuti che deve essere erogata dall'interfaccia di
     * visualizzazione dei contenuti.
     */
    public List<String> getContents() {
        List<String> result = null;
        try {
            ContentFinderSearchInfo searchInfo = getContentSearchInfo();
            List<String> allowedGroups = this.getContentGroupCodes();
            String[] categories = null;
            Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
            if (null != category && !category.isRoot()) {
                String catCode = this.getCategoryCode().trim();
                categories = new String[]{catCode};
                searchInfo.setCategoryCode(catCode);
            } else {
                searchInfo.setCategoryCode(null);
            }
            result = this.getContentManager().loadWorkContentsId(categories, this.getFilters(), allowedGroups);
        } catch (Throwable t) {
            logger.error("error in getContents", t);
            throw new RuntimeException("error in getContents", t);
        }
        return result;
    }

    public String changeOrder() {
        try {
            if (null == this.getGroupBy()) {
                return SUCCESS;
            }
            if (this.getGroupBy().equals(this.getLastGroupBy())) {
                boolean condition = (null == this.getLastOrder()
                        || this.getLastOrder().equals(EntitySearchFilter.ASC_ORDER));
                String order = (condition ? EntitySearchFilter.DESC_ORDER : EntitySearchFilter.ASC_ORDER);
                this.setLastOrder(order);
            } else {
                this.setLastOrder(EntitySearchFilter.DESC_ORDER);
            }
            this.setLastGroupBy(this.getGroupBy());
        } catch (Throwable t) {
            logger.error("error in changeOrder", t);
            throw new RuntimeException("error in changeOrder", t);
        }
        return this.execute();
    }

    /**
     * Esegue la publicazione di un singolo contenuto direttamente
     * dall'interfaccia di visualizzazione dei contenuti in lista.
     *
     * @return Il codice del risultato dell'azione.
     */
    public String insertOnLine() {
        try {
            if (null == this.getContentIds()) {
                this.addActionError(this.getText("error.contents.nothingSelected"));
                return INPUT;
            }
            Iterator<String> iter = this.getContentIds().iterator();
            List<Content> publishedContents = new ArrayList<Content>();
            while (iter.hasNext()) {
                String contentId = (String) iter.next();
                Content contentToPublish = this.getContentManager().loadContent(contentId, false);
                String[] msgArg = new String[1];
                if (null == contentToPublish) {
                    msgArg[0] = contentId;
                    this.addActionError(this.getText("error.content.contentToPublishNull", msgArg));
                    continue;
                }
                msgArg[0] = contentToPublish.getDescription();
                if (!this.isUserAllowed(contentToPublish)) {
                    this.addActionError(this.getText("error.content.userNotAllowedToPublishContent", msgArg));
                    continue;
                }
                this.getContentActionHelper().scanEntity(contentToPublish, this);
                if (this.getFieldErrors().size() > 0) {
                    this.addActionError(this.getText("error.content.publishingContentWithErrors", msgArg));
                    continue;
                }
                this.getContentManager().insertOnLineContent(contentToPublish);
                logger.info("Published content {} by user {}", contentToPublish.getId(),
                        this.getCurrentUser().getUsername());
                publishedContents.add(contentToPublish);
                this.addActivityStreamInfo(contentToPublish, (ApsAdminSystemConstants.ADD + 10), true);
            }
            // RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
            this.addConfirmMessage("message.content.publishedContents", publishedContents);
        } catch (Throwable t) {
            logger.error("error in insertOnLine", t);
            throw new RuntimeException("Error publishing contents", t);
        }
        return SUCCESS;
    }

    /**
     * Esegue la rimozione dall'area pubblica di un singolo contenuto
     * direttamente dall'interfaccia di visualizzazione dei contenuti in lista.
     *
     * @return Il codice del risultato dell'azione.
     */
    public String removeOnLine() {
        try {
            if (null == this.getContentIds()) {
                this.addActionError(this.getText("error.contents.nothingSelected"));
                return INPUT;
            }
            Iterator<String> contentsIdsItr = this.getContentIds().iterator();
            List<Content> removedContents = new ArrayList<Content>();
            while (contentsIdsItr.hasNext()) {
                String contentId = (String) contentsIdsItr.next();
                Content contentToSuspend = this.getContentManager().loadContent(contentId, false);
                String[] msgArg = new String[1];
                if (null == contentToSuspend) {
                    msgArg[0] = contentId;
                    this.addActionError(this.getText("error.content.contentToSuspendNull", msgArg));
                    continue;
                }
                msgArg[0] = contentToSuspend.getDescription();
                if (!this.isUserAllowed(contentToSuspend)) {
                    this.addActionError(this.getText("error.content.userNotAllowedToSuspendContent", msgArg));
                    continue;
                }
                Map references = this.getContentActionHelper().getReferencingObjects(contentToSuspend,
                        this.getRequest());
                if (references.size() > 0) {
                    this.addActionError(this.getText("error.content.suspendingContentWithReferences", msgArg));
                    continue;
                }
                this.getContentManager().removeOnLineContent(contentToSuspend);
                logger.info("Suspended Content '{}' by user {}", contentToSuspend.getId(),
                        this.getCurrentUser().getUsername());
                removedContents.add(contentToSuspend);
                this.addActivityStreamInfo(contentToSuspend, (ApsAdminSystemConstants.DELETE + 10), true);
            }
            // RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
            this.addConfirmMessage("message.content.suspendedContents", removedContents);
        } catch (Throwable t) {
            logger.error("Error on suspending contents", t);
            throw new RuntimeException("Error on suspending contents", t);
        }
        return SUCCESS;
    }

    /**
     * We've moved to deletion check here in the 'trash' action so to have
     * errors notified immediately. Be design we share all the messages with the
     * 'delete' action.
     *
     * @return the result code of the action: "success" if all the contents can
     * be deleted, "cannotProceed" if blocking errors are detected
     */
    public String trash() {
        if (null == this.getContentIds()) {
            this.addActionError(this.getText("error.contents.nothingSelected"));
            return INPUT;
        }
        try {
            Iterator<String> itr = this.getContentIds().iterator();
            while (itr.hasNext()) {
                String currentContentId = itr.next();
                String msgArg[] = new String[1];
                Content contentToTrash = this.getContentManager().loadContent(currentContentId, false);
                if (null == contentToTrash) {
                    msgArg[0] = currentContentId;
                    this.addActionError(this.getText("error.content.contentToDeleteNull", msgArg));
                    continue;
                }
                msgArg[0] = contentToTrash.getDescription();
                if (!this.isUserAllowed(contentToTrash)) {
                    this.addActionError(this.getText("error.content.userNotAllowedToContentToDelete", msgArg));
                    continue;
                }
                if (contentToTrash.isOnLine()) {
                    this.addActionError(this.getText("error.content.notAllowedToDeleteOnlineContent", msgArg));
                    continue;
                }
            }
        } catch (Throwable t) {
            logger.error("Error on deleting contents - trash", t);
            throw new RuntimeException("Error on deleting contents", t);
        }
        if (this.getActionErrors().isEmpty()) {
            return SUCCESS;
        }
        return "cannotProceed";
    }

    /**
     * Esegue l'operazione di cancellazione contenuto o gruppo contenuti.
     *
     * @return Il codice del risultato.
     */
    public String delete() {
        try {
            if (null == this.getContentIds()) {
                this.addActionError(this.getText("error.contents.nothingSelected"));
                return INPUT;
            }
            Iterator<String> iter = this.getContentIds().iterator();
            List<Content> deletedContents = new ArrayList<Content>();
            while (iter.hasNext()) {
                String contentId = (String) iter.next();
                Content contentToDelete = this.getContentManager().loadContent(contentId, false);
                String[] msgArg = new String[1];
                if (null == contentToDelete) {
                    msgArg[0] = contentId;
                    this.addActionError(this.getText("error.content.contentToDeleteNull", msgArg));
                    continue;
                }
                msgArg[0] = contentToDelete.getDescription();
                if (!this.isUserAllowed(contentToDelete)) {
                    this.addActionError(this.getText("error.content.userNotAllowedToContentToDelete", msgArg));
                    continue;
                }
                if (contentToDelete.isOnLine()) {
                    this.addActionError(this.getText("error.content.notAllowedToDeleteOnlineContent", msgArg));
                    continue;
                }
                this.getContentManager().deleteContent(contentToDelete);
                logger.info("Deleted Content '{}' by user {}", contentToDelete.getId(),
                        this.getCurrentUser().getUsername());
                deletedContents.add(contentToDelete);
                this.addActivityStreamInfo(contentToDelete, ApsAdminSystemConstants.DELETE, false);
            }
            // RIVISITARE LABEL e LOGICA DI COSTRUZIONE LABEL
            this.addConfirmMessage("message.content.deletedContents", deletedContents);
        } catch (Throwable t) {
            logger.error("Error deleting contentd - delete", t);
            throw new RuntimeException("Errore in cancellazione contenuti", t);
        }
        return SUCCESS;
    }

    /**
     * Restituisce il contenuto vo in base all'identificativo.
     *
     * @param contentId L'identificativo del contenuto.
     * @return Il contenuto vo cercato.
     */
    public ContentRecordVO getContentVo(String contentId) {
        ContentRecordVO contentVo = null;
        try {
            contentVo = this.getContentManager().loadContentVO(contentId);
        } catch (Throwable t) {
            logger.error("error in getContentVo for content {}", contentId, t);
            throw new RuntimeException("Errore in caricamento contenuto vo", t);
        }
        return contentVo;
    }

    public List<SmallEntityType> getContentTypes() {
        return this.getContentManager().getSmallEntityTypes();
    }

    public SmallEntityType getSmallContentType(String code) {
        return this.getContentManager().getSmallContentTypesMap().get(code);
    }

    /**
     * Restituisce la lista di stati di contenuto definiti nel sistema, come
     * insieme di chiave e valore Il metodo è a servizio delle jsp che
     * richiedono questo dato per fornire una corretta visualizzazione della
     * pagina.
     *
     * @return La lista di stati di contenuto definiti nel sistema.
     */
    public List<SelectItem> getAvalaibleStatus() {
        String[] status = Content.AVAILABLE_STATUS;
        List<SelectItem> items = new ArrayList<SelectItem>(status.length);
        for (int i = 0; i < status.length; i++) {
            SelectItem item = new SelectItem(status[i], "name.contentStatus." + status[i]);
            items.add(item);
        }
        return items;
    }

    public String entryBulkActions() {
        try {
            Set<String> contentIds = null;
            if (this.isAllContentsSelected()) {
                this.getRequest().getSession().setAttribute(ContentFinderSearchInfo.SESSION_NAME,
                        new ContentFinderSearchInfo());
                this.createFilters();
                List<String> allContentIds = this.getContents();
                if (allContentIds != null) {
                    contentIds = new TreeSet<String>(allContentIds);
                }
            } else {
                contentIds = this.getContentIds();
            }
            this.setSelectedIds(contentIds);
            if (contentIds == null || contentIds.isEmpty()) {
                this.addActionError(this.getText("error.content.bulkAction.empty"));
                return INPUT;
            }
        } catch (Throwable t) {
            logger.error("error in execute", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    /**
     * Restituisce un gruppo in base al nome.
     *
     * @param groupName Il nome del gruppo da restituire.
     * @return Il gruppo cercato.
     */
    public Group getGroup(String groupName) {
        return this.getGroupManager().getGroup(groupName);
    }

    public List<Group> getAllowedGroups() {
        return super.getActualAllowedGroups();
    }

    /**
     * Restituisce la lista ordinata dei gruppi presenti nel sistema.
     *
     * @return La lista dei gruppi presenti nel sistema.
     */
    public List<Group> getGroups() {
        return this.getGroupManager().getGroups();
    }

    public Category getCategoryRoot() {
        return (Category) this.getCategoryManager().getRoot();
    }

    public String getContentType() {
        return super.getEntityTypeCode();
    }

    public void setContentType(String contentType) {
        super.setEntityTypeCode(contentType);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOnLineState() {
        return onLineState;
    }

    public void setOnLineState(String onLineState) {
        this.onLineState = onLineState;
    }

    public void setContentIdToken(String contentIdToken) {
        this.contentIdToken = contentIdToken;
    }

    public String getContentIdToken() {
        return contentIdToken;
    }

    public String getOwnerGroupName() {
        return ownerGroupName;
    }

    public void setOwnerGroupName(String ownerGroupName) {
        this.ownerGroupName = ownerGroupName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(String order) {
        this.lastOrder = order;
    }

    public String getLastGroupBy() {
        return lastGroupBy;
    }

    public void setLastGroupBy(String lastGroupBy) {
        this.lastGroupBy = lastGroupBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public boolean isViewCode() {
        return viewCode;
    }

    public void setViewCode(boolean viewCode) {
        this.viewCode = viewCode;
    }

    public boolean isViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(boolean viewStatus) {
        this.viewStatus = viewStatus;
    }

    public boolean isViewCreationDate() {
        return viewCreationDate;
    }

    public void setViewCreationDate(boolean viewCreationDate) {
        this.viewCreationDate = viewCreationDate;
    }

    public boolean getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(boolean viewGroup) {
        this.viewGroup = viewGroup;
    }

    public boolean getViewTypeDescr() {
        return viewTypeDescr;
    }

    public void setViewTypeDescr(boolean viewTypeDescr) {
        this.viewTypeDescr = viewTypeDescr;
    }

    public String getPagerName() {
        return pagerName;
    }

    public void setPagerName(String pagerName) {
        this.pagerName = pagerName;
    }

    public Set<String> getContentIds() {
        return contentIds;
    }

    public void setContentIds(Set<String> contentIds) {
        this.contentIds = contentIds;
    }

    public Set<String> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Set<String> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public boolean isAllContentsSelected() {
        return allContentsSelected;
    }

    public void setAllContentsSelected(boolean allContentsSelected) {
        this.allContentsSelected = allContentsSelected;
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    protected boolean isUserAllowed(Content content) {
        return this.getContentActionHelper().isUserAllowed(content, this.getCurrentUser());
    }

    protected void addConfirmMessage(String key, List<Content> contents) {
        if (contents.size() > 0) {
            // RIVISITARE LOGICA DI COSTRUZIONE MESSAGGIO
            String confirm = this.getText(key);
            for (int i = 0; i < contents.size(); i++) {
                Content content = contents.get(i);
                if (i > 0) {
                    confirm += " - ";
                }
                confirm += " '" + content.getDescription() + "'";
            }
            this.addActionMessage(confirm);
        }
    }

    @SuppressWarnings("rawtypes")
    protected void restoreCommonSearchState(ContentFinderSearchInfo searchInfo) {
        if (null != searchInfo.getFilter(IContentManager.CONTENT_STATUS_FILTER_KEY)) {
            EntitySearchFilter filterToAdd = searchInfo.getFilter(IContentManager.CONTENT_STATUS_FILTER_KEY);
            this.addFilter(filterToAdd);
            this.setState((String) filterToAdd.getValue());
        }

        if (null != searchInfo.getFilter(IContentManager.CONTENT_DESCR_FILTER_KEY)) {
            EntitySearchFilter filterToAdd = searchInfo.getFilter(IContentManager.CONTENT_DESCR_FILTER_KEY);
            this.addFilter(filterToAdd);
            this.setText((String) filterToAdd.getValue());
        }

        if (null != searchInfo.getFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY)) {
            EntitySearchFilter filterToAdd = searchInfo.getFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY);
            this.addFilter(filterToAdd);
            this.setOwnerGroupName((String) filterToAdd.getValue());
        }

        if (null != searchInfo.getFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY)) {
            EntitySearchFilter filterToAdd = searchInfo.getFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY);
            this.addFilter(filterToAdd);
            this.setOnLineState(filterToAdd.isNullOption() ? "no" : "yes");
        }

        if (null != searchInfo.getFilter(IContentManager.ENTITY_ID_FILTER_KEY)) {
            EntitySearchFilter filterToAdd = searchInfo.getFilter(IContentManager.ENTITY_ID_FILTER_KEY);
            this.addFilter(filterToAdd);
            this.setContentIdToken((String) filterToAdd.getValue());
        }
    }

    protected IContentActionHelper getContentActionHelper() {
        return (IContentActionHelper) super.getEntityActionHelper();
    }

    @Override
    protected void deleteEntity(String entityId) throws Throwable {
        // method not supported
    }

    @Override
    protected IEntityManager getEntityManager() {
        return this.getContentManager();
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    protected ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public int getLastUpdateResponseSize() {
        return lastUpdateResponseSize;
    }

    public void setLastUpdateResponseSize(int lastUpdateResponseSize) {
        this.lastUpdateResponseSize = lastUpdateResponseSize;
    }

    /**
     * Restituisce la lista di gruppi (codici) dei contenuti che devono essere
     * visualizzati in lista. La lista viene ricavata in base alle
     * autorizzazioni dall'utente corrente.
     *
     * @return La lista di gruppi cercata.
     */
    protected List<String> getContentGroupCodes() {
        return super.getActualAllowedGroupCodes();
    }

    /**
     * Restitusce i filtri per la selezione e l'ordinamento dei contenuti
     * erogati nell'interfaccia.
     *
     * @return Il filtri di selezione ed ordinamento dei contenuti.
     */
    @SuppressWarnings("rawtypes")
    protected EntitySearchFilter[] createFilters() {
        ContentFinderSearchInfo searchInfo = getContentSearchInfo();
        this.createBaseFilters();

        if (null != this.getState() && this.getState().trim().length() > 0) {
            EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, false,
                    this.getState(), false);
            this.addFilter(filterToAdd);
            searchInfo.addFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, filterToAdd);
        } else {
            searchInfo.removeFilter(IContentManager.CONTENT_STATUS_FILTER_KEY);
        }

        if (null != this.getText() && this.getText().trim().length() > 0) {
            EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false,
                    this.getText(), true);
            this.addFilter(filterToAdd);
            searchInfo.addFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, filterToAdd);
        } else {
            searchInfo.removeFilter(IContentManager.CONTENT_DESCR_FILTER_KEY);
        }

        if (null != this.getOwnerGroupName() && this.getOwnerGroupName().trim().length() > 0) {
            EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY,
                    false, this.getOwnerGroupName(), false);
            this.addFilter(filterToAdd);
            searchInfo.addFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY, filterToAdd);
        } else {
            searchInfo.removeFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY);
        }

        if (null != this.getOnLineState() && this.getOnLineState().trim().length() > 0) {
            EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
            filterToAdd.setNullOption(this.getOnLineState().trim().equals("no"));
            this.addFilter(filterToAdd);
            searchInfo.addFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, filterToAdd);
        } else {
            searchInfo.removeFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY);
        }

        if (null != this.getContentIdToken() && this.getContentIdToken().trim().length() > 0) {
            EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false,
                    this.getContentIdToken(), true);
            this.addFilter(filterToAdd);
            searchInfo.addFilter(IContentManager.ENTITY_ID_FILTER_KEY, filterToAdd);
        } else {
            searchInfo.removeFilter(IContentManager.ENTITY_ID_FILTER_KEY);
        }

        this.savePagerSearchState(searchInfo);
        EntitySearchFilter orderFilter = this.getContentActionHelper().getOrderFilter(this.getLastGroupBy(),
                this.getLastOrder());
        super.addFilter(orderFilter);
        searchInfo.addFilter(ContentFinderSearchInfo.ORDER_FILTER, orderFilter);

        return this.getFilters();
    }

    @SuppressWarnings("rawtypes")
    protected void createBaseFilters() {
        try {
            int initSize = this.getFilters().length;
            ContentFinderSearchInfo searchInfo = getContentSearchInfo();

            EntitySearchFilter[] roleFilters = this.getEntityActionHelper().getRoleFilters(this);
            this.addFilters(roleFilters);
            IApsEntity prototype = this.getEntityPrototype();
            searchInfo.removeFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY);
            searchInfo.removeFilterByPrefix(ContentFinderSearchInfo.ATTRIBUTE_FILTER);
            if (null != prototype) {
                EntitySearchFilter filterToAdd = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY,
                        false, prototype.getTypeCode(), false);
                this.addFilter(filterToAdd);
                searchInfo.addFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, filterToAdd);

                EntitySearchFilter[] filters = this.getEntityActionHelper().getAttributeFilters(this, prototype);
                this.addFilters(filters);
                searchInfo.addAttributeFilters(filters);
            }
            this.setAddedAttributeFilter(this.getFilters().length > initSize);
        } catch (Throwable t) {
            logger.error("Error while creating entity filters", t);
            throw new RuntimeException("Error while creating entity filters", t);
        }
    }

    protected void addActivityStreamInfo(Content content, int strutsAction, boolean addLink) {
        ActivityStreamInfo asi = this.getContentActionHelper().createActivityStreamInfo(content, strutsAction, addLink);
        super.addActivityStreamInfo(asi);
    }

    protected void savePagerSearchState(ContentFinderSearchInfo searchInfo) {
        boolean found = searchInfo.setPagerFromParameters(this.getRequest().getParameterNames());
        if (!found) {
            String pagerName = this.getPagerName();
            String pos = this.getRequest().getParameter(pagerName);
            if (StringUtils.isNotBlank(pos) && StringUtils.isNumeric(pos)) {
                searchInfo.setPageName(pagerName);
                searchInfo.setPagePos(new Integer(pos));
            }
        }
    }

    protected void restorePagerSearchState(ContentFinderSearchInfo searchInfo) {
        String pageName = searchInfo.getPageName();
        if (StringUtils.isNotBlank(pageName)) {
            this.getRequest().setAttribute(pageName, searchInfo.getPagePos());
        }
    }

    @SuppressWarnings("rawtypes")
    protected void restoreEntitySearchState(ContentFinderSearchInfo searchInfo) {
        if (null != searchInfo.getFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
            EntitySearchFilter filter = searchInfo.getFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY);
            this.addFilter(filter);
            this.setEntityTypeCode((String) filter.getValue());

            EntitySearchFilter[] filters = searchInfo.getFiltersByKeyPrefix(ContentFinderSearchInfo.ATTRIBUTE_FILTER);
            if (null != filters && filters.length > 0) {
                for (EntitySearchFilter entitySearchFilter : filters) {
                    this.addFilter(entitySearchFilter);

                    String attrName = entitySearchFilter.getKey();
                    ApsEntity proto = (ApsEntity) this.getEntityPrototype();

                    String[] attributeFilterFieldNames = this.getEntityActionHelper().getAttributeFilterFieldName(proto,
                            attrName);
                    if (null != attributeFilterFieldNames && attributeFilterFieldNames.length == 1) {
                        this.getRequest().setAttribute(attributeFilterFieldNames[0], entitySearchFilter.getValue());
                    } else if (null != attributeFilterFieldNames && attributeFilterFieldNames.length == 2) {
                        Date start = (Date) entitySearchFilter.getStart();
                        if (null != start) {
                            this.getRequest().setAttribute(attributeFilterFieldNames[0],
                                    DateConverter.getFormattedDate(start, EntitySearchFilter.DATE_PATTERN));
                        }
                        Date end = (Date) entitySearchFilter.getEnd();
                        if (null != end) {
                            this.getRequest().setAttribute(attributeFilterFieldNames[1],
                                    DateConverter.getFormattedDate(end, EntitySearchFilter.DATE_PATTERN));
                        }
                    }

                }
            }
        }
    }

    private void restoreCategorySearchState(ContentFinderSearchInfo searchInfo) {
        String catCode = searchInfo.getCategoryCode();
        if (StringUtils.isNotBlank(catCode)) {
            Category category = this.getCategoryManager().getCategory(catCode);
            if (null != category && !category.isRoot()) {
                this.setCategoryCode(catCode);
            }
        }
    }

    private ContentFinderSearchInfo getContentSearchInfo() {
        ContentFinderSearchInfo searchInfo = (ContentFinderSearchInfo) this.getRequest().getSession()
                .getAttribute(ContentFinderSearchInfo.SESSION_NAME);
        return searchInfo;
    }
}
