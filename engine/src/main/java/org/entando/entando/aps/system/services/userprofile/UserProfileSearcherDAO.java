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

import java.sql.ResultSet;

import org.entando.entando.aps.system.services.userprofile.model.UserProfileRecord;

import com.agiletec.aps.system.common.entity.AbstractEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

/**
 * Data Access Object for Search of UserProfile Object. 
 * @author E.Santoboni
 */
public class UserProfileSearcherDAO extends AbstractEntitySearcherDAO {

	@Override
	protected ApsEntityRecord createRecord(ResultSet result) throws Throwable {
		UserProfileRecord record = new UserProfileRecord();
		record.setId(result.getString("username"));
		record.setXml(result.getString("profilexml"));
		record.setTypeCode(result.getString("profiletype"));
		record.setPublicProfile(result.getInt("publicprofile") == 1);
		return record;
	}
	
	@Override
	protected String getEntityMasterTableName() {
		return "authuserprofiles";
	}
	
	@Override
	protected String getEntityMasterTableIdFieldName() {
		return "username";
	}
	
	@Override
	protected String getEntityMasterTableIdTypeFieldName() {
		return "profiletype";
	}
	
	@Override
	protected String getEntitySearchTableName() {
		return "authuserprofilesearch";
	}
	
	@Override
	protected String getEntitySearchTableIdFieldName() {
		return "username";
	}
	
	@Override
	protected String getEntityAttributeRoleTableName() {
		return "authuserprofileattrroles";
	}
	
	@Override
	protected String getEntityAttributeRoleTableIdFieldName() {
		return "username";
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		if (metadataFieldKey.equalsIgnoreCase("username")) {
			return this.getEntityMasterTableIdFieldName();
		} else if (metadataFieldKey.equals(IEntityManager.ENTITY_ID_FILTER_KEY)) {
			return this.getEntityMasterTableIdFieldName();
		} else if (metadataFieldKey.equals(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
			return this.getEntityMasterTableIdTypeFieldName();
		} else if (metadataFieldKey.equals(IUserProfileManager.PUBLIC_PROFILE_FILTER_KEY)) {
			return "publicprofile";
		} else {
			throw new RuntimeException("Key '" + metadataFieldKey + "' not recognized");
		}
	}
	
}
