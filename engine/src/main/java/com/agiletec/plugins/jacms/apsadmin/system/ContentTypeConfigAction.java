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
package com.agiletec.plugins.jacms.apsadmin.system;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.system.entity.type.EntityTypeConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.apsadmin.util.CmsPageActionUtil;

/**
 * @author E.Santoboni
 */
public class ContentTypeConfigAction extends EntityTypeConfigAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(ContentTypeConfigAction.class);
	
	@Override
	protected IApsEntity updateEntityOnSession() {
		Content contentType = (Content) super.updateEntityOnSession();
		contentType.setViewPage(this.getViewPageCode());
		if (null != this.getListModelId()) {
			contentType.setListModel(this.getListModelId().toString());
		}
		if (null != this.getDefaultModelId()) {
			contentType.setDefaultModel(this.getDefaultModelId().toString());
		}
		return contentType;
	}
	
	/**
	 * Return a plain list of the free viewer pages in the portal.
	 * @return the list of the free viewer pages of the portal.
	 */
	public List<IPage> getFreeViewerPages() {
		IPage root = this.getPageManager().getRoot();
		List<IPage> pages = new ArrayList<IPage>();
		this.addPages(root, pages);
		return pages;
	}
	
	private void addPages(IPage page, List<IPage> pages) {
		if (page.getGroup().equals(Group.FREE_GROUP_NAME) && CmsPageActionUtil.isFreeViewerPage(page, null)) {
			pages.add(page);
		}
		IPage[] children = page.getChildren();
		for (int i=0; i<children.length; i++) {
			this.addPages(children[i], pages);
		}
	}
	
	/**
	 * Return the list of contentmodel given the content type code.
	 * @param typeCode The content type code.
	 * @return The Content Models found
	 */
	public List<ContentModel> getContentModels(String typeCode) {
		if (null == typeCode) return new ArrayList<ContentModel>();
		List<ContentModel> models = null;
		try {
			models = this.getContentModelManager().getModelsForContentType(typeCode);
		} catch (Throwable t) {
			_logger.error("Error on extracting models by type  {}", typeCode, t);
			//ApsSystemUtils.logThrowable(t, this, "getModels");
			throw new RuntimeException("Error on extracting models by type " + typeCode, t);
		}
		return models;
	}
	
	public String getViewPageCode() {
		return _viewPageCode;
	}
	public void setViewPageCode(String viewPageCode) {
		this._viewPageCode = viewPageCode;
	}
	
	public Integer getListModelId() {
		return _listModelId;
	}
	public void setListModelId(Integer listModelId) {
		this._listModelId = listModelId;
	}
	
	public Integer getDefaultModelId() {
		return _defaultModelId;
	}
	public void setDefaultModelId(Integer defaultModelId) {
		this._defaultModelId = defaultModelId;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	private String _viewPageCode;
	private Integer _listModelId;
	private Integer _defaultModelId;
	
	private IPageManager _pageManager;
	private IContentModelManager _contentModelManager;
	
}