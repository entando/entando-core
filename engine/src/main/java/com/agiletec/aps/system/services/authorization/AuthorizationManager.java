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
package com.agiletec.aps.system.services.authorization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio di autorizzazione. Il servizio espone tutti i metodi necessari per
 * la verifica verifica delle autorizzazioni utente, qualsiasi sia la sua
 * provenienza e definizione.
 * @author E.Santoboni
 */
@Aspect
public class AuthorizationManager extends AbstractService implements IAuthorizationManager, GroupUtilizer {

    private static final Logger _logger = LoggerFactory.getLogger(AuthorizationManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }

    @Override
    @Deprecated
    public boolean isAuth(UserDetails user, IApsAuthority auth) {
        return this.checkAuth(user, auth);
    }

    @Override
    public boolean isAuthOnGroupAndPermission(UserDetails user, String groupName, String permissionName, boolean chechAdmin) {
        if (null == user || null == groupName || null == permissionName) {
            return false;
        }
        List<Role> roles = new ArrayList<Role>();
        List<Role> rolesWithPermission = this.getRoleManager().getRolesWithPermission(permissionName);
        if (null != rolesWithPermission) {
            roles.addAll(rolesWithPermission);
        }
        if (chechAdmin) {
            List<Role> rolesWithSupPermission = this.getRoleManager().getRolesWithPermission(Permission.SUPERUSER);
            if (null != rolesWithSupPermission) {
                roles.addAll(rolesWithSupPermission);
            }
        }
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            if (null != role) {
                boolean check = this.isAuthOnGroupAndRole(user, groupName, role.getName(), chechAdmin);
                if (check) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean isAuthOnGroupAndRole(UserDetails user, String groupName, String roleName, boolean chechAdmin) {
        if (null == user || (null == groupName && null == roleName)) {
            return false;
        }
        List<Authorization> userAuths = user.getAuthorizations();
        for (int i = 0; i < userAuths.size(); i++) {
            Authorization userAuth = userAuths.get(i);
            if (null == userAuth) {
                continue;
            }
            Group group = userAuth.getGroup();
            if ((null == group && null != groupName) || (null != group && null == groupName)) {
                continue;
            } else if (null != group && null != groupName) {
                if (!chechAdmin && !groupName.equals(group.getName())) {
                    continue;
                } else if (chechAdmin && !Group.ADMINS_GROUP_NAME.equals(group.getName())) {
                    continue;
                }
            }
            Role role = userAuth.getRole();
            if (null == roleName) {
                return true;
            } else {
                boolean isSuper = role.hasPermission(Permission.SUPERUSER);
                if (role.getName().equals(roleName) || (chechAdmin && isSuper)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<IApsAuthority> getRelatedAuthorities(UserDetails user, IApsAuthority auth) {
        if (null == user || null == auth) {
            return null;
        }
        String requiredAuthName = auth.getAuthority();
        boolean isRole = (auth instanceof Role);
        return (List<IApsAuthority>) this.getRelatedAuthorities(user, requiredAuthName, isRole);
    }

    private List getRelatedAuthorities(UserDetails user, String requiredAuthName, boolean isRole) {
        if (null == user || null == requiredAuthName || (isRole && null == this.getRoleManager().getRole(requiredAuthName)) || (!isRole && null == this.getGroupManager().getGroup(requiredAuthName))) {
            return null;
        }

        List<String> adminRoleNames = new ArrayList<String>();
        if (isRole) {
            List<Role> adminRoles = this.getRolesWithPermission(user, Permission.SUPERUSER);
            if (null != adminRoles && !adminRoles.isEmpty()) {
                for (int i = 0; i < adminRoles.size(); i++) {
                    Role role = adminRoles.get(i);
                    if (null != role) {
                        adminRoleNames.add(role.getName());
                    }
                }
            }
        }
        List authorities = new ArrayList<IApsAuthority>();
        List<Authorization> userAuths = user.getAuthorizations();
        for (int i = 0; i < userAuths.size(); i++) {
            Authorization userAuth = userAuths.get(i);
            if (null == userAuth) {
                continue;
            }
            if (!isRole && null != userAuth.getGroup() && (userAuth.getGroup().getName().equals(Group.ADMINS_GROUP_NAME) || requiredAuthName.equals(userAuth.getGroup().getAuthority()))) {
                authorities.add(userAuth.getRole());
            }
            if (isRole && null != userAuth.getRole() && (adminRoleNames.contains(userAuth.getRole().getName()) || requiredAuthName.equals(userAuth.getRole().getAuthority()))) {
                if (userAuth.getGroup().getName().equals(Group.ADMINS_GROUP_NAME)) {
                    return this.getGroupManager().getGroups();
                } else {
                    authorities.add(userAuth.getGroup());
                }
            }
        }
        return authorities;
    }

    @Override
    public boolean isAuth(UserDetails user, Group group) {
        return this.isAuthOnGroup(user, group.getName());
    }

    @Override
    public List<IApsAuthority> getAuthoritiesByGroup(UserDetails user, Group group) {
        return this.getRelatedAuthorities(user, group);
    }

    @Override
    public List<Role> getRolesByGroup(UserDetails user, Group group) {
        return this.getRelatedAuthorities(user, group.getName(), false);
    }

    @Override
    public boolean isAuth(UserDetails user, IApsEntity entity) {
        if (null == entity || null == user) {
            return false;
        }
        String mainGroupName = entity.getMainGroup();
        if (mainGroupName.equals(Group.FREE_GROUP_NAME) || this.checkAuth(user, mainGroupName, false) || this.checkAuth(user, Group.ADMINS_GROUP_NAME, false)) {
            return true;
        }
        Set<String> groups = entity.getGroups();
        return isAuth(user, groups);
    }

    @Override
    public boolean isAuth(UserDetails user, Set<String> groups) {
        if (null == user) {
            return false;
        }
        if (this.checkAuth(user, Group.ADMINS_GROUP_NAME, false)) {
            return true;
        }
        if (null == groups || groups.isEmpty()) {
            return false;
        }
        Iterator<String> iter = groups.iterator();
        while (iter.hasNext()) {
            String groupName = iter.next();
            if (groupName.equals(Group.FREE_GROUP_NAME) || this.checkAuth(user, groupName, false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAuth(UserDetails user, Permission permission) {
        return this.isAuthOnPermission(user, permission.getName());
    }

    @Override
    public List<IApsAuthority> getAuthoritiesByPermission(UserDetails user, Permission permission) {
        if (null == permission) {
            return null;
        }
        return this.getAuthoritiesByPermissionName(user, permission.getName());
    }

    @Override
    public List<Group> getGroupsByPermission(UserDetails user, Permission permission) {
        if (null == permission) {
            return null;
        }
        return this.getGroupsByPermission(user, permission.getName());
    }

    @Override
    public List<Group> getGroupsByPermission(UserDetails user, String permissionName) {
        if (null == user) {
            return null;
        }
        List<Group> groups = new ArrayList<Group>();
        List<IApsAuthority> auths = this.getAuthoritiesByPermissionName(user, permissionName);
        for (int i = 0; i < auths.size(); i++) {
            IApsAuthority auth = auths.get(i);
            if (null != auth && auth instanceof Group) {
                String groupName = auth.getAuthority();
                if (Group.ADMINS_GROUP_NAME.equals(groupName)) {
                    return this.getGroupManager().getGroups();
                } else {
                    if (!groups.contains((Group) auth)) {
                        groups.add((Group) auth);
                    }
                }
            }
        }
        return groups;
    }

    private List getAuthoritiesByPermissionName(UserDetails user, String permissionName) {
        if (null == user) {
            return null;
        }
        if (null == permissionName) {
            return null;
        }
        List auths = new ArrayList();
        this.addAuthoritiesByPermissionName(user, permissionName, auths);
        this.addAuthoritiesByPermissionName(user, Permission.SUPERUSER, auths);
        return auths;
    }

    private void addAuthoritiesByPermissionName(UserDetails user, String permissionName, List auths) {
        List<Role> roles = this.getRolesWithPermission(user, permissionName);
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            List groupAuths = this.getRelatedAuthorities(user, role);
            if (null != groupAuths) {
                auths.addAll(groupAuths);
            }
        }
    }

    @Override
    public boolean isAuth(UserDetails user, IPage page) {
        if (null == user) {
            return false;
        }
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

    @Override
    public boolean isAuthOnGroup(UserDetails user, String groupName) {
        return ((this.checkAuth(user, groupName, false) || this.checkAuth(user, Group.ADMINS_GROUP_NAME, false)));
    }

    @Override
    public List<IApsAuthority> getAuthoritiesByGroup(UserDetails user, String groupName) {
        return this.getRelatedAuthorities(user, groupName, false);
    }

    @Override
    public List<Role> getRolesByGroup(UserDetails user, String groupName) {
        return this.getRelatedAuthorities(user, groupName, false);
    }

    @Override
    public boolean isAuthOnRole(UserDetails user, String roleName) {
        return ((this.isAuthOnPermission(user, Permission.SUPERUSER) || this.checkAuth(user, roleName, true)));
    }

    @Override
    public List<IApsAuthority> getAuthoritiesByRole(UserDetails user, String roleName) {
        return this.getRelatedAuthorities(user, roleName, true);
    }

    @Override
    public List<Group> getGroupsByRole(UserDetails user, String roleName) {
        return this.getRelatedAuthorities(user, roleName, true);
    }

    @Override
    public boolean isAuthOnPermission(UserDetails user, String permissionName) {
        boolean check = this.isAuthOnSinglePermission(user, permissionName);
        if (check) {
            return true;
        }
        return this.isAuthOnSinglePermission(user, Permission.SUPERUSER);
    }

    private boolean isAuthOnSinglePermission(UserDetails user, String permissionName) {
        if (null == user) {
            return false;
        }
        List<Role> rolesWithPermission = this.getRolesWithPermission(user, permissionName);
        for (int i = 0; i < rolesWithPermission.size(); i++) {
            Role role = rolesWithPermission.get(i);
            boolean check = this.checkAuth(user, role.getAuthority(), true);
            if (check) {
                return true;
            }
        }
        return false;
    }

    private List<Role> getRolesWithPermission(UserDetails user, String permissionName) {
        if (null == user) {
            return null;
        }
        List<Role> roles = new ArrayList<Role>();
        List<Authorization> auths = user.getAuthorizations();
        for (int i = 0; i < auths.size(); i++) {
            Authorization auth = auths.get(i);
            if (null == auth) {
                continue;
            }
            if (null != auth.getRole() && auth.getRole().hasPermission(permissionName)) {
                roles.add(auth.getRole());
            }
        }
        return roles;
    }

    @Override
    @Deprecated
    public List<Group> getGroupsOfUser(UserDetails user) {
        return this.getUserGroups(user);
    }

    @Override
    public List<Group> getUserGroups(UserDetails user) {
        if (null == user) {
            return null;
        }
        List<Group> groups = new ArrayList<Group>();
        List<Authorization> auths = user.getAuthorizations();
        for (int i = 0; i < auths.size(); i++) {
            Authorization auth = auths.get(i);
            if (null != auth && null != auth.getGroup()) {
                String groupName = auth.getGroup().getName();
                if (Group.ADMINS_GROUP_NAME.equals(groupName)) {
                    return this.getGroupManager().getGroups();
                } else {
                    groups.add(auth.getGroup());
                }
            }
        }
        return groups;
    }

    @Override
    public List<Role> getUserRoles(UserDetails user) {
        if (null == user) {
            return null;
        }
        List<Role> roles = new ArrayList<Role>();
        List<Authorization> auths = user.getAuthorizations();
        for (int i = 0; i < auths.size(); i++) {
            Authorization auth = auths.get(i);
            if (null != auth && null != auth.getRole()) {
                roles.add(auth.getRole());
            }
        }
        return roles;
    }

    @Deprecated
    private boolean checkAuth(UserDetails user, IApsAuthority requiredAuth) {
        if (null == requiredAuth) {
            return false;
        }
        boolean isRole = (requiredAuth instanceof Role);
        String requiredAuthName = requiredAuth.getAuthority();
        return this.checkAuth(user, requiredAuthName, isRole);
    }

    private boolean checkAuth(UserDetails user, String requiredAuthName, boolean isRole) {
        if (null == requiredAuthName) {
            return false;
        }
        List<Authorization> auths = user.getAuthorizations();
        for (int i = 0; i < auths.size(); i++) {
            Authorization auth = auths.get(i);
            if (null == auth) {
                continue;
            }
            if (isRole && null != auth.getRole() && requiredAuthName.equals(auth.getRole().getAuthority())) {
                return true;
            }
            if (!isRole && null != auth.getGroup() && requiredAuthName.equals(auth.getGroup().getAuthority())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Authorization> getUserAuthorizations(String username) throws ApsSystemException {
        List<Authorization> authorizations = null;
        try {
            Map<String, Group> groups = (Map<String, Group>) this.getAuthorityMap(this.getGroupManager().getGroups());
            Map<String, Role> roles = (Map<String, Role>) this.getAuthorityMap(this.getRoleManager().getRoles());
            authorizations = this.getAuthorizationDAO().getUserAuthorizations(username, groups, roles);
        } catch (Throwable t) {
            _logger.error("Error extracting user authorizations for user '{}'", username, t);
            throw new ApsSystemException("Error extracting user authorizations for user " + username, t);
        }
        return authorizations;
    }

    private Map getAuthorityMap(List list) {
        Map<String, IApsAuthority> map = new HashMap<String, IApsAuthority>();
        for (int i = 0; i < list.size(); i++) {
            IApsAuthority authority = (IApsAuthority) list.get(i);
            map.put(authority.getAuthority(), authority);
        }
        return map;
    }

    @Override
    public void addUserAuthorization(String username, String groupName, String roleName) throws ApsSystemException {
        try {
            Group group = (null != groupName) ? this.getGroupManager().getGroup(groupName) : null;
            if (null != groupName && null == group) {
                _logger.warn("invalid authorization -  invalid referenced group name");
                return;
            }
            Role role = (null != roleName) ? this.getRoleManager().getRole(roleName) : null;
            if (null != roleName && null == role) {
                _logger.warn("invalid authorization -  invalid referenced role name");
                return;
            }
            Authorization authorization = new Authorization(group, role);
            this.addUserAuthorization(username, authorization);
        } catch (Throwable t) {
            _logger.error("Error adding user authorization for user '{}'", username, t);
            throw new ApsSystemException("Error adding user authorization for user " + username, t);
        }
    }

    @Override
    public void addUserAuthorization(String username, Authorization authorization) throws ApsSystemException {
        if (null == username || null == authorization) {
            return;
        }
        try {
            if (this.checkAuthorization(authorization)) {
                this.getAuthorizationDAO().addUserAuthorization(username, authorization);
            }
        } catch (Throwable t) {
            _logger.error("Error adding user authorization for user '{}'", username, t);
            throw new ApsSystemException("Error adding user authorization for user " + username, t);
        }
    }

    @Override
    public void addUserAuthorizations(String username, List<Authorization> authorizations) throws ApsSystemException {
        if (null == username) {
            return;
        }
        try {
            List<Authorization> toAdd = this.checkAuthorizations(authorizations);
            if (null == toAdd) {
                return;
            }
            this.getAuthorizationDAO().addUserAuthorizations(username, toAdd);
        } catch (Throwable t) {
            _logger.error("Error adding user authorizations for user '{}'", username, t);
            throw new ApsSystemException("Error adding user authorizations for user " + username, t);
        }
    }

    @Override
    public void updateUserAuthorizations(String username, List<Authorization> authorizations) throws ApsSystemException {
        if (null == username) {
            return;
        }
        try {
            List<Authorization> toSet = this.checkAuthorizations(authorizations);
            if (null == toSet) {
                return;
            }
            this.getAuthorizationDAO().updateUserAuthorizations(username, toSet);
        } catch (Throwable t) {
            _logger.error("Error updating user authorizations for user '{}'", username, t);
            throw new ApsSystemException("Error updating user authorizations for user " + username, t);
        }
    }

    private List<Authorization> checkAuthorizations(List<Authorization> authorizations) throws Throwable {
        if (null == authorizations) {
            return null;
        }
        List<Authorization> checked = new ArrayList<Authorization>();
        for (int i = 0; i < authorizations.size(); i++) {
            Authorization authorization = authorizations.get(i);
            if (this.checkAuthorization(authorization)) {
                checked.add(authorization);
            }
        }
        return checked;
    }

    private boolean checkAuthorization(Authorization authorization) throws Throwable {
        if (null == authorization) {
            _logger.warn("invalid authorization -  null object");
            return false;
        }
        String groupName = (null != authorization.getGroup()) ? authorization.getGroup().getName() : null;
        String roleName = (null != authorization.getRole()) ? authorization.getRole().getName() : null;
        if (null == roleName && null == groupName) {
            _logger.warn("invalid authorization -  null group and role");
            return false;
        }
        if (null != groupName && null == this.getGroupManager().getGroup(groupName)) {
            _logger.warn("invalid authorization -  invalid referenced group");
            return false;
        }
        if (null != roleName && null == this.getRoleManager().getRole(roleName)) {
            _logger.warn("invalid authorization -  invalid referenced role");
            return false;
        }
        return true;
    }

    @Override
    public void deleteUserAuthorization(String username, String groupname, String rolename) throws ApsSystemException {
        try {
            this.getAuthorizationDAO().deleteUserAuthorization(username, groupname, rolename);
        } catch (Throwable t) {
            _logger.error("Error deleting user authorization for user '{}'", username, t);
            throw new ApsSystemException("Error deleting user authorization for user " + username, t);
        }
    }

    @Override
    public void deleteUserAuthorizations(String username) throws ApsSystemException {
        try {
            this.getAuthorizationDAO().deleteUserAuthorizations(username);
        } catch (Throwable t) {
            _logger.error("Error deleting user authorizations for user '{}'", username, t);
            throw new ApsSystemException("Error deleting user authorizations for user " + username, t);
        }
    }

    @AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
    public void deleteUser(Object key) {
        String username = null;
        if (key instanceof String) {
            username = key.toString();
        } else if (key instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) key;
            username = userDetails.getUsername();
        }
        if (username != null) {
            try {
                this.deleteUserAuthorizations(username);
            } catch (Throwable t) {
                _logger.error("Error deleting user authorizations. user: {}", username, t);
            }
        }
    }

    @Override
    public List<String> getUsersByRole(IApsAuthority authority, boolean includeAdmin) throws ApsSystemException {
        if (null == authority || !(authority instanceof Role) || null == this.getRoleManager().getRole(authority.getAuthority())) {
            return null;
        }
        return this.getUsersByAuthorities(null, authority.getAuthority(), includeAdmin);
    }

    @Override
    public List<String> getUsersByRole(String roleName, boolean includeAdmin) throws ApsSystemException {
        Role role = this.getRoleManager().getRole(roleName);
        if (null == role) {
            return null;
        }
        return this.getUsersByAuthorities(null, roleName, includeAdmin);
    }

    @Override
    public List<String> getUsersByGroup(IApsAuthority authority, boolean includeAdmin) throws ApsSystemException {
        if (null == authority || !(authority instanceof Group) || null == this.getGroupManager().getGroup(authority.getAuthority())) {
            return null;
        }
        return this.getUsersByAuthorities(authority.getAuthority(), null, includeAdmin);
    }

    @Override
    public List<String> getUsersByGroup(String groupName, boolean includeAdmin) throws ApsSystemException {
        Group group = this.getGroupManager().getGroup(groupName);
        if (null == group) {
            return null;
        }
        return this.getUsersByAuthorities(groupName, null, includeAdmin);
    }

    @Override
    public List<String> getUsersByAuthorities(String groupName, String roleName, boolean includeAdmin) throws ApsSystemException {
        List<String> usernames = null;
        try {
            List<String> groupNames = null;
            if (!StringUtils.isEmpty(groupName)) {
                groupNames = new ArrayList<String>();
                groupNames.add(groupName);
                if (includeAdmin && !groupName.equals(Group.ADMINS_GROUP_NAME)) {
                    groupNames.add(Group.ADMINS_GROUP_NAME);
                }
            }
            List<String> roleNames = null;
            if (!StringUtils.isEmpty(roleName)) {
                roleNames = new ArrayList<String>();
                roleNames.add(roleName);
                if (includeAdmin) {
                    List<Role> adminRoles = this.getRoleManager().getRolesWithPermission(Permission.SUPERUSER);
                    if (null != adminRoles) {
                        for (int i = 0; i < adminRoles.size(); i++) {
                            Role role = adminRoles.get(i);
                            if (null != role && !roleNames.contains(role.getName())) {
                                roleNames.add(role.getName());
                            }
                        }
                    }
                }
            }
            usernames = this.getAuthorizationDAO().getUsersByAuthorities(groupNames, roleNames);
        } catch (Throwable t) {
            _logger.error("Error extracting usernames by authorities - group '{}' : role {}", groupName, roleName, t);
            throw new ApsSystemException("Error extracting usernames by authorities", t);
        }
        return usernames;
    }

    @Override
    public List<String> getUsersByAuthority(IApsAuthority authority, boolean includeAdmin) throws ApsSystemException {
        if (authority instanceof Group) {
            return this.getUsersByGroup(authority, includeAdmin);
        } else {
            return this.getUsersByRole(authority, includeAdmin);
        }
    }

	@Override
    public List<String> getGroupUtilizers(String groupName) throws ApsSystemException {
		return this.getUsersByGroup(groupName, false);
	}

    protected IAuthorizationDAO getAuthorizationDAO() {
        return _authorizationDAO;
    }

    public void setAuthorizationDAO(IAuthorizationDAO authorizationDAO) {
        this._authorizationDAO = authorizationDAO;
    }

    protected IRoleManager getRoleManager() {
        return _roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this._roleManager = roleManager;
    }

    protected IGroupManager getGroupManager() {
        return _groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }

    private IAuthorizationDAO _authorizationDAO;
    private IRoleManager _roleManager;
    private IGroupManager _groupManager;

}