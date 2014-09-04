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

import java.util.Date;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * Action base delegata alla gestione utenze.
 * L'azione gestisce le operazioni di aggiunta, modifica e salvataggio degli oggetti 
 * utente, senza interagire con le autorizzazioni, la cui gestione è delegata alle 
 * funzionalità apposite.
 * @author E.Santoboni
 */
public class UserAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(UserAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
			this.checkDuplicatedUser();
		} else {
			try {
				if (this.hasActionErrors() || this.hasErrors() || this.hasFieldErrors()) {
					String username = this.getUsername();
					UserDetails user = this.getUserManager().getUser(username);
					this.setUser(user);
				}
			} catch (Throwable t) {
				_logger.error("Error validating user", t);
				//ApsSystemUtils.logThrowable(t, this, "validate", "Error validating user ");
			}
		}
	}
	
	/**
	 * Esegue in fase di aggiunta la verifica sulla duplicazione dell'utente.<br />
	 * Nel caso la verifica risulti negativa aggiunge un fieldError.
	 */
	protected void checkDuplicatedUser() {
		String username = this.getUsername();
		try {
			if (this.existsUser(username)) {
				String[] args = {username};
				this.addFieldError("username", this.getText("error.user.duplicateUser", args));
			}
		} catch (Throwable t) {
			_logger.error("Error checking duplicate user '{}'", username, t);
			//ApsSystemUtils.logThrowable(t, this, "checkDuplicatedUser", "Error checking duplicate user '" + username + "'");
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
			String username = this.getUsername();
			UserDetails user = this.getUserManager().getUser(username);
			if (!user.isEntandoUser()) {
				this.addActionError(this.getText("error.user.notLocal"));
				return "userList";
			}
			this.setActive(!user.isDisabled());
			this.setUser(user);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			//ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		User user = null;
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
				this.checkUserProfile(user, false);
			}
			user.setDisabled(!this.isActive());
			if (this.isReset()) {
				user.setLastAccess(new Date());
				user.setLastPasswordChange(new Date());
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getUserManager().addUser(user);
				this.checkUserProfile(user, true);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getUserManager().updateUser(user);
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					this.getUserManager().changePassword(this.getUsername(), this.getPassword());
				}
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			//ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void checkUserProfile(User user, boolean add) throws ApsSystemException {
		String username = user.getUsername();
		try {
			IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
			if (null == userProfile) {
				userProfile = this.getUserProfileManager().getDefaultProfileType();
				if (null != userProfile) {
					userProfile.setId(username);
					this.getUserProfileManager().addProfile(username, userProfile);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error adding default profile for user {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "checkUserProfile");
			throw new ApsSystemException("Error adding default profile for user " + username, t);
		}
	}
	
	public String trash() {
		try {
			String result = this.checkUserForDelete();
			if (null != result) return result;
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			//ApsSystemUtils.logThrowable(t, this, "trash");
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
			_logger.error("error in delete", t);
			//ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected boolean isCurrentUser() {
		UserDetails currentUser = this.getCurrentUser();
		return currentUser.getUsername().equals(this.getUsername());
	}
	
	/**
	 * Verifica l'esistenza dell'utente.
	 * @param username Lo username dell'utente da verificare.
	 * @return true in caso positivo, false nel caso l'utente non esista.
	 * @throws Throwable In caso di errore.
	 */
	protected boolean existsUser(String username) throws Throwable {
		return (username != null && username.trim().length() >= 0 && null != this.getUserManager().getUser(username));
	}
	
	/**
	 * Verifica se l'utente è un utente locale di jAPS
	 * @param username Lo username dell'utente da verificare.
	 * @return true in caso positivo, false nel caso contrario.
	 * @throws Throwable In caso di errore.
	 * @deprecated use isEntandoUser
	 */
	protected boolean isJapsUser(String username) throws Throwable {
		return this.isEntandoUser(username);
	}
	
	protected boolean isEntandoUser(String username) throws Throwable {
		UserDetails user = this.getUserManager().getUser(username);
		return (null != user && user.isEntandoUser());
	}
	
	/**
	 * Esegue i controlli necessari per la modifica di un utente ed imposta gli eventuali messaggi di errore.
	 * @return Il codice del risultato in funzione dell'eventuale errore riscontrato, null se non viene riscontrato nessun errore.
	 * @throws Throwable In caso di errore.
	 */
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
	
	/**
	 * Esegue i controlli necessari per la cancellazione di un utente ed imposta gli opportuni messaggi di errore.
	 * @return Il codice del risultato in funzione dell'eventuale errore riscontrato, null se non viene riscontrato nessun errore.
	 * @throws Throwable In caso di errore.
	 */
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
	
	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
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
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public boolean isReset() {
		return _reset;
	}
	public void setReset(boolean reset) {
		this._reset = reset;
	}
	
	public UserDetails getUser() {
		return _user;
	}
	public void setUser(UserDetails user) {
		this._user = user;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private IUserManager _userManager;
	private IUserProfileManager _userProfileManager;
	
	private int _strutsAction;
	private String _username;
	private String _password;
	private String _passwordConfirm;
	
	private boolean _active = false;
	private boolean _reset;
	
	private UserDetails _user;
	
}
