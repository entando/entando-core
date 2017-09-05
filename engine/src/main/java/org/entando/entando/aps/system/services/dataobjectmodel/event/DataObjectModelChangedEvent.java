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
package org.entando.entando.aps.system.services.dataobjectmodel.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;

/**
 * Evento specifico da rilanciare in corrispondenza di modifica di un modello di
 * data type.
 *
 * @author E.Santoboni - C.Siddi
 */
public class DataObjectModelChangedEvent extends ApsEvent {

	@Override
	public void notify(IManager srv) {
		((DataObjectModelChangedObserver) srv).updateFromContentModelChanged(this);
	}

	@Override
	public Class getObserverInterface() {
		return DataObjectModelChangedObserver.class;
	}

	public DataObjectModel getContentModel() {
		return _contentModel;
	}

	public void setContentModel(DataObjectModel contentModel) {
		this._contentModel = contentModel;
	}

	public int getOperationCode() {
		return _operationCode;
	}

	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	private DataObjectModel _contentModel;

	private int _operationCode;

	public static final int INSERT_OPERATION_CODE = 1;

	public static final int REMOVE_OPERATION_CODE = 2;

	public static final int UPDATE_OPERATION_CODE = 3;

}
