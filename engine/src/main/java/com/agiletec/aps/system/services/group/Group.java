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
package com.agiletec.aps.system.services.group;

import com.agiletec.aps.system.services.authorization.IApsAuthority;

/**
 * Rappresentazione di un'oggetto "Gruppo".
 * @author E.Santoboni
 */
public class Group implements IApsAuthority {
	
	@Override
	public String getAuthority() {
		return this.getName();
	}
	
	/**
	 * Indica se il gruppo è definito localmente 
	 * all'interno del db "serv" di jAPS.
	 * @return true se è un gruppo locale di jAPS, 
	 * false se è un gruppo definito in un'altra base dati.
	 */
	public boolean isJapsGroup() {
    	return true;
    }
    
    /**
	 * Restituisce il nome del gruppo.
	 * @return Il nome del gruppo.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Setta il nome del gruppo.
	 * @param name Il nome del gruppo.
	 */
	public void setName(String name) {
		this._name = name;
	}
	
	/**
	 * Restituisce la descrizione del gruppo.
	 * @return La descrizione del gruppo.
	 */
	public String getDescr() {
		return _descr;
	}
	
	/**
	 * Setta la descrizione del gruppo.
	 * @param descr La descrizione del gruppo.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	private String _name;
	private String _descr;
	
	/**
	 * Nome del gruppo degli amministratori.
	 */
	public static final String ADMINS_GROUP_NAME = "administrators";
	
	/**
	 * Nome del gruppo "Ad accesso libero".
	 */
	public static final String FREE_GROUP_NAME = "free";
	
}
