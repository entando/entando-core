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
package org.entando.entando.apsadmin.common;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.aps.system.services.activitystream.ISocialActivityStreamManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
