/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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