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
package org.entando.entando.aps.internalservlet.api;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.url.IURLManager;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.entando.entando.aps.system.services.api.model.ApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ApiServiceAction extends org.entando.entando.apsadmin.api.ApiServiceAction implements ServletResponseAware {

	private static final Logger _logger = LoggerFactory.getLogger(ApiServiceAction.class);
	
	public String detail() {
		try {
			String check = this.checkService();
			if (null != check) return check;
		} catch (Throwable t) {
			_logger.error("error in detail", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	protected String checkService() throws Throwable {
		ApiService apiService = this.getApiService(this.getServiceKey());
		if (apiService == null || !apiService.isActive() || apiService.isHidden()) {
			this.addActionError(this.getText("error.service.invalid", new String[]{this.getServiceKey()}));
			return INPUT;
		}
		return null;
	}
	
	@Override
	public String generateResponseBodySchema() {
		try {
			String check = this.checkService();
			if (null != check) return check;
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
			builder.append("do/Front/Api/Service/").append(actionName).append(".action");
			builder.append("?serviceKey=").append(this.getServiceKey());
			url = this.getResponse().encodeRedirectURL(builder.toString());
		} catch (Throwable t) {
			_logger.error("Error generating redirect url", t);
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
