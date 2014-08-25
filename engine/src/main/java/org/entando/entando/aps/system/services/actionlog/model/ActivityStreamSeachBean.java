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

package org.entando.entando.aps.system.services.actionlog.model;

/**
 * @author S.Loru
 */
public class ActivityStreamSeachBean extends ActionLogRecordSearchBean implements IActivityStreamSearchBean {

	@Override
	public String getActivityStreamInfo() {
		return _activityStreamInfo;
	}


	public void setActivityStreamInfo(String activityStreamInfo) {
		this._activityStreamInfo = activityStreamInfo;
	}
	
	private String _activityStreamInfo;

}
