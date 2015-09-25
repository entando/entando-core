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

import com.agiletec.aps.system.services.role.Permission;

/**
 * Interceptor gestore della verifica delle autorizzazioni dell'utente corrente.
 * Verifica che l'utente corrente sia abilitato all'accesso all'area di backoffice.
 * Nel caso di verifica non a buon fine, si viene redirezionati alla pagina di login.
 * @author E.Santoboni
 */
public class InterceptorMadMax2nd extends BaseInterceptorMadMax {
	
	@Override
	public String getErrorResultName() {
		return "apslogin";
	}
	
	/**
	 * Restituisce il permesso di accesso al backoffice.
	 * @return Il permesso di accesso al backoffice.
	 */
	@Override
	public String getRequiredPermission() {
		return Permission.BACKOFFICE;
	}
	
	@Override
	public Boolean getORClause() {
		return false;
	}
	
	@Override
	public String getRequiredPermissions() {
		return null;
	}
	
}
