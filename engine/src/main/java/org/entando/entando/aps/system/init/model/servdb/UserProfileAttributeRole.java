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
@DatabaseTable(tableName = UserProfileAttributeRole.TABLE_NAME)
public class UserProfileAttributeRole implements ExtendedColumnDefinition {

    public UserProfileAttributeRole() {
    }

    @DatabaseField(columnName = "id",
            dataType = DataType.INTEGER,
            canBeNull = false,
            generatedId = true)
    private int _id;

    @DatabaseField(foreign = true, columnName = "username",
            width = 40,
            canBeNull = false, index = true)
    private UserProfile _username;

    @DatabaseField(columnName = "attrname",
            dataType = DataType.STRING,
            width = 30,
            canBeNull = false, index = true)
    private String _attributeName;

    @DatabaseField(columnName = "rolename",
            dataType = DataType.STRING,
            width = 50,
            canBeNull = false, index = true)
    private String _roleName;

    @Override
    public String[] extensions(IDatabaseManager.DatabaseType type) {
        String tableName = TABLE_NAME;
        String profileTableName = UserProfile.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + tableName + "`";
            profileTableName = "`" + UserProfile.TABLE_NAME + "`";
        }
        return new String[]{"ALTER TABLE " + tableName + " "
            + "ADD CONSTRAINT authusprofroles_fkey FOREIGN KEY (username) "
            + "REFERENCES " + profileTableName + " (username)"};
    }

    public static final String TABLE_NAME = "authuserprofileattrroles";

}
