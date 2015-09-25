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
package org.entando.entando.apsadmin.common.currentuser;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.apsadmin.common.UserAvatarAction;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public class CurrentAvatarAction extends UserAvatarAction {
	
	@Override
	protected IUserProfile getUserProfile() throws ApsSystemException {
		UserDetails currentUser = super.getCurrentUser();
		IUserProfile profile = (null != currentUser && null != currentUser.getProfile()) 
				? (IUserProfile) currentUser.getProfile() 
				: null;
		return profile;
	}
	
}