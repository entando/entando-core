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

import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * Base Action Helper for all helper class of the system.
 * @author E.Santoboni
 */
public abstract class BaseActionHelper {
	
	/**
	 * Metodo di servizio utilizzato per uniformare i codici.
	 * Elimina i caratteri non compresi tra "a" e "z" e tra "0" e "9" dalla stringa immessa.
	 * @param code La stringa da analizzare.
	 * @return La stringa corretta.
	 */
	public static String purgeString(String code) {
		code = code.trim().toLowerCase();
		code = code.replaceAll("[^ _a-z0-9]", "");
		code = code.trim().replace(' ', '_');
		return code;
    }
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	@Deprecated
	protected IAuthorizationManager getAuthorizatorManager() {
		return _authorizationManager;
	}
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	private ILangManager _langManager;
	private IGroupManager _groupManager;
	private IAuthorizationManager _authorizationManager;
	
}