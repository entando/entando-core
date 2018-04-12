/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.util.Date;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ActionLogLikeRecord.TABLE_NAME)
public class ActionLogLikeRecord implements ExtendedColumnDefinition {

    public ActionLogLikeRecord() {
    }

    @DatabaseField(columnName = "id",
            dataType = DataType.INTEGER,
            canBeNull = false,
            generatedId = true)
    private int _id;

    @DatabaseField(columnName = "recordid",
            foreign = true,
            canBeNull = false)
    private ActionLogRecord _record;

    @DatabaseField(columnName = "username",
            dataType = DataType.STRING,
            width = 20,
            canBeNull = false)
    private String _username;

    @DatabaseField(columnName = "likedate",
            dataType = DataType.DATE,
            canBeNull = false)
    private Date _likeDate;

    @Override
    public String[] extensions(IDatabaseManager.DatabaseType type) {
        String tableName = TABLE_NAME;
        String recordTableName = ActionLogRecord.TABLE_NAME;
        if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
            tableName = "`" + tableName + "`";
            recordTableName = "`" + recordTableName + "`";
        }
        return new String[]{"ALTER TABLE " + tableName + " "
            + "ADD CONSTRAINT actionloglikerec_recid_fkey FOREIGN KEY (recordid) "
            + "REFERENCES " + recordTableName + " (id)"};
    }

    public static final String TABLE_NAME = "actionloglikerecords";

}
/*
CREATE TABLE actionloglikerecords
(
  recordid integer NOT NULL,
  username character varying(20) NOT NULL,
  likedate timestamp without time zone NOT NULL,
  CONSTRAINT actionloglikerec_recid_fkey FOREIGN KEY (recordid)
      REFERENCES actionloggerrecords (id)
)
*/
