/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.user.validator;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.IApsEncrypter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.util.argon2.Argon2Encrypter;
import org.entando.entando.web.user.UserController;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserPasswordRequest;
import org.slf4j.LoggerFactory;
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

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ERRCODE_AUTHORITIES_INVALID_REQ = "1";

    public static final String ERRCODE_AUTHORITIES_SELF_UPDATE = "2";

    @Autowired
    IUserManager userManager;

    @Autowired
    IGroupManager groupManager;

    @Autowired
    IRoleManager roleManager;

    @Autowired
    private IApsEncrypter encrypter;

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

    public IApsEncrypter getEncrypter() {
        return encrypter;
    }

    public void setEncrypter(IApsEncrypter encrypter) {
        this.encrypter = encrypter;
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

    public void validateBody(String username, String usernameReq, Errors errors) {
        if (!StringUtils.equals(username, usernameReq)) {
            errors.rejectValue("username", UserController.ERRCODE_USERNAME_MISMATCH, new String[]{username, usernameReq}, "user.username.mismatch");
        }
    }

    public void validatePassword(String username, String password, Errors errors) {
        if (!this.verifyPassword(username, password)) {
            errors.rejectValue("password", UserController.ERRCODE_OLD_PASSWORD_FORMAT, new String[]{}, "user.password.invalid");
        }
    }

    public void validatePasswords(UserPasswordRequest passwordRequest, Errors errors) {
        if (StringUtils.equals(passwordRequest.getNewPassword(), passwordRequest.getOldPassword())) {
            errors.rejectValue("newPassword", UserController.ERRCODE_NEW_PASSWORD_FORMAT, new String[]{}, "user.passwords.same");
        } else {
            if (!this.verifyPassword(passwordRequest.getUsername(), passwordRequest.getOldPassword())) {
                errors.rejectValue("oldPassword", UserController.ERRCODE_OLD_PASSWORD_FORMAT, new String[]{}, "user.password.old.invalid");
            }
        }
    }

    private boolean verifyPassword(String username, String password) {
        UserDetails user = null;
        try {
            user = this.getUserManager().getUser(username);
        } catch (ApsSystemException e) {
            logger.error("Error loading user {}", username, e);
            throw new RestServerError("Error loading user", e);
        }
        if (this.getEncrypter() instanceof Argon2Encrypter) {
            Argon2Encrypter encrypter = (Argon2Encrypter) this.getEncrypter();
            return encrypter.verify(user.getPassword(), password);
        } else {
            String encrypdedPassword = null;
            try {
                encrypdedPassword = this.getEncrypter().encrypt(password);
            } catch (ApsSystemException e) {
                logger.error("Error encrypting password", e);
                throw new RestServerError("Error encrypting passwor", e);
            }
            return user.getPassword().equals(encrypdedPassword);
        }
    }

}
