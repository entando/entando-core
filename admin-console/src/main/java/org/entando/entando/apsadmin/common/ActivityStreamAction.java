/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.apsadmin.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamSeachBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author S.Loru
 */
public class ActivityStreamAction extends BaseAction{

	private static final Logger _logger =  LoggerFactory.getLogger(ActivityStreamAction.class);
	
	public String viewMore(){
		List<Integer> actionRecordIds = new ArrayList<Integer>();
		try {
			Date timestamp = this.getTimestamp();
			ActivityStreamSeachBean searchBean = new ActivityStreamSeachBean();
			List<Group> userGroups = this.getAuthorizationManager().getUserGroups(this.getCurrentUser());
			searchBean.setUserGroupCodes(groupsToStringCode(userGroups));
			if(timestamp != null){
				timestamp.setTime(timestamp.getTime() - 100);
			}
			searchBean.setEndCreation(timestamp);
			actionRecordIds = this.getActionLogManager().getActionRecords(searchBean);
		} catch (Throwable t) {
			_logger.error("Error on loading more activities", t);
        }
		this.setActionRecordIds(actionRecordIds);
		return SUCCESS;
	}
	
	public String update(){
		List<Integer> actionRecordIds = new ArrayList<Integer>();
		try {
			Date timestamp = this.getTimestamp();
			if(timestamp != null){
				timestamp.setTime(timestamp.getTime() + 100);
			}
			ActivityStreamSeachBean searchBean = new ActivityStreamSeachBean();
			List<Group> userGroups = this.getAuthorizationManager().getUserGroups(this.getCurrentUser());
			searchBean.setUserGroupCodes(groupsToStringCode(userGroups));
			searchBean.setStartUpdate(timestamp);
			searchBean.setEndUpdate(new Date());
			actionRecordIds = this.getActionLogManager().getActionRecords(searchBean);
		} catch (Throwable t) {
			_logger.error("Error on loading updated activities", t);
        }
		this.setActionRecordIds(actionRecordIds);
		return SUCCESS;
	}
	
	private List<String> groupsToStringCode(List<Group> groups) {
		List<String> groupCodes = new ArrayList<String>();
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			groupCodes.add(group.getName());
		}
		return groupCodes;
	}

	public IActionLogManager getActionLogManager() {
		return _actionLogManager;
	}

	public void setActionLogManager(IActionLogManager actionLogManager) {
		this._actionLogManager = actionLogManager;
	}

	public Date getTimestamp() {
		return _timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this._timestamp = timestamp;
	}

	public List<Integer> getActionRecordIds() {
		return _actionRecordIds;
	}

	public void setActionRecordIds(List<Integer> actionRecordIds) {
		this._actionRecordIds = actionRecordIds;
	}

	
	private IActionLogManager _actionLogManager;
	private Date _timestamp;
	private List<Integer> _actionRecordIds;

}
