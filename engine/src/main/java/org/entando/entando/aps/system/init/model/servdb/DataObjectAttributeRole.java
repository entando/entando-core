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
@DatabaseTable(tableName = DataObjectAttributeRole.TABLE_NAME)
public class DataObjectAttributeRole implements ExtendedColumnDefinition {

    public DataObjectAttributeRole() {
    }

    @DatabaseField(columnName = "id",
            dataType = DataType.INTEGER,
            canBeNull = false,
            generatedId = true)
    private int _id;

    @DatabaseField(foreign = true, columnName = "dataid",
            width = 16,
            canBeNull = false, index = true)
    private DataObject _dataId;

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
        String contentTableName = DataObject.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + tableName + "`";
            contentTableName = "`" + DataObject.TABLE_NAME + "`";
        }
        return new String[]{"ALTER TABLE " + tableName + " "
            + "ADD CONSTRAINT contentattrroles_contid_fkey FOREIGN KEY (dataid) "
            + "REFERENCES " + contentTableName + " (dataid)"};
    }

    public static final String TABLE_NAME = "dataobjectattributeroles";

}
