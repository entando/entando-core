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
