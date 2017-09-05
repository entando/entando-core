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

import java.util.Date;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = DataTypeSearch.TABLE_NAME)
public class DataTypeSearch implements ExtendedColumnDefinition {

	public DataTypeSearch() {
	}

	@DatabaseField(foreign = true, columnName = "contentid",
			width = 16,
			canBeNull = false, index = true)
	private DataTypeTable _contentId;

	@DatabaseField(columnName = "attrname",
			dataType = DataType.STRING,
			width = 30,
			canBeNull = false, index = true)
	private String _attributeName;

	@DatabaseField(columnName = "textvalue",
			dataType = DataType.STRING,
			canBeNull = true)
	private String _textValue;

	@DatabaseField(columnName = "datevalue",
			dataType = DataType.DATE,
			canBeNull = true)
	private Date _dateValue;

	@DatabaseField(columnName = "numvalue",
			dataType = DataType.INTEGER,
			canBeNull = true)
	private int _numberValue;

	@DatabaseField(columnName = "langcode",
			dataType = DataType.STRING,
			width = 3,
			canBeNull = true)
	private String _langCode;

	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String contentTableName = DataTypeTable.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			contentTableName = "`" + contentTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
			+ "ADD CONSTRAINT " + TABLE_NAME + "_contid_fkey FOREIGN KEY (contentid) "
			+ "REFERENCES " + contentTableName + " (contentid)"};
	}

	public static final String TABLE_NAME = "datatypesearch";

}
/*
CREATE TABLE contentsearch
(
  contentid character varying(16) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT contentsearch_contentid_fkey FOREIGN KEY (contentid)
      REFERENCES contents (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */
