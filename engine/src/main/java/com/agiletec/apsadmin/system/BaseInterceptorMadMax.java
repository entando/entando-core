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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * Interceptor gestore della verifica delle autorizzazioni dell'utente corrente.
 * Verifica che l'utente corrente sia abilitato all'esecuzione dell'azione richiesta.
 * @author E.Santoboni
 */
public abstract class BaseInterceptorMadMax extends AbstractInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(BaseInterceptorMadMax.class);
	
    public String intercept(ActionInvocation invocation) throws Exception {
        boolean isAuthorized = false;
        try {
            HttpSession session = ServletActionContext.getRequest().getSession();
            UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
            IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, ServletActionContext.getRequest());
            if (currentUser != null) {
                Set<String> authorizations = this.extractAllRequiredPermissions();
                if (null == authorizations || authorizations.isEmpty() || 
                        authManager.isAuthOnPermission(currentUser, Permission.SUPERUSER)) {
                    isAuthorized = true;
                } else {
                    isAuthorized = this.checkAuthorizations(currentUser, authorizations, authManager);
                }
                if (!isAuthorized) {
                    return this.getErrorResultName();
                }
            }
            if (isAuthorized) {
                return this.invoke(invocation);
            }
        } catch (Throwable t) {
        	_logger.error("Error occurred verifying authority of current user", t);
            //ApsSystemUtils.logThrowable(t, this, "intercept", "Error occurred verifying authority of current user");
            return BaseAction.FAILURE;
        }
        return this.getErrorResultName();
    }
    
    private Set<String> extractAllRequiredPermissions() {
        Set<String> authorizations = new HashSet<String>();
        if (null != this.getRequiredPermission()) {
            authorizations.add(this.getRequiredPermission().trim());
        }
        if (null != this.getRequiredPermissions()) {
            String[] permissions = this.getRequiredPermissions().split(",");
            for (int i = 0; i < permissions.length; i++) {
                authorizations.add(permissions[i].trim());
            }
        }
        return authorizations;
    }
    
    private boolean checkAuthorizations(UserDetails currentUser, Set<String> authorizations, IAuthorizationManager authManager) {
        Iterator<String> iter = authorizations.iterator();
        boolean orClause = (null != this.getORClause()) ? this.getORClause().booleanValue() : false;
        while (iter.hasNext()) {
            String permission = iter.next();
            if (orClause && authManager.isAuthOnPermission(currentUser, permission)) {
                return true;
            } else if (!orClause && !authManager.isAuthOnPermission(currentUser, permission)) {
                return false;
            }
        }
        return (orClause) ? false : true;
    }
    
    /**
     * Restituisce il permesso specifico.
     * @return Il permesso specifico.
     */
    public abstract String getRequiredPermission();
    
    public abstract String getRequiredPermissions();
    
    public abstract Boolean getORClause();
    
    public abstract String getErrorResultName();
    
    /**
     * Invokes the next step in processing this ActionInvocation. 
     * @see com.opensymphony.xwork2.ActionInvocation#invoke()
     * @return The code of the execution result.
     */
    protected String invoke(ActionInvocation invocation) throws Exception {
        return invocation.invoke();
    }
    
}