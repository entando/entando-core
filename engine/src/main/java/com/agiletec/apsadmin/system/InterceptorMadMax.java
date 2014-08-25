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
package com.agiletec.apsadmin.system;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * Interceptor gestore della verifica delle autorizzazioni dell'utente corrente.
 * Verifica che l'utente corrente sia abilitato all'esecuzione dell'azione richiesta.
 * @author E.Santoboni
 */
public class InterceptorMadMax extends BaseInterceptorMadMax {

	private static final Logger _logger = LoggerFactory.getLogger(InterceptorMadMax.class);
	
    /**
     * Return the single required permission.
     * @return The required permission.
     */
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
     * @param requiredPermission The required permissions, list of string comma separated.
     */
    public String getRequiredPermissions() {
        return _requiredPermissions;
    }
    
    /**
     * Set the required permissions, list of string comma separated.
     * @param requiredPermission The required permissions, list of string comma separated.
     */
    public void setRequiredPermissions(String requiredPermissions) {
        this._requiredPermissions = requiredPermissions;
    }
    
    public String getErrorResultName() {
        if (this._errorResultName == null) {
            return DEFAULT_ERROR_RESULT;
        }
        return this._errorResultName;
    }
    
    public void setErrorResultName(String errorResultName) {
        this._errorResultName = errorResultName;
    }
    
    public Boolean getORClause() {
        return _ORClause;
    }
    public void setORClause(Boolean ORClause) {
        this._ORClause = ORClause;
    }
    
    protected String invoke(ActionInvocation invocation) throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession();
        UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        _logger.debug("Action invoked '{}' on namespace '{}' from user '{}'", invocation.getProxy().getActionName(), invocation.getProxy().getNamespace(),currentUser.getUsername());
        return super.invoke(invocation);
    }
    
    private String _requiredPermission;
    private String _requiredPermissions;
    private String _errorResultName;
    private Boolean _ORClause;
    public static final String DEFAULT_ERROR_RESULT = "userNotAllowed";
    
}
