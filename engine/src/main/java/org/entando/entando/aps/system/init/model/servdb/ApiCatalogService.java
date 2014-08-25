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
@DatabaseTable(tableName = ApiCatalogService.TABLE_NAME)
public class ApiCatalogService implements ExtendedColumnDefinition {
	
	public ApiCatalogService() {}
	
	@DatabaseField(columnName = "servicekey", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false, id = true)
	private String _serviceKey;
	
	@DatabaseField(columnName = "resourcecode", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _resourceCode;
	
	@DatabaseField(columnName = "description", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "parameters", 
			dataType = DataType.LONG_STRING)
	private String _parameters;
	
	@DatabaseField(columnName = "tag", 
			dataType = DataType.STRING, 
			width = 100)
	private String _tag;
	
	@DatabaseField(columnName = "freeparameters", 
			dataType = DataType.LONG_STRING)
	private String _freeParameters;
	
	@DatabaseField(columnName = "isactive", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _active;
	
	@DatabaseField(columnName = "ishidden", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _hidden;
	
	@DatabaseField(columnName = "myentando", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _myentando;
	
	@DatabaseField(columnName = "authenticationrequired", 
			dataType = DataType.SHORT)
	private short _authenticationRequired;
	
	@DatabaseField(columnName = "requiredpermission", 
			foreign = true, width = 30)
	private Permission _requiredPermission;
	
	@DatabaseField(columnName = "requiredgroup", 
			foreign = true, width = 20)
	private Group _requiredGroup;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String permissionsTableName = Permission.TABLE_NAME;
		String groupsTableName = Group.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			permissionsTableName = "`" + permissionsTableName + "`";
			groupsTableName = "`" + groupsTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT " + TABLE_NAME + "_gr_fkey FOREIGN KEY (requiredgroup) "
				+ "REFERENCES " + groupsTableName + " (groupname)", 
			"ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT " + TABLE_NAME + "_perm_fkey FOREIGN KEY (requiredpermission) "
				+ "REFERENCES " + permissionsTableName + " (permissionname)"};
	}
	
	public static final String TABLE_NAME = "apicatalog_services";
	
}
/*
CREATE TABLE apicatalog_services
(
  servicekey character varying(100) NOT NULL,
  resourcecode character varying(100) NOT NULL,
  description character varying NOT NULL,
  parameters character varying,
  tag character varying(100),
  freeparameters character varying,
  isactive smallint NOT NULL,
  ishidden smallint NOT NULL,
  myentando smallint NOT NULL,
  authenticationrequired smallint,
  requiredpermission character varying(30),
  requiredgroup character varying(20),
  CONSTRAINT apicatalog_services_pkey PRIMARY KEY (servicekey ),
  CONSTRAINT apicatalog_services_requiredgroup_fkey FOREIGN KEY (requiredgroup)
      REFERENCES authgroups (groupname) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT apicatalog_services_requiredpermission_fkey FOREIGN KEY (requiredpermission)
      REFERENCES authpermissions (permissionname) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
)
 */