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
package org.entando.entando.aps.system.services.user.model;

import com.agiletec.aps.system.services.authorization.Authorization;

/**
 *
 * @author paddeo
 */
public class UserAuthorityDto {

    private String group;
    private String role;

    public UserAuthorityDto(String group, String role) {
        this.group = group;
        this.role = role;
    }

    public UserAuthorityDto(Authorization auth) {
        if (null != auth.getRole()) {
            this.setRole(auth.getRole().getName());
        }
        if (null != auth.getGroup()) {
            this.setGroup(auth.getGroup().getName());
        }
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
