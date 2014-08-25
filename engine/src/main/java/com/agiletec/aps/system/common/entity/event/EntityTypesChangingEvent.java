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
package com.agiletec.aps.system.common.entity.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * This class manages the events triggered by the change of the Entity Types.
 * @author E.Santoboni
 */
public class EntityTypesChangingEvent extends ApsEvent {
	
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