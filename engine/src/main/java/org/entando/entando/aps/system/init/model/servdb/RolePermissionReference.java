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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = RolePermissionReference.TABLE_NAME)
public class RolePermissionReference implements ExtendedColumnDefinition {
	
	public RolePermissionReference() {}
	
	@DatabaseField(columnName = "rolename", 
			foreign = true,
			width = 20, 
			canBeNull = false)
	private Role _role;
	
	@DatabaseField(columnName = "permissionname", 
			foreign = true,
			width = 30, 
			canBeNull = false)
	private Permission _permission;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String roleTableName = Role.TABLE_NAME;
		String permissionableName = Permission.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			roleTableName = "`" + roleTableName + "`";
			permissionableName = "`" + permissionableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_perm_fkey FOREIGN KEY (permissionname) "
				+ "REFERENCES " + permissionableName + " (permissionname)", 
			"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_role_fkey FOREIGN KEY (rolename) "
				+ "REFERENCES " + roleTableName + " (rolename)"};
	}
	
	public static final String TABLE_NAME = "authrolepermissions";
	
}
/*
CREATE TABLE authrolepermissions
(
  rolename character varying(20) NOT NULL,
  permissionname character varying(30) NOT NULL,
  CONSTRAINT authrolepermissions_pkey PRIMARY KEY (rolename , permissionname ),
  CONSTRAINT authrolepermissions_permissionname_fkey FOREIGN KEY (permissionname)
      REFERENCES authpermissions (permissionname) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT authrolepermissions_rolename_fkey FOREIGN KEY (rolename)
      REFERENCES authroles (rolename) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
)
 */