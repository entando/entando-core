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
package org.entando.entando.aps.system.services.userprofile.event;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * @author E.Santoboni
 */
public class ProfileChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((ProfileChangedObserver) srv).updateFromProfileChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return ProfileChangedObserver.class;
	}

	public IUserProfile getProfile() {
		return _profile;
	}
	public void setProfile(IUserProfile profile) {
		this._profile = profile;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	private IUserProfile _profile;
	
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	
	public static final int REMOVE_OPERATION_CODE = 2;
	
	public static final int UPDATE_OPERATION_CODE = 3;
	
}
