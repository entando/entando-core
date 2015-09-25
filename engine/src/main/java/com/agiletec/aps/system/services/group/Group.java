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
