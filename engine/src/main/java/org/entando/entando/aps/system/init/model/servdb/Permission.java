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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Permission.TABLE_NAME)
public class Permission {
	
	public Permission() {}
	
	@DatabaseField(columnName = "permissionname", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, id = true)
	private String _groupName;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			width = 50, 
			canBeNull = false)
	private String _description;
	
	public static final String TABLE_NAME = "authpermissions";
	
}
/*
CREATE TABLE authpermissions
(
  permissionname character varying(30) NOT NULL,
  descr character varying(50),
  CONSTRAINT authpermissions_pkey PRIMARY KEY (permissionname )
)
*/