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
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.IApsEncrypter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.util.argon2.Argon2Encrypter;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserPasswordRequest;
import org.entando.entando.web.user.model.UserRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

/**
 *
 * @author paddeo
 */
@Component
public class UserValidator extends AbstractPaginationValidator {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ERRCODE_USER_ALREADY_EXISTS = "1";

    public static final String ERRCODE_USERNAME_FORMAT_INVALID = "2";

    public static final String ERRCODE_PASSWORD_FORMAT_INVALID = "3";

    public static final String ERRCODE_USERNAME_MISMATCH = "2";
    public static final String ERRCODE_OLD_PASSWORD_FORMAT = "4";
    public static final String ERRCODE_NEW_PASSWORD_FORMAT = "5";

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
        try {
            if (null != this.getUserManager().getUser(username)) {
                bindingResult.reject(UserValidator.ERRCODE_USER_ALREADY_EXISTS, new String[]{username}, "user.exists");
                throw new ValidationConflictException(bindingResult);
            }
            Pattern pattern = Pattern.compile("([a-zA-Z0-9_\\.])+");
            Matcher matcherUsername = pattern.matcher(username);
            int usLength = username.length();
            if (usLength < 8 || usLength > 20 || !matcherUsername.matches()) {
                bindingResult.reject(UserValidator.ERRCODE_USERNAME_FORMAT_INVALID, new String[]{username}, "user.username.format.invalid");
            }
            int pwLength = request.getPassword().length();
            Matcher matcherPassword = pattern.matcher(request.getPassword());
            if (pwLength < 8 || pwLength > 20 || !matcherPassword.matches()) {
                bindingResult.reject(UserValidator.ERRCODE_PASSWORD_FORMAT_INVALID, new String[]{username}, "user.password.format.invalid");
            }
        } catch (ValidationConflictException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error adding user {}", username, e);
            throw new RestServerError("Error adding user", e);
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
            errors.rejectValue("username", ERRCODE_USERNAME_MISMATCH, new String[]{username, usernameReq}, "user.username.mismatch");
        }
    }

    public void validatePassword(String username, String password, Errors errors) {
        if (!this.verifyPassword(username, password)) {
            errors.rejectValue("password", ERRCODE_OLD_PASSWORD_FORMAT, new String[]{}, "user.password.invalid");
        }
    }

    public void validatePasswords(UserPasswordRequest passwordRequest, Errors errors) {
        if (StringUtils.equals(passwordRequest.getNewPassword(), passwordRequest.getOldPassword())) {
            errors.rejectValue("newPassword", ERRCODE_NEW_PASSWORD_FORMAT, new String[]{}, "user.passwords.same");
        } else if (!this.verifyPassword(passwordRequest.getUsername(), passwordRequest.getOldPassword())) {
            errors.rejectValue("oldPassword", ERRCODE_OLD_PASSWORD_FORMAT, new String[]{}, "user.password.old.invalid");
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

    @Override
    protected String getDefaultSortProperty() {
        return "username";
    }

}
