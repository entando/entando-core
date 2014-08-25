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

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ContentAttributeRole.TABLE_NAME)
public class ContentAttributeRole implements ExtendedColumnDefinition {
	
	public ContentAttributeRole() {}
	
	@DatabaseField(foreign = true, columnName = "contentid", 
			width = 16, 
			canBeNull = false, index = true)
	private Content _contentId;
	
	@DatabaseField(columnName = "attrname", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, index = true)
	private String _attributeName;
	
	@DatabaseField(columnName = "rolename", 
			dataType = DataType.STRING, 
			width = 50, 
			canBeNull = false, index = true)
	private String _roleName;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String contentTableName = Content.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			contentTableName = "`" + Content.TABLE_NAME + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT contentattrroles_contid_fkey FOREIGN KEY (contentid) "
				+ "REFERENCES " + contentTableName + " (contentid)"};
	}
	
	public static final String TABLE_NAME = "contentattributeroles";
	
}