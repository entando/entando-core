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
package org.entando.entando.aps.system.services.actionlog;

import java.util.List;
import java.util.Date;

import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.IActivityStreamSearchBean;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.Set;

/**
 * Interface for the service that manages the {@link ActionLogRecord}
 * @author E.Santoboni - S.Puddu
 */
public interface IActionLogManager {
	
	/**
	 * Load a list of {@link ActionLogRecord} codes that match the search criteria rapresented by the searchBean
	 * @param searchBean object containing the search criteria
	 * @return a list of codes
	 * @throws ApsSystemException if an error occurs
	 */
	public List<Integer> getActionRecords(IActionLogRecordSearchBean searchBean) throws ApsSystemException;
	
	/**
	 * Save a new {@link ActionLogRecord}
	 * @param actionRecord
	 * @throws ApsSystemException
	 */
	public void addActionRecord(ActionLogRecord actionRecord) throws ApsSystemException;
	
	/**
	 * Load a {@link ActionLogRecord}
	 * @param id the code of the record to load
	 * @return an {@link ActionLogRecord} 
	 * @throws ApsSystemException if an error occurs
	 */
	public ActionLogRecord getActionRecord(int id) throws ApsSystemException;
	
	/**
	 * Delete a {@link ActionLogRecord}
	 * @param id the code of the record to delete
	 * @throws ApsSystemException if an error occurs
	 */
	public void deleteActionRecord(int id) throws ApsSystemException;
	
	public List<Integer> getActivityStream(List<String> userGroupCodes) throws ApsSystemException;
	
	public List<Integer> getActivityStream(UserDetails loggedUser) throws ApsSystemException;
	
	public List<Integer> getActivityStream(IActivityStreamSearchBean activityStreamSearchBean) throws ApsSystemException;
	/*
	public void editActionLikeRecord(int id, String username, boolean add) throws ApsSystemException;
	
	public List<ActivityStreamLikeInfo> getActionLikeRecords(int id) throws ApsSystemException;
	
	public void addActionCommentRecord(String username, String commentText, int streamId) throws ApsSystemException;
	
	public void deleteActionCommentRecord(int id, int streamId) throws ApsSystemException;
	
	public List<ActivityStreamComment> getActionCommentRecords(int id) throws ApsSystemException;
	*/
	
	public Set<Integer> extractOldRecords(Integer maxActivitySizeByGroup) throws ApsSystemException;
	
	public void updateRecordDate(int id) throws ApsSystemException;
	
	public Date lastUpdateDate(UserDetails loggedUser) throws ApsSystemException;
	
	public static final String LOG_APPENDER_THREAD_NAME_PREFIX = SystemConstants.ENTANDO_THREAD_NAME_PREFIX + "ActionRecordAppender_";
	public static final String LOG_CLEANER_THREAD_NAME_PREFIX = SystemConstants.ENTANDO_THREAD_NAME_PREFIX + "ActivityStreamCleanerThread_";
	
}
