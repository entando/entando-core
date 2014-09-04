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
package com.agiletec.plugins.jacms.apsadmin.portal;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.apsadmin.util.CmsPageActionUtil;

/**
 * @author E.Santoboni
 */
public class PageAction extends com.agiletec.apsadmin.portal.PageAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			if (this.getStrutsAction() != ApsAdminSystemConstants.EDIT) return;
			IContentManager contentManager = (IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, this.getRequest());
			IPage page = this.createTempPage();
			List<Content> contents = this.getPublishedContents(this.getPageCode());
			for (int i = 0; i < contents.size(); i++) {
				Content content = contents.get(i);
				if (null != content && !CmsPageActionUtil.isContentPublishableOnPage(content, page)) {
					List<String> contentGroups = new ArrayList<String>();
					contentGroups.add(content.getMainGroup());
					if (null != content.getGroups()) {
						contentGroups.addAll(content.getGroups());
					}
					this.addFieldError("extraGroups", this.getText("error.page.extraGoups.invalidGroupsForPublishedContent", 
							new String[]{contentGroups.toString(), content.getId(), content.getDescr()}));
				}
			}
			List<String> linkingContentsVo = ((PageUtilizer) contentManager).getPageUtilizers(this.getPageCode());
			if (null != linkingContentsVo) {
				for (int i = 0; i < linkingContentsVo.size(); i++) {
					String contentId = linkingContentsVo.get(i);
					Content linkingContent = contentManager.loadContent(contentId, true);
					if (null != linkingContent && !CmsPageActionUtil.isPageLinkableByContent(page, linkingContent)) {
						this.addFieldError("extraGroups", this.getText("error.page.extraGoups.pageHasToBeFree", 
								new String[]{linkingContent.getId(), linkingContent.getDescr()}));
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error on validate page", t);
			throw new RuntimeException("Error on validate page", t);
		}
	}
	
	private IPage createTempPage() {
		IPage pageOnEdit = (Page) this.getPageManager().getPage(this.getPageCode());
		Page page = new Page();
		page.setGroup(this.getGroup());
		page.setExtraGroups(this.getExtraGroups());
		page.setWidgets(pageOnEdit.getWidgets());
		page.setCode(this.getPageCode());
		return page;
	}
	
	/**
	 * Check if a page che publish a single content.
	 * @param page The page to check
	 * @return True if the page can publish a free content, else false.
	 */
	public boolean isFreeViewerPage(IPage page) {
		return CmsPageActionUtil.isFreeViewerPage(page, this.getViewerWidgetCode());
	}
	
	@Override
	protected IPage buildNewPage() throws ApsSystemException {
		IPage page = super.buildNewPage();
		this.checkViewerPage(page);
		return page;
	}
	
	@Override
	protected IPage getUpdatedPage() throws ApsSystemException {
		IPage page = super.getUpdatedPage();
		this.checkViewerPage(page);
		return page;
	}
	
	protected void checkViewerPage(IPage page) {
		int mainFrame = page.getModel().getMainFrame();
		if (this.isViewerPage() && mainFrame>-1) {
			IWidgetTypeManager showletTypeManager = (IWidgetTypeManager) ApsWebApplicationUtils.getBean(SystemConstants.WIDGET_TYPE_MANAGER, this.getRequest());
			Widget viewer = new Widget();
			viewer.setConfig(new ApsProperties());
			WidgetType type = showletTypeManager.getWidgetType(this.getViewerWidgetCode());
			if (null == type) {
				throw new RuntimeException("Widget 'Contenuto Singolo' assente o non valida : Codice " + this.getViewerWidgetCode());
			}
			viewer.setType(type);
			Widget[] widgets = page.getWidgets();
			widgets[mainFrame] = viewer;
		}
	}
	
	public List<Content> getPublishedContents(String pageCode) {
		List<Content> contents = new ArrayList<Content>();
		try {
			IPage page = this.getPage(pageCode);
			if (null == page) return contents;
			Widget[] widgets = page.getWidgets();
			for (int i=0; i<widgets.length; i++) {
				Widget widget = widgets[i];
				if (null != widget) {
					String contentId = widget.getPublishedContent();
					if (null != contentId) {
						Content content = this.getContentManager().loadContent(contentId, true);
						if (null != content) {
							contents.add(content);
						}
					}
				}
			}
		} catch (Throwable t) {
			String msg = "Error extracting published contents on page '" + pageCode + "'";
			_logger.error("Error extracting published contents on page '{}'", pageCode, t);
			throw new RuntimeException(msg, t);
		}
		return contents;
	}
	
	public List<ContentRecordVO> getReferencingContents(String pageCode) {
		List<ContentRecordVO> referencingContents = null;
		try {
			List<String> referencingContentsId = this.getReferencingContentsId(pageCode);
			if (null != referencingContentsId) {
				referencingContents = new ArrayList<ContentRecordVO>();
				for (int i = 0; i < referencingContentsId.size(); i++) {
					ContentRecordVO contentVo = this.getContentManager().loadContentVO(referencingContentsId.get(i));
					if (null != contentVo) referencingContents.add(contentVo);
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