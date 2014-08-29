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

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Sottoservizio di controllo esecutore dell'autenticazione.
 * @author M.Diana - E.Santoboni
 */
public class Authenticator extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(Authenticator.class);

	
	@Override
    public void afterPropertiesSet() throws Exception {
    	_logger.debug("{} ready", this.getClass().getName());
	}
    
	/**
     * Esecuzione.
     * Il metodo service esegue le seguenti operazioni (nell'ordine indicato): 
     * 1) se nella richiesta sono presenti dei parametri user e password, viene
     * caricato l'utente relativo; se l'utente restituito è non nullo,
     * lo si mette in sessione; se l'utente restituito è nullo, non si fa nulla.
     * 2) si controlla l'esistenza di un utente in sessione; se non esiste, si richiede 
     * un utente di default e lo si mette in sessione. 
     * @param reqCtx Il contesto di richiesta
     * @param status Lo stato di uscita del servizio precedente
     * @return Lo stato di uscita
     */
	@Override
    public int service(RequestContext reqCtx, int status) {
    	_logger.debug("Invoked {}", this.getClass().getName());
        int retStatus = ControllerManager.INVALID_STATUS;
        if (status == ControllerManager.ERROR) {
        	return status;
        }
        try {
            HttpServletRequest req = reqCtx.getRequest();
            String username = req.getParameter(SystemConstants.LOGIN_USERNAME_PARAM_NAME);
            String password = req.getParameter(SystemConstants.LOGIN_PASSWORD_PARAM_NAME);
            HttpSession session = req.getSession();
            //Punto 1
            if (username != null && password != null) {
				String returnUrl = req.getParameter("returnUrl");
				returnUrl = (null != returnUrl && returnUrl.trim().length() > 0) ? returnUrl : null;
            	_logger.debug("user {} - password ******** ", username );
                UserDetails user = this.getAuthenticationProvider().getUser(username, password);
                if (user != null) {
                	if (!user.isAccountNotExpired()) {
                		req.setAttribute("accountExpired", new Boolean(true));
                	} else {
                		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
                		_logger.debug("New user: {}", user.getUsername());
						if (null != returnUrl) {
							return super.redirectUrl(URLDecoder.decode(returnUrl, "ISO-8859-1"), reqCtx);
						}
                	}
                } else {
                	req.setAttribute("wrongAccountCredential", new Boolean(true));
					if (null != returnUrl) {
						req.setAttribute("returnUrl", returnUrl);
					}
                }
            }
            //Punto 2
            if (session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) == null) {
            	UserDetails guestUser = this.getUserManager().getGuestUser();
                session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, guestUser);
            }
            retStatus = ControllerManager.CONTINUE;
        } catch (Throwable t) {
        	_logger.error("Error, could not fulfill the request", t);
            retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return retStatus;
    }
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IAuthenticationProviderManager getAuthenticationProvider() {
		return _authenticationProvider;
	}
	public void setAuthenticationProvider(IAuthenticationProviderManager authenticationProvider) {
		this._authenticationProvider = authenticationProvider;
	}
	
    private IUserManager _userManager;
    private IAuthenticationProviderManager _authenticationProvider;
    
}