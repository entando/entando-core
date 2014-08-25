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
@DatabaseTable(tableName = PageModel.TABLE_NAME)
public class PageModel {
	
	public PageModel() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 50, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "frames", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _frame;
	
	@DatabaseField(columnName = "plugincode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = true)
	private String _pluginCode;
	
	@DatabaseField(columnName = "templategui", 
			dataType = DataType.LONG_STRING, 
			canBeNull = true)
	private String _templateGui;
	
	public static final String TABLE_NAME = "pagemodels";
	
}
/*
CREATE TABLE pagemodels
(
  code character varying(40) NOT NULL,
  descr character varying(50) NOT NULL,
  frames character varying,
  plugincode character varying(30),
  templategui text,
  CONSTRAINT pagemodels_pkey PRIMARY KEY (code )
)
 */
