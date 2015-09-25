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
package com.agiletec.apsadmin.system;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.opensymphony.xwork2.ActionInvocation;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor gestore della verifica delle autorizzazioni dell'utente corrente.
 * Verifica che l'utente corrente sia abilitato all'esecuzione dell'azione
 * richiesta.
 * @author E.Santoboni
 */
public class InterceptorMadMax extends BaseInterceptorMadMax {

	private static final Logger _logger = LoggerFactory.getLogger(InterceptorMadMax.class);

	/**
	 * Return the single required permission.
	 * @return The required permission.
	 */
	@Override
	public String getRequiredPermission() {
		return _requiredPermission;
	}

	/**
	 * Set the single required permission.
	 * @param requiredPermission The single required permission.
	 */
	public void setRequiredPermission(String requiredPermission) {
		this._requiredPermission = requiredPermission;
	}

	/**
	 * Return the required permissions, list of string comma separated.
	 * @return the required permissions
	 */
	@Override
	public String getRequiredPermissions() {
		return _requiredPermissions;
	}

	/**
	 * Set the required permissions, list of string comma separated.
	 * @param requiredPermissions The required permissions, list of string comma
	 * separated.
	 */
	public void setRequiredPermissions(String requiredPermissions) {
		this._requiredPermissions = requiredPermissions;
	}

	@Override
	public String getErrorResultName() {
		if (this._errorResultName == null) {
			return DEFAULT_ERROR_RESULT;
		}
		return this._errorResultName;
	}
	
	public void setErrorResultName(String errorResultName) {
		this._errorResultName = errorResultName;
	}
	
	@Override
	public Boolean getORClause() {
		return _ORClause;
	}
	
	public void setORClause(Boolean ORClause) {
		this._ORClause = ORClause;
	}
	
	@Override
	protected String invoke(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		if (action instanceof BaseAction) {
			Set<String> requiredPermissions = super.extractAllRequiredPermissions();
			((BaseAction) action).setRequiredPermissions(requiredPermissions);
		}
		HttpSession session = ServletActionContext.getRequest().getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		_logger.debug("Action invoked '{}' on namespace '{}' from user '{}'", invocation.getProxy().getActionName(), invocation.getProxy().getNamespace(), currentUser.getUsername());
		return super.invoke(invocation);
	}
	
	private String _requiredPermission;
	private String _requiredPermissions;
	private String _errorResultName;
	private Boolean _ORClause;
	
	public static final String DEFAULT_ERROR_RESULT = "userNotAllowed";
	
}
