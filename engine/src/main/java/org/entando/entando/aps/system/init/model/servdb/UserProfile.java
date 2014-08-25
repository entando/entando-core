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
			width = 40, 
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
  username character varying(40) NOT NULL,
  profiletype character varying(30) NOT NULL,
  profilexml character varying NOT NULL,
  publicprofile smallint NOT NULL,
  CONSTRAINT userprofile_autuserprofiles_pkey PRIMARY KEY (username)
);
 */