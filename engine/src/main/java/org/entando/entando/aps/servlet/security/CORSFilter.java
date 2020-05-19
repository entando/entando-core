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

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.misc.ObjectInputFilter.Config;

/**
 *
 * @author paddeo
 */
public class CORSFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private String allowedCredentials;
    private String maxAge;

    /**
     * Loads CORS configurations from Environment variables.
     * If not present, fallbacks to {@code systemParams} with converted key.
     * Example: {@code CORS_ACCESS_CONTROL_ALLOW_ORIGIN} fallbacks to {@code systemParams} using key {@code cors.access.control.allow.origin}
     * Useful for having default values or used during integration tests.
     * @param configs Fallback systemParams Bean
     */
    public CORSFilter(ConfigInterface configs) {
        this.allowedOrigins = configs.getProperty(ConfigInterface.CORS_ALLOWED_ORIGIN);
        this.allowedMethods = configs.getProperty(ConfigInterface.CORS_ALLOWED_METHODS);
        this.allowedHeaders = configs.getProperty(ConfigInterface.CORS_ALLOWED_HEADERS);
        this.allowedCredentials = configs.getProperty(ConfigInterface.CORS_ALLOWED_CREDENTIALS);
        this.maxAge = configs.getProperty(ConfigInterface.CORS_MAX_AGE);
    }

    /**
     * Loads CORS configurations from Environment variables.
     * If not present, fallbacks to manually provided {@code Property} map using a converted key.
     * Example: {@code CORS_ACCESS_CONTROL_ALLOW_ORIGIN} fallbacks to provided properties map using key {@code cors.access.control.allow.origin}
     * Useful for having default values or used during integration tests.
     * @param properties Fallback properties map
     */
    public CORSFilter(Properties properties) {
        this.allowedOrigins = ConfigInterface.getProperty(properties, ConfigInterface.CORS_ALLOWED_ORIGIN);
        this.allowedMethods = ConfigInterface.getProperty(properties, ConfigInterface.CORS_ALLOWED_METHODS);
        this.allowedHeaders = ConfigInterface.getProperty(properties, ConfigInterface.CORS_ALLOWED_HEADERS);
        this.allowedCredentials = ConfigInterface.getProperty(properties, ConfigInterface.CORS_ALLOWED_CREDENTIALS);
        this.maxAge = ConfigInterface.getProperty(properties, ConfigInterface.CORS_MAX_AGE);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.trace("Sending Header....");
        // CORS "pre-flight" request
        response.addHeader("Access-Control-Allow-Origin", allowedOrigins);
        response.addHeader("Access-Control-Allow-Methods", allowedMethods);
        response.addHeader("Access-Control-Allow-Headers", allowedHeaders);
        response.addHeader("Access-Control-Allow-Credentials", allowedCredentials);
        response.addHeader("Access-Control-Max-Age", maxAge);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
