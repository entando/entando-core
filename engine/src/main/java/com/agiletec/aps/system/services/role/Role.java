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
package com.agiletec.aps.system.services.role;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.agiletec.aps.system.services.authorization.IApsAuthority;

/**
 * Rappresentazione di un ruolo per gli utenti del portale. 
 * Il ruolo contiene i permessi che di cui disporrà l'utente.
 * @author M.Diana - E.Santoboni
 */
public class Role implements IApsAuthority {
	
	@Override
	public String getAuthority() {
		return this.getName();
	}

	/**
	 * Inizializza il set di permessi costituenti il ruolo.
	 */
	public Role() {
		this._permissions = new HashSet<String>();
	}

	/**
	 * Restituisce il nome del ruolo.
	 * @return Il nome del ruolo.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Setta il nome del ruolo.
	 * @param roleName Il nome del ruolo.
	 */
	public void setName(String roleName) {
		this._name = roleName;
	}

	/**
	 * Restituisce la descrizione del ruolo.
	 * @return La descrizione del ruolo.
	 */
	public String getDescription() {
		return _descr;
	}

	/**
	 * Setta la descrizione del ruolo.
	 * @param descr La descrizione del ruolo.
	 */
	public void setDescription(String descr) {
		this._descr = descr;
	}

	/**
	 * Aggiunge un permesso al ruolo
	 * @param permissionName Stringa identificatrice del permesso da aggiungere al ruolo.
	 */
	public void addPermission(String permissionName) {
		if (null != permissionName && permissionName.trim().length()>0) {
			_permissions.add(permissionName);
		}
	}

	/**
	 * Rimuove un permesso dal ruolo
	 * @param permissionName Stringa identificatrice del permission da rimuovere dal ruolo.
	 */
	public void removePermission(String permissionName) {
		_permissions.remove(permissionName);
	}

	/**
	 * Restituisce l'insieme dei permessi del ruolo.
	 * @return Set di stringhe identificatrici dei permessi del ruolo.
	 */
	public Set<String> getPermissions() {
		return _permissions;
	}

	/**
	 * Verifica se il ruolo ha un certo permesso.
	 * @param permissionName Il nome del permesso da verificare.
	 * @return True se il ruolo ha il permesso.
	 */
	public boolean hasPermission(String permissionName){
		return _permissions.contains(permissionName);
	}

	public Object clone() {
		Role clone = new Role();
		clone.setDescription(this._descr);
		clone.setName(this._name);
		Iterator<String> iter = this._permissions.iterator();
		while (iter.hasNext()) {
			String perm = (String) iter.next();
			clone.addPermission(perm);
		}
		return clone;
	}

	private String _name;
	private String _descr;
	private HashSet<String> _permissions;

}
