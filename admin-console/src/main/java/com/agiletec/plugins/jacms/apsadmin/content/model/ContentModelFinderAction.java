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
package com.agiletec.plugins.jacms.apsadmin.content.model;

import java.util.List;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

/**
 * Classi action delegata alle operazioni 
 * di erogazione e ricerca modelli di contenuti in lista.
 * @author E.Santoboni
 */
public class ContentModelFinderAction extends BaseAction implements IContentModelFinderAction {
	
	@Override
	public List<ContentModel> getContentModels() {
		List<ContentModel> contentModels = null;
		if (null != this.getContentType() && this.getContentType().trim().length() > 0) {
			contentModels = this.getContentModelManager().getModelsForContentType(getContentType());
		} else {
			contentModels =  this.getContentModelManager().getContentModels();
		}
		return contentModels;
	}

	public List<SmallContentType> getSmallContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	public SmallContentType getSmallContentType(String typeCode) {
		return this.getContentManager().getSmallContentTypesMap().get(typeCode);
	}
	
	public String getContentType() {
		return _contentType;
	}
	
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	public IContentManager getContentManager() {
		return _contentManager;
	}

	private IContentModelManager _contentModelManager;
	private IContentManager _contentManager;
	private String _contentType = "";

}
