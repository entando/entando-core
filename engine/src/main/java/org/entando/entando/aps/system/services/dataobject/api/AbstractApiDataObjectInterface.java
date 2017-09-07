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
package org.entando.entando.aps.system.services.dataobject.api;

import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * @author E.Santoboni
 */
public abstract class AbstractApiDataObjectInterface {

	public IDataObjectModelManager getDataObjectModelManager() {
		return _dataObjectModelManager;
	}

	public void setDataObjectModelManager(IDataObjectModelManager dataObjectModelManager) {
		this._dataObjectModelManager = dataObjectModelManager;
	}

	public IDataObjectManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IDataObjectManager _dataObjectManager) {
		this._dataObjectManager = _dataObjectManager;
	}

	private IDataObjectManager _dataObjectManager;
	private IDataObjectModelManager _dataObjectModelManager;

}
