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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Sysconfig.TABLE_NAME)
public class Sysconfig implements ExtendedColumnDefinition {
	
	public Sysconfig() {}
	
	@DatabaseField(columnName = "version", 
			dataType = DataType.STRING, 
			width = 10, 
			canBeNull = false)
	private String _version;
	
	@DatabaseField(columnName = "item", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _item;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _descr;
	
	@DatabaseField(columnName = "config", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _config;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		return new String[]{"ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT " + TABLE_NAME + "_pkey PRIMARY KEY(version , item )"};
	}
	
	public static final String TABLE_NAME = "sysconfig";
	
}

/*
CREATE TABLE sysconfig
(
  version character varying(10) NOT NULL,
  item character varying(40) NOT NULL,
  descr character varying(100),
  config character varying,
  CONSTRAINT system_pkey PRIMARY KEY (version , item )
)
 */