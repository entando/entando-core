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
package org.entando.entando.web.user.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author paddeo
 */
public class UserAuthoritiesRequest {

    @JsonRawValue
    @Size(min = 1)
    private List<UserAuthority> authorities;

    public UserAuthoritiesRequest() {
    }

    public List<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<UserAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserAuthoritiesRequest{" + "authorities:[" + authorities.stream().iterator().next().toString() + "]}";
    }

}
