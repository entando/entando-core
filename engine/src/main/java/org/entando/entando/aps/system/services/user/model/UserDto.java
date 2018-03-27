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
package org.entando.entando.aps.system.services.user.model;

import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.Date;
import org.entando.entando.aps.system.services.user.IUserService;

/**
 *
 * @author paddeo
 */
public class UserDto {

    private String username;
    private Date registration;
    private Date lastLogin;
    private Date lastPasswordChange;
    private String status;
    private boolean passwordChangeRequired = false;

    public UserDto(UserDetails user) {
        this.username = user.getUsername();
        if (user.isEntandoUser()) {
            User entandoUser = (User) user;
            this.registration = entandoUser.getCreationDate();
            this.lastLogin = entandoUser.getLastAccess();
            this.lastPasswordChange = entandoUser.getLastPasswordChange();
            this.status = entandoUser.isDisabled() ? IUserService.STATUS_DISABLED : IUserService.STATUS_ACTIVE;
            this.passwordChangeRequired = !(entandoUser.isAccountNotExpired() || entandoUser.isCredentialsNotExpired() || entandoUser.isDisabled());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegistration() {
        return registration;
    }

    public void setRegistration(Date registration) {
        this.registration = registration;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "username":
                return "username";
            default:
                return null;
        }
    }
}
