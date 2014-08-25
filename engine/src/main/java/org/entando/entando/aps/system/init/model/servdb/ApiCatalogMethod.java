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
@DatabaseTable(tableName = ApiCatalogMethod.TABLE_NAME)
public class ApiCatalogMethod implements ExtendedColumnDefinition {
	
	public ApiCatalogMethod() {}
	
	@DatabaseField(columnName = "resourcecode", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _resourceCode;
	
	@DatabaseField(columnName = "httpmethod", 
			dataType = DataType.STRING, 
			width = 6, 
			canBeNull = false)
	private String _httpmethod;
	
	@DatabaseField(columnName = "isactive", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _active;
	
	@DatabaseField(columnName = "ishidden", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _hidden;
	
	@DatabaseField(columnName = "authenticationrequired", 
			dataType = DataType.SHORT)
	private short _authenticationRequired;
	
	@DatabaseField(columnName = "authorizationrequired", 
			foreign = true, width = 30)
	private Permission _authorizationRequired;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String permissionsTableName = Permission.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			permissionsTableName = "`" + permissionsTableName + "`";
		}
		return new String[]{"ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT " 
				+ TABLE_NAME + "_pkey PRIMARY KEY(resourcecode , httpmethod)", 
			"ALTER TABLE " + tableName + " ADD CONSTRAINT " 
				+ TABLE_NAME + "_auth_fkey FOREIGN KEY (authorizationrequired) REFERENCES " 
				+ permissionsTableName + " (permissionname)"};
	}
	
	public static final String TABLE_NAME = "apicatalog_methods";
	
}
/*
CREATE TABLE apicatalog_methods
(
  resourcecode character varying(100) NOT NULL,
  httpmethod character varying(6) NOT NULL,
  isactive smallint NOT NULL,
  ishidden smallint NOT NULL,
  authenticationrequired smallint,
  authorizationrequired character varying(30),
  CONSTRAINT apicatalog_status_pkey PRIMARY KEY (resourcecode , httpmethod ),
  CONSTRAINT apicatalog_methods_authorizationrequired_fkey FOREIGN KEY (authorizationrequired)
      REFERENCES authpermissions (permissionname) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
)
 */