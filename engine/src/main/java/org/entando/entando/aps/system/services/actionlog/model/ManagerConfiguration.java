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