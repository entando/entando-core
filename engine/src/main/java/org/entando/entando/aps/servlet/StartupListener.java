/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Init the system when the web application is started
 * @author E.Santoboni
 */
public class StartupListener extends org.springframework.web.context.ContextLoaderListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext svCtx = event.getServletContext();
		String msg = this.getClass().getName()+ ": INIT " + svCtx.getServletContextName();
		System.out.println(msg);
		super.contextInitialized(event);
		msg = this.getClass().getName() + ": INIT DONE "+ svCtx.getServletContextName();
		System.out.println(msg);
	}
	
}
