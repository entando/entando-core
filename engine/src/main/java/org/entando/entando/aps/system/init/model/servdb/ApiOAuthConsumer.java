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
@DatabaseTable(tableName = ApiOAuthConsumer.TABLE_NAME)
public class ApiOAuthConsumer {
	
	public ApiOAuthConsumer() {}
	
	@DatabaseField(columnName = "consumerkey", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false, id = true)
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
	
	@DatabaseField(columnName = "callbackurl", 
			dataType = DataType.LONG_STRING)
	private String _callbackUrl;
	
	@DatabaseField(columnName = "expirationdate", 
			dataType = DataType.DATE)
	private Date _expirationDate;
	
	public static final String TABLE_NAME = "api_oauth_consumers";
	
}
/*
CREATE TABLE api_oauth_consumers
(
  consumerkey character varying(100) NOT NULL,
  consumersecret character varying(100) NOT NULL,
  description character varying(500) NOT NULL,
  callbackurl character varying(500),
  expirationdate date,
  CONSTRAINT api_oauth_consumers_pkey PRIMARY KEY (consumerkey )
)
*/