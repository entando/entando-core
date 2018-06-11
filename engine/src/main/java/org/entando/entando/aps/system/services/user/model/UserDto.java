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
package org.entando.entando.aps.system.services.user.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.entando.entando.aps.system.services.user.IUserService;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 *
 * @author paddeo
 */
public class UserDto {

    private String username;
    private String registration;
    private String lastLogin;
    private String lastPasswordChange;
    private String status;
    private boolean accountNotExpired;
    private boolean credentialsNotExpired;
    private String profileType;
    private Map<String, Object> profileAttributes = new HashMap<>();
    private int maxMonthsSinceLastAccess;
    private int maxMonthsSinceLastPasswordChange;

    public UserDto(UserDetails user) {
        this.username = user.getUsername();
        this.status = user.isDisabled() ? IUserService.STATUS_DISABLED : IUserService.STATUS_ACTIVE;
        this.accountNotExpired = user.isAccountNotExpired();
        this.credentialsNotExpired = user.isCredentialsNotExpired();
        if (user instanceof User) {
            User entandoUser = (User) user;
            if (null != entandoUser.getCreationDate()) {
                this.registration = DateConverter.getFormattedDate(entandoUser.getCreationDate(), SystemConstants.API_DATE_FORMAT);
            }
            if (null != entandoUser.getLastAccess()) {
                this.lastLogin = DateConverter.getFormattedDate(entandoUser.getLastAccess(), SystemConstants.API_DATE_FORMAT);
            }
            if (null != entandoUser.getLastPasswordChange()) {
                this.lastPasswordChange = DateConverter.getFormattedDate(entandoUser.getLastPasswordChange(), SystemConstants.API_DATE_FORMAT);
            }
            this.maxMonthsSinceLastAccess = entandoUser.getMaxMonthsSinceLastAccess();
            this.maxMonthsSinceLastPasswordChange = entandoUser.getMaxMonthsSinceLastPasswordChange();
        }
        if (user.getProfile() != null) {
            this.profileAttributes = convertUserProfileAttributes((IUserProfile) user.getProfile());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(String lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAccountNotExpired() {
        return accountNotExpired;
    }

    public void setAccountNotExpired(boolean accountNotExpired) {
        this.accountNotExpired = accountNotExpired;
    }

    public boolean isCredentialsNotExpired() {
        return credentialsNotExpired;
    }

    public void setCredentialsNotExpired(boolean credentialsNotExpired) {
        this.credentialsNotExpired = credentialsNotExpired;
    }

    public Map<String, Object> getProfileAttributes() {
        return profileAttributes;
    }

    public void setProfileAttributes(Map<String, Object> profileAttributes) {
        this.profileAttributes = profileAttributes;
    }

    public int getMaxMonthsSinceLastAccess() {
        return maxMonthsSinceLastAccess;
    }

    public void setMaxMonthsSinceLastAccess(int maxMonthsSinceLastAccess) {
        this.maxMonthsSinceLastAccess = maxMonthsSinceLastAccess;
    }

    public int getMaxMonthsSinceLastPasswordChange() {
        return maxMonthsSinceLastPasswordChange;
    }

    public void setMaxMonthsSinceLastPasswordChange(int maxMonthsSinceLastPasswordChange) {
        this.maxMonthsSinceLastPasswordChange = maxMonthsSinceLastPasswordChange;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "username":
                return "username";
            default:
                return dtoFieldName;
        }
    }

    private Map<String, Object> convertUserProfileAttributes(IUserProfile profile) {
        Map<String, Object> attributes = new HashMap<>();
        profile.getAttributeList().forEach(elem -> {
            Optional<String[]> rolesOpt = Optional.ofNullable(elem.getRoles());
            rolesOpt.ifPresent(roles -> Arrays.asList(roles).forEach(role -> {
                attributes.put(role.replace("userprofile:", ""), getValue(elem));
            }));
            if (!rolesOpt.isPresent()) {
                attributes.put(elem.getName(), getValue(elem));
            }
        });
        return attributes;
    }

    private Object getValue(AttributeInterface attribute) {
        if (null == attribute) {
            return "";
        }
        if (attribute.isTextAttribute()) {
            return ((ITextAttribute) attribute).getText();
        } else if (attribute instanceof NumberAttribute) {
            return ((NumberAttribute) attribute).getValue();
        } else if (attribute instanceof BooleanAttribute) {
            return ((BooleanAttribute) attribute).getValue();
        } else if (attribute instanceof DateAttribute) {
            Date dateAttr = ((DateAttribute) attribute).getDate();
            if (null == dateAttr) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(dateAttr);
        } else if (!attribute.isSimple()) {
            String text = "";
            List<AttributeInterface> attributes = ((AbstractComplexAttribute) attribute).getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                if (i > 0) {
                    text += ",";
                }
                AttributeInterface attributeElem = attributes.get(i);
                text += this.getValue(attributeElem);
            }
            return text;
        }
        return null;
    }
}
