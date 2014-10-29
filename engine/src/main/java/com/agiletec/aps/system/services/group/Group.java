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

import com.agiletec.aps.system.services.authorization.AbstractAuthority;

import java.io.Serializable;

/**
 * Rappresentazione di un'oggetto "Gruppo".
 * @author E.Santoboni
 */
public class Group extends AbstractAuthority implements Serializable {
	
	@Override
	public String getAuthority() {
		return this.getName();
	}
	
	/**
	 * Indica se il gruppo è definito localmente 
	 * all'interno del db "serv" di Entando.
	 * @return true se è un gruppo locale di Entando, 
	 * false se è un gruppo definito in un'altra base dati.
	 */
	public boolean isEntandoGroup() {
    	return true;
    }
    
	@Deprecated
    public boolean isJapsGroup() {
    	return this.isEntandoGroup();
    }
    
	@Deprecated
	public String getDescr() {
		return this.getDescription();
	}
	@Deprecated
	public void setDescr(String description) {
		this.setDescription(description);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null != obj && (obj instanceof Group)) {
			return this.getName().equals(((Group) obj).getName());
		} else {
			return super.equals(obj);
		}
	}
	
	/**
	 * Nome del gruppo degli amministratori.
	 */
	public static final String ADMINS_GROUP_NAME = "administrators";
	
	/**
	 * Nome del gruppo "Ad accesso libero".
	 */
	public static final String FREE_GROUP_NAME = "free";
	
}
