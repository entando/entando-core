/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.user.validator;

import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.user.IUserManager;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author paddeo
 */
@Component
public class UserValidator implements Validator {

    public static final String ERRCODE_AUTHORITIES_INVALID_REQ = "1";

    public static final String ERRCODE_AUTHORITIES_SELF_UPDATE = "2";

    @Autowired
    IUserManager userManager;

    @Autowired
    IGroupManager groupManager;

    @Autowired
    IRoleManager roleManager;

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public IRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return UserAuthoritiesRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof UserAuthoritiesRequest) {
            UserAuthoritiesRequest request = (UserAuthoritiesRequest) target;
            validateGroupsAndRoles(request, errors);
        }
    }

    public void validateGroupsAndRoles(UserAuthoritiesRequest request, Errors errors) {
        List<String> invalidAuths = new ArrayList<>();
        request.forEach(authority -> {
            if (this.getGroupManager().getGroup(authority.getGroup()) == null) {
                invalidAuths.add(authority.getGroup());
            }
            if (this.getRoleManager().getRole(authority.getRole()) == null) {
                invalidAuths.add(authority.getRole());
            }
        });
        if (!invalidAuths.isEmpty()) {
            errors.reject(ERRCODE_AUTHORITIES_INVALID_REQ, invalidAuths.toArray(), "user.authorities.invalid.req");
        }
    }

    public void validateUpdateSelf(String username, String currentUser, Errors errors) {
        if (username.equals(currentUser)) {
            errors.reject(ERRCODE_AUTHORITIES_SELF_UPDATE, new String[]{username}, "user.authorities.self.update");
        }
    }

}
