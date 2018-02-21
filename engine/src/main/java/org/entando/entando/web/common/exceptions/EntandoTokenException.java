package org.entando.entando.web.common.exceptions;

import javax.servlet.http.HttpServletRequest;

public class EntandoTokenException extends EntandoAuthorizationException {

    public EntandoTokenException(String message, HttpServletRequest request, String username) {
        super(message, request, username);
    }

}
