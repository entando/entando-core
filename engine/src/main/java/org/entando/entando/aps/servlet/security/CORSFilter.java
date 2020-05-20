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
package org.entando.entando.aps.servlet.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author paddeo, f.leandro
 */
public class CORSFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private boolean enabled;
    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private String allowedCredentials;
    private String maxAge;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!enabled) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        logger.trace("Configuring CORS Headers....");
        // CORS "pre-flight" request

        httpResponse.addHeader("Access-Control-Allow-Origin", allowedOrigins);
        httpResponse.addHeader("Access-Control-Allow-Methods", allowedMethods);
        httpResponse.addHeader("Access-Control-Allow-Headers", allowedHeaders);
        httpResponse.addHeader("Access-Control-Allow-Credentials", allowedCredentials);
        httpResponse.addHeader("Access-Control-Max-Age", maxAge);

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(httpRequest, httpResponse);
        }
    }

    @Override public void init(final FilterConfig filterConfig) {}
    @Override public void destroy() {}

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public void setAllowedCredentials(String allowedCredentials) {
        this.allowedCredentials = allowedCredentials;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
