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
@DatabaseTable(tableName = Category.TABLE_NAME)
public class Category {

	public Category() {}
	
	@DatabaseField(columnName = "catcode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, id = true)
	private String _categoryCode;
	
	@DatabaseField(columnName = "parentcode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _parentCode;
	
	@DatabaseField(columnName = "titles", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _titles;
	
	public static final String TABLE_NAME = "categories";
	
}
/*
CREATE TABLE categories
(
  catcode character varying(30) NOT NULL,
  parentcode character varying(30) NOT NULL,
  titles character varying NOT NULL,
  CONSTRAINT categories_pkey PRIMARY KEY (catcode )
)
 */