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
package org.entando.entando.plugins.jacms.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Content.TABLE_NAME)
public class Content {
	
	public Content() {}
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String _contentId;
	
	@DatabaseField(columnName = "contenttype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _contentType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "status", 
			dataType = DataType.STRING, 
			width = 12, 
			canBeNull = false, index = true)
	private String _status;
	
	@DatabaseField(columnName = "workxml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _workXml;
	
	@DatabaseField(columnName = "created", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = true)
	private String _created;
	
	@DatabaseField(columnName = "lastmodified", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = true, index = true)
	private String _lastModified;
	
	@DatabaseField(columnName = "onlinexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _onlineXml;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = false, index = true)
	private String _mainGroup;
	
	@DatabaseField(columnName = "currentversion", 
			dataType = DataType.STRING, 
			width = 7, 
			canBeNull = false)
	private String _currentVersion;
	
	@DatabaseField(columnName = "lasteditor", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = true)
	private String _lastEditor;
	
	public static final String TABLE_NAME = "contents";
	
}
/*
CREATE TABLE contents
(
  contentid character varying(16) NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(260) NOT NULL,
  status character varying(12) NOT NULL,
  workxml character varying NOT NULL,
  created character varying(20),
  lastmodified character varying(20),
  onlinexml character varying,
  maingroup character varying(20) NOT NULL,
  currentversion character varying(7) NOT NULL,
  lasteditor character varying(40),
  CONSTRAINT contents_pkey PRIMARY KEY (contentid )
)
 */