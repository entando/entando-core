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
package com.agiletec.plugins.jacms.apsadmin.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;

/**
 * @author E.Santoboni
 */
public class PageAction extends com.agiletec.apsadmin.portal.PageAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageAction.class);

	@Override
	public void validate() {
		super.validate();
		try {
			if (this.getStrutsAction() != ApsAdminSystemConstants.EDIT) {
				return;
			}
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.getRequest());
			IPage page = this.createTempPage();
			Collection<Content> contents = this.getPublishedContents(this.getPageCode());
			for (Content content : contents) {
				if (null != content && !CmsPageUtil.isContentPublishableOnPageDraft(content, page)) {
					List<String> contentGroups = new ArrayList<String>();
					contentGroups.add(content.getMainGroup());
					if (null != content.getGroups()) {
						contentGroups.addAll(content.getGroups());
					}
					this.addFieldError("extraGroups", this.getText("error.page.extraGoups.invalidGroupsForPublishedContent",
							new String[]{contentGroups.toString(), content.getId(), content.getDescription()}));
				}
			}
			List<String> linkingContentsVo = ((PageUtilizer) contentManager).getPageUtilizers(this.getPageCode());
			if (null != linkingContentsVo) {
				for (int i = 0; i < linkingContentsVo.size(); i++) {
					String contentId = linkingContentsVo.get(i);
					Content linkingContent = contentManager.loadContent(contentId, true);
					if (null != linkingContent && !CmsPageUtil.isPageLinkableByContentDraft(page, linkingContent)) {
						this.addFieldError("extraGroups", this.getText("error.page.extraGoups.pageHasToBeFree",
								new String[]{linkingContent.getId(), linkingContent.getDescription()}));
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on validate page", t);
			throw new RuntimeException("Error on validate page", t);
		}
	}

	private IPage createTempPage() {
		IPage pageOnEdit = (Page) this.getPage(this.getPageCode());
		Page page = new Page();
		page.setGroup(this.getGroup());
		PageMetadata metadata = new PageMetadata();
		metadata.setExtraGroups(this.getExtraGroups());
		page.setMetadata(metadata);
		page.setWidgets(pageOnEdit.getWidgets());
		page.setCode(this.getPageCode());
		return page;
	}

	/**
	 * Check if a page che publish a single content.
	 *
	 * @param page The page to check
	 * @return True if the page can publish a free content, else false.
	 */
	public boolean isFreeViewerPage(IPage page) {
		return CmsPageUtil.isDraftFreeViewerPage(page, this.getViewerWidgetCode());
	}

	public String setViewerPageAPI() {
		IPage page = null;
		try {
			page = this.getPage(this.getPageCode());
			int mainFrame = page.getMetadata().getModel().getMainFrame();
			if (mainFrame > -1) {
				IWidgetTypeManager showletTypeManager = (IWidgetTypeManager) ApsWebApplicationUtils.getBean(SystemConstants.WIDGET_TYPE_MANAGER, this.getRequest());
				Widget viewer = new Widget();
				viewer.setConfig(new ApsProperties());
				WidgetType type = showletTypeManager.getWidgetType(this.getViewerWidgetCode());
				if (null == type) {
					_logger.warn("No widget found for on-the-fly publishing config for page {}", page.getCode());
					return SUCCESS;
				}
				viewer.setType(type);
				Widget[] widgets = page.getWidgets();
				widgets[mainFrame] = viewer;
			}
			this.getPageManager().updatePage(page);
		} catch (Throwable t) {
			_logger.error("Error setting on-the-fly publishing config to page {}", page.getCode(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Collection<Content> getPublishedContents(String pageCode) {
		Set<Content> contents = new HashSet<Content>();
		try {
			IPage page = this.getOnlinePage(pageCode);
			if (null == page) {
				return contents;
			}
			this.addPublishedContents(page.getWidgets(), contents);
		} catch (Throwable t) {
			String msg = "Error extracting published contents on page '" + pageCode + "'";
			_logger.error("Error extracting published contents on page '{}'", pageCode, t);
			throw new RuntimeException(msg, t);
		}
		return contents;
	}

	public Collection<Content> getOnlinePublishedContents(String pageCode) {
		Collection<Content> contents = new HashSet<Content>();
		try {
			IPage page = this.getOnlinePage(pageCode);
			if (page != null) {
				this.addPublishedContents(page.getWidgets(), contents);
			}
		} catch (Throwable t) {
			String msg = "Error extracting published contents on page '" + pageCode + "'";
			_logger.error("Error extracting published contents on page '{}'", pageCode, t);
			throw new RuntimeException(msg, t);
		}
		return contents;
	}

	protected void addPublishedContents(Widget[] widgets, Collection<Content> contents) {
		try {
			if (widgets != null) {
				for (Widget widget : widgets) {
					ApsProperties config = (null != widget) ? widget.getConfig() : null;
					if (null == config || config.isEmpty()) {
						continue;
					}
					String extracted = config.getProperty("contentId");
					this.addContent(contents, extracted);
					String contentsParam = config.getProperty("contents");
					List<Properties> properties = (null != contentsParam) ? RowContentListHelper.fromParameterToContents(contentsParam) : null;
					if (null == properties || properties.isEmpty()) {
						continue;
					}
					for (int j = 0; j < properties.size(); j++) {
						Properties widgProp = properties.get(j);
						String extracted2 = widgProp.getProperty("contentId");
						this.addContent(contents, extracted2);
					}
				}
			}
		} catch (Throwable t) {
			String msg = "Error extracting published contents on page";
			_logger.error("Error extracting published contents on page", t);
			throw new RuntimeException(msg, t);
		}
	}

	private void addContent(Collection<Content> contents, String contentId) {
		try {
			if (null != contentId) {
				Content content = this.getContentManager().loadContent(contentId, true);
				if (null != content) {
					contents.add(content);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting published content '{}'", contentId, t);
		}
	}

	public List<ContentRecordVO> getReferencingContents(String pageCode) {
		List<ContentRecordVO> referencingContents = null;
		try {
			List<String> referencingContentsId = this.getReferencingContentsId(pageCode);
			if (null != referencingContentsId) {
				referencingContents = new ArrayList<ContentRecordVO>();
				for (int i = 0; i < referencingContentsId.size(); i++) {
					ContentRecordVO contentVo = this.getContentManager().loadContentVO(referencingContentsId.get(i));
					if (null != contentVo) {
						referencingContents.add(contentVo);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error getting referencing contents by page '{}'", pageCode, t);
			String msg = "Error getting referencing contents by page '" + pageCode + "'";
			throw new RuntimeException(msg, t);
		}
		return referencingContents;
	}

	public List<String> getReferencingContentsId(String pageCode) {
		List<String> referencingContentsId = null;
		try {
			referencingContentsId = ((PageUtilizer) this.getContentManager()).getPageUtilizers(pageCode);
		} catch (Throwable t) {
			_logger.error("Error getting referencing contents by page '{}'", pageCode, t);
			String msg = "Error getting referencing contents by page '" + pageCode + "'";
			throw new RuntimeException(msg, t);
		}
		return referencingContentsId;
	}

	public boolean isViewerPage() {
		return _viewerPage;
	}

	public void setViewerPage(boolean viewerPage) {
		this._viewerPage = viewerPage;
	}

	@Deprecated
	protected String getViewerShowletCode() {
		return this.getViewerWidgetCode();
	}

	@Deprecated
	public void setViewerShowletCode(String viewerShowletCode) {
		this.setViewerWidgetCode(viewerShowletCode);
	}

	protected String getViewerWidgetCode() {
		return _viewerWidgetCode;
	}

	public void setViewerWidgetCode(String viewerWidgetCode) {
		this._viewerWidgetCode = viewerWidgetCode;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	private boolean _viewerPage;
	private String _viewerWidgetCode;

	private IContentManager _contentManager;

}
