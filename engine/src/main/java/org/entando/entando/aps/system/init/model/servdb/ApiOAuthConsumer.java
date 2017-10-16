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
@DatabaseTable(tableName = ApiOAuthConsumer.TABLE_NAME)
public class ApiOAuthConsumer {

	@DatabaseField(columnName = "clientid",
			dataType = DataType.STRING,
			width = 100,
			canBeNull = false, id = true)
	private String _clientId;

	public ApiOAuthConsumer() {}
	@DatabaseField(columnName = "consumerkey",
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _consumerKey;

	@DatabaseField(columnName = "consumersecret",
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _consumerSecret;

	@DatabaseField(columnName = "description",
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _description;


    @DatabaseField(columnName = "authorizationcode",
            dataType = DataType.STRING,
            canBeNull = false)
    private String _authorizationCode;

	@DatabaseField(columnName = "callbackurl",
			dataType = DataType.LONG_STRING)
	private String _callbackUrl;

	@DatabaseField(columnName = "expirationdate",
			dataType = DataType.DATE)
	private Date _expirationDate;

	@DatabaseField(columnName = "issueddate",
			dataType = DataType.DATE)
	private Date _issuedDate;


	public static final String TABLE_NAME = "api_oauth_consumers";
	
}
/*
CREATE TABLE api_oauth_consumers
(
  clientid character varying(100) NOT NULL,
  consumerkey character varying(100) NOT NULL,
  consumersecret character varying(100) NOT NULL,
  description character varying(500) NOT NULL,
  authorizationcode character varying(500) NOT NULL,
  callbackurl character varying(500),
  expirationdate date,
  issueddate date
  CONSTRAINT api_oauth_consumers_pkey PRIMARY KEY (clientid )
)
*/