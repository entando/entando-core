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

import com.agiletec.aps.system.services.role.Permission;

/**
 * Interceptor gestore della verifica delle autorizzazioni dell'utente corrente.
 * Verifica che l'utente corrente sia abilitato all'accesso all'area di backoffice.
 * Nel caso di verifica non a buon fine, si viene redirezionati alla pagina di login.
 * @author E.Santoboni
 */
public class InterceptorMadMax2nd extends BaseInterceptorMadMax {
    
    public String getErrorResultName() {
        return "apslogin";
    }
    
    /**
     * Restituisce il permesso di accesso al backoffice.
     * @return Il permesso di accesso al backoffice.
     */
    public String getRequiredPermission() {
        return Permission.BACKOFFICE;
    }
    
    public Boolean getORClause() {
        return new Boolean(false);
    }
    
    public String getRequiredPermissions() {
        return null;
    }
    
}
