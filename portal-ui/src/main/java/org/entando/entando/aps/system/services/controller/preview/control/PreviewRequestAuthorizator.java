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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.page.IPageTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Sottoservizio delegato al controllo dell'autorizzazione dell'utente corrente.
 * Esegue la verifica dell'autorizzazione all'accesso alla pagina richiesta da 
 * parte dell'utente corrente. Nel caso di richiesta non valida, il controllo 
 * imposta il redirect alla pagina di login.
 * @author M.Diana
 */
public class PreviewRequestAuthorizator extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(PreviewRequestAuthorizator.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} : initialized", this.getClass().getName());
	}

	/**
	 * Verifica che l'utente in sessione sia abilitato all'accesso alla pagina richiesta.
	 * Se è autorizzato il metodo termina con CONTINUE, altrimenti con SYS_ERROR.
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
			HttpServletRequest request = reqCtx.getRequest();
			HttpSession session = request.getSession();
			IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null == currentUser) {
				currentUser = this.getUserManager().getGuestUser();
                session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, currentUser);
			}
			if (this.isAllowed(currentUser, currentPage, request)) {
				retStatus = ControllerManager.CONTINUE;
			} else {
				retStatus = ControllerManager.SYS_ERROR;
			}
		} catch (Throwable t) {
			_logger.error("Error while processing the request", t);
			//ApsSystemUtils.logThrowable(t, this, "service", "Error while processing the request");
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return retStatus;
	}

	private boolean isAllowed(UserDetails currentUser, IPage currentPage, HttpServletRequest request) {
		boolean isValid = false;
		IAuthorizationManager authManager = this.getAuthManager();
		if (authManager.isAuthOnPermission(currentUser, Permission.SUPERUSER)) {
			isValid = true;
		} else {
			String token = request.getParameter("token");
			if (StringUtils.isNotEmpty(token)) {
				String result = this.getPageTokenMager().decrypt(token);
				if (result != null && currentPage != null && result.equals(currentPage.getCode())) {
					isValid = true;
				}
			}
		}
		if (isValid) {
			isValid = authManager.isAuth(currentUser, currentPage);
		}
		return isValid;
	}

	protected IAuthorizationManager getAuthManager() {
		return _authManager;
	}
	public void setAuthManager(IAuthorizationManager authManager) {
		this._authManager = authManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	protected IPageTokenManager getPageTokenMager() {
		return _pageTokenMager;
	}
	public void setPageTokenMager(IPageTokenManager pageTokenMager) {
		this._pageTokenMager = pageTokenMager;
	}

	private IAuthorizationManager _authManager;
    private IUserManager _userManager;
    private IPageTokenManager _pageTokenMager;

}