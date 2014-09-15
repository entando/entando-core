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
package org.entando.entando.apsadmin.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;

import org.entando.entando.apsadmin.system.services.activitystream.ISocialActivityStreamManager;

/**
 * @author E.Santoboni
 */
public class ActivityStreamLikeAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ActivityStreamLikeAction.class);
	
	public String likeActivity() {
		return this.editLikeActivity(true);
	}
	
	public String unlikeActivity() {
		return this.editLikeActivity(false);
	}
	
	public String editLikeActivity(boolean add) {
		try {
			if (null == this.getRecordId()) {
				_logger.error("Null record id");
				return SUCCESS;
			}
			UserDetails user = super.getCurrentUser();
			this.getSocialActivityStreamManager().editActionLikeRecord(this.getRecordId(), user.getUsername(), add);
		} catch (Throwable t) {
			_logger.error("Error on like/unlike activity", t);
            return FAILURE;
        }
		return SUCCESS;
	}
	
	public Integer getRecordId() {
		return _recordId;
	}
	public void setRecordId(Integer recordId) {
		this._recordId = recordId;
	}
	
	protected ISocialActivityStreamManager getSocialActivityStreamManager() {
		return _socialActivityStreamManager;
	}
	public void setSocialActivityStreamManager(ISocialActivityStreamManager socialActivityStreamManager) {
		this._socialActivityStreamManager = socialActivityStreamManager;
	}
	
	private Integer _recordId;
	
	private ISocialActivityStreamManager _socialActivityStreamManager;
	
}
