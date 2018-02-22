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

import javax.servlet.http.HttpServletRequest;

public class EntandoAuthorizationException extends RuntimeException {

    private HttpServletRequest request;
    private String username;

    public EntandoAuthorizationException(String message, HttpServletRequest request, String username) {
        this.request = request;
        this.username = username;
    }


    public String getRequestURI() {
        return this.request.getRequestURI();
    }

    public String getMethod() {
        return this.request.getMethod();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
