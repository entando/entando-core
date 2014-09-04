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

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Sottoservizio delegato al controllo dell'autorizzazione dell'utente corrente.
 * Esegue la verifica dell'autorizzazione all'accesso alla pagina richiesta da 
 * parte dell'utente corrente. Nel caso di richiesta non valida, il controllo 
 * imposta il redirect alla pagina di login.
 * @author M.Diana
 */
public class RequestAuthorizator extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(RequestAuthorizator.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} : initialized", this.getClass().getName());
	}
	
	/**
	 * Verifica che l'utente in sessione sia abilitato all'accesso alla pagina richiesta.
	 * Se è autorizzato il metodo termina con CONTINUE, altrimenti 
	 * con REDIRECT impostando prima i parametri di redirezione alla pagina di login.
	 * @param reqCtx Il contesto di richiesta
	 * @param status Lo stato di uscita del servizio precedente
	 * @return Lo stato di uscita
	 */
	@Override
	public int service(RequestContext reqCtx, int status) {
		_logger.debug("Invoked: {}", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try {
			HttpServletRequest req = reqCtx.getRequest();
			HttpSession session = req.getSession();
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null == currentUser) {
				throw new ApsSystemException("no user on session");
			}
			boolean authorized = this.getAuthManager().isAuth(currentUser, currentPage);
			if (authorized) {
				retStatus = ControllerManager.CONTINUE;
			} else {
				StringBuilder targetUrl = new StringBuilder(req.getRequestURL());
				String queryString = req.getQueryString();
				if (null != queryString && queryString.trim().length() > 0) {
					targetUrl.append("?").append(queryString);
				}
				Map<String, String> params = new HashMap<String, String>();
				params.put("returnUrl", URLEncoder.encode(targetUrl.toString(), "UTF-8"));
				retStatus = this.redirect(this.getLoginPageCode(), params, reqCtx);
			}
		} catch (Throwable t) {
			_logger.error("Error while processing the request", t);
			//ApsSystemUtils.logThrowable(t, this, "service", "Error while processing the request");
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return retStatus;
	}
	
	protected String getLoginPageCode() {
		return this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_LOGIN_PAGE_CODE);
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configService) {
		this._configManager = configService;
	}

	protected IAuthorizationManager getAuthManager() {
		return _authManager;
	}
	public void setAuthManager(IAuthorizationManager authManager) {
		this._authManager = authManager;
	}
	
	private IAuthorizationManager _authManager;
	private ConfigInterface _configManager;

}