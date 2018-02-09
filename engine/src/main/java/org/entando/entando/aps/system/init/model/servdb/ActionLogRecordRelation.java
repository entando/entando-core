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
@DatabaseTable(tableName = ActionLogRecordRelation.TABLE_NAME)
public class ActionLogRecordRelation implements ExtendedColumnDefinition {

    public ActionLogRecordRelation() {
    }

    @DatabaseField(columnName = "id",
            dataType = DataType.INTEGER,
            canBeNull = false,
            generatedId = true)
    private int _id;

    @DatabaseField(foreign = true, columnName = "recordid",
            canBeNull = false, index = true)
    private ActionLogRecord _recordId;

    @DatabaseField(columnName = "refgroup",
            dataType = DataType.STRING,
            width = 20, index = true)
    private String _group;

    @Override
    public String[] extensions(IDatabaseManager.DatabaseType type) {
        String tableName = TABLE_NAME;
        String logTableName = ActionLogRecord.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + TABLE_NAME + "`";
            logTableName = "`" + logTableName + "`";
        }
        return new String[]{"ALTER TABLE " + tableName + " "
            + "ADD CONSTRAINT " + TABLE_NAME + "_recid_fkey FOREIGN KEY (recordid) "
            + "REFERENCES " + logTableName + " (id)"};
    }

    public static final String TABLE_NAME = "actionlogrelations";

}
/*
CREATE TABLE actionlogrelations
(
  recordid integer NOT NULL,
  refgroup character varying(20),
  CONSTRAINT actionlogrelations_recid_fkey FOREIGN KEY (recordid)
      REFERENCES actionloggerrecords (id)
)
 */
