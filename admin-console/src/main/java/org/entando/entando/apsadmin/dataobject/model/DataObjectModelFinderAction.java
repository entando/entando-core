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
package org.entando.entando.apsadmin.dataobject.model;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import java.util.List;

import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;

/**
 * Classi action delegata alle operazioni di erogazione e ricerca modelli di
 * DataObject in lista.
 *
 * @author E.Santoboni
 */
public class DataObjectModelFinderAction extends BaseAction {

	public List<DataObjectModel> getContentModels() {
		List<DataObjectModel> contentModels = null;
		if (null != this.getContentType() && this.getContentType().trim().length() > 0) {
			contentModels = this.getDataObjectModelManager().getModelsForDataObjectType(getContentType());
		} else {
			contentModels = this.getDataObjectModelManager().getDataObjectModels();
		}
		return contentModels;
	}

	public List<SmallEntityType> getSmallContentTypes() {
		return this.getDataObjectManager().getSmallEntityTypes();
	}

	public SmallEntityType getSmallContentType(String typeCode) {
		return this.getDataObjectManager().getSmallDataTypesMap().get(typeCode);
	}

	public String getContentType() {
		return _dataType;
	}

	public void setContentType(String contentType) {
		this._dataType = contentType;
	}

	protected IDataObjectModelManager getDataObjectModelManager() {
		return _dataObjectModelManager;
	}

	public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
		this._dataObjectModelManager = dataObjectModelManager;
	}

	protected IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	private IDataObjectModelManager _dataObjectModelManager;
	private IDataObjectManager _dataObjectManager;
	private String _dataType = "";

}
