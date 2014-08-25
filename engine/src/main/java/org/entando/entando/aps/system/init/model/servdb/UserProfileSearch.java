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

import java.util.Date;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserProfileSearch.TABLE_NAME)
public class UserProfileSearch implements ExtendedColumnDefinition {
	
	public UserProfileSearch() {}
	
	@DatabaseField(foreign = true, columnName = "username", 
			width = 40, 
			canBeNull = false, index = true)
	private UserProfile _username;
	
	@DatabaseField(columnName = "attrname", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _attributeName;
	
	@DatabaseField(columnName = "textvalue", 
			dataType = DataType.STRING)
	private String _textValue;
	
	@DatabaseField(columnName = "datevalue", 
			dataType = DataType.DATE)
	private Date _dateValue;
	
	@DatabaseField(columnName = "numvalue", 
			dataType = DataType.INTEGER)
	private int _numberValue;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 3)
	private String _langCode;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String profileTableName = UserProfile.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			profileTableName = "`" + profileTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_fkey FOREIGN KEY (username) "
				+ "REFERENCES " + profileTableName + " (username)"};
	}
	
	public static final String TABLE_NAME = "authuserprofilesearch";
	
}
/*
CREATE TABLE userprofile_profilesearch
(
  username character varying(40) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpuserprofile_profilesearch_username_fkey FOREIGN KEY (username)
      REFERENCES jpuserprofile_authuserprofiles (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
 */