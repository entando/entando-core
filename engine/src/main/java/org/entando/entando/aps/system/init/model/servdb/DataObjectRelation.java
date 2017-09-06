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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = DataObjectRelation.TABLE_NAME)
public class DataObjectRelation /*implements ExtendedColumnDefinition*/ {

	public DataObjectRelation() {
	}

	@DatabaseField(foreign = true, columnName = "contentid",
			width = 16,
			canBeNull = false, index = true)
	private DataObject _content;

	@DatabaseField(columnName = "refcategory",
			dataType = DataType.STRING,
			width = 30, index = true)
	private String _category;

	@DatabaseField(columnName = "refgroup",
			dataType = DataType.STRING,
			width = 20, index = true)
	private String _group;
	/*
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String groupTableName = Group.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + TABLE_NAME + "`";
			groupTableName = "`" + groupTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
			+ "ADD CONSTRAINT " + TABLE_NAME + "_refgroup_fkey FOREIGN KEY (refgroup) "
			+ "REFERENCES " + groupTableName + " (groupname)"};
	}
	 */
	public static final String TABLE_NAME = "datatyperelations";

}
