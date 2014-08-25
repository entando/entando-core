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
package com.agiletec.aps.system.services.authorization;

import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interfaccia base per il servizio di autorizzazione.
 * Il servizio verifica le autorizzazioni di 
 * utenti ad azioni (attraverso il permesso associato) o ad oggetti del sistema 
 * (attraverso il gruppo associato).
 * @author E.Santoboni
 */
public interface IAuthorizationManager {
	
	/**
	 * Verifica se l'utente specificato possiede l'autorizzazione richiesta.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param auth L'autorizzazione da verificare.
	 * @return True se l'utente possiede l'autorizzazione, false in caso contrario.
	 */
	public boolean isAuth(UserDetails user, IApsAuthority auth);
	
	/**
	 * Verifica se l'utente specificato possiede il permesso richiesto.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param permission Il permesso da verificare.
	 * @return True se l'utente possiede il permesso, false in caso contrario.
	 */
	public boolean isAuth(UserDetails user, Permission permission);
	
	/**
	 * Verifica se l'utente specificato appartiene al gruppo specificato.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param group Il gruppo da verificare.
	 * @return True se l'utente fa parte del gruppo, false in caso contrario.
	 */
	public boolean isAuth(UserDetails user, Group group);
	
	/**
	 * Verifica se l'utente specificato è abilitato all'accesso alla pagina specificata.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param page La pagina da analizzare.
	 * @return True se l'utente è abilitato all'accesso alla pagina specificata, false in caso contrario.
	 */
	public boolean isAuth(UserDetails user, IPage page);
	
	/**
	 * Verifica se l'utente specificato appartiene al gruppo specificato.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param groupName Il nome del gruppo da verificare.
	 * @return True se l'utente fa parte del gruppo, false in caso contrario.
	 */
	public boolean isAuthOnGroup(UserDetails user, String groupName);
	
	/**
	 * Verifica se l'utente specificato appartiene al ruolo specificato.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param roleName Il nome del ruolo da verificare.
	 * @return True se l'utente possiede il ruolo specificato, false in caso contrario.
	 */
	public boolean isAuthOnRole(UserDetails user, String roleName);
	
	/**
	 * Verifica se l'utente specificato possiede il permesso richiesto.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param permissionName Il nome del permesso da verificare.
	 * @return True se l'utente possiede il permesso, false in caso contrario.
	 */
	public boolean isAuthOnPermission(UserDetails user, String permissionName);
	
	/**
	 * Verifica se l'utente specificato possiede l'autorizzazione all'accesso all'entità specificata.
	 * @param user L'utente di cui verificare l'autorizzazione.
	 * @param entity L'entità di cui verificare l'accesso.
	 * @return True se l'utente possiede il permesso di accesso all'entità, false in caso contrario.
	 */
	public boolean isAuth(UserDetails user, IApsEntity entity);
	
	/**
	 * Check if the user has at least one of the specified groups.
	 * @param user the user to check. Must be not null
	 * @param groups A set of group codes to check against user's groups
	 * @return True if the user has at least one group specified in groups, or if groups contains "free" or if the user belongs to the "administrators" group
	 */
	public boolean isAuth(UserDetails user, Set<String> groups);
	
	/**
	 * Returns the groups of the given user
	 * @param user The user
	 * @return The list of groups the given user
         * @deprecated from Entando 2.4.0, Use getUserGroups(UserDetails)
	 */
	public List<Group> getGroupsOfUser(UserDetails user);
        
        /**
	 * Returns the groups of the given user
	 * @param user The user
	 * @return The list of groups the given user
	 */
	public List<Group> getUserGroups(UserDetails user);
        
        /**
         * Returns the roles of the given user
         * @param user The user.
	 * @return The list of roles of the given user.
         */
        public List<Role> getUserRoles(UserDetails user);
	
}
