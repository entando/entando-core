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
package com.agiletec.aps.system.services.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.util.crypto.LegacyPasswordEncryptor;

/**
 * Servizio di gestione degli utenti.
 *
 * @author M.Diana - E.Santoboni
 */
public class UserManager extends AbstractService implements IUserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    private IUserDAO userDao;
    private ConfigInterface configManager;
    
    @Override
    public void init() throws Exception {
        if (!this.getConfigManager().areLegacyPasswordsUpdated()) {
            List<UserDetails> users = this.getUserDAO().loadUsers();
            for (UserDetails user : users) {
                updateLegacyPassword(user);
            }
            Map<String, String> newParam = new HashMap<>();
            newParam.put(ConfigInterface.LEGACY_PASSWORDS_UPDATED, "true");
            String oldParam = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
            String newXmlParams = null;
            try {
                newXmlParams = SystemParamsUtils.getNewXmlParams(oldParam, newParam);
            } catch (Throwable ex) {
                logger.error("Error updating XML param", ex);
            }
            this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
        }
        logger.debug("{} ready", this.getClass().getName());
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
            logger.error("Error searching usernames by text '{}'", text, t);
            throw new ApsSystemException("Error loading the username list", t);
        }
        return usernames;
    }

    /**
     * Restituisce la lista completa degli utenti (in oggetti User).
     *
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
            for (int i = 0; i < users.size(); i++) {
                this.setUserCredentialCheckParams(users.get(i));
            }
        } catch (Throwable t) {
            logger.error("Error searching users by text '{}'", text, t);
            throw new ApsSystemException("Error loading the user list", t);
        }
        return users;
    }

    /**
     * Elimina un'utente dal db.
     *
     * @param user L'utente da eliminare dal db.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    @Override
    public void removeUser(UserDetails user) throws ApsSystemException {
        try {
            this.getUserDAO().deleteUser(user);
        } catch (Throwable t) {
            logger.error("Error deleting user '{}'", user, t);
            throw new ApsSystemException("Error deleting a user", t);
        }
    }

    @Override
    public void removeUser(String username) throws ApsSystemException {
        try {
            this.getUserDAO().deleteUser(username);
        } catch (Throwable t) {
            logger.error("Error deleting user '{}'", username, t);
            throw new ApsSystemException("Error deleting a user", t);
        }
    }

    /**
     * Aggiorna un utente nel db.
     *
     * @param user L'utente da aggiornare nel db.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    @Override
    public void updateUser(UserDetails user) throws ApsSystemException {
        try {
            this.getUserDAO().updateUser(user);
        } catch (Throwable t) {
            logger.error("Error updating user '{}'", user, t);
            throw new ApsSystemException("Error updating the User", t);
        }
    }

    @Override
    public void changePassword(String username, String password) throws ApsSystemException {
        try {
            this.getUserDAO().changePassword(username, password);
        } catch (Throwable t) {
            logger.error("Error on change password for user '{}'", username, t);
            throw new ApsSystemException("Error updating the password of the User" + username, t);
        }
    }

    @Override
    public void updateLastAccess(UserDetails user) throws ApsSystemException {
        if (!user.isEntandoUser()) {
            return;
        }
        try {
            this.getUserDAO().updateLastAccess(user.getUsername());
        } catch (Throwable t) {
            logger.error("Error on update last access for user '{}'", user, t);
            throw new ApsSystemException("Error while refreshing the last access date of the User " + user.getUsername(), t);
        }
    }

    /**
     * Aggiunge un utente nel db.
     *
     * @param user L'utente da aggiungere nel db.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    @Override
    public void addUser(UserDetails user) throws ApsSystemException {
        try {
            this.getUserDAO().addUser(user);
        } catch (Throwable t) {
            logger.error("Error on add user '{}'", user, t);
            throw new ApsSystemException("Error adding a new user ", t);
        }
    }

    /**
     * Recupera un'user caricandolo da db. Se la userName non corrisponde ad un
     * utente restituisce null.
     *
     * @param username Lo username dell'utente da restituire.
     * @return L'utente cercato, null se non vi è nessun utente corrispondente
     * alla username immessa.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    @Override
    public UserDetails getUser(String username) throws ApsSystemException {
        UserDetails user = null;
        try {
            user = this.getUserDAO().loadUser(username);
        } catch (Throwable t) {
            logger.error("Error loading user by username '{}'", username, t);
            throw new ApsSystemException("Error loading user", t);
        }
        this.setUserCredentialCheckParams(user);
        return user;
    }

    /**
     * Recupera un'user caricandolo da db. Se userName e password non
     * corrispondono ad un utente, restituisce null.
     *
     * @param username Lo username dell'utente da restituire.
     * @param password La password dell'utente da restituire.
     * @return L'utente cercato, null se non vi è nessun utente corrispondente
     * alla username e password immessa.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    @Override
    public UserDetails getUser(String username, String password) throws ApsSystemException {
        UserDetails user = null;
        try {
            user = this.getUserDAO().loadUser(username, password);
        } catch (Throwable t) {
            logger.error("Error loading user by username and password. username: '{}'", username, t);
            throw new ApsSystemException("Error loading user", t);
        }
        this.setUserCredentialCheckParams(user);
        return user;
    }

    /**
     * Inserisce nell'utenza le informazioni necessarie per la verifica della
     * validità delle credenziali. In particolare, in base allo stato del Modulo
     * Privacy (attivo oppure no), inserisce le informazioni riguardo il numero
     * massimo di mesi consentiti dal ultimo accesso e il numero massimo di mesi
     * consentiti dal ultimo cambio password (parametri estratti dalla
     * configurazioni di sistema).
     *
     * @param user L'utenza sulla quale inserire le informazioni necessarie per
     * la verifica della validità delle credenziali.
     */
    protected void setUserCredentialCheckParams(UserDetails user) {
        if (null != user && user.isEntandoUser()) {
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

    private void updateLegacyPassword(UserDetails user) throws ApsSystemException {
        String pwd = user.getPassword();
        if (!isBCryptEncoded(pwd) && !isArgon2Encoded(pwd)) {
            try {
                pwd = new LegacyPasswordEncryptor().decrypt(pwd);
            } catch (Exception e) {
                logger.warn("Plain text password for user {}", user.getUsername());
                pwd = user.getPassword();
            }
            this.changePassword(user.getUsername(), pwd);
        }
    }

    private boolean isArgon2Encoded(String encoded) {
        if (StringUtils.isBlank(encoded)) {
            return false;
        }
        return encoded.startsWith("$argon2");
    }

    private boolean isBCryptEncoded(String encoded) {
        if (StringUtils.isBlank(encoded)) {
            return false;
        }
        return encoded.startsWith("{bcrypt}");
    }

    /**
     * Restituisce l'utente di default di sistema. L'utente di default
     * rappresenta un utente "ospite" senza nessuna autorizzazione di accesso ad
     * elementi non "liberi" e senza nessuna autorizzazione ad eseguire
     * qualunque azione sugli elementi del sistema.
     *
     * @return L'utente di default di sistema.
     */
    @Override
    public UserDetails getGuestUser() {
        User user = new User();
        user.setUsername(SystemConstants.GUEST_USER_NAME);
        return user;
    }

    protected ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    protected IUserDAO getUserDAO() {
        return userDao;
    }

    public void setUserDAO(IUserDAO userDao) {
        this.userDao = userDao;
    }
}
