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

import java.util.Date;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import java.util.List;

public class UserAction extends BaseAction {
	
	private static final Logger logger =  LoggerFactory.getLogger(UserAction.class);

	private IUserManager userManager;
	private IUserProfileManager userProfileManager;

	private int strutsAction;
	private String username;
	private String password;
	private String passwordConfirm;

	private String profileTypeCode;

	private boolean active = false;
	private boolean reset;

	private UserDetails user;

	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			this.checkAddUserUser();
		} else {
			try {
				if (this.hasActionErrors() || this.hasErrors() || this.hasFieldErrors()) {
					UserDetails user = this.getUserManager().getUser(username);
					this.setUser(user);
				}
			} catch (Throwable t) {
				logger.error("Error validating user", t);
			}
		}
	}
	
	protected void checkAddUserUser() {
		String username = this.getUsername();
		String profileTypeCode = this.getProfileTypeCode();
		try {
			if (this.existsUser(username)) {
				String[] args = {username};
				this.addFieldError("username", this.getText("error.user.duplicateUser", args));
			}
			if (!this.getProfileTypes().isEmpty() && null == this.getUserProfileManager().getProfileType(profileTypeCode)) {
				String[] args = {profileTypeCode};
				this.addFieldError("profileTypeCode", this.getText("error.user.profileTypeCode.invalid", args));
			}
		} catch (Throwable t) {
			logger.error("Error checking user '{}'", username, t);
		}
	}
	
	public String newUser() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
	public String edit() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		try {
			String result = this.checkUserForEdit();
			if (null != result) return result;
			UserDetails user = this.getUserManager().getUser(username);
			if (!user.isEntandoUser()) {
				this.addActionError(this.getText("error.user.notLocal"));
				return "userList";
			}
			this.setActive(!user.isDisabled());
			this.setUser(user);
		} catch (Throwable t) {
			logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		return this.executeSave(false);
	}
	
	public String saveAndContinue() {
		return this.executeSave(true);
	}
	
	protected String executeSave(boolean editProfile) {
		User user = null;
		boolean hasProfile = false;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				user = new User();
				user.setUsername(this.getUsername());
				user.setPassword(this.getPassword());
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				user = (User) this.getUserManager().getUser(this.getUsername());
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					user.setPassword(this.getPassword());
				}
				hasProfile = this.checkUserProfile(this.getUsername(), null);
			}
			user.setDisabled(!this.isActive());
			if (this.isReset()) {
				user.setLastAccess(new Date());
				user.setLastPasswordChange(new Date());
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getUserManager().addUser(user);
				hasProfile = this.checkUserProfile(this.getUsername(), this.getProfileTypeCode());
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getUserManager().updateUser(user);
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					this.getUserManager().changePassword(this.getUsername(), this.getPassword());
				}
			}
			if (editProfile && hasProfile) {
				return "editProfile";
			}
		} catch (Throwable t) {
			logger.error("error in executeSave", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private boolean checkUserProfile(String username, String profileTypeCode) throws ApsSystemException {
		try {
			IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
			if (null == userProfile) {
				userProfile = (null != profileTypeCode) ? this.getUserProfileManager().getProfileType(profileTypeCode) : null;
				if (null == userProfile) {
					userProfile = this.getUserProfileManager().getDefaultProfileType();
				}
				if (null != userProfile) {
					userProfile.setId(username);
					this.getUserProfileManager().addProfile(username, userProfile);
					return true;
				}
			} else {
				return true;
			}
		} catch (Throwable t) {
			logger.error("Error adding default profile for user {}", username, t);
			throw new ApsSystemException("Error adding default profile for user " + username, t);
		}
		return false;
	}
	
	public String trash() {
		try {
			String result = this.checkUserForDelete();
			if (null != result) return result;
		} catch (Throwable t) {
			logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			String result = this.checkUserForDelete();
			if (null != result) return result;
			this.getUserManager().removeUser(this.getUsername());
		} catch (Throwable t) {
			logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected boolean isCurrentUser() {
		UserDetails currentUser = this.getCurrentUser();
		return currentUser.getUsername().equals(this.getUsername());
	}
	
	protected boolean existsUser(String username) throws Throwable {
		return (username != null) && (userManager.getUser(username) != null);
	}

	/**
	 * @deprecated Replaced by {@link #isEntandoUser(String)}
	 */
	@Deprecated
	protected boolean isJapsUser(String username) throws Throwable {
		return this.isEntandoUser(username);
	}
	
	protected boolean isEntandoUser(String username) throws Throwable {
		UserDetails user = this.getUserManager().getUser(username);
		return (null != user && user.isEntandoUser());
	}
	
	protected String checkUserForEdit() throws Throwable {
		if (!this.existsUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.notExist"));
			return "userList";
		}
		if (!this.isEntandoUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.notLocal"));
			return "userList";
		}
		return null;
	}
	
	protected String checkUserForDelete() throws Throwable {
		if (!this.existsUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.notExist"));
			return "userList";
		} else if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotDeleteAdminUser"));
			return "userList";
		} else if (this.isCurrentUser()) {
			this.addActionError(this.getText("error.user.cannotDeleteCurrentUser"));
			return "userList";
		} else if (!this.isEntandoUser(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotDeleteNotLocalUser"));
			return "userList";
		}
		return null;
	}
	
	public List<SmallEntityType> getProfileTypes() {
		return this.getUserProfileManager().getSmallEntityTypes();
	}
	
	public int getStrutsAction() {
		return strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this.strutsAction = strutsAction;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	public String getProfileTypeCode() {
		return profileTypeCode;
	}
	public void setProfileTypeCode(String profileTypeCode) {
		this.profileTypeCode = profileTypeCode;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isReset() {
		return reset;
	}
	public void setReset(boolean reset) {
		this.reset = reset;
	}
	
	public UserDetails getUser() {
		return user;
	}
	public void setUser(UserDetails user) {
		this.user = user;
	}
	
	protected IUserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this.userProfileManager = userProfileManager;
	}
}
