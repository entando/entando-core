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
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.util.crypto.Argon2PasswordEncoder;
import org.entando.entando.aps.util.crypto.CompatiblePasswordEncoder;
import org.entando.entando.aps.util.crypto.LegacyPasswordEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserManagerTest {

    @Mock
    private IUserDAO userDao;

    @Mock
    private ConfigInterface configManager;

    @InjectMocks
    private UserManager userManager;

    private List<UserDetails> users = new ArrayList<>();

    private final String params = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
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

    private final PasswordEncoder passwordEncoder = getCompatiblePasswordEncoder();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(userDao.getPasswordEncoder()).thenReturn(passwordEncoder);
        doNothing().when(configManager).updateConfigItem(anyString(), anyString());
        when(configManager.getConfigItem(Mockito.anyString())).thenReturn(params);
    }

    @Test
    public void testUserManagerInitWithBCrypt_OnlyAdmin() throws Exception {
        when(configManager.areLegacyPasswordsUpdated()).thenReturn(true);
        this.emptyUsers();
        this.mockAdminPlainText();
        this.prepareInit();
        userManager.init();
        Mockito.when(userDao.loadUser("admin")).thenReturn(this.getMockUser("admin"));
        UserDetails admin = userManager.getUser("admin");
        assertNotNull(admin);
        assertBCrypt(admin.getPassword());
    }

    @Test
    public void testUserManagerInitWithBCrypt_AdminNull() throws Exception {
        when(configManager.areLegacyPasswordsUpdated()).thenReturn(true);
        this.emptyUsers();
        this.prepareInit();
        userManager.init();
        Mockito.when(userDao.loadUser("admin")).thenReturn(this.getMockUser("admin"));
        UserDetails admin = userManager.getUser("admin");
        assertNull(admin);
    }

    @Test
    public void testUserManagerInitPortingToBCryptPlainTextPasswords() throws Exception {
        when(configManager.areLegacyPasswordsUpdated()).thenReturn(false);
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
        assertEquals(6, usersList.size());
        for (UserDetails user : usersList) {
            assertBCrypt(user.getPassword());
        }
    }

    @Test
    public void testUserManagerInitPortingToBCryptOldEncryptionPasswords() throws Exception {
        when(configManager.areLegacyPasswordsUpdated()).thenReturn(false);
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
        assertEquals(6, usersList.size());
        for (UserDetails user : usersList) {
            assertBCrypt(user.getPassword());
        }
    }

    @Test
    public void testUserManagerInitPortingToBCryptOldEncryptionAndPlainTextPasswords() throws Exception {
        when(configManager.areLegacyPasswordsUpdated()).thenReturn(false);
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
        assertEquals(6, usersList.size());
        for (UserDetails user : usersList) {
            assertBCrypt(user.getPassword());
        }
    }

    private void mockAdminPlainText() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("adminadmin");
        users.add(user);
    }

    private void mockAdminOldEncryption() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(new LegacyPasswordEncryptor().encrypt("adminadmin"));
        users.add(user);
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
        user.setPassword(new LegacyPasswordEncryptor().encrypt(password));
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
        List<UserDetails> oldUsers = new ArrayList<>(users);
        this.emptyUsers();
        for (UserDetails user : oldUsers) {
            if (user.getUsername().equals(username)) {
                User cur = new User();
                cur.setUsername(username);
                cur.setPassword(passwordEncoder.encode(password));
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
        UserDetails admin;
        if ((admin = this.getMockUser("admin")) != null) {
            Mockito.doNothing().when(userDao).changePassword(admin.getUsername(), admin.getPassword());
            this.changePasswordMock("admin", admin.getPassword());
        }
    }

    private void assertBCrypt(String password) {
        assertTrue(CompatiblePasswordEncoder.isBCrypt(password));
    }

    private CompatiblePasswordEncoder getCompatiblePasswordEncoder() {
        return new CompatiblePasswordEncoder(new BCryptPasswordEncoder(),
                new Argon2PasswordEncoder(), new LegacyPasswordEncryptor());
    }
}
