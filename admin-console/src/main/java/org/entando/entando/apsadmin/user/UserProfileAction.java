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
package org.entando.entando.apsadmin.user;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class UserProfileAction extends AbstractApsEntityAction {

	private static final Logger _logger =  LoggerFactory.getLogger(UserProfileAction.class);
	
	@Override
    public String edit() {
        String username = this.getUsername();
        try {
            String checkUsernameResult = this.checkUsername(username, false);
            if (null != checkUsernameResult) return checkUsernameResult;
            IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                List<IApsEntity> userProfileTypes = new ArrayList<IApsEntity>();
                userProfileTypes.addAll(this.getUserProfileManager().getEntityPrototypes().values());
                if (userProfileTypes.isEmpty()) {
                    throw new RuntimeException("Unexpected error - no one user profile types");
                } else if (userProfileTypes.size() == 1) {
                    userProfile = (IUserProfile) userProfileTypes.get(0);
                    userProfile.setId(username);
                } else {
                    return "chooseType";
                }
            }
            this.getRequest().getSession().setAttribute(USERPROFILE_ON_SESSION, userProfile);
        } catch (Throwable t) {
        	_logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }
	
	public String saveEmpty() {
		return this.createNewProfile(true);
	}
    
	public String saveAndContinue() {
		return this.createNewProfile(false);
	}
    
	protected String createNewProfile(boolean onlyVoid) {
        String username = this.getUsername();
        String profileTypeCode = this.getProfileTypeCode();
        try {
            String check = this.checkUsername(username, false);
            if (null != check) return check;
			this.getUserProfileManager().deleteProfile(username);
			/*
            IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfile(username);
            if (null != userProfile) {
                this.getRequest().getSession().setAttribute(USERPROFILE_ON_SESSION, userProfile);
                return "edit";
            }
			*/
            if (StringUtils.isBlank(profileTypeCode)) {
                String[] args = {profileTypeCode};
                this.addFieldError("profileTypeCode", this.getText("error.profileType.mandatory", args));
                return INPUT;
            }
            IUserProfile prototype = (IUserProfile) this.getUserProfileManager().getEntityPrototype(profileTypeCode);
            if (null == prototype) {
                String[] args = {profileTypeCode};
                this.addFieldError("profileTypeCode", this.getText("error.newUserProfile.invalidProfileType", args));
                return INPUT;
            }
            prototype.setId(this.getUsername());
			this.getUserProfileManager().addProfile(username, prototype);
			if (onlyVoid) {
				this.getRequest().getSession().removeAttribute(USERPROFILE_ON_SESSION);
			} else {
				this.getRequest().getSession().setAttribute(USERPROFILE_ON_SESSION, prototype);
			}
        } catch (Throwable t) {
        	_logger.error("error in createNewProfile", t);
            return FAILURE;
        }
        return SUCCESS;
    }
	
    @Override
    public String createNew() {
        return this.createNewProfile(false);
    }
    
    @Override
    public String save() {
        try {
            IUserProfile userProfile = (IUserProfile) this.getApsEntity();
            String username = userProfile.getUsername();
            if (null == this.getUserProfileManager().getProfile(userProfile.getUsername())) {
                this.getUserProfileManager().addProfile(username, userProfile);
            } else {
                this.getUserProfileManager().updateProfile(username, userProfile);
            }
			UserDetails currentUser = super.getCurrentUser();
			if (null != currentUser 
					&& currentUser.getUsername().equals(username)
					&& (currentUser instanceof AbstractUser)) {
				((AbstractUser) currentUser).setProfile(userProfile);
			}
        } catch (Throwable t) {
        	_logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
    @Override
    public String view() {
        String username = this.getUsername();
        try {
            String checkUsernameResult = this.checkUsername(username, true);
            if (null != checkUsernameResult) return checkUsernameResult;
            IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                String[] args = {username};
                this.addFieldError("username", this.getText("error.viewUserProfile.userWithoutProfile", args));
                return INPUT;
            }
        } catch (Throwable t) {
        	_logger.error("error in view", t);
            return FAILURE;
        }
        return SUCCESS;
    }
    
	public String changeProfileType() {
        String username = this.getUsername();
        try {
            String check = this.checkUsername(username, false);
            if (null != check) return check;
            this.getRequest().getSession().removeAttribute(USERPROFILE_ON_SESSION);
        } catch (Throwable t) {
        	_logger.error("error changing Profile Type");
            return FAILURE;
        }
        return SUCCESS;
    }
	
    private String checkUsername(String username, boolean checkNullProfile) throws ApsSystemException {
        if (StringUtils.isBlank(username) || (checkNullProfile && null == this.getUserProfileManager().getProfile(username))) {
            String[] args = {username};
            this.addFieldError("username", this.getText("error.newUserProfile.invalidUsername", args));
            return INPUT;
        }
        return null;
    }
    
    public List<SmallEntityType> getUserProfileTypes() {
        List<SmallEntityType> userProfileTypes = null;
        try {
            userProfileTypes = this.getUserProfileManager().getSmallEntityTypes();
        } catch (Throwable t) {
        	_logger.error("error in getUserProfileTypes", t);
        }
        return userProfileTypes;
    }
    
    public IUserProfile getUserProfile() {
        return (IUserProfile) this.getApsEntity();
    }
    
    public IUserProfile getUserProfile(String username) {
        IUserProfile userProfile = null;
        try {
            userProfile = this.getUserProfileManager().getProfile(username);
        } catch (Throwable t) {
        	_logger.error("Error extracting user profile by username {}", username, t);
        }
        return userProfile;
    }
    
	@Override
    public IApsEntity getApsEntity() {
        return (IUserProfile) this.getRequest().getSession().getAttribute(USERPROFILE_ON_SESSION);
    }
    
    public String getUsername() {
        return _username;
    }
    public void setUsername(String username) {
        this._username = username;
    }
    
    public String getProfileTypeCode() {
        return _profileTypeCode;
    }
    public void setProfileTypeCode(String profileTypeCode) {
        this._profileTypeCode = profileTypeCode;
    }
    
    protected IUserProfileManager getUserProfileManager() {
        return _userProfileManager;
    }
    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this._userProfileManager = userProfileManager;
    }
	
    private String _username;
    private String _profileTypeCode;
    private IUserProfileManager _userProfileManager;
	
    public static final String USERPROFILE_ON_SESSION = "userprofile_profileOnSession";
    
}
