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
package org.entando.entando.web.common.exceptions;

import org.springframework.validation.BindingResult;

public class ResourcePermissionsException extends RuntimeException {

    private final BindingResult bindingResult;

    private String username;

    private String resource;

    public ResourcePermissionsException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public ResourcePermissionsException(BindingResult bindingResult, String username, String resource) {
        this.bindingResult = bindingResult;
        this.username = username;
        this.resource = resource;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

}
