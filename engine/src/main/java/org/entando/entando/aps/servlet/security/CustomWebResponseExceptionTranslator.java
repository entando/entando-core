/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.servlet.security;

import java.io.IOException;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * @author E.Santoboni
 */
public class CustomWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        // Try to extract a SpringSecurityException from the stacktrace
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        if ((null != throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain))
                || (null != throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain))
                || (throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain) instanceof AccessDeniedException)
                || (throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class, causeChain) instanceof HttpRequestMethodNotSupportedException)
                || (throwableAnalyzer.getFirstThrowableOfType(UsernameNotFoundException.class, causeChain) instanceof UsernameNotFoundException)) {
            return super.translate(e);
        }
        if (throwableAnalyzer.getFirstThrowableOfType(RuntimeException.class, causeChain) instanceof RuntimeException) {
            return handleOAuth2Exception(new BadRequestException(e.getMessage(), e));
        }
        return super.translate(e);
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }
        ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(e, headers,
                HttpStatus.valueOf(status));
        return response;
    }

    @SuppressWarnings("serial")
    private static class BadRequestException extends OAuth2Exception {

        public BadRequestException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "bad_request";
        }

        @Override
        public int getHttpErrorCode() {
            return 400;
        }
    }

}
