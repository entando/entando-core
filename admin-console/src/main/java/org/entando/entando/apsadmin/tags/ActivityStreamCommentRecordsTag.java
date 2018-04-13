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
package org.entando.entando.apsadmin.tags;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.tags.AbstractObjectInfoTag;
import org.entando.entando.aps.system.services.activitystream.ISocialActivityStreamManager;


/**
 * Returns the list of like records of an activity through the code.
 * @author E.Santoboni
 */
public class ActivityStreamCommentRecordsTag extends AbstractObjectInfoTag {
	
	@Override
	protected Object getMasterObject(String keyValue) throws Throwable {
		Integer recordId = Integer.parseInt(keyValue);
		ISocialActivityStreamManager socialActivityStreamManager = 
				(ISocialActivityStreamManager) ApsWebApplicationUtils.getBean("SocialActivityStreamManager", this.pageContext);
		return socialActivityStreamManager.getActionCommentRecords(recordId);
	}
	
	public String getRecordId() {
		return super.getKey();
	}
	public void setRecordId(String recordId) {
		super.setKey(recordId);
	}
	
}