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
package org.entando.entando.aps.system.services.userprofile.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * Provides core UserProfile information.
 * @author E.Santoboni
 */
public interface IUserProfile extends IApsEntity {
	
	/**
	 * Return of the username that is associated with the profile.
	 * @return The username.
	 */
	public String getUsername();
	
	public String getDisplayName();
	
	/**
	 * Returns the value of an attribute identified by his key. 
	 * The value can be of any type.
	 * @param key The key of the attribute.
	 * @return The value of the attribute.
	 */
	public Object getValue(String key);
	
	public Object getValueByRole(String rolename);
	
	/**
	 * Return the name of the attribute that represents the fullname.
	 * @return the name of the attribute.
	 */
	public String getFullNameAttributeName();
	
	/**
	 * Return the name of the attribute that represents the mail address.
	 * @return the name of the attribute.
	 */
	public String getMailAttributeName();
	
	public boolean isPublicProfile();
	
	public void setPublicProfile(boolean isPublic);
	
}