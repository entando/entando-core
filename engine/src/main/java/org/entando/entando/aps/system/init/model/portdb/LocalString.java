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
@DatabaseTable(tableName = LocalString.TABLE_NAME)
public class LocalString implements ExtendedColumnDefinition {
	
	public LocalString() {}
	
	@DatabaseField(columnName = "keycode", 
			dataType = DataType.STRING, 
			width = 50, 
			canBeNull = false)
	private String _keyCode;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 2, 
			canBeNull = false)
	private String _langCode;
	
	@DatabaseField(columnName = "stringvalue", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _stringValue;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		return new String[]{"ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT " + TABLE_NAME + "_pkey PRIMARY KEY(keycode , langcode )"};
	}
	
	public static final String TABLE_NAME = "localstrings";
	
}
/*
CREATE TABLE localstrings
(
  keycode character varying(50) NOT NULL,
  langcode character varying(2) NOT NULL,
  stringvalue character varying NOT NULL,
  CONSTRAINT localstrings_pkey PRIMARY KEY (keycode , langcode )
)
 */