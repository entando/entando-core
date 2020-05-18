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
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Inizializer Class for Spring Security
 *
 * @author E.Santoboni
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        super.beforeSpringSecurityFilterChain(servletContext);

        Properties properties = loadSystemParams(servletContext);
        super.insertFilters(servletContext, new CORSFilter(properties));
    }

    private Properties loadSystemParams(ServletContext servletContext) {
        String filename = "/WEB-INF/conf/systemParams.properties";
        try {
            InputStream is = servletContext.getResourceAsStream(filename);
            Properties props = new Properties();
            props.load(is);
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load properties file: " + filename);
        }
    }

}
