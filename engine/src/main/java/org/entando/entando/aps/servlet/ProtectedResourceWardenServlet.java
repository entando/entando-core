/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
