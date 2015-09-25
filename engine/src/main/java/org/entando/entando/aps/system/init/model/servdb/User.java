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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = User.TABLE_NAME)
public class User {
	
	public User() {}
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _username;
	
	@DatabaseField(columnName = "passwd", 
			dataType = DataType.STRING, 
			width = 40)
	private String _password;
	
	@DatabaseField(columnName = "registrationdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _registrationDate;
	
	@DatabaseField(columnName = "lastaccess", 
			dataType = DataType.DATE)
	private Date _lastAccess;
	
	@DatabaseField(columnName = "lastpasswordchange", 
			dataType = DataType.DATE)
	private Date _lastPasswordChange;
	
	@DatabaseField(columnName = "active", 
			dataType = DataType.SHORT)
	private short _active;
	
	public static final String TABLE_NAME = "authusers";
	
}
/*
CREATE TABLE authusers
(
  username character varying(40) NOT NULL,
  passwd character varying(40),
  registrationdate date NOT NULL,
  lastaccess date,
  lastpasswordchange date,
  active smallint,
  CONSTRAINT authusers_pkey PRIMARY KEY (username )
)
 */