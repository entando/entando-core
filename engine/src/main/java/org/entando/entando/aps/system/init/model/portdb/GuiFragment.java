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
package org.entando.entando.aps.system.init.model.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

@DatabaseTable(tableName = GuiFragment.TABLE_NAME)
public class GuiFragment implements ExtendedColumnDefinition {
	
	public GuiFragment() {}
	
	@DatabaseField(columnName = "code",
			width = 50,
			dataType = DataType.STRING,
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(foreign = true, columnName = "widgettypecode",
			width = 40,
			canBeNull = true)
	private WidgetCatalog _widgetType;
	
	@DatabaseField(columnName = "plugincode",
			dataType = DataType.STRING,
			width = 30, canBeNull = true)
	private String _pluginCode;
	
	@DatabaseField(columnName = "gui",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _gui;
	
	@DatabaseField(columnName = "defaultgui",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _defaultGui;
	
	@DatabaseField(columnName = "locked", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _locked;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String widgetCatalogTableName = WidgetCatalog.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			widgetCatalogTableName = "`" + widgetCatalogTableName + "`";
		}
		String[] queries = new String[1];
		queries[0] = "ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT " + TABLE_NAME + "_wdgtypecode_fkey FOREIGN KEY (widgettypecode) "
				+ "REFERENCES " + widgetCatalogTableName + " (code)";
		return queries;
	}
	
	public static final String TABLE_NAME = "guifragment";
	
}
