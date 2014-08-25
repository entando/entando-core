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
package com.agiletec.apsadmin.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Action specifica per la gestione della password utente corrente.
 * @author E.Santoboni
 */
public class BaseCommonAction extends BaseAction implements IBaseCommonAction {

	private static final Logger _logger = LoggerFactory.getLogger(BaseCommonAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (!this.getFieldErrors().isEmpty()) {
			return;
		}
		try {
			UserDetails currentUser = this.getCurrentUser();
			if (!currentUser.isJapsUser()) {
				this.addFieldError("username", this.getText("error.user.changePassword.currentUserNotLocal"));
			} else if (null == this.getUserManager().getUser(currentUser.getUsername(), this.getOldPassword())) {
				this.addFieldError("oldPassword", this.getText("error.user.changePassword.wrongOldPassword"));
			}
		} catch (ApsSystemException e) {
			throw new RuntimeException("Error extracting user", e);
		}
	}
	
	@Override
	public String editPassword() {
		return SUCCESS;
	}
	
	@Override
	public String changePassword() {
		try {
			this.getUserManager().changePassword(this.getUsername(), this.getPassword());
			this.addActionMessage(this.getText("message.passwordChanged"));
		} catch (Throwable t) {
			_logger.error("error in changePassword", t);
			//ApsSystemUtils.logThrowable(t, this, "changePassword");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getUsername() {
		return this.getCurrentUser().getUsername();
	}
	
	public String getOldPassword() {
		return _oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this._oldPassword = oldPassword;
	}
	
	public String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		this._password = password;
	}
	
	public String getPasswordConfirm() {
		return _passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this._passwordConfirm = passwordConfirm;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	private IUserManager _userManager;
	
	private String _oldPassword;
	private String _password;
	private String _passwordConfirm;
	
}
