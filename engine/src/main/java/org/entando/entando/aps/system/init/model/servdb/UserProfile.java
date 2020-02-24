/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.model.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserProfile.TABLE_NAME)
public class UserProfile {
	
	public UserProfile() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 80,
			canBeNull = false, id = true)
	private String _username;
	
	@DatabaseField(columnName = "profiletype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _profileType;
	
	@DatabaseField(columnName = "profilexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _profileXml;
	
	@DatabaseField(columnName = "publicprofile", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _publicProfile;
	
	public static final String TABLE_NAME = "authuserprofiles";
	
}
/*
CREATE TABLE userprofile_authuserprofiles
(
  username character varying(80) NOT NULL,
  profiletype character varying(30) NOT NULL,
  profilexml character varying NOT NULL,
  publicprofile smallint NOT NULL,
  CONSTRAINT userprofile_autuserprofiles_pkey PRIMARY KEY (username)
);
 */