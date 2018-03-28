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
    private boolean accountNotExpired;
    private boolean credentialsNotExpired;

    private int maxMonthsSinceLastAccess;
    private int maxMonthsSinceLastPasswordChange;

    public UserDto(UserDetails user) {
        this.username = user.getUsername();
        this.status = user.isDisabled() ? IUserService.STATUS_DISABLED : IUserService.STATUS_ACTIVE;
        this.accountNotExpired = user.isAccountNotExpired();
        this.credentialsNotExpired = user.isCredentialsNotExpired();
        if (user.isEntandoUser()) {
            User entandoUser = (User) user;
            this.registration = entandoUser.getCreationDate();
            this.lastLogin = entandoUser.getLastAccess();
            this.lastPasswordChange = entandoUser.getLastPasswordChange();
            this.maxMonthsSinceLastAccess = entandoUser.getMaxMonthsSinceLastAccess();
            this.maxMonthsSinceLastPasswordChange = entandoUser.getMaxMonthsSinceLastPasswordChange();
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

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    public int getMaxMonthsSinceLastAccess() {
        return maxMonthsSinceLastAccess;
    }

    public void setMaxMonthsSinceLastAccess(int maxMonthsSinceLastAccess) {
        this.maxMonthsSinceLastAccess = maxMonthsSinceLastAccess;
    }

    public int getMaxMonthsSinceLastPasswordChange() {
        return maxMonthsSinceLastPasswordChange;
    }

    public void setMaxMonthsSinceLastPasswordChange(int maxMonthsSinceLastPasswordChange) {
        this.maxMonthsSinceLastPasswordChange = maxMonthsSinceLastPasswordChange;
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
