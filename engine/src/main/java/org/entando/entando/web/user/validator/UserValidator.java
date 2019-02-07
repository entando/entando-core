/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.user.validator;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.user.*;
import com.agiletec.aps.util.IApsEncrypter;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.*;
import org.entando.entando.aps.util.argon2.Argon2Encrypter;
import org.entando.entando.web.common.RestErrorCodes;
import org.entando.entando.web.common.exceptions.*;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.user.model.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import java.util.*;
import java.util.regex.*;

/**
 *
 * @author paddeo
 */
@Component
public class UserValidator extends AbstractPaginationValidator {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    private Pattern pattern = Pattern.compile("([a-zA-Z0-9_\\.])+");

    public static final String ERRCODE_USER_ALREADY_EXISTS = "1";

    public static final String ERRCODE_USER_NOT_FOUND = "1";

    public static final String ERRCODE_USERNAME_FORMAT_INVALID = "2";

    public static final String ERRCODE_PASSWORD_FORMAT_INVALID = "3";

    public static final String ERRCODE_USERNAME_MISMATCH = "2";
    public static final String ERRCODE_OLD_PASSWORD_FORMAT = "4";
    public static final String ERRCODE_NEW_PASSWORD_EQUALS = "5";

    public static final String ERRCODE_AUTHORITIES_INVALID_REQ = "2";

    public static final String ERRCODE_SELF_UPDATE = "6";

    public static final String ERRCODE_DELETE_ADMIN = "7";

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
        return UserAuthoritiesRequest.class.equals(paramClass) || UserRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof UserAuthoritiesRequest) {
            UserAuthoritiesRequest request = (UserAuthoritiesRequest) target;
            validateGroupsAndRoles(request, errors);
        }
    }

    public void validateUserPost(UserRequest request, BindingResult bindingResult) {
        String username = request.getUsername();
        UserDetails user = this.extractUser(username);
        if (null != user) {
            bindingResult.reject(UserValidator.ERRCODE_USER_ALREADY_EXISTS, new String[]{username}, "user.exists");
            throw new ValidationConflictException(bindingResult);
        }
        Matcher matcherUsername = pattern.matcher(username);
        int usLength = username.length();
        if (usLength < 4 || usLength > 20 || !matcherUsername.matches()) {
            bindingResult.reject(UserValidator.ERRCODE_USERNAME_FORMAT_INVALID, new String[]{username}, "user.username.format.invalid");
        }
        this.checkNewPassword(username, request.getPassword(), bindingResult);
    }

    public static boolean isValidDeleteUser(String username) {
        return !StringUtils.equalsIgnoreCase("admin", username);
    }

    public static BindingResult createDeleteAdminError() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "admin");
        BindingResult bindingResult = new MapBindingResult(map, "username");
        bindingResult.reject(UserValidator.ERRCODE_DELETE_ADMIN, new String[]{}, "user.admin.cant.delete");
        return bindingResult;
    }

    public void validateGroupsAndRoles(UserAuthoritiesRequest request, Errors errors) {
        List<String> invalidAuths = new ArrayList<>();
        request.forEach(authority -> {
            if (authority.getGroup() != null && this.getGroupManager().getGroup(authority.getGroup()) == null) {
                invalidAuths.add(authority.getGroup());
            }
            if (authority.getRole() != null && this.getRoleManager().getRole(authority.getRole()) == null) {
                invalidAuths.add(authority.getRole());
            }
        });
        if (!invalidAuths.isEmpty()) {
            errors.reject(ERRCODE_AUTHORITIES_INVALID_REQ, invalidAuths.toArray(), "user.authorities.invalid.req");
        }
    }

    public void validateUpdateSelf(String username, String currentUser, BindingResult bindingResult) {
        if (username.equals(currentUser)) {
            bindingResult.reject(ERRCODE_SELF_UPDATE, new String[]{username}, "user.authorities.self.update");
            throw new ResourcePermissionsException(bindingResult);
        }
    }

    public void validatePutBody(String username, UserRequest userRequest, BindingResult bindingResult) {
        if (!StringUtils.equals(username, userRequest.getUsername())) {
            bindingResult.rejectValue("username", ERRCODE_USERNAME_MISMATCH, new String[]{username, userRequest.getUsername()}, "user.username.mismatch");
            throw new ValidationConflictException(bindingResult);
        } else {
            UserDetails user = this.extractUser(username);
            if (null == user) {
                throw new ResourceNotFoundException(ERRCODE_USER_NOT_FOUND, "username", username);
            }
            if (null != userRequest.getPassword()) {
                this.checkNewPassword(username, userRequest.getPassword(), bindingResult);
            }
        }
    }

    public void validateChangePasswords(String username, UserPasswordRequest passwordRequest, BindingResult bindingResult) {
        UserDetails user = this.extractUser(username);
        if (!StringUtils.equals(username, passwordRequest.getUsername())) {
            bindingResult.rejectValue("username", ERRCODE_USERNAME_MISMATCH, new String[]{username, passwordRequest.getUsername()}, "user.username.mismatch");
            throw new ValidationConflictException(bindingResult);
        } else if (null == user) {
            throw new ResourceNotFoundException(ERRCODE_USER_NOT_FOUND, "username", username);
        } else if (StringUtils.equals(passwordRequest.getNewPassword(), passwordRequest.getOldPassword())) {
            bindingResult.rejectValue("newPassword", ERRCODE_NEW_PASSWORD_EQUALS, new String[]{}, "user.passwords.same");
        } else if (!this.verifyPassword(passwordRequest.getUsername(), passwordRequest.getOldPassword())) {
            bindingResult.rejectValue("oldPassword", ERRCODE_OLD_PASSWORD_FORMAT, new String[]{}, "user.password.old.invalid");
        } else {
            this.checkNewPassword(username, passwordRequest.getNewPassword(), bindingResult);
        }
    }

    private void checkNewPassword(String username, String password, BindingResult bindingResult) {
        if (null == password) {
            bindingResult.reject(RestErrorCodes.NOT_NULL, new String[]{username}, "user.password.NotBlank");
            return;
        }
        int pwLength = password.length();
        Matcher matcherPassword = pattern.matcher(password);
        if (pwLength < 8 || pwLength > 20 || !matcherPassword.matches()) {
            bindingResult.reject(UserValidator.ERRCODE_PASSWORD_FORMAT_INVALID, new String[]{username}, "user.password.format.invalid");
        }
    }

    private boolean verifyPassword(String username, String password) {
        UserDetails user = this.extractUser(username);
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

    private UserDetails extractUser(String username) {
        UserDetails user = null;
        try {
            user = this.getUserManager().getUser(username);
        } catch (ApsSystemException e) {
            logger.error("Error loading user {}", username, e);
            throw new RestServerError("Error loading user", e);
        }
        return user;
    }

    @Override
    protected String getDefaultSortProperty() {
        return "username";
    }

}
