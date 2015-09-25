/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.tags;

import javax.servlet.http.HttpSession;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Current User Profile tag.
 * Return an attribute value of the current user profile.
 * @author E.Santoboni
 */
public class CurrentUserProfileAttributeTag extends UserProfileAttributeTag {

	private static final Logger _logger =  LoggerFactory.getLogger(CurrentUserProfileAttributeTag.class);
	
	@Override
    protected IUserProfile getUserProfile() throws Throwable {
        HttpSession session = this.pageContext.getSession();
        UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        if (currentUser == null || currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME) || null == currentUser.getProfile()) {
            _logger.error("User '{}' : Null user, or guest user or user without profile", currentUser);
            return null;
        }
        return (IUserProfile) currentUser.getProfile();
    }
    
}
