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
package com.agiletec.aps.system.common.entity.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * This class manages the events triggered by the change of the Entity Types.
 * @author E.Santoboni
 */
public class EntityTypesChangingEvent extends ApsEvent {
	
	@Override
	public Class getObserverInterface() {
		return EntityTypesChangingObserver.class;
	}
	
	@Override
	public void notify(IManager srv) {
		((EntityTypesChangingObserver) srv).updateFromEntityTypesChanging(this);
	}
	
	public String getEntityManagerName() {
		return _entityManagerName;
	}
	public void setEntityManagerName(String entityManagerName) {
		this._entityManagerName = entityManagerName;
	}
	
	public IApsEntity getOldEntityType() {
		return _oldEntityType;
	}
	public void setOldEntityType(IApsEntity oldEntityType) {
		this._oldEntityType = oldEntityType;
	}
	
	public IApsEntity getNewEntityType() {
		return _newEntityType;
	}
	public void setNewEntityType(IApsEntity newEntityType) {
		this._newEntityType = newEntityType;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	private String _entityManagerName;
	
	private IApsEntity _oldEntityType;
	private IApsEntity _newEntityType;
	
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;
	
}