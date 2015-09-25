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
package org.entando.entando.aps.system.services.actionlog.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author E.Santoboni - S.Puddu
 */
public class ActionLogRecord implements Serializable {
	
	public void setId(int id) {
		this._id = id;
	}
	public int getId() {
		return _id;
	}
	
	public Date getActionDate() {
		return _actionDate;
	}
	public void setActionDate(Date actionDate) {
		this._actionDate = actionDate;
	}

	public Date getUpdateDate() {
		return _updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this._updateDate = updateDate;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	public String getNamespace() {
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	public String getActionName() {
		return _actionName;
	}
	public void setActionName(String actionName) {
		this._actionName = actionName;
	}
	
	public String getParameters() {
		return _parameters;
	}
	public void setParameters(String parameters) {
		this._parameters = parameters;
	}
	
	public ActivityStreamInfo getActivityStreamInfo() {
		return _activityStreamInfo;
	}
	public void setActivityStreamInfo(ActivityStreamInfo activityStreamInfo) {
		this._activityStreamInfo = activityStreamInfo;
	}
	
	private int _id;
	private Date _actionDate;
	private Date _updateDate;
	private String _username;
	private String _namespace;
	private String _actionName;
	private String _parameters;
	
	private ActivityStreamInfo _activityStreamInfo;
	
	
}