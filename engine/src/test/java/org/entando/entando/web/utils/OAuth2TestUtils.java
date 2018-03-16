/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.utils;

import java.util.Calendar;

import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.User;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;

public class OAuth2TestUtils {

    public static OAuth2Token getOAuth2Token(String username, String accessToken) {
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setAccessToken(accessToken);
        oAuth2Token.setRefreshToken("refresh_token");
        oAuth2Token.setClientId(username);
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, 3600);
        oAuth2Token.setExpiresIn(calendar.getTime());
        oAuth2Token.setGrantType("password");
        return oAuth2Token;
    }

    public static String getValidAccessToken() {
        return getAccessToken(true);
    }

    public static String getAccessToken(boolean valid) {
        if (valid) {
            return "valid_token";
        }
        return "wrong_token";
    }

    public static User createMockUser(String username, String pws) {
        User rawUser = new User();
        rawUser.setUsername(username);
        rawUser.setPassword(pws);
        return rawUser;
    }

    public static void addAuthorization(User user, String groupName, String roleName, String[] permissions) {

        Group group = null;
        if (StringUtils.isNotBlank(groupName)) {
            group = new Group();
            group.setName(groupName);
            group.setDescription(groupName + " descr");
        }

        Role role = null;
        if (StringUtils.isNotBlank(roleName)) {
            role = new Role();
            role.setName(roleName);
            role.setDescription(roleName + " descr");
            if (null != permissions) {
                for (String permissionName : permissions) {
                    role.addPermission(permissionName);
                }
            }
        }

        Authorization auth = new Authorization(group, role);
        user.addAuthorization(auth);
    }

    public static class UserBuilder {

        private User user;

        public UserBuilder(String username, String password) {
            this.user = OAuth2TestUtils.createMockUser(username, password);
        }

        public UserBuilder withGroup(String groupName) {
            OAuth2TestUtils.addAuthorization(this.user, groupName, null, null);
            return this;
        }

        public UserBuilder withAuthorization(String groupName, String roleName, String... permissions) {
            OAuth2TestUtils.addAuthorization(this.user, groupName, roleName, permissions);
            return this;
        }

        public UserBuilder grantedToRoleAdmin() {

            OAuth2TestUtils.addAuthorization(this.user, "administrators", "admin", new String[]{Permission.SUPERUSER});
            return this;
        }

        public User build() {
            return user;
        }

    }

}
