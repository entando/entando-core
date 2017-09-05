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
package org.entando.entando.aps.system.services.datatype.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.aps.system.services.datatype.model.DataObject;

public class PublicDataChangedEvent extends ApsEvent {

	@Override
	public void notify(IManager srv) {
		((PublicDataChangedObserver) srv).updateFromPublicContentChanged(this);
	}

	public Class getObserverInterface() {
		return PublicDataChangedObserver.class;
	}

	public DataObject getContent() {
		return _content;
	}

	public void setContent(DataObject content) {
		this._content = content;
	}

	public int getOperationCode() {
		return _operationCode;
	}

	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	private DataObject _content;

	private int _operationCode;

	public static final int INSERT_OPERATION_CODE = 1;

	public static final int REMOVE_OPERATION_CODE = 2;

	public static final int UPDATE_OPERATION_CODE = 3;

}
