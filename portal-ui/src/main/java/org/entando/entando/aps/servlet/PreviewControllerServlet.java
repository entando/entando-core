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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Controller Servlet for pages preview. It's the entry point for the preview of
 * the draft version of a page. Check the request, verify the authorization,
 * generate the page and dispose the response.
 * 
 * @author E.Mezzano
 */
public class PreviewControllerServlet extends ControllerServlet {

	private static final Logger _logger = LoggerFactory.getLogger(PreviewControllerServlet.class);

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		RequestContext reqCtx = this.initRequestContext(request, response);
		int status = this.controlRequest(request, reqCtx);
		if (status == ControllerManager.OUTPUT) {
			_logger.debug("Output");
			try {
				this.initFreemarker(request, response, reqCtx);
				this.executePage(request, reqCtx);
			} catch (Throwable t) {
				_logger.error("Error building response", t);
				throw new ServletException("Error building response", t);
			}
		} else {
//			if (status == ControllerManager.REDIRECT) {
//				_logger.debug("Redirection");
//				this.redirect(reqCtx, response);
//			} else if (status == ControllerManager.ERROR) {
//				_logger.debug("Error");
//				this.outputError(reqCtx, response);
//			} else {
				_logger.error("Error: final status = {} - request: {} - path: {}",
						ControllerManager.getStatusDescription(status),
						request.getServletPath(), request.getPathInfo());
				throw new ServletException("Service not available");
//			}
		}
		return;
	}

	@Override
	protected int controlRequest(HttpServletRequest request, RequestContext reqCtx) {
		int status = ControllerManager.INVALID_STATUS;
		List<ControlServiceInterface> services = (List<ControlServiceInterface>) ApsWebApplicationUtils.getBean("PreviewControllerServices", request);
		try {
			for (ControlServiceInterface service : services) {
				status = service.service(reqCtx, status);
				if (status == ControllerManager.OUTPUT
						|| status == ControllerManager.REDIRECT
						|| status == ControllerManager.SYS_ERROR) {
					break;
				}
			}
		} catch (Throwable t) {
			_logger.error("generic error", t);
			// ApsSystemUtils.logThrowable(t, this, "service");
			status = ControllerManager.SYS_ERROR;
		}
		return status;
	}

}
