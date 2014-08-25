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
package org.entando.entando.aps.system.services.userprofile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.cache.CacheInfoEvict;
import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.userprofile.event.ProfileChangedEvent;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Implementation of ProfileManager.
 * The service is included in a transparent manner in the workflow of required 
 * to insert, update or delete user, using the Aspect-oriented programming. 
 * In that way, you can join a user with his Profile whatever implementation of User Management.
 * @author E.Santoboni
 */
@Aspect
public class UserProfileManager extends ApsEntityManager implements IUserProfileManager {

	private static final Logger _logger =  LoggerFactory.getLogger(UserProfileManager.class);
	
    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.getUser(..))", returning = "user")
    public void injectProfile(Object user) {
        if (user != null) {
            AbstractUser userDetails = (AbstractUser) user;
            if (null == userDetails.getProfile()) {
                try {
                    IUserProfile profile = this.getProfile(userDetails.getUsername());
                    userDetails.setProfile(profile);
                } catch (Throwable t) {
                	_logger.error("Error injecting profile on user {}", userDetails.getUsername(), t);
                }
            }
        }
    }
	
    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.addUser(..)) && args(user,..)")
    public void addProfile(Object user) {
        if (user != null) {
            UserDetails userDetails = (UserDetails) user;
            Object profile = userDetails.getProfile();
            if (null != profile) {
                try {
                    this.addProfile(userDetails.getUsername(), (IUserProfile) profile);
                } catch (Throwable t) {
                	_logger.error("Error adding profile on user {}", userDetails.getUsername(), t);
                }
            }
        }
    }
	
    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.updateUser(..)) && args(user,..)")
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'UserProfile_'.concat(#user.username)")
    public void updateProfile(Object user) {
        if (user != null) {
            UserDetails userDetails = (UserDetails) user;
            Object profile = userDetails.getProfile();
            if (null != profile) {
                try {
                    this.updateProfile(userDetails.getUsername(), (IUserProfile) profile);
                } catch (Throwable t) {
                	_logger.error("Error updating profile to user {}", userDetails.getUsername(), t);
                }
            }
        }
    }
	
    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'UserProfile_'.concat(#key)")
    public void deleteProfile(Object key) {
        String username = null;
        if (key instanceof String) {
            username = key.toString();
        } else if (key instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) key;
            username = userDetails.getUsername();
        }
        if (username != null) {
            try {
                this.deleteProfile(username);
            } catch (Throwable t) {
            	_logger.error("Error deleting profile. user: {}", username, t);
            }
        }
    }
	
	@Override
    public IApsEntity getEntity(String entityId) throws ApsSystemException {
        return this.getProfile(entityId);
    }
    
	@Override
    public IUserProfile getDefaultProfileType() {
        IUserProfile profileType = (IUserProfile) super.getEntityPrototype(SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
        if (null == profileType) {
            List<String> entityTypes = new ArrayList<String>();
            entityTypes.addAll(this.getEntityPrototypes().keySet());
            if (!entityTypes.isEmpty()) {
                Collections.sort(entityTypes);
                profileType = (IUserProfile) super.getEntityPrototype(entityTypes.get(0));
            }
        }
        return profileType;
    }
    
    @Override
    public IUserProfile getProfileType(String typeCode) {
        return (IUserProfile) super.getEntityPrototype(typeCode);
    }
    
	@Override
    public void addProfile(String username, IUserProfile profile) throws ApsSystemException {
        try {
            profile.setId(username);
            this.getProfileDAO().addEntity(profile);
            this.notifyProfileChanging(profile, ProfileChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
        	_logger.error("Error saving profile - user: {}", username, t);
            throw new ApsSystemException("Error saving profile", t);
        }
    }
    
	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'UserProfile_'.concat(#username)")
    public void deleteProfile(String username) throws ApsSystemException {
        try {
            IUserProfile profileToDelete = this.getProfile(username);
            if (null == profileToDelete) {
                return;
            }
            this.getProfileDAO().deleteEntity(username);
            this.notifyProfileChanging(profileToDelete, ProfileChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
        	_logger.error("Error deleting user profile {}", username, t);
            throw new ApsSystemException("Error deleting user profile", t);
        }
    }
    
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'UserProfile_'.concat(#username)")
	@CacheableInfo(groups = "'UserProfileTypes_cacheGroup'")
	public IUserProfile getProfile(String username) throws ApsSystemException {
        IUserProfile profile = null;
        try {
            UserProfileRecord profileVO = (UserProfileRecord) this.getProfileDAO().loadEntityRecord(username);
            if (profileVO != null) {
                profile = (IUserProfile) this.createEntityFromXml(profileVO.getTypeCode(), profileVO.getXml());
                profile.setPublicProfile(profileVO.isPublicProfile());
            }
        } catch (Throwable t) {
        	_logger.error("Error loading profile. user: {} ", username, t);
            throw new ApsSystemException("Error loading profile", t);
        }
        return profile;
    }
    
	@Override
    @CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'UserProfile_'.concat(#username)")
	public void updateProfile(String username, IUserProfile profile) throws ApsSystemException {
        try {
            profile.setId(username);
            this.getProfileDAO().updateEntity(profile);
            this.notifyProfileChanging(profile, ProfileChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
        	_logger.error("Error updating profile {}", username, t);
            throw new ApsSystemException("Error updating profile", t);
        }
    }
	
    private void notifyProfileChanging(IUserProfile profile, int operationCode) throws ApsSystemException {
        ProfileChangedEvent event = new ProfileChangedEvent();
        event.setProfile(profile);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }
	
	@Override
	@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "'UserProfileTypes_cacheGroup'")
	public void removeEntityPrototype(String entityTypeCode) throws ApsSystemException {
		super.removeEntityPrototype(entityTypeCode);
	}
	
	@Override
	@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "'UserProfileTypes_cacheGroup'")
	public void updateEntityPrototype(IApsEntity entityType) throws ApsSystemException {
		super.updateEntityPrototype(entityType);
	}
	
	@Override
    protected ICategoryManager getCategoryManager() {
        return null;
    }
    
    @Override
    protected IEntityDAO getEntityDao() {
        return (IEntityDAO) this.getProfileDAO();
    }
    
    @Override
    protected IEntitySearcherDAO getEntitySearcherDao() {
        return _entitySearcherDAO;
    }

    protected IUserProfileDAO getProfileDAO() {
        return _profileDAO;
    }
    public void setProfileDAO(IUserProfileDAO profileDAO) {
        this._profileDAO = profileDAO;
    }

    protected IEntitySearcherDAO getEntitySearcherDAO() {
        return _entitySearcherDAO;
    }
    public void setEntitySearcherDAO(IEntitySearcherDAO entitySearcherDAO) {
        this._entitySearcherDAO = entitySearcherDAO;
    }
    
    private IUserProfileDAO _profileDAO;
    private IEntitySearcherDAO _entitySearcherDAO;
    
}
