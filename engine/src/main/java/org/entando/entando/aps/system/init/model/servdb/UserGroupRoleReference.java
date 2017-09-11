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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserGroupRoleReference.TABLE_NAME)
public class UserGroupRoleReference implements ExtendedColumnDefinition {

    public UserGroupRoleReference() {
    }

    @DatabaseField(columnName = "id",
            dataType = DataType.INTEGER,
            canBeNull = false,
            generatedId = true)
    private int _id;

    @DatabaseField(columnName = "username",
            dataType = DataType.STRING,
            width = 40,
            canBeNull = false)
    private String _username;

    @DatabaseField(columnName = "groupname",
            foreign = true,
            width = 20,
            canBeNull = true)
    private Group _group;

    @DatabaseField(columnName = "rolename",
            foreign = true,
            width = 20,
            canBeNull = true)
    private Role _role;

    @Override
    public String[] extensions(IDatabaseManager.DatabaseType type) {
        String tableName = TABLE_NAME;
        String groupTableName = Group.TABLE_NAME;
        String roleTableName = Role.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + tableName + "`";
            groupTableName = "`" + groupTableName + "`";
            roleTableName = "`" + roleTableName + "`";
        }
        return new String[]{"ALTER TABLE " + tableName + " "
            + "ADD CONSTRAINT " + TABLE_NAME + "_grn_fkey FOREIGN KEY (groupname) "
            + "REFERENCES " + groupTableName + " (groupname)",
            "ALTER TABLE " + tableName + " "
            + "ADD CONSTRAINT " + TABLE_NAME + "_rln_fkey FOREIGN KEY (rolename) "
            + "REFERENCES " + roleTableName + " (rolename)"};
    }

    public static final String TABLE_NAME = "authusergrouprole";

}
/*
CREATE TABLE authusergrouprole
(
  username character varying(40) NOT NULL,
  groupname character varying(20),
  rolename character varying(20),
  CONSTRAINT authusergrouprole_grn_fkey FOREIGN KEY (groupname)
      REFERENCES authgroups (groupname),
  CONSTRAINT authusergrouprole_rln_fkey FOREIGN KEY (rolename)
      REFERENCES authroles (rolename)
)
 */
