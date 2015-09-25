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
@DatabaseTable(tableName = ApiOAuthToken.TABLE_NAME)
public class ApiOAuthToken {
	
	public ApiOAuthToken() {}
	
	@DatabaseField(columnName = "accesstoken", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false, id = true)
	private String _accessToken;
	
	@DatabaseField(columnName = "tokensecret", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _tokenSecret;
	
	@DatabaseField(columnName = "consumerkey", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _consumerKey;
	
	@DatabaseField(columnName = "lastaccess", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _lastAccess;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	public static final String TABLE_NAME = "api_oauth_tokens";
	
}
/*
CREATE TABLE api_oauth_tokens
(
  accesstoken character(100) NOT NULL,
  tokensecret character varying(100) NOT NULL,
  consumerkey character varying(100) NOT NULL,
  lastaccess date NOT NULL,
  username character varying(40) NOT NULL,
  CONSTRAINT api_oauth_tokens_pkey PRIMARY KEY (accesstoken )
)
*/