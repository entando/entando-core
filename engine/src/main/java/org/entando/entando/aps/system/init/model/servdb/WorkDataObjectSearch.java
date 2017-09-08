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
@DatabaseTable(tableName = WorkDataObjectSearch.TABLE_NAME)
public class WorkDataObjectSearch implements ExtendedColumnDefinition {

	public WorkDataObjectSearch() {
	}

	@DatabaseField(foreign = true, columnName = "contentid",
			width = 16,
			canBeNull = false, index = true)
	private DataObject _contentId;

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
		String contentTableName = DataObject.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			contentTableName = "`" + DataObject.TABLE_NAME + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
			+ "ADD CONSTRAINT workdataobjsear_contid_fkey FOREIGN KEY (contentid) "
			+ "REFERENCES " + contentTableName + " (contentid)"};
	}

	public static final String TABLE_NAME = "workdataobjectsearch";

}
