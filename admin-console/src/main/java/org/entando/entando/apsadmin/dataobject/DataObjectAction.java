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
package org.entando.entando.apsadmin.dataobject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Action principale per la redazione DataObject.
 *
 * @author E.Santoboni
 */
public class DataObjectAction extends AbstractDataObjectAction {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectAction.class);

	@Override
	public void validate() {
		DataObject content = this.updateDataObjectOnSession(true);
		super.validate();
		this.getDataObjectActionHelper().scanEntity(content, this);
	}

	/**
	 * Esegue l'azione di edit di un DataObject.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String edit() {
		try {
			DataObject content = this.getDataObjectManager().loadDataObject(this.getContentId(), false);
			if (null == content) {
				throw new ApsSystemException("DataObject in edit '" + this.getContentId() + "' nullo!");
			}
			if (!this.isUserAllowed(content)) {
				_logger.info("Utente non abilitato all'editazione del DataObject {}", content.getId());
				return USER_NOT_ALLOWED;
			}
			String marker = buildDataObjectOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
			super.setContentOnSessionMarker(marker);
			this.getRequest().getSession().setAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX + marker, content);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Esegue l'azione di copia/incolla di un DataObject.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String copyPaste() {
		try {
			DataObject dataObject = this.getDataObjectManager().loadDataObject(this.getContentId(), this.isCopyPublicVersion());
			if (null == dataObject) {
				throw new ApsSystemException("DataObject in copyPaste '" + this.getContentId() + "' nullo ; copia di DataObject pubblico "
						+ this.isCopyPublicVersion());
			}
			if (!this.isUserAllowed(dataObject)) {
				_logger.info("Utente non abilitato all'accesso del DataObject {}", dataObject.getId());
				return USER_NOT_ALLOWED;
			}
			String marker = buildDataObjectOnSessionMarker(dataObject, ApsAdminSystemConstants.PASTE);
			dataObject.setId(null);
			dataObject.setVersion(DataObject.INIT_VERSION);
			dataObject.setDescription(this.getText("label.copyOf") + " " + dataObject.getDescription());
			dataObject.setFirstEditor(this.getCurrentUser().getUsername());
			super.setContentOnSessionMarker(marker);
			this.getRequest().getSession().setAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX + marker, dataObject);
		} catch (Throwable t) {
			_logger.error("error in copyPaste", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String forwardToEntryContent() {
		return SUCCESS;
	}

	public String configureMainGroup() {
		DataObject content = this.updateDataObjectOnSession();
		try {
			if (/* null == content.getId() && */null == content.getMainGroup()) {
				String mainGroup = this.getRequest().getParameter("mainGroup");
				if (mainGroup != null && null != this.getGroupManager().getGroup(mainGroup)) {
					content.setMainGroup(mainGroup);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in setMainGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Esegue l'azione di associazione di un gruppo al DataObject in fase di
	 * redazione.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String joinGroup() {
		this.updateDataObjectOnSession();
		try {
			String extraGroupName = this.getExtraGroupName();
			Group group = this.getGroupManager().getGroup(extraGroupName);
			if (null != group) {
				this.getContent().addGroup(extraGroupName);
			}
		} catch (Throwable t) {
			_logger.error("error in joinGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Esegue l'azione di rimozione di un gruppo dal DataObject in fase di
	 * redazione.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeGroup() {
		this.updateDataObjectOnSession();
		try {
			String extraGroupName = this.getExtraGroupName();
			Group group = this.getGroupManager().getGroup(extraGroupName);
			if (null != group) {
				this.getContent().getGroups().remove(group.getName());
			}
		} catch (Throwable t) {
			_logger.error("error in removeGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String saveAndContinue() {
		try {
			DataObject currentDataObject = this.updateDataObjectOnSession();
			if (null != currentDataObject) {
				String descr = currentDataObject.getDescription();
				if (StringUtils.isEmpty(descr)) {
					this.addFieldError("descr", this.getText("error.content.descr.required"));
				} else if (StringUtils.isEmpty(currentDataObject.getMainGroup())) {
					this.addFieldError("mainGroup", this.getText("error.content.mainGroup.required"));
				} else {
					currentDataObject.setLastEditor(this.getCurrentUser().getUsername());
					this.getDataObjectManager().saveDataObject(currentDataObject);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in saveAndContinue", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Esegue l'azione di salvataggio del DataObject in fase di redazione.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String saveDataObject() {
		return this.saveDataObject(false);
	}

	/**
	 * Esegue l'azione di salvataggio e pubblicazione del contenuto in fase di
	 * redazione.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String saveAndApprove() {
		return this.saveDataObject(true);
	}

	protected String saveDataObject(boolean approve) {
		try {
			DataObject currentContent = this.getContent();
			if (null != currentContent) {
				int strutsAction = (null == currentContent.getId()) ? ApsAdminSystemConstants.ADD : ApsAdminSystemConstants.EDIT;
				if (approve) {
					strutsAction += 10;
				}
				if (!this.getDataObjectActionHelper().isUserAllowed(currentContent, this.getCurrentUser())) {
					_logger.info("User not allowed to save DataObject {}", currentContent.getId());
					return USER_NOT_ALLOWED;
				}
				currentContent.setLastEditor(this.getCurrentUser().getUsername());
				if (approve) {
					this.getDataObjectManager().insertOnLineDataObject(currentContent);
				} else {
					this.getDataObjectManager().saveDataObject(currentContent);
				}
				this.addActivityStreamInfo(currentContent, strutsAction, true);
				this.getRequest().getSession().removeAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX
						+ super.getContentOnSessionMarker());
				this.getRequest().getSession().removeAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_GROUP);
				_logger.info("Saving content {} - Description: '{}' - User: {}", currentContent.getId(), currentContent.getDescription(),
						this.getCurrentUser().getUsername());
			} else {
				_logger.error("Save/approve NULL content - User: {}", this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			_logger.error("error in saveContent", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Esegue l'azione di rimozione del contenuto pubblico e salvataggio del
	 * contenuto in fase di redazione.
	 *
	 * @return Il codice del risultato dell'azione.
	 */
	public String suspend() {
		try {
			DataObject currentContent = this.updateDataObjectOnSession();
			if (null != currentContent) {
				if (!this.getDataObjectActionHelper().isUserAllowed(currentContent, this.getCurrentUser())) {
					_logger.info("User not allowed to unpublish DataObject {}", currentContent.getId());
					return USER_NOT_ALLOWED;
				}
				Map references = this.getDataObjectActionHelper().getReferencingObjects(currentContent, this.getRequest());
				if (references.size() > 0) {
					this.setReferences(references);
					return "references";
				}
				this.getDataObjectManager().removeOnLineDataObject(currentContent);
				this.addActivityStreamInfo(currentContent, (ApsAdminSystemConstants.DELETE + 10), true);
				this.getRequest().getSession().removeAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX
						+ super.getContentOnSessionMarker());
				_logger.info("Content {} suspended - Description: '{}' - Utente: {}", currentContent.getId(), currentContent
						.getDescription(), this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			_logger.error("error in suspend", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public IPage getPage(String pageCode) {
		return this.getPageManager().getOnlinePage(pageCode);
	}

	public String getHtmlEditorCode() {
		return this.getConfigManager().getParam("hypertextEditor");
	}

	/**
	 * Return the list of the showing pages of the current content on edit
	 *
	 * @return The list of the showing pages.
	 */
	public List<SelectItem> getShowingPageSelectItems() {
		List<SelectItem> pageItems = new ArrayList<SelectItem>();
		try {
			DataObject content = this.getContent();
			if (null != content) {
				IPage defaultViewerPage = this.getPageManager().getOnlinePage(content.getViewPage());
				if (null != defaultViewerPage && CmsPageUtil.isOnlineFreeViewerPage(defaultViewerPage, null)) {
					pageItems.add(new SelectItem("", this.getText("label.default")));
				}
				if (null == content.getId()) {
					return pageItems;
				}
				/*
				DataTypeUtilizer pageManagerWrapper = (DataTypeUtilizer) ApsWebApplicationUtils.getBean(
						JacmsSystemConstants.PAGE_MANAGER_WRAPPER, this.getRequest());
				List<IPage> pages = pageManagerWrapper.getContentUtilizers(content.getId());
				for (int i = 0; i < pages.size(); i++) {
					IPage page = pages.get(i);
					String pageCode = page.getCode();
					pageItems.add(new SelectItem(pageCode, super.getTitle(pageCode, page.getTitles())));
				}
				 */
			}
		} catch (Throwable t) {
			_logger.error("Error on extracting showing pages", t);
			throw new RuntimeException("Error on extracting showing pages", t);
		}
		return pageItems;
	}

	public Map getReferences() {
		return _references;
	}

	protected void setReferences(Map references) {
		this._references = references;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public String getContentId() {
		return _contentId;
	}

	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getExtraGroupName() {
		return _extraGroupName;
	}

	public void setExtraGroupName(String extraGroupName) {
		this._extraGroupName = extraGroupName;
	}

	public boolean isCopyPublicVersion() {
		return _copyPublicVersion;
	}

	public void setCopyPublicVersion(boolean copyPublicVersion) {
		this._copyPublicVersion = copyPublicVersion;
	}

	private IPageManager _pageManager;
	private ConfigInterface _configManager;

	private Map _references;

	private String _contentId;

	private String _extraGroupName;

	private boolean _copyPublicVersion;

}
