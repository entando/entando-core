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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.apsadmin.util.CmsPageActionUtil;
import com.agiletec.plugins.jacms.apsadmin.util.ResourceIconUtil;

/**
 * Action principale per la redazione contenuti.
 * @author E.Santoboni
 */
public class ContentAction extends AbstractContentAction implements IContentAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentAction.class);
	
	@Override
	public void validate() {
		Content content = this.updateContentOnSession();
		super.validate();
		this.getContentActionHelper().scanEntity(content, this);
	}

	@Override
	public String edit() {
		try {
			Content content = this.getContentManager().loadContent(this.getContentId(), false);
			if (null == content) {
				throw new ApsSystemException("Contenuto in edit '" + this.getContentId() + "' nullo!");
			}
			if (!this.isUserAllowed(content)) {
				_logger.info("Utente non abilitato all'editazione del contenuto {}", content.getId());
				return USER_NOT_ALLOWED;
			}
			String marker = buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
			super.setContentOnSessionMarker(marker);
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + marker, content);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			//ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String copyPaste() {
		try {
			Content content = this.getContentManager().loadContent(this.getContentId(), this.isCopyPublicVersion());
			if (null == content) {
				throw new ApsSystemException("Contenuto in copyPaste '"
						+ this.getContentId() + "' nullo ; copia di contenuto pubblico " + this.isCopyPublicVersion());
			}
			if (!this.isUserAllowed(content)) {
				_logger.info("Utente non abilitato all'accesso del contenuto {}", content.getId());
				return USER_NOT_ALLOWED;
			}
			String marker = buildContentOnSessionMarker(content, ApsAdminSystemConstants.PASTE);
			content.setId(null);
			content.setVersion(Content.INIT_VERSION);
			content.setDescr(this.getText("label.copyOf") + " " + content.getDescr());
			super.setContentOnSessionMarker(marker);
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + marker, content);
		} catch (Throwable t) {
			_logger.error("error in copyPaste", t);
			//ApsSystemUtils.logThrowable(t, this, "copyPaste");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String forwardToEntryContent() {
		return SUCCESS;
	}

	public String configureMainGroup() {
		Content content = this.updateContentOnSession();
		try {
			if (null == content.getId() && null == content.getMainGroup()) {
				String mainGroup = this.getRequest().getParameter("mainGroup");
				if (mainGroup != null && null != this.getGroupManager().getGroup(mainGroup)) {
					content.setMainGroup(mainGroup);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in setMainGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "setMainGroup");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	@Deprecated (/** From jAPS 2.0 version 2.1, use joinCategory of {@link IContentCategoryAction} action */)
	public String joinCategory() {
		this.updateContentOnSession();
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.getCode().equals(category.getParentCode())
					&& !this.getContent().getCategories().contains(category)) {
				this.getContent().addCategory(category);
			}
		} catch (Throwable t) {
			_logger.error("error in joinCategory", t);
			//ApsSystemUtils.logThrowable(t, this, "joinCategory");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	@Deprecated (/** From jAPS 2.0 version 2.1, use removeCategory of {@link IContentCategoryAction} action */)
	public String removeCategory() {
		this.updateContentOnSession();
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				this.getContent().removeCategory(category);
			}
		} catch (Throwable t) {
			_logger.error("error in removeCategory", t);
			//ApsSystemUtils.logThrowable(t, this, "removeCategory");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String joinGroup() {
		this.updateContentOnSession();
		try {
			String extraGroupName = this.getExtraGroupName();
			Group group = this.getGroupManager().getGroup(extraGroupName);
			if (null != group) {
				this.getContent().addGroup(extraGroupName);
			}
		} catch (Throwable t) {
			_logger.error("error in joinGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "joinGroup");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String removeGroup() {
		this.updateContentOnSession();
		try {
			String extraGroupName = this.getExtraGroupName();
			Group group = this.getGroupManager().getGroup(extraGroupName);
			if (null != group) {
				this.getContent().getGroups().remove(group.getName());
			}
		} catch (Throwable t) {
			_logger.error("error in removeGroup", t);
			//ApsSystemUtils.logThrowable(t, this, "removeGroup");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String saveAndContinue() {
		try {
			Content currentContent = this.updateContentOnSession();
			if (null != currentContent) {
				if (null == currentContent.getDescr() || currentContent.getDescr().trim().length() == 0) {
					this.addFieldError("descr", this.getText("error.content.descr.required"));
				} else {
					currentContent.setLastEditor(this.getCurrentUser().getUsername());
					this.getContentManager().saveContent(currentContent);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in saveAndContinue", t);
			//ApsSystemUtils.logThrowable(t, this, "saveAndContinue");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String saveContent() {
		return this.saveContent(false);
	}

	@Override
	public String saveAndApprove() {
		return this.saveContent(true);
	}

	protected String saveContent(boolean approve) {
		try {
			Content currentContent = this.getContent();
			if (null != currentContent) {
				int strutsAction = (null == currentContent.getId()) ? ApsAdminSystemConstants.ADD : ApsAdminSystemConstants.EDIT;
				if (approve) {
					strutsAction += 10;
				}
				if (!this.getContentActionHelper().isUserAllowed(currentContent, this.getCurrentUser())) {
					_logger.info("User not allowed to save content {}", currentContent.getId());
					return USER_NOT_ALLOWED;
				}
				currentContent.setLastEditor(this.getCurrentUser().getUsername());
				if (approve) {
					this.getContentManager().insertOnLineContent(currentContent);
				} else {
					this.getContentManager().saveContent(currentContent);
				}
				this.addActivityStreamInfo(currentContent, strutsAction, true);
				this.getRequest().getSession().removeAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + super.getContentOnSessionMarker());
				_logger.info("Saving content {} - Description: '{}' - User: {}",   currentContent.getId(), currentContent.getDescr(), this.getCurrentUser().getUsername());
			} else {
				_logger.error("Save/approve NULL content - User: {}", this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			_logger.error("error in saveContent", t);
			//ApsSystemUtils.logThrowable(t, this, "saveContent");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String suspend() {
		try {
			Content currentContent = this.updateContentOnSession();
			if (null != currentContent) {
				if (!this.getContentActionHelper().isUserAllowed(currentContent, this.getCurrentUser())) {
					_logger.info("User not allowed to unpublish content {}", currentContent.getId());
					return USER_NOT_ALLOWED;
				}
				Map references = this.getContentActionHelper().getReferencingObjects(currentContent, this.getRequest());
				if (references.size()>0) {
					this.setReferences(references);
					return "references";
				}
				this.getContentManager().removeOnLineContent(currentContent);
				this.addActivityStreamInfo(currentContent, (ApsAdminSystemConstants.DELETE + 10), true);
				this.getRequest().getSession().removeAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + super.getContentOnSessionMarker());
				_logger.info("Content {} suspended - Description: '{}' - Utente: {}", currentContent.getId(), currentContent.getDescr(), this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			_logger.error("error in suspend", t);
			//ApsSystemUtils.logThrowable(t, this, "suspend");
			return FAILURE;
		}
		return SUCCESS;
	}

	public int[] getLinkDestinations() {
		return SymbolicLink.getDestinationTypes();
	}

	@Deprecated (/** From jAPS 2.0 version 2.1, use {@link IContentCategoryAction} action */)
	public Category getCategoryRoot() {
		return (Category) this.getCategoryManager().getRoot();
	}

	public IPage getPage(String pageCode) {
		return this.getPageManager().getPage(pageCode);
	}

	public String getHtmlEditorCode() {
		return this.getConfigManager().getParam("hypertextEditor");
	}

	/**
	 * Restituice la lista di pagine dove è pubblicato il contenuto in fase di redazione.
	 * @return La lista di pagine che referenziano dal contenuto.
	 * @deprecated From jAPS 2.0 version 2.0.9, use getShowingPageSelectItems()
	 */
	public List<IPage> getShowingPages() {
		List<IPage> pages = new ArrayList<IPage>();
		try {
			Content content = this.getContent();
			if (null != content && null != content.getId()) {
				IPageManager pageManager = this.getPageManager();
				pages = ((ContentUtilizer) pageManager).getContentUtilizers(content.getId());
			}
		} catch (Throwable t) {
			_logger.error("error loading referenced pages", t);
			//ApsSystemUtils.logThrowable(t, this, "getShowingPages");
			throw new RuntimeException("Errore in estrazione pagine referenziate", t);
		}
		return pages;
	}

	/**
	 * Return the list of the showing pages of the current content on edit
	 * @return The list of the showing pages.
	 */
	public List<SelectItem> getShowingPageSelectItems() {
		List<SelectItem> pageItems = new ArrayList<SelectItem>();
		try {
			Content content = this.getContent();
			if (null != content) {
				IPage defaultViewerPage = this.getPageManager().getPage(content.getViewPage());
				if (null != defaultViewerPage && CmsPageActionUtil.isFreeViewerPage(defaultViewerPage, null)) {
					pageItems.add(new SelectItem("", this.getText("label.default")));
				}
				if (null == content.getId()) return pageItems;
				IPageManager pageManager = this.getPageManager();
				List<IPage> pages = ((ContentUtilizer) pageManager).getContentUtilizers(content.getId());
				for (int i = 0; i < pages.size(); i++) {
					IPage page = pages.get(i);
					String pageCode = page.getCode();
					pageItems.add(new SelectItem(pageCode, super.getTitle(pageCode, page.getTitles())));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on extracting showing pages", t);
			//ApsSystemUtils.logThrowable(t, this, "getShowingPageSelectItems");
			throw new RuntimeException("Error on extracting showing pages", t);
		}
		return pageItems;
	}

	public String getIconFile(String fileName) {
		return this.getResourceIconUtil().getIconFile(fileName);
	}

	@Deprecated (/** From jAPS 2.0 version 2.1, use {@link IContentCategoryAction} action */)
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	@Deprecated (/** From jAPS 2.0 version 2.1, use {@link IContentCategoryAction} action */)
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
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

	@Deprecated (/** From jAPS 2.0 version 2.1, use {@link IContentCategoryAction} action */)
	public String getCategoryCode() {
		return _categoryCode;
	}
	@Deprecated (/** From jAPS 2.0 version 2.1, use {@link IContentCategoryAction} action */)
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
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

	protected ResourceIconUtil getResourceIconUtil() {
		return _resourceIconUtil;
	}
	public void setResourceIconUtil(ResourceIconUtil resourceIconUtil) {
		this._resourceIconUtil = resourceIconUtil;
	}

	private ICategoryManager _categoryManager;
	private IPageManager _pageManager;
	private ConfigInterface _configManager;

	private Map _references;

	private String _contentId;

	private String _categoryCode;
	private String _extraGroupName;

	private boolean _copyPublicVersion;

	private ResourceIconUtil _resourceIconUtil;

}
