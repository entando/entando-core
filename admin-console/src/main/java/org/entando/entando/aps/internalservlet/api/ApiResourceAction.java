/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.internalservlet.api;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.url.IURLManager;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ApiResourceAction extends org.entando.entando.apsadmin.api.ApiResourceAction implements ServletResponseAware {

	private static final Logger _logger = LoggerFactory.getLogger(ApiResourceAction.class);
	
	@Override
	public String generateRequestBodySchema() {
		try {
			String result = super.generateRequestBodySchema();
			if (!result.equals(SUCCESS)) return result;
			this.getResponse().sendRedirect(this.generateRedirectUrl("executeRequestSchema"));
		} catch (Throwable t) {
			_logger.error("error in generateRequestBodySchema", t);
			return FAILURE;
		}
		return null;
	}
	
	public String executeRequestBodySchema() {
		return super.generateRequestBodySchema();
	}
	
	@Override
	public String generateResponseBodySchema() {
		try {
			String result = super.generateResponseBodySchema();
			if (!result.equals(SUCCESS)) return result;
			this.getResponse().sendRedirect(this.generateRedirectUrl("executeResponseSchema"));
		} catch (Throwable t) {
			_logger.error("error in generateRequestBodySchema", t);
			return FAILURE;
		}
		return null;
	}
	
	public String executeResponseBodySchema() {
		return super.generateResponseBodySchema();
	}
	
	private String generateRedirectUrl(String actionName) throws ApsSystemException {
		String url = null;
		try {
			String applicationBaseUrl = this.getUrlManager().getApplicationBaseURL(this.getRequest());
			StringBuilder builder = new StringBuilder(applicationBaseUrl);
			if (!builder.toString().endsWith("/")) builder.append("/");
			builder.append("do/Front/Api/Resource/").append(actionName).append(".action");
			builder.append("?resourceName=").append(this.getResourceName());
			builder.append("&namespace=").append(this.getNamespace());
			builder.append("&httpMethod=").append(this.getHttpMethod());
			url = this.getResponse().encodeRedirectURL(builder.toString());
		} catch (Throwable t) {
			_logger.error("Error generating redirect url ", t);
			//ApsSystemUtils.logThrowable(t, this, "generateRedirectUrl");
			throw new ApsSystemException("Error generating redirect url", t);
		}
		return url;
	}
	
	protected HttpServletResponse getResponse() {
		return _response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}

	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	private HttpServletResponse _response;
	
	private IURLManager _urlManager;
	
}
