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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserGroupReference.TABLE_NAME)
public class UserGroupReference implements ExtendedColumnDefinition {
	
	public UserGroupReference() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "groupname", 
			foreign = true,
			width = 20, 
			canBeNull = false)
	private Group _permission;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String groupTableName = Group.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			groupTableName = "`" + groupTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_group_fkey FOREIGN KEY (groupname) "
				+ "REFERENCES " + groupTableName + " (groupname)"};
	}
	
	public static final String TABLE_NAME = "authusergroups";
	
}
/*
CREATE TABLE authusergroups
(
  username character varying(40) NOT NULL,
  groupname character varying(20) NOT NULL,
  CONSTRAINT authusergroups_pkey PRIMARY KEY (username , groupname ),
  CONSTRAINT authusergroups_groupname_fkey FOREIGN KEY (groupname)
      REFERENCES authgroups (groupname) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
)
 */