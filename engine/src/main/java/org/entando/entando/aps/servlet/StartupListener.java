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
package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import org.entando.entando.aps.system.exception.CSRFProtectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.Objects;

/**
 * Init the system when the web application is started
 *
 * @author E.Santoboni
 */
public class StartupListener extends org.springframework.web.context.ContextLoaderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext svCtx = event.getServletContext();
        String msg = this.getClass().getName() + ": INIT " + svCtx.getServletContextName();
        System.out.println(msg);
        super.contextInitialized(event);
        msg = this.getClass().getName() + ": INIT DONE " + svCtx.getServletContextName();
        System.out.println(msg);
        boolean isActive = Objects.nonNull(System.getenv(SystemConstants.ENTANDO_CSRF_PROTECTION));
        String whiteList = System.getenv(SystemConstants.ENTANDO_CSRF_ALLOWED_DOMAINS);
        if (!isActive || !SystemConstants.CSRF_BASIC_PROTECTION.equals(System.getenv(SystemConstants.ENTANDO_CSRF_PROTECTION))) {
            LOGGER.warn("CSRF protection is not enabled");
        } else if (SystemConstants.CSRF_BASIC_PROTECTION.equals(System.getenv(SystemConstants.ENTANDO_CSRF_PROTECTION))) {
            if (whiteList != null && !whiteList.equals("")) {
                String message = "CSRF protection is enabled Domains --> ".concat(whiteList);
                LOGGER.info(message);
            } else {
                LOGGER.error("CSRF protection is enabled but the domains are initialized. Please initialize the domains");
                throw new CSRFProtectionException("CSRF protection is enabled but the domains are not initialized. Please initialize the domains");
            }
        }

    }

}
