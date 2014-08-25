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
package org.entando.entando.apsadmin.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;

/**
 * @author E.Santoboni
 */
public class UserProfileAction extends AbstractApsEntityAction {

	private static final Logger _logger =  LoggerFactory.getLogger(UserProfileAction.class);
	
	@Override
    public String edit() {
        String username = this.getUsername();
        try {
            String chechUsernameResult = this.checkUsername(username, false);
            if (null != chechUsernameResult) return chechUsernameResult;
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
        	_logger.error("error in edit");
            //ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    @Override
    public String createNew() {
        String username = this.getUsername();
        String profileTypeCode = this.getProfileTypeCode();
        try {
            String chechUsernameResult = this.checkUsername(username, false);
            if (null != chechUsernameResult) return chechUsernameResult;
            IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfile(username);
            if (null != userProfile) {
                this.getRequest().getSession().setAttribute(USERPROFILE_ON_SESSION, userProfile);
                return "edit";
            }
            if (StringUtils.isBlank(profileTypeCode)) {
                String[] args = {profileTypeCode};
                this.addFieldError("profileTypeCode", this.getText("error.newUserProfile.invalidProfileType", args));
                return INPUT;
            }
            userProfile = (IUserProfile) this.getUserProfileManager().getEntityPrototype(profileTypeCode);
            if (null == userProfile) {
                String[] args = {profileTypeCode};
                this.addFieldError("profileTypeCode", this.getText("error.newUserProfile.invalidProfileType", args));
                return INPUT;
            }
            userProfile.setId(this.getUsername());
            this.getRequest().getSession().setAttribute(USERPROFILE_ON_SESSION, userProfile);
        } catch (Throwable t) {
        	_logger.error("error in createNew");
           //ApsSystemUtils.logThrowable(t, this, "createNew");
            return FAILURE;
        }
        return SUCCESS;
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
        	_logger.error("error in save");
            //ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    @Override
    public String view() {
        String username = this.getUsername();
        try {
            String chechUsernameResult = this.checkUsername(username, true);
            if (null != chechUsernameResult) return chechUsernameResult;
            IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                String[] args = {username};
                this.addFieldError("username", this.getText("error.viewUserProfile.userWithoutProfile", args));
                return INPUT;
            }
        } catch (Throwable t) {
        	_logger.error("error in view");
            //ApsSystemUtils.logThrowable(t, this, "view");
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
    
    public List<IApsEntity> getUserProfileTypes() {
        List<IApsEntity> userProfileTypes = null;
        try {
            userProfileTypes = new ArrayList<IApsEntity>();
            userProfileTypes.addAll(this.getUserProfileManager().getEntityPrototypes().values());
        } catch (Throwable t) {
        	_logger.error("error in getUserProfileTypes");
            //ApsSystemUtils.logThrowable(t, this, "getUserProfileTypes");
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
            //ApsSystemUtils.logThrowable(t, this, "getUserProfile", "Error extracting user profile by username " + username);
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
