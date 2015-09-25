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

/**
 * @author E.Santoboni
 */
public class ManagerConfiguration {
	
	public Boolean getCleanOldActivities() {
		return _cleanOldActivities;
	}
	public void setCleanOldActivities(Boolean cleanOldActivities) {
		this._cleanOldActivities = cleanOldActivities;
	}
	
	public Integer getMaxActivitySizeByGroup() {
		return _maxActivitySizeByGroup;
	}
	public void setMaxActivitySizeByGroup(Integer maxActivitySizeByGroup) {
		this._maxActivitySizeByGroup = maxActivitySizeByGroup;
	}

	public Integer getNumberOfStreamsOnHistory() {
		return _numberOfStreamsOnHistory;
	}

	public void setNumberOfStreamsOnHistory(Integer numberOfStreamsOnHistory) {
		this._numberOfStreamsOnHistory = numberOfStreamsOnHistory;
	}
	
	private Boolean _cleanOldActivities = false;
	private Integer _maxActivitySizeByGroup = 10;
	private Integer _numberOfStreamsOnHistory = 500;
	
}