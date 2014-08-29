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
package com.agiletec.aps.system.services.controller.control;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.url.PageURL;

/**
 * Implementazione del sottoservizio di controllo che gestisce gli errori
 * @author M.Diana
 */
public class ErrorManager extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(ErrorManager.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} : initialized", this.getClass().getName());
	}
	
	@Override
	public int service(RequestContext reqCtx, int status) {
		int retStatus = ControllerManager.INVALID_STATUS;
		_logger.debug("Intervention of the error service");
		try {
			PageURL url = this.getUrlManager().createURL(reqCtx);
			url.setPageCode(this.getErrorPageCode());
			String redirUrl = url.getURL();

			_logger.debug("Redirecting to " + redirUrl);
			reqCtx.clearError();
			reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, redirUrl);
			retStatus = ControllerManager.REDIRECT;
		} catch (Throwable t) {
			_logger.debug("Error detected while processing the request", t);
			//ApsSystemUtils.logThrowable(t, this, "service", "Error detected while processing the request");
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		return retStatus;
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