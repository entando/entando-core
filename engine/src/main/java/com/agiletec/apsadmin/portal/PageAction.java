/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.apsadmin.portal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseActionHelper;

/**
 * Main action for pages handling
 * @author E.Santoboni
 */
public class PageAction extends AbstractPortalAction implements IPageAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageAction.class);
	
	@Override
	public void validate() {
		super.validate();
		this.checkCode();
		this.checkTitles();
	}
	
	private void checkTitles() {
		this.updateTitles();
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String title = (String) this.getTitles().get(lang.getCode());
			if (null == title || title.trim().length() == 0) {
				String[] args = {lang.getDescr()};
				String titleKey = "lang" + lang.getCode();
				this.addFieldError(titleKey, this.getText("error.page.insertTitle", args));
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
	
	private void checkCode() {
		String code = this.getPageCode();
		if ((this.getStrutsAction() == ApsAdminSystemConstants.ADD || 
				this.getStrutsAction() == ApsAdminSystemConstants.PASTE) 
				&& null != code && code.trim().length() > 0) {
			String currectCode = BaseActionHelper.purgeString(code.trim());
			if (currectCode.length() > 0 && null != this.getPageManager().getPage(currectCode)) {
				String[] args = {currectCode};
				this.addFieldError("pageCode", this.getText("error.page.duplicateCode", args));
			}
			this.setPageCode(currectCode);
		}
	}
	
	@Override
	public String newPage() {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(this.getSelectedNode());
			if (null != check) {
				return check;
			}
			IPage parentPage = this.getPageManager().getPage(selectedNode);
			this.valueFormForNew(parentPage);
		} catch (Throwable t) {
			_logger.error("error in newPage", t);
			//ApsSystemUtils.logThrowable(t, this, "newPage");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void valueFormForNew(IPage parentPage) {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		this.setParentPageCode(parentPage.getCode());
		this.setGroup(parentPage.getGroup());
		boolean isParentFree = parentPage.getGroup().equals(Group.FREE_GROUP_NAME);
		this.setGroupSelectLock(!(this.isCurrentUserMemberOf(Group.ADMINS_GROUP_NAME) && isParentFree));
		this.setDefaultShowlet(true);
		this.setShowable(true);
	}
	
	@Override
	public String edit() {
		String pageCode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(pageCode);
			if (null != check) {
				return check;
			}
			IPage page = this.getPageManager().getPage(pageCode);
			this.valueFormForEdit(page);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			//ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String joinExtraGroup() {
		try {
			this.updateTitles();
			this.getExtraGroups().add(super.getParameter("extraGroupName"));
		} catch (Throwable t) {
			_logger.error("error in joinExtraGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "joinExtraGroup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeExtraGroup() {
		try {
			this.updateTitles();
			this.getExtraGroups().remove(super.getParameter("extraGroupName"));
		} catch (Throwable t) {
			_logger.error("error in removeExtraGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "removeExtraGroup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String showDetail() {
		String pageCode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(pageCode);
			if (null != check) {
				return check;
			}
			IPage page = this.getPageManager().getPage(pageCode);
			this.setPageToShow(page);
		} catch (Throwable t) {
			_logger.error("error in showDetail", t);
			//ApsSystemUtils.logThrowable(t, this, "detail");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void valueFormForEdit(IPage pageToEdit) {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		this.setParentPageCode(pageToEdit.getParent().getCode());
		this.setPageCode(pageToEdit.getCode());
		this.setTitles(pageToEdit.getTitles());
		this.setGroup(pageToEdit.getGroup());
		this.setGroupSelectLock(true);
		this.setExtraGroups(pageToEdit.getExtraGroups());
		this.setModel(pageToEdit.getModel().getCode());
		this.setShowable(pageToEdit.isShowable());
		this.setUseExtraTitles(pageToEdit.isUseExtraTitles());
		this.setCharset(pageToEdit.getCharset());
		this.setMimeType(pageToEdit.getMimeType());
	}
	
	@Override
	public String paste() {
		String selectedNode = this.getSelectedNode();
		String copyingPageCode = this.getRequest().getParameter("copyingPageCode");
		try {
			String check = this.checkSelectedNode(selectedNode);
			if (null != check) {
				return check;
			}
			if ("".equals(copyingPageCode) || null == this.getPageManager().getPage(copyingPageCode)) {
				this.addActionError(this.getText("error.page.selectPageToCopy"));
				return "pageTree";
			}
			IPage selectedPage = this.getPageManager().getPage(selectedNode);
			IPage copiedPage = this.getPageManager().getPage(copyingPageCode);
			this.setStrutsAction(ApsAdminSystemConstants.PASTE);
			this.setCopyPageCode(copyingPageCode);
			this.setGroup(selectedPage.getGroup());
			this.setExtraGroups(copiedPage.getExtraGroups());
			this.setModel(copiedPage.getModel().getCode());
			this.setShowable(copiedPage.isShowable());
			this.setUseExtraTitles(copiedPage.isUseExtraTitles());
			this.setParentPageCode(selectedNode);
			this.setCharset(copiedPage.getCharset());
			this.setMimeType(copiedPage.getMimeType());
		} catch (Throwable t) {
			_logger.error("error in paste", t);
			//ApsSystemUtils.logThrowable(t, this, "paste");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		IPage page = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				page = this.getUpdatedPage();
				this.getPageManager().updatePage(page);
				_logger.debug("Updating page " + page.getCode());
			} else {
				page = this.buildNewPage();
				this.getPageManager().addPage(page);
				_logger.debug("Adding new page");
			}
			this.addActivityStreamInfo(page, this.getStrutsAction(), true);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			//ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected IPage buildNewPage() throws ApsSystemException {
		Page page = new Page();
		try {
			page.setParent(this.getPageManager().getPage(this.getParentPageCode()));
			page.setGroup(this.getGroup());
			page.setShowable(this.isShowable());
			page.setUseExtraTitles(this.isUseExtraTitles());
			PageModel pageModel = this.getPageModelManager().getPageModel(this.getModel());
			page.setModel(pageModel);
			if (this.getStrutsAction() == ApsAdminSystemConstants.PASTE) {
				IPage copyPage = this.getPageManager().getPage(this.getCopyPageCode());
				page.setWidgets(copyPage.getWidgets());
			} else {
				if (this.isDefaultShowlet()) {
					this.setDefaultShowlets(page);
				} else {
					page.setWidgets(new Widget[pageModel.getFrames().length]);
				}
			}
			page.setTitles(this.getTitles());
			page.setExtraGroups(this.getExtraGroups());
			//ricava il codice
			String newPageCode = this.getPageCode();
			if (null != newPageCode && newPageCode.trim().length() > 0) {
				if (newPageCode.length() > 0) {
					page.setCode(newPageCode);
				}
			}
			if (null == page.getCode()) {
				String defaultLangCode = this.getLangManager().getDefaultLang().getCode();
				String pageCode = 
					this.getPageActionHelper().buildCode(page.getTitle(defaultLangCode), "page", 25);
				page.setCode(pageCode);
			}
			String charset = this.getCharset();
			if (null != charset && charset.trim().length() > 0) {
				page.setCharset(charset);
			} else {
				page.setCharset(null);
			}
			String mimetype = this.getMimeType();
			if (null != mimetype && mimetype.trim().length() > 0) {
				page.setMimeType(mimetype);
			} else {
				page.setMimeType(null);
			}
		} catch (Throwable t) {
			_logger.error("Error building new page", t);
			//ApsSystemUtils.logThrowable(t, this, "buildNewPage");
			throw new ApsSystemException("Error building new page", t);
		}
		return page;
	}
	
	protected IPage getUpdatedPage() throws ApsSystemException {
		Page page = null;
		try {
			page = (Page) this.getPageManager().getPage(this.getPageCode());
			page.setGroup(this.getGroup());
			page.setShowable(this.isShowable());
			page.setUseExtraTitles(this.isUseExtraTitles());
			if (!page.getModel().getCode().equals(this.getModel())) {
				//Ho cambiato modello e allora cancello tutte le showlets Precedenti
				PageModel model = this.getPageModelManager().getPageModel(this.getModel());
				page.setModel(model);
				page.setWidgets(new Widget[model.getFrames().length]);
			}
			if (this.isDefaultShowlet()) {
				this.setDefaultShowlets(page);
			}
			page.setTitles(this.getTitles());
			page.setExtraGroups(this.getExtraGroups());
			String charset = this.getCharset();
			if (null != charset && charset.trim().length() > 0) {
				page.setCharset(charset);
			} else {
				page.setCharset(null);
			}
			String mimetype = this.getMimeType();
			if (null != mimetype && mimetype.trim().length() > 0) {
				page.setMimeType(mimetype);
			} else {
				page.setMimeType(null);
			}
		} catch (Throwable t) {
			_logger.error("Error updating page", t);
			//ApsSystemUtils.logThrowable(t, this, "getUpdatedPage");
			throw new ApsSystemException("Error updating page", t);
		}
		return page;
	}
	
	protected void setDefaultShowlets(Page page) throws ApsSystemException {
		try {
			Widget[] defaultWidgets = page.getModel().getDefaultWidget();
			if (null == defaultWidgets) {
				return;
			}
			Widget[] showlets = new Widget[defaultWidgets.length];
			for (int i=0; i<defaultWidgets.length; i++) {
				Widget defaultWidget = defaultWidgets[i];
				if (null != defaultWidget) {
					if (null == defaultWidget.getType()) {
						_logger.error("Widget Type null when adding defaulWidget (of pagemodel '{}') on frame '{}' of page '{}'", page.getModel().getCode(), i, page.getCode());
						continue;
					}
					showlets[i] = defaultWidget;
				}
			}
			page.setWidgets(showlets);
		} catch (Throwable t) {
			_logger.error("Error setting default widget to page {}", page.getCode(), t);
			//ApsSystemUtils.logThrowable(t, this, "setDefaultShowlets");
			throw new ApsSystemException("Error setting default widget to page '" + page.getCode() + "'", t);
		}
	}
	
	@Override
	public String trash() {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkDelete(selectedNode);
			if (null != check) {
				return check;
			}
			IPage currentPage = this.getPageManager().getPage(selectedNode);
			Map references = this.getPageActionHelper().getReferencingObjects(currentPage, this.getRequest());
			if (references.size()>0) {
				this.setReferences(references);
				return "references";
			}
			this.setNodeToBeDelete(selectedNode);
			this.setSelectedNode(currentPage.getParent().getCode());
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			//ApsSystemUtils.logThrowable(t, this, "trash");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		try {
			String check = this.checkDelete(this.getNodeToBeDelete());
			if (null != check) {
				return check;
			}
			IPage pageToDelete = this.getPageManager().getPage(this.getNodeToBeDelete());
			this.getPageManager().deletePage(this.getNodeToBeDelete());
			this.addActivityStreamInfo(pageToDelete, ApsAdminSystemConstants.DELETE, false);
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			//ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected String checkDelete(String selectedNode) {
		String check = this.checkSelectedNode(selectedNode);
		if (null != check) {
			return check;
		}
		IPage currentPage = this.getPageManager().getPage(selectedNode);
		if (this.getPageManager().getRoot().getCode().equals(currentPage.getCode())) {
			this.addActionError(this.getText("error.page.removeHome.notAllowed"));
			return "pageTree";
		} else if (!isUserAllowed(currentPage) || !isUserAllowed(currentPage.getParent())) {
			this.addActionError(this.getText("error.page.remove.notAllowed"));
			return "pageTree";
		} else if (currentPage.getChildren().length != 0) {
			this.addActionError(this.getText("error.page.remove.notAllowed2"));
			return "pageTree";
        }
		return null;
	}
	
	protected void addActivityStreamInfo(IPage page, int strutsAction, boolean addLink) {
		ActivityStreamInfo asi = this.getPageActionHelper().createActivityStreamInfo(page, 
				strutsAction, addLink, "edit");
		super.addActivityStreamInfo(asi);
	}
	
	/**
	 * Return the list of allowed groups.
	 * @return The list of allowed groups.
	 */
	public List<Group> getAllowedGroups() {
		return this.getPageActionHelper().getAllowedGroups(this.getCurrentUser());
	}
	
	/**
	 * Return the list of system groups.
	 * @return The list of system groups.
	 */
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}
	
	public List<PageModel> getPageModels() {
		List<PageModel> models = new ArrayList<PageModel>(this.getPageModelManager().getPageModels());
		return models;
	}
	
	public PageModel getPageModel(String code) {
		return this.getPageModelManager().getPageModel(code);
	}
	
	public String getCopyPageCode() {
		return _copyPageCode;
	}
	public void setCopyPageCode(String copyPageCode) {
		this._copyPageCode = copyPageCode;
	}
	public boolean isDefaultShowlet() {
		return _defaultShowlet;
	}
	public void setDefaultShowlet(boolean defaultShowlet) {
		this._defaultShowlet = defaultShowlet;
	}
	public String getGroup() {
		return _group;
	}
	public void setGroup(String group) {
		this._group = group;
	}
	public boolean isGroupSelectLock() {
		return _groupSelectLock;
	}
	public void setGroupSelectLock(boolean groupSelectLock) {
		this._groupSelectLock = groupSelectLock;
	}
	public void setExtraGroups(Set<String> extraGroups) {
		this._extraGroups = extraGroups;
	}
	public Set<String> getExtraGroups() {
		return _extraGroups;
	}
	public String getModel() {
		return _model;
	}
	public void setModel(String model) {
		this._model = model;
	}
	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	public String getParentPageCode() {
		return _parentPageCode;
	}
	public void setParentPageCode(String parentPageCode) {
		this._parentPageCode = parentPageCode;
	}
	
	public boolean isShowable() {
		return _showable;
	}
	public void setShowable(boolean showable) {
		this._showable = showable;
	}
	
	public boolean isUseExtraTitles() {
		return _useExtraTitles;
	}
	public void setUseExtraTitles(boolean useExtraTitles) {
		this._useExtraTitles = useExtraTitles;
	}
	
	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getCharset() {
		return _charset;
	}
	public void setCharset(String charset) {
		this._charset = charset;
	}
	
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	
	public ApsProperties getTitles() {
		return _titles;
	}
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	
	public String getNodeToBeDelete() {
		return _nodeToBeDelete;
	}
	public void setNodeToBeDelete(String nodeToBeDelete) {
		this._nodeToBeDelete = nodeToBeDelete;
	}
	
	public IPage getPageToShow() {
		return _pageToShow;
	}
	protected void setPageToShow(IPage pageToShow) {
		this._pageToShow = pageToShow;
	}
	
	public Map getReferences() {
		return _references;
	}
	protected void setReferences(Map references) {
		this._references = references;
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
	
	@Deprecated
	protected IPageActionHelper getHelper() {
		return this.getPageActionHelper();
	}
	@Deprecated
	public void setHelper(IPageActionHelper helper) {
		this.setPageActionHelper(helper);
	}
	protected IPageActionHelper getPageActionHelper() {
		return _pageActionHelper;
	}
	public void setPageActionHelper(IPageActionHelper pageActionHelper) {
		this._pageActionHelper = pageActionHelper;
	}
	
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}
	
	private String _pageCode;
	private String _parentPageCode;
	private String _copyPageCode;
	private String _group;
	private boolean _groupSelectLock;
	private Set<String> _extraGroups = new HashSet<String>();
	private String _model;
	private boolean _defaultShowlet = false;
	private ApsProperties _titles = new ApsProperties();
	private boolean _showable = false;
	private boolean _useExtraTitles;
	private int _strutsAction;
	
	private String _mimeType;
	private String _charset;
	
	private String _nodeToBeDelete;
	
	private IPage _pageToShow;
	
	private Map _references;
	
	private String _allowedMimeTypesCSV;
	private String _allowedCharsetsCSV;
	
	private IPageModelManager _pageModelManager;
	private IPageActionHelper _pageActionHelper;
	
}
