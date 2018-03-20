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

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.DefaultApsEncrypter;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import org.entando.entando.aps.util.argon2.Argon2Encrypter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author paddeo
 */
public class UserManagerTest {

    @Mock
    private IUserDAO userDao;

    @Mock
    private ConfigInterface configManager;

    @InjectMocks
    private UserManager userManager;

    private List<UserDetails> users = new ArrayList<>();

    private String params = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<Params>\n"
            + "	<Param name=\"urlStyle\">classic</Param>\n"
            + "	<Param name=\"hypertextEditor\">none</Param>\n"
            + "	<Param name=\"treeStyle_page\">classic</Param>\n"
            + "	<Param name=\"treeStyle_category\">classic</Param>\n"
            + "	<Param name=\"startLangFromBrowser\">false</Param>\n"
            + "	<Param name=\"firstTimeMessages\">false</Param>\n"
            + "	<Param name=\"baseUrl\">request</Param>\n"
            + "	<Param name=\"baseUrlContext\">true</Param>\n"
            + "	<Param name=\"useJsessionId\">false</Param>\n"
            + "	<Param name=\"gravatarIntegrationEnabled\">false</Param>\n"
            + "	<Param name=\"editEmptyFragmentEnabled\">false</Param>\n"
            + "	<SpecialPages>\n"
            + "		<Param name=\"notFoundPageCode\">notfound</Param>\n"
            + "		<Param name=\"homePageCode\">homepage</Param>\n"
            + "		<Param name=\"errorPageCode\">errorpage</Param>\n"
            + "		<Param name=\"loginPageCode\">login</Param>\n"
            + "	</SpecialPages>\n"
            + "	<FeaturesOnDemand>\n"
            + "		<Param name=\"groupsOnDemand\">true</Param>\n"
            + "		<Param name=\"categoriesOnDemand\">true</Param>\n"
            + "		<Param name=\"contentTypesOnDemand\">true</Param>\n"
            + "		<Param name=\"contentModelsOnDemand\">true</Param>\n"
            + "		<Param name=\"apisOnDemand\">true</Param>\n"
            + "		<Param name=\"resourceArchivesOnDemand\">true</Param>\n"
            + "	</FeaturesOnDemand>\n"
            + "	<ExtendendPrivacyModule>\n"
            + "		<Param name=\"extendedPrivacyModuleEnabled\">false</Param>\n"
            + "		<Param name=\"maxMonthsSinceLastAccess\">6</Param>\n"
            + "		<Param name=\"maxMonthsSinceLastPasswordChange\">3</Param>\n"
            + "	</ExtendendPrivacyModule>\n"
            + "	<ExtraParams>\n"
            + "		<Param name=\"page_preview_hash\">ofc99CdASX8GBGMiiXLZ</Param>\n"
            + "	</ExtraParams>\n"
            + "</Params>\n";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(userDao.getEncrypter()).thenReturn(new Argon2Encrypter());
        Mockito.doNothing().when(configManager).updateConfigItem(Mockito.anyString(), Mockito.anyString());
        Mockito.when(configManager.getConfigItem(Mockito.anyString())).thenReturn(params);
    }

    @Test
    public void testUserManagerInitWithArgon2_OnlyAdmin() throws Exception {
        when(configManager.isArgon2()).thenReturn(true);
        this.emptyUsers();
        this.mockAdminPlainText();
        this.prepareInit();
        userManager.init();
        Mockito.when(userDao.loadUser("admin")).thenReturn(this.getMockUser("admin"));
        UserDetails admin = userManager.getUser("admin");
        assertNotNull(admin);
        boolean res = this.userManager.isArgon2Encrypted(admin.getPassword());
        assertTrue(res);
    }

    @Test
    public void testUserManagerInitWithArgon2_AdminNull() throws Exception {
        when(configManager.isArgon2()).thenReturn(true);
        this.emptyUsers();
        this.prepareInit();
        userManager.init();
        Mockito.when(userDao.loadUser("admin")).thenReturn(this.getMockUser("admin"));
        UserDetails admin = userManager.getUser("admin");
        assertNull(admin);
    }

    @Test
    public void testUserManagerInitPortingToArgon2PlainTextPasswords() throws Exception {
        when(configManager.isArgon2()).thenReturn(false);
        this.emptyUsers();
        this.mockAdminPlainText();
        this.mockUsersPlainText();
        this.prepareInit();
        Mockito.when(userDao.loadUser(Mockito.anyString())).thenReturn(this.getMockUser("admin"));
        Mockito.when(userDao.searchUsers(null)).thenReturn(users);
        Mockito.when(userDao.searchUsers(Mockito.anyString())).thenReturn(users);
        userManager.init();
        List<UserDetails> usersList = this.userManager.getUsers();
        assertNotNull(usersList);
        assertTrue(usersList.size() == 6);
        boolean res = true;
        for (UserDetails user : usersList) {
            res = this.userManager.isArgon2Encrypted(user.getPassword());
            if (!res) {
                break;
            }
        }
        assertTrue(res);
    }

    @Test
    public void testUserManagerInitPortingToArgon2OldEncryptionPasswords() throws Exception {
        when(configManager.isArgon2()).thenReturn(false);
        this.emptyUsers();
        this.mockAdminOldEncryption();
        this.mockUsersOldEncryption();
        this.prepareInit();
        Mockito.when(userDao.loadUser(Mockito.anyString())).thenReturn(this.getMockUser("admin"));
        Mockito.when(userDao.searchUsers(null)).thenReturn(users);
        Mockito.when(userDao.searchUsers(Mockito.anyString())).thenReturn(users);
        userManager.init();
        List<UserDetails> usersList = this.userManager.getUsers();
        assertNotNull(usersList);
        assertTrue(usersList.size() == 6);
        boolean res = true;
        for (UserDetails user : usersList) {
            res = this.userManager.isArgon2Encrypted(user.getPassword());
            if (!res) {
                break;
            }
        }
        assertTrue(res);
    }

    @Test
    public void testUserManagerInitPortingToArgon2OldEncryptionAndPlainTextPasswords() throws Exception {
        when(configManager.isArgon2()).thenReturn(false);
        this.emptyUsers();
        this.mockAdminPlainText();
        this.mockUsersMixed();
        this.prepareInit();
        Mockito.when(userDao.loadUser(Mockito.anyString())).thenReturn(this.getMockUser("admin"));
        Mockito.when(userDao.searchUsers(null)).thenReturn(users);
        Mockito.when(userDao.searchUsers(Mockito.anyString())).thenReturn(users);
        userManager.init();
        List<UserDetails> usersList = this.userManager.getUsers();
        assertNotNull(usersList);
        assertTrue(usersList.size() == 6);
        boolean res = true;
        for (UserDetails user : usersList) {
            res = this.userManager.isArgon2Encrypted(user.getPassword());
            if (!res) {
                break;
            }
        }
        assertTrue(res);
    }

    private void mockAdminPlainText() {
        UserDetails admin = null;
        User user = new User();
        user.setUsername("admin");
        user.setPassword("adminadmin");
        admin = user;
        users.add(admin);
    }

    private void mockAdminOldEncryption() throws Exception {
        UserDetails admin = null;
        User user = new User();
        user.setUsername("admin");
        user.setPassword(DefaultApsEncrypter.encryptString("adminadmin"));
        admin = user;
        users.add(admin);
    }

    private void mockUsersPlainText() {
        for (int i = 1; i <= 5; i++) {
            UserDetails user = this.mockUserPlainText("user_" + i, "password_" + i);
            users.add(user);
        }
    }

    private UserDetails mockUserPlainText(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private void mockUsersOldEncryption() throws Exception {
        for (int i = 1; i <= 5; i++) {
            UserDetails user = this.mockUserOldEncryption("user_" + i, "password_" + i);
            users.add(user);
        }
    }

    private UserDetails mockUserOldEncryption(String username, String password) throws Exception {
        User user = new User();
        user.setUsername(username);
        user.setPassword(DefaultApsEncrypter.encryptString(password));
        return user;
    }

    private void mockUsersMixed() throws Exception {
        for (int i = 1; i <= 3; i++) {
            UserDetails user = this.mockUserOldEncryption("user_" + i, "password_" + i);
            users.add(user);
        }
        for (int i = 4; i <= 5; i++) {
            UserDetails user = this.mockUserPlainText("user_" + i, "password_" + i);
            users.add(user);
        }
    }

    private void emptyUsers() {
        users = new ArrayList<>();
    }

    private UserDetails getMockUser(String username) {
        for (UserDetails user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private void changePasswordMock(String username, String password) {
        List<UserDetails> oldUsers = new ArrayList<>();
        oldUsers.addAll(users);
        this.emptyUsers();
        for (UserDetails user : oldUsers) {
            if (user.getUsername().equals(username)) {
                User cur = new User();
                cur.setUsername(username);
                cur.setPassword(Argon2Encrypter.encryptString(password));
                user = cur;
            }
            users.add(user);
        }
    }

    private void prepareInit() {
        for (int i = 1; i <= 5; i++) {
            String username = "user_" + i;
            UserDetails user = this.getMockUser(username);
            if (user != null) {
                Mockito.doNothing().when(userDao).changePassword(user.getUsername(), user.getPassword());
                this.changePasswordMock(username, user.getPassword());
            }
        }
        UserDetails admin = null;
        if ((admin = this.getMockUser("admin")) != null) {
            Mockito.doNothing().when(userDao).changePassword(admin.getUsername(), admin.getPassword());
            this.changePasswordMock("admin", admin.getPassword());
        }
    }

}
