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
package org.entando.entando.aps.system.services.userprofile;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interface for Manager of UserProfile Object. 
 * @author E.Santoboni
 */
public interface IUserProfileManager extends IEntityManager {
	
	/**
	 * Return a default UserProfile Prototype. 
	 * The prototype contains all Attribute (empty) of the Profile.
	 * @return A UserProfile Prototype.
	 */
	public IUserProfile getDefaultProfileType();
	
	/**
	 * Return a UserProfile Prototype. 
	 * The prototype contains all Attribute (empty) of the Profile.
	 * @param typeCode The type of required profile.
	 * @return A UserProfile Prototype.
	 */
	public IUserProfile getProfileType(String typeCode);
	
	/**
	 * Return a UserProfile by username.
	 * @param username The username of the profile to return.
	 * @return The UserProfile required.
	 * @throws ApsSystemException In case of Exception.
	 */
	public IUserProfile getProfile(String username)	throws ApsSystemException;
	
	/**
	 * Add a UserProfile.
	 * @param username The username of the Profile owner.
	 * @param profile The UserProfile to add.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void addProfile(String username, IUserProfile profile) throws ApsSystemException;
	
	/**
	 * Delete a UserProfile by username.
	 * @param username The username of the Profile owner that you must delete the profile.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void deleteProfile(String username) throws ApsSystemException;
	
	/**
	 * Update a UserProfile.
	 * @param username The username of the user that you must update the profile.
	 * @param profile The profile to update.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void updateProfile(String username, IUserProfile profile) throws ApsSystemException;
	
	public static final String PUBLIC_PROFILE_FILTER_KEY = "publicprofile";
	
}