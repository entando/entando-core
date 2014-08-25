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
package org.entando.entando.aps.system.services.actionlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamLikeInfo;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.ManagerConfiguration;
import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedEvent;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedObserver;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamSeachBean;
import org.entando.entando.aps.system.services.actionlog.model.IActivityStreamSearchBean;

/**
 * @author E.Santoboni - S.Puddu
 */
public class ActionLogManager extends AbstractService implements IActionLogManager, ProfileChangedObserver {

	private static final Logger _logger = LoggerFactory.getLogger(ActionLogManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready",this.getClass().getName());
	}

	@Override
	public void addActionRecord(ActionLogRecord actionRecord) throws ApsSystemException {
		try {
			ActionLogAppenderThread thread = new ActionLogAppenderThread(actionRecord, this);
			String threadName = LOG_APPENDER_THREAD_NAME_PREFIX + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
			thread.setName(threadName);
			thread.start();
		} catch (Throwable t) {
			_logger.error("Error adding an actionlogger record", t);
			throw new ApsSystemException("Error adding an actionlogger record", t);
		}
	}

	protected synchronized void addActionRecordByThread(ActionLogRecord actionRecord) throws ApsSystemException {
		try {
			Integer key = null;
			List<Integer> ids = null;
			do {
				key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				FieldSearchFilter filter = new FieldSearchFilter("id", key, true);
				FieldSearchFilter[] filters = {filter};
				ids = this.getActionLogDAO().getActionRecords(filters);
			} while (!ids.isEmpty());
			actionRecord.setId(key);
			actionRecord.setActionDate(new Date());
			this.getActionLogDAO().addActionRecord(actionRecord);
		} catch (Throwable t) {
			_logger.error("Error adding an actionlogger record", t);
			throw new ApsSystemException("Error adding an actionlogger record", t);
		}
	}

	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActionLogRecord_'.concat(#id)")
	public void deleteActionRecord(int id) throws ApsSystemException {
		try {
			this.getActionLogDAO().deleteActionRecord(id);
		} catch (Throwable t) {
			_logger.error("Error deleting the actionlogger record: {}", id, t);
			throw new ApsSystemException("Error deleting the actionlogger record: " + id, t);
		}
	}

	@Override
	public List<Integer> getActionRecords(IActionLogRecordSearchBean searchBean) throws ApsSystemException {
		List<Integer> records = new ArrayList<Integer>();
		try {
			records = this.getActionLogDAO().getActionRecords(searchBean);
		} catch (Throwable t) {
			_logger.error("Error loading actionlogger records", t);
			throw new ApsSystemException("Error loading actionlogger records", t);
		}
		return records;
	}

	@Override
	public ActionLogRecord getActionRecord(int id) throws ApsSystemException {
		ActionLogRecord record = null;
		try {
			record = this.getActionLogDAO().getActionRecord(id);
		} catch (Throwable t) {
			_logger.error("Error loading actionlogger record with id: {}", id, t);
			throw new ApsSystemException("Error loading actionlogger record with id: " + id, t);
		}
		return record;
	}

	@Override
	public List<Integer> getActivityStream(List<String> userGroupCodes) throws ApsSystemException {
		List<Integer> recordIds = null;
		try {
			recordIds = this.getActionLogDAO().getActivityStream(userGroupCodes);
			ManagerConfiguration config = this.getManagerConfiguration();
			if (null != recordIds && null != config &&
					config.getCleanOldActivities() && config.getMaxActivitySizeByGroup() < recordIds.size()) {
				ActivityStreamCleanerThread thread = new ActivityStreamCleanerThread(config.getNumberOfStreamsOnHistory(), this.getActionLogDAO());
				String threadName = LOG_CLEANER_THREAD_NAME_PREFIX + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
    			thread.setName(threadName);
    			thread.start();
			}
			if (null != config && null != recordIds && recordIds.size() > config.getMaxActivitySizeByGroup()) {
				recordIds = recordIds.subList(0, config.getMaxActivitySizeByGroup());
			}
		} catch (Throwable t) {
			_logger.error("Error loading activity stream records", t);
			throw new ApsSystemException("Error loading activity stream records", t);
		}
		return recordIds;
	}

	@Override
	public List<Integer> getActivityStream(IActivityStreamSearchBean activityStreamSearchBean) {
		List<Integer> recordIds = null;
		recordIds = this.getActionLogDAO().getActionRecords(activityStreamSearchBean);
		ManagerConfiguration config = this.getManagerConfiguration();
		if (null != config && null != recordIds && recordIds.size() > config.getMaxActivitySizeByGroup()) {
			recordIds = recordIds.subList(0, config.getMaxActivitySizeByGroup());
		}
		return recordIds;
	}
	
	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamLikeRecords_id_'.concat(#id)")
	public void editActionLikeRecord(int id, String username, boolean add) throws ApsSystemException {
		try {
			this.getActionLogDAO().editActionLikeRecord(id, username, add);
		} catch (Throwable t) {
			_logger.error("Error editing activity stream like records", t);
			throw new ApsSystemException("Error editing activity stream like records", t);
		}
	}
	
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamLikeRecords_id_'.concat(#id)")
	@CacheableInfo(groups = "'ActivityStreamLikeRecords_cacheGroup'")
	public List<ActivityStreamLikeInfo> getActionLikeRecords(int id) throws ApsSystemException {
		List<ActivityStreamLikeInfo> infos = null;
		try {
			infos = this.getActionLogDAO().getActionLikeRecords(id);
			if (null != infos) {
				for (int i = 0; i < infos.size(); i++) {
					ActivityStreamLikeInfo asli = infos.get(i);
					String username = asli.getUsername();
					IUserProfile profile = this.getUserProfileManager().getProfile(username);
					String displayName = (null != profile) ? profile.getDisplayName() : username;
					asli.setDisplayName(displayName);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting activity stream like records", t);
			throw new ApsSystemException("Error extracting activity stream like records", t);
		}
		return infos;
	}
	
	@Override
	public void updateFromProfileChanged(ProfileChangedEvent event) {
		try {
			ICacheInfoManager cacheInfoManager = (ICacheInfoManager) this.getBeanFactory().getBean(SystemConstants.CACHE_INFO_MANAGER);
			cacheInfoManager.flushGroup(ICacheInfoManager.DEFAULT_CACHE_NAME, "ActivityStreamLikeRecords_cacheGroup");
		} catch (Throwable t) {
			_logger.error("Error flushing cache group", t);
		}
	}
	
	@Override
	public List<Integer> getActivityStream(UserDetails loggedUser) throws ApsSystemException {
		List<String> userGroupCodes = this.extractUserGroupCodes(loggedUser);
		return this.getActivityStream(userGroupCodes);
	}
	
	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamCommentRecords_id_'.concat(#streamId)")
	public void addActionCommentRecord(String username, String commentText, int streamId) throws ApsSystemException {
		try {
			Integer key = null;
			List<Integer> ids = null;
			do {
				key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				FieldSearchFilter filter = new FieldSearchFilter("id", key, true);
				FieldSearchFilter[] filters = {filter};
				ids = this.getActionLogDAO().getActionRecords(filters);
			} while (!ids.isEmpty());
			this.getActionLogDAO().addActionCommentRecord(key, streamId, username, commentText);
		} catch (Throwable t) {
			_logger.error("Error adding a comment record to stream with id:{}", streamId, t);
			throw new ApsSystemException("Error adding a comment record", t);
		}
	}

	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamCommentRecords_id_'.concat(#streamId)")
	public void deleteActionCommentRecord(int id, int streamId) throws ApsSystemException {
		try {
			this.getActionLogDAO().deleteActionCommentRecord(id, streamId);
		} catch (Throwable t) {
			_logger.error("Error deleting comment with id {} from stream with id {}", id, streamId, t);
			throw new ApsSystemException("Error deleting comment", t);
		}
	}
	
	@Override
	public Date lastUpdateDate(UserDetails loggedUser) throws ApsSystemException {
		List<Integer> actionRecordIds = null;
		Date lastUpdate = new Date();
		try {
			ActivityStreamSeachBean searchBean = new ActivityStreamSeachBean();
			searchBean.setUserGroupCodes(this.extractUserGroupCodes(loggedUser));
			searchBean.setEndUpdate(lastUpdate);
			actionRecordIds = this.getActionRecords(searchBean);
			if(null != actionRecordIds && actionRecordIds.size() > 0) {
				ActionLogRecord actionRecord = this.getActionRecord(actionRecordIds.get(0));
				lastUpdate = actionRecord.getUpdateDate();
			}
		} catch (Throwable t) {
			_logger.error("Error on loading updated activities", t);
			throw new ApsSystemException("Error on loading updated activities", t);
        }
		return lastUpdate;
	}
	
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamCommentRecords_id_'.concat(#id)")
	@CacheableInfo(groups = "'ActivityStreamCommentRecords_cacheGroup'")
	public List<ActivityStreamComment> getActionCommentRecords(int id) throws ApsSystemException {
		List<ActivityStreamComment> infos = null;
		try {
			infos = this.getActionLogDAO().getActionCommentRecords(id);
			if (null != infos) {
				for (int i = 0; i < infos.size(); i++) {
					ActivityStreamComment comment = infos.get(i);
					String username = comment.getUsername();
					IUserProfile profile = this.getUserProfileManager().getProfile(username);
					String displayName = (null != profile) ? profile.getDisplayName() : username;
					comment.setDisplayName(displayName);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting activity stream like records for stream with id {}", id, t);
			throw new ApsSystemException("Error extracting activity stream like records", t);
		}
		return infos;
	}

	private List<String> extractUserGroupCodes(UserDetails loggedUser) {
		List<String> codes = new ArrayList<String>();
		IApsAuthority[] autorities = loggedUser.getAuthorities();
		if (null != autorities) {
			for (int i = 0; i < autorities.length; i++) {
				IApsAuthority autority = autorities[i];
				if (autority instanceof Group) {
					codes.add(autority.getAuthority());
				}
			}
		}
		if (!codes.contains(Group.FREE_GROUP_NAME)) {
			codes.add(Group.FREE_GROUP_NAME);
		}
		return codes;
	}

	protected ManagerConfiguration getManagerConfiguration() {
		return _managerConfiguration;
	}
	public void setManagerConfiguration(ManagerConfiguration managerConfiguration) {
		this._managerConfiguration = managerConfiguration;
	}

	protected IActionLogDAO getActionLogDAO() {
		return _actionLogDAO;
	}
	public void setActionLogDAO(IActionLogDAO actionLogDAO) {
		this._actionLogDAO = actionLogDAO;
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}

	private ManagerConfiguration _managerConfiguration;

	private IActionLogDAO _actionLogDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	private IUserProfileManager _userProfileManager;

}
