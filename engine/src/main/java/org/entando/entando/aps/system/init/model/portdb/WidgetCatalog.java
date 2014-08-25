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
package org.entando.entando.aps.system.init.model.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = WidgetCatalog.TABLE_NAME)
public class WidgetCatalog {
	
	public WidgetCatalog() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(columnName = "titles", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _titles;
	
	@DatabaseField(columnName = "parameters", 
			dataType = DataType.LONG_STRING)
	private String _parameters;
	
	@DatabaseField(columnName = "plugincode", 
			dataType = DataType.STRING, 
			width = 30)
	private String _pluginCode;
	
	@DatabaseField(columnName = "parenttypecode", 
			dataType = DataType.STRING, 
			width = 40)
	private String _parentTypeCode;
	
	@DatabaseField(columnName = "defaultconfig", 
			dataType = DataType.LONG_STRING)
	private String _defaultConfig;
	
	@DatabaseField(columnName = "locked", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _locked;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20)
	private String _mainGroup;
	
	public static final String TABLE_NAME = "widgetcatalog";
	
}
/*
CREATE TABLE widgetcatalog
(
  code character varying(40) NOT NULL,
  titles character varying NOT NULL,
  parameters character varying,
  plugincode character varying(30),
  parenttypecode character varying(40),
  defaultconfig character varying,
  locked smallint NOT NULL,
  maingroup character varying(20),
  CONSTRAINT showletcatalog_pkey PRIMARY KEY (code )
)
 */