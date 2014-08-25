/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.userprofile;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileRecord;

import com.agiletec.aps.system.common.entity.AbstractEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * Data Access Object for UserProfile Object. 
 * @author E.Santoboni
 */
public class UserProfileDAO extends AbstractEntityDAO implements IUserProfileDAO  {
	
	@Override
	protected String getLoadEntityRecordQuery() {
		return GET_PROFILE_VO;
	}
	
	@Override
	protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		UserProfileRecord profile = new UserProfileRecord();
		profile.setId(res.getString("username"));
		profile.setTypeCode(res.getString("profiletype"));
		profile.setXml(res.getString("profilexml"));
		profile.setPublicProfile(res.getInt("publicprofile") == 1);
		return profile;
	}
	
	@Override
	protected String getAddEntityRecordQuery() {
		return INSERT_PROFILE;
	}
	
	@Override
	protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		IUserProfile profile = (IUserProfile) entity;
		stat.setString(1, profile.getUsername());
		stat.setString(2, profile.getTypeCode());
		stat.setString(3, profile.getXML());
		if (profile.isPublicProfile()) {
			stat.setInt(4, 1);
		} else {
			stat.setInt(4, 0);
		}
	}
	
	@Override
	protected String getUpdateEntityRecordQuery() {
		return UPDATE_PROFILE;
	}
	
	@Override
	protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		IUserProfile profile = (IUserProfile) entity;
		stat.setString(1, profile.getTypeCode());
		stat.setString(2, profile.getXML());
		if (profile.isPublicProfile()) {
			stat.setInt(3, 1);
		} else {
			stat.setInt(3, 0);
		}
		stat.setString(4, profile.getUsername());
	}
	
	@Override
	protected String getDeleteEntityRecordQuery() {
		return DELETE_PROFILE_BY_USER;
	}
	
	@Override
	protected String getAddingSearchRecordQuery() {
		return ADD_PROFILE_SEARCH_RECORD;
	}

	@Override
	protected String getExtractingAllEntityIdQuery() {
		return GET_ALL_ENTITY_ID;
	}

	@Override
	protected String getRemovingSearchRecordQuery() {
		return DELETE_PROFILE_SEARCH_RECORD;
	}
	
	@Override
	protected String getAddingAttributeRoleRecordQuery() {
		return ADD_ATTRIBUTE_ROLE_RECORD;
	}
	
	@Override
	protected String getRemovingAttributeRoleRecordQuery() {
		return DELETE_ATTRIBUTE_ROLE_RECORD;
	}
	
	private final String ADD_PROFILE_SEARCH_RECORD =
		"INSERT INTO authuserprofilesearch " +
		"(username, attrname, textvalue, datevalue, numvalue, langcode) " +
		"VALUES ( ? , ? , ? , ? , ? , ? ) ";
	
	private final String DELETE_PROFILE_SEARCH_RECORD =
		"DELETE FROM authuserprofilesearch WHERE username = ? ";
	
	private final String GET_ALL_ENTITY_ID = 
		"SELECT username FROM authuserprofiles";
	
	private final String INSERT_PROFILE = 
		"INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) values ( ? , ? , ? , ? ) ";
	
	private final String DELETE_PROFILE_BY_USER = 
		"DELETE FROM authuserprofiles WHERE username = ? ";
	
	private final String GET_PROFILE_VO = 
		"SELECT username, profiletype, profilexml, publicprofile FROM authuserprofiles WHERE username = ? ";
	
	private final String UPDATE_PROFILE = 
		"UPDATE authuserprofiles SET profiletype = ? , profilexml = ? , publicprofile = ? WHERE username = ? ";
	
	private final String ADD_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM authuserprofileattrroles WHERE username = ? ";
	
}
