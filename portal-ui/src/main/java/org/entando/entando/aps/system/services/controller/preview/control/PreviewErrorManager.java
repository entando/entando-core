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
package org.entando.entando.aps.system.services.controller.preview.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;

/**
 * Implementazione del sottoservizio di controllo che gestisce gli errori
 * @author M.Diana
 */
public class PreviewErrorManager extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(PreviewErrorManager.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} : initialized", this.getClass().getName());
	}
	
	@Override
	public int service(RequestContext reqCtx, int status) {
		if (status == ControllerManager.CONTINUE || status == ControllerManager.OUTPUT) {
			return ControllerManager.OUTPUT;
		} 
//		int retStatus = ControllerManager.INVALID_STATUS;
//		_logger.debug("Intervention of the error service");
//		try {
//			PageURL url = this.getUrlManager().createURL(reqCtx);
//			url.setPageCode(this.getErrorPageCode());
//			String redirUrl = url.getURL();
//
//			_logger.debug("Redirecting to " + redirUrl);
//			reqCtx.clearError();
//			reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, redirUrl);
//			retStatus = ControllerManager.REDIRECT;
//		} catch (Throwable t) {
//			_logger.debug("Error detected while processing the request", t);
//			retStatus = ControllerManager.SYS_ERROR;
//			reqCtx.setHTTPError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
//		}
//		return retStatus;
		return status;
	}
	
	protected String getErrorPageCode() {
		return this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE);
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configService) {
		this._configManager = configService;
	}
	
	private ConfigInterface _configManager;
	
}