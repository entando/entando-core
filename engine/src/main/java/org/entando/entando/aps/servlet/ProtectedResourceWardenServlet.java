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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This servlet handles the requests for protected resources. 
 * @author E.Santoboni
 */
public class ProtectedResourceWardenServlet extends HttpServlet {
	
	private static final Logger _logger = LoggerFactory.getLogger(ProtectedResourceWardenServlet.class);
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		_logger.debug("Request: {}", request.getRequestURI());
		try {
			ServletContext svCtx = request.getSession().getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(svCtx);
			String[] resourceProviderNames = wac.getBeanNamesForType(IProtectedResourceProvider.class);
			for (int i = 0; i < resourceProviderNames.length; i++) {
				String resourceProviderName = resourceProviderNames[i];
				IProtectedResourceProvider provider = (IProtectedResourceProvider) wac.getBean(resourceProviderName);
				boolean responseCommitted = provider.provideProtectedResource(request, response);
				if (responseCommitted) {
					return;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error providing protected resource", t);
			throw new ServletException("Error providing protected resource", t);
		}
	}
	
}
