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
package com.agiletec.aps.system.services.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.GroupUtilizer;

/**
 * Servizio di gestione degli utenti.
 * @author M.Diana - E.Santoboni
 */
public class UserManager extends AbstractService implements IUserManager, GroupUtilizer {

	private static final Logger _logger =  LoggerFactory.getLogger(UserManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public List<String> getUsernames() throws ApsSystemException {
		return this.searchUsernames(null);
	}
	
	@Override
	public List<String> searchUsernames(String text) throws ApsSystemException {
		List<String> usernames = null;
		try {
			usernames = this.getUserDAO().searchUsernames(text);
		} catch (Throwable t) {
			_logger.error("Error searching usernames by text '{}'", text,  t);
			//ApsSystemUtils.logThrowable(t, this, "searchUsernames");
			throw new ApsSystemException("Error loading the username list", t);
		}
		return usernames;
	}
	
	/**
	 * Restituisce la lista completa degli utenti (in oggetti User).
	 * @return La lista completa degli utenti (in oggetti User).
	 * @throws ApsSystemException In caso di errore in accesso al db.
	 */
	@Override
	public List<UserDetails> getUsers() throws ApsSystemException {
		return this.searchUsers(null);
	}

	@Override
	public List<UserDetails> searchUsers(String text) throws ApsSystemException {
		List<UserDetails> users = null;
		try {
			users = this.getUserDAO().searchUsers(text);
			for (int i=0; i<users.size(); i++) {
				this.setUserCredentialCheckParams(users.get(i));
			}
		} catch (Throwable t) {
			_logger.error("Error searching users by text '{}'", text,  t);
			//ApsSystemUtils.logThrowable(t, this, "searchUsers");
			throw new ApsSystemException("Error loading the user list", t);
		}
		return users;
	}

	/**
	 * Elimina un'utente dal db.
	 * @param user L'utente da eliminare dal db.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public void removeUser(UserDetails user) throws ApsSystemException {
		try {
			this.getUserDAO().deleteUser(user);
		} catch (Throwable t) {
			_logger.error("Error deleting user '{}'", user,  t);
			//ApsSystemUtils.logThrowable(t, this, "removeUser");
			throw new ApsSystemException("Error deleting a user", t);
		}
	}

	@Override
	public void removeUser(String username) throws ApsSystemException {
		try {
			this.getUserDAO().deleteUser(username);
		} catch (Throwable t) {
			_logger.error("Error deleting user '{}'", username,  t);
			//ApsSystemUtils.logThrowable(t, this, "removeUser");
			throw new ApsSystemException("Error deleting a user", t);
		}
	}

	/**
	 * Aggiorna un utente nel db.
	 * @param user L'utente da aggiornare nel db.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public void updateUser(UserDetails user) throws ApsSystemException {
		try {
			this.getUserDAO().updateUser(user);
		} catch (Throwable t) {
			_logger.error("Error updating user '{}'", user,  t);
			//ApsSystemUtils.logThrowable(t, this, "updateUser");
			throw new ApsSystemException("Error updating the User", t);
		}
	}

	@Override
	public void changePassword(String username, String password) throws ApsSystemException {
		try {
			this.getUserDAO().changePassword(username, password);
		} catch (Throwable t) {
			_logger.error("Error on change password for user '{}'", username,  t);
			//ApsSystemUtils.logThrowable(t, this, "changePassword");
			throw new ApsSystemException("Error updating the password of the User" + username, t);
		}
	}

	@Override
	public void updateLastAccess(UserDetails user) throws ApsSystemException {
		if (!user.isJapsUser()) return;
		try {
			this.getUserDAO().updateLastAccess(user.getUsername());
		} catch (Throwable t) {
			_logger.error("Error on update last access for user '{}'", user,  t);
			//ApsSystemUtils.logThrowable(t, this, "updateLastAccess");
			throw new ApsSystemException("Error while refreshing the last access date of the User " + user.getUsername(), t);
		}
	}

	/**
	 * Aggiunge un utente nel db.
	 * @param user L'utente da aggiungere nel db.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public void addUser(UserDetails user) throws ApsSystemException {
		try {
			this.getUserDAO().addUser(user);
		} catch (Throwable t) {
			_logger.error("Error on add user '{}'", user,  t);
			//ApsSystemUtils.logThrowable(t, this, "addUser");
			throw new ApsSystemException("Error adding a new user ", t);
		}
	}

	/**
	 * Recupera un'user caricandolo da db. Se la userName non 
	 * corrisponde ad un utente restituisce null.
	 * @param username Lo username dell'utente da restituire.
	 * @return L'utente cercato, null se non vi è nessun utente 
	 * corrispondente alla username immessa.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public UserDetails getUser(String username) throws ApsSystemException {
		UserDetails user = null;
		try {
			user = this.getUserDAO().loadUser(username);
		} catch (Throwable t) {
			_logger.error("Error loading user by username '{}'", username,  t);
			//ApsSystemUtils.logThrowable(t, this, "getUser");
			throw new ApsSystemException("Error loading user", t);
		}
		this.setUserCredentialCheckParams(user);
		return user;
	}

	/**
	 * Recupera un'user caricandolo da db. Se userName e password non
	 * corrispondono ad un utente, restituisce null.
	 * @param username Lo username dell'utente da restituire.
	 * @param password La password dell'utente da restituire.
	 * @return L'utente cercato, null se non vi è nessun utente 
	 * corrispondente alla username e password immessa.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public UserDetails getUser(String username, String password) throws ApsSystemException {
		UserDetails user = null;
		try {
			user = this.getUserDAO().loadUser(username, password);
		} catch (Throwable t) {
			_logger.error("Error loading user by username and password. username: '{}'", username,  t);
			//ApsSystemUtils.logThrowable(t, this, "getUser");
			throw new ApsSystemException("Error loading user", t);
		}
		this.setUserCredentialCheckParams(user);
		return user;
	}

	/**
	 * Inserisce nell'utenza le informazioni necessarie per la verifica della validità delle credenziali.
	 * In particolare, in base allo stato del Modulo Privacy (attivo oppure no), inserisce le informazioni 
	 * riguardo il numero massimo di mesi consentiti dal ultimo accesso e il numero massimo di mesi consentiti 
	 * dal ultimo cambio password (parametri estratti dalla configurazioni di sistema). 
	 * @param user L'utenza sulla quale inserire le informazioni necessarie 
	 * per la verifica della validità delle credenziali.
	 */
	protected void setUserCredentialCheckParams(UserDetails user) {
		if (null != user && user.isJapsUser()) {
			User japsUser = (User) user;
			String enabledPrivacyModuleParValue = this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_PM_ENABLED);
			boolean enabledPrivacyModule = Boolean.parseBoolean(enabledPrivacyModuleParValue);
			japsUser.setCheckCredentials(enabledPrivacyModule);
			if (enabledPrivacyModule) {
				int maxMonthsSinceLastAccess = this.extractNumberParamValue(SystemConstants.CONFIG_PARAM_PM_MM_LAST_ACCESS, 6);
				japsUser.setMaxMonthsSinceLastAccess(maxMonthsSinceLastAccess);
				int maxMonthsSinceLastPasswordChange = this.extractNumberParamValue(SystemConstants.CONFIG_PARAM_PM_MM_LAST_PASSWORD_CHANGE, 3);
				japsUser.setMaxMonthsSinceLastPasswordChange(maxMonthsSinceLastPasswordChange);
			}
		}
	}

	private int extractNumberParamValue(String paramName, int defaultValue) {
		String parValue = this.getConfigManager().getParam(paramName);
		int value = 0;
		try {
			value = Integer.parseInt(parValue);
		} catch (NumberFormatException e) {
			value = defaultValue;
		}
		return value;
	}
	
	/**
	 * Restituisce l'utente di default di sistema.
	 * L'utente di default rappresenta un utente "ospite" senza nessuna autorizzazione 
	 * di accesso ad elementi non "liberi" e senza nessuna autorizzazione ad eseguire 
	 * qualunque azione sugli elementi del sistema. 
	 * @return L'utente di default di sistema.
	 */
	@Override
	public UserDetails getGuestUser() {
		User user = new User();
		user.setUsername(SystemConstants.GUEST_USER_NAME);
		return user;
	}
	
	@Override
	public List<UserDetails> getGroupUtilizers(String groupName) throws ApsSystemException {
		List<String> usernames = null;
		List<UserDetails> utilizers = new ArrayList<UserDetails>();
		try {
			usernames = this.getUserDAO().loadUsernamesForGroup(groupName);
			if (usernames != null) {
				for (int i=0; i<usernames.size(); i++) {
					String username = usernames.get(i);
					UserDetails user = this.getUser(username);
					if (null != user) {
						utilizers.add(user);
					} else {
						//ApsSystemUtils.getLogger().info("Searching for the references of the group '" + groupName +	 "' - The username '" + username + "'referenced does not correspond to any valid user."+ "Deleting reference!");
						_logger.info("Searching for the references of the group '{}' - The username '{}'referenced does not correspond to any valid user. Deleting reference!", groupName, username);
						this.getUserDAO().deleteUser(username);
					}
				}
			}
		} catch (Throwable t) {
			//ApsSystemUtils.logThrowable(t, this, "getGroupUtilizers");
			_logger.error("Error while loading the members of the group: '{}'", groupName,  t);
			throw new ApsSystemException("Error while loading the members of the group "+ groupName, t);
		}
		return utilizers;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected IUserDAO getUserDAO() {
		return _userDao;
	}
	public void setUserDAO(IUserDAO userDao) {
		this._userDao = userDao;
	}

	private IUserDAO _userDao;

	private ConfigInterface _configManager;

}