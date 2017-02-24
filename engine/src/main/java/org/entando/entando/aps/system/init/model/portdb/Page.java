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

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Page.TABLE_NAME)
public class Page {
	
	public Page() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(columnName = "parentcode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _parentCode;
	
	@DatabaseField(columnName = "pos", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _position;
	
	@DatabaseField(columnName = "groupcode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _groupCode;
	
	public static final String TABLE_NAME = "pages";
	
}
/*
CREATE TABLE pages
(
  code character varying(30) NOT NULL,
  parentcode character varying(30),
  pos integer NOT NULL,
  groupcode character varying(30) NOT NULL,
  CONSTRAINT pages_pkey PRIMARY KEY (code )
)
 */
