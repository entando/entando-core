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
package org.entando.entando.aps.system.services.actionlog.model;

import java.util.Date;
import java.util.List;

/**
 * @author E.Santoboni - S.Puddu
 */
public class ActionLogRecordSearchBean implements IActionLogRecordSearchBean {

	@Override
	public Date getStartCreation() {
		return _startCreation;
	}

	public void setStartCreation(Date startCreation) {
		this._startCreation = startCreation;
	}

	@Override
	public Date getEndCreation() {
		return _endCreation;
	}

	public void setEndCreation(Date endCreation) {
		this._endCreation = endCreation;
	}

	@Override
	public Date getStartUpdate() {
		return _startUpdate;
	}

	public void setStartUpdate(Date startUpdate) {
		this._startUpdate = startUpdate;
	}

	@Override
	public Date getEndUpdate() {
		return _endUpdate;
	}

	public void setEndUpdate(Date endUpdate) {
		this._endUpdate = endUpdate;
	}
	
	@Override
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	@Override
	public String getNamespace() {
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	@Override
	public String getActionName() {
		return _actionName;
	}
	public void setActionName(String actionName) {
		this._actionName = actionName;
	}
	
	@Override
	public String getParams() {
		return _params;
	}
	public void setParams(String params) {
		this._params = params;
	}

	public List<String> getUserGroupCodes() {
		return _userGroupCodes;
	}

	public void setUserGroupCodes(List<String> userGroupCodes) {
		this._userGroupCodes = userGroupCodes;
	}
	
	private Date _startCreation;
	private Date _endCreation;
	private Date _startUpdate;
	private Date _endUpdate;
	private String _username;
	private String _namespace;
	private String _actionName;
	private String _params;
	private List<String> _userGroupCodes;

}