/*
 * Copyright 2013-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ActionLogRecord.TABLE_NAME)
public class ActionLogRecord {
	
	public ActionLogRecord() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = true)
	private String _username;
	
	@DatabaseField(columnName = "actiondate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _actionDate;
	
	@DatabaseField(columnName = "namespace", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _namespace;
	
	@DatabaseField(columnName = "actionname", 
			dataType = DataType.STRING, 
			width = 250, 
			canBeNull = true)
	private String _actionname;
	
	@DatabaseField(columnName = "parameters", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _parameters;
	
	@DatabaseField(columnName = "activitystreaminfo", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _activitystreaminfo;
	
	
	@DatabaseField(columnName = "updatedate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _updateDate;
	
	public static final String TABLE_NAME = "actionlogrecords";
	
}
/*
CREATE TABLE actionloggerrecords
(
  id integer NOT NULL,
  username character varying(20),
  actiondate timestamp without time zone,
  namespace character varying,
  actionname character varying(255),
  parameters character varying,
  activitystreaminfo character varying,
  CONSTRAINT actionlogrecords_pkey PRIMARY KEY (id)
)
 */