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
import org.entando.entando.aps.system.init.model.portdb.Category;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = WorkContentRelation.TABLE_NAME)
public class WorkContentRelation implements ExtendedColumnDefinition {
	
	public WorkContentRelation() {}
	
	@DatabaseField(foreign = true, columnName = "contentid", 
			width = 16, 
			canBeNull = false, index = true)
	private Content _content;
	
	@DatabaseField(foreign = true, columnName = "refcategory", 
			width = 30, index = true)
	private Category _category;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String contentTableName = Content.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			contentTableName = "`" + contentTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_id_fkey FOREIGN KEY (contentid) "
				+ "REFERENCES " + contentTableName + " (contentid)"};
	}
	
	public static final String TABLE_NAME = "workcontentrelations";
	
}
/*
CREATE TABLE workcontentrelations
(
  contentid character varying(16) NOT NULL,
  refcategory character varying(30),
  CONSTRAINT workcontentrelations_contentid_fkey FOREIGN KEY (contentid)
      REFERENCES contents (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */