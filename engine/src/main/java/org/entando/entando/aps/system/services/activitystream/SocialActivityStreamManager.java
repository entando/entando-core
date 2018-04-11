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
package org.entando.entando.aps.system.services.activitystream;

import java.util.List;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamLikeInfo;
import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedEvent;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedObserver;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

/**
 * @author E.Santoboni - S.Puddu
 */
public class SocialActivityStreamManager extends AbstractService implements ISocialActivityStreamManager, ProfileChangedObserver {

    private static final Logger _logger = LoggerFactory.getLogger(SocialActivityStreamManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }

    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamLikeRecords_id_'.concat(#id)")
    public void editActionLikeRecord(int id, String username, boolean add) throws ApsSystemException {
        try {
            this.getSocialActivityStreamDAO().editActionLikeRecord(id, username, add);
            this.getActionLogManager().updateRecordDate(id);
        } catch (Throwable t) {
            _logger.error("Error editing activity stream like records", t);
            throw new ApsSystemException("Error editing activity stream like records", t);
        }
    }

    @Override
    @CachePut(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamLikeRecords_id_'.concat(#id)")
    @CacheableInfo(groups = "'ActivityStreamLikeRecords_cacheGroup'")
    public List<ActivityStreamLikeInfo> getActionLikeRecords(int id) throws ApsSystemException {
        List<ActivityStreamLikeInfo> infos = null;
        try {
            infos = this.getSocialActivityStreamDAO().getActionLikeRecords(id);
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
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamCommentRecords_id_'.concat(#streamId)")
    public void addActionCommentRecord(String username, String commentText, int streamId) throws ApsSystemException {
        try {
            Integer key = null;
            ActionLogRecord record = null;
            do {
                key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
                record = this.getActionLogManager().getActionRecord(key);
            } while (null != record);
            this.getSocialActivityStreamDAO().addActionCommentRecord(key, streamId, username, commentText);
            this.getActionLogManager().updateRecordDate(streamId);
        } catch (Throwable t) {
            _logger.error("Error adding a comment record to stream with id:{}", streamId, t);
            throw new ApsSystemException("Error adding a comment record", t);
        }
    }

    @Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamCommentRecords_id_'.concat(#streamId)")
    public void deleteActionCommentRecord(int id, int streamId) throws ApsSystemException {
        try {
            this.getSocialActivityStreamDAO().deleteActionCommentRecord(id);
            this.getActionLogManager().updateRecordDate(streamId);
        } catch (Throwable t) {
            _logger.error("Error deleting comment with id {} from stream with id {}", id, streamId, t);
            throw new ApsSystemException("Error deleting comment", t);
        }
    }

    @Override
    @CachePut(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'ActivityStreamCommentRecords_id_'.concat(#id)")
    @CacheableInfo(groups = "'ActivityStreamCommentRecords_cacheGroup'")
    public List<ActivityStreamComment> getActionCommentRecords(int id) throws ApsSystemException {
        List<ActivityStreamComment> infos = null;
        try {
            infos = this.getSocialActivityStreamDAO().getActionCommentRecords(id);
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

    @Before("execution(* org.entando.entando.aps.system.services.actionlog.IActionLogManager.deleteActionRecord(..)) && args(id,..)")
    public void listenDeleteActionRecord(Integer id) {
        try {
            ICacheInfoManager cacheInfoManager = (ICacheInfoManager) this.getBeanFactory().getBean(SystemConstants.CACHE_INFO_MANAGER);
            cacheInfoManager.flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, "ActivityStreamCommentRecords_id_" + id);
            cacheInfoManager.flushEntry(ICacheInfoManager.DEFAULT_CACHE_NAME, "ActivityStreamLikeRecords_id_" + id);
            this.getSocialActivityStreamDAO().deleteSocialRecordsRecord(id);
        } catch (Throwable t) {
            _logger.error("Error deleting action record", t);
        }
    }

    protected IActionLogManager getActionLogManager() {
        return _actionLogManager;
    }

    public void setActionLogManager(IActionLogManager actionLogManager) {
        this._actionLogManager = actionLogManager;
    }

    protected ISocialActivityStreamDAO getSocialActivityStreamDAO() {
        return _socialActivityStreamDAO;
    }

    public void setSocialActivityStreamDAO(ISocialActivityStreamDAO socialActivityStreamDAO) {
        this._socialActivityStreamDAO = socialActivityStreamDAO;
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

    private IActionLogManager _actionLogManager;

    private ISocialActivityStreamDAO _socialActivityStreamDAO;
    private IKeyGeneratorManager _keyGeneratorManager;
    private IUserProfileManager _userProfileManager;

}
