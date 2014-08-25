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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Servizio di autorizzazione.
 * Il servizio espone tutti i metodi necessari per la verifica verifica delle autorizzazioni utente, 
 * qualsiasi sia la sua provenienza e definizione.
 * @author E.Santoboni
 */
public class AuthorizationManager extends AbstractService implements IAuthorizationManager {
 
	private static final Logger _logger = LoggerFactory.getLogger(AuthorizationManager.class);
	
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }
    
    public boolean isAuth(UserDetails user, IApsAuthority auth) {
        return this.checkAuth(user, auth);
    }
    
    public boolean isAuth(UserDetails user, Group group) {
        return this.isAuthOnGroup(user, group.getName());
    }
    
    public boolean isAuth(UserDetails user, IApsEntity entity) {
        if (null == entity) {
            return false;
        }
        String mainGroupName = entity.getMainGroup();
        //Group group = this.getGroupManager().getGroup(mainGroupName);
        if (mainGroupName.equals(Group.FREE_GROUP_NAME) 
        		|| this.checkAuth(user, mainGroupName, AuthorityType.GROUP) 
        		|| this.checkAuth(user, Group.ADMINS_GROUP_NAME, AuthorityType.GROUP)) {
            return true;
        }
        Set<String> groups = entity.getGroups();
        return isAuth(user, groups);
    }
    
    public boolean isAuth(UserDetails user, Set<String> groups) {
        if (this.checkAuth(user, Group.ADMINS_GROUP_NAME, AuthorityType.GROUP)) {
            return true;
        }
        if (null == groups || groups.isEmpty()) return false;
        Iterator<String> iter = groups.iterator();
        while (iter.hasNext()) {
            String groupName = iter.next();
            if (groupName.equals(Group.FREE_GROUP_NAME) || this.checkAuth(user, groupName, AuthorityType.GROUP)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAuth(UserDetails user, Permission permission) {
        return this.isAuthOnPermission(user, permission.getName());
    }
    
    public boolean isAuth(UserDetails user, IPage page) {
        if (this.isAuthOnGroup(user, Group.ADMINS_GROUP_NAME)) {
            return true;
        }
        String pageGroup = page.getGroup();
        if (Group.FREE_GROUP_NAME.equals(pageGroup)) {
            return true;
        }
        boolean isAuthorized = this.isAuthOnGroup(user, pageGroup);
        if (isAuthorized) {
            return true;
        }
        Collection<String> extraGroups = page.getExtraGroups();
        if (null != extraGroups && !extraGroups.isEmpty()) {
            if (extraGroups.contains(Group.FREE_GROUP_NAME)) {
                return true;
            }
            Iterator<String> iter = extraGroups.iterator();
            while (iter.hasNext()) {
                String extraGroupName = iter.next();
                if (this.isAuthOnGroup(user, extraGroupName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isAuthOnGroup(UserDetails user, String groupName) {
        //Group group = this.getGroupManager().getGroup(groupName);
        //Group adminGroup = this.getGroupManager().getGroup(Group.ADMINS_GROUP_NAME);
        //return (this.checkAuth(user, group) || this.checkAuth(user, adminGroup));
        return ((this.checkAuth(user, groupName, AuthorityType.GROUP) 
                || this.checkAuth(user, Group.ADMINS_GROUP_NAME, AuthorityType.GROUP)));
    }
    
    public boolean isAuthOnRole(UserDetails user, String roleName) {
        //Role role = this.getRoleManager().getRole(roleName);
        //return (this.isAuthOnPermission(user, Permission.SUPERUSER) || this.checkAuth(user, role));
        return ((this.isAuthOnPermission(user, Permission.SUPERUSER) 
                || this.checkAuth(user, roleName, AuthorityType.ROLE)));
    }
    
    public boolean isAuthOnPermission(UserDetails user, String permissionName) {
        boolean check = this.isAuthOnSinglePermission(user, permissionName);
        if (check) {
            return true;
        }
        return this.isAuthOnSinglePermission(user, Permission.SUPERUSER);
    }

    private boolean isAuthOnSinglePermission(UserDetails user, String permissionName) {
        List<Role> rolesWithPermission = this.getRolesWithPermission(user, permissionName);
        for (int i = 0; i < rolesWithPermission.size(); i++) {
            Role role = rolesWithPermission.get(i);
            boolean check = this.checkAuth(user, role.getAuthority(), AuthorityType.ROLE);
            if (check) {
                return true;
            }
        }
        return false;
    }
    
    private List<Role> getRolesWithPermission(UserDetails user, String permissionName) {
        List<Role> roles = new ArrayList<Role>();
        if (null == user) return roles;
        IApsAuthority[] auths = user.getAuthorities();
        if (null != auths) {
            for (int i = 0; i < auths.length; i++) {
                IApsAuthority auth = auths[i];
                if (null == auth) {
                    continue;
                }
                if (auth instanceof Role && ((Role) auth).hasPermission(permissionName)) {
                    roles.add((Role) auth);
                }
            }
        }
        return roles;
    }

    @Deprecated
    public List<Group> getGroupsOfUser(UserDetails user) {
        return this.getUserGroups(user);
    }
    
    public List<Group> getUserGroups(UserDetails user) {
        if (null == user) {
            return null;
        }
        List<Group> groups = new ArrayList<Group>();
        IApsAuthority[] auths = user.getAuthorities();
        if (null != auths) {
            for (int i = 0; i < auths.length; i++) {
                IApsAuthority auth = auths[i];
                if (null == auth) {
                    continue;
                }
                if (auth instanceof Group) {
                    groups.add((Group) auth);
                }
            }
        }
        return groups;
    }
    
    public List<Role> getUserRoles(UserDetails user) {
        if (null == user) {
            return null;
        }
        List<Role> roles = new ArrayList<Role>();
        IApsAuthority[] auths = user.getAuthorities();
        if (null != auths) {
            for (int i = 0; i < auths.length; i++) {
                IApsAuthority auth = auths[i];
                if (null == auth) {
                    continue;
                }
                if (auth instanceof Role) {
                    roles.add((Role) auth);
                }
            }
        }
        return roles;
    }
    
    private boolean checkAuth(UserDetails user, IApsAuthority requiredAuth) {
        if (null == requiredAuth) {
            return false;
        }
        IApsAuthority[] auths = user.getAuthorities();
        if (null != auths) {
            for (int i = 0; i < auths.length; i++) {
                IApsAuthority auth = auths[i];
                if (null == auth) {
                    continue;
                }
                String authName = auth.getAuthority();
                if (requiredAuth.getAuthority().equals(authName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkAuth(UserDetails user, String requiredAuthName, AuthorityType type) {
        if (null == requiredAuthName) {
            return false;
        }
        IApsAuthority[] auths = user.getAuthorities();
        if (null != auths) {
            for (int i = 0; i < auths.length; i++) {
                IApsAuthority auth = auths[i];
                if (null == auth) {
                    continue;
                }
                String authName = auth.getAuthority();
                boolean check = requiredAuthName.equals(authName);
                if ((check && type.equals(AuthorityType.GROUP) && (auth instanceof Group)) 
                        || (check && type.equals(AuthorityType.ROLE) && (auth instanceof Role))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private enum AuthorityType{ROLE,GROUP}
    /*
    protected IGroupManager getGroupManager() {
        return _groupManager;
    }
    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }
    
    protected IRoleManager getRoleManager() {
        return _roleManager;
    }
    public void setRoleManager(IRoleManager roleManager) {
        this._roleManager = roleManager;
    }
    
    private IGroupManager _groupManager;
    private IRoleManager _roleManager;
    */
}