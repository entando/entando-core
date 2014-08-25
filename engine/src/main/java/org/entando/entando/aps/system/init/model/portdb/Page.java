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
@DatabaseTable(tableName = Page.TABLE_NAME)
public class Page implements ExtendedColumnDefinition {
	
	public Page() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(columnName = "parentcode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _parentCode;
	
	@DatabaseField(columnName = "pos", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _position;
	
	@DatabaseField(foreign = true, columnName = "modelcode", 
			width = 40, 
			canBeNull = false)
	private PageModel _model;
	
	@DatabaseField(columnName = "titles", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _titles;
	
	@DatabaseField(columnName = "groupcode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _groupCode;
	
	@DatabaseField(columnName = "showinmenu", 
			dataType = DataType.SHORT, 
			canBeNull = false)
	private short _showInMenu;
	
	@DatabaseField(columnName = "extraconfig", 
			dataType = DataType.LONG_STRING)
	private String _extraConfig;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String pageModelTableName = PageModel.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			pageModelTableName = "`" + pageModelTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_modelcode_fkey FOREIGN KEY (modelcode) "
				+ "REFERENCES " + pageModelTableName + " (code)"};
	}
	
	public static final String TABLE_NAME = "pages";
	
}
/*
CREATE TABLE pages
(
  code character varying(30) NOT NULL,
  parentcode character varying(30),
  pos integer NOT NULL,
  modelcode character varying(40) NOT NULL,
  titles character varying,
  groupcode character varying(30) NOT NULL,
  showinmenu smallint NOT NULL,
  extraconfig character varying,
  CONSTRAINT pages_pkey PRIMARY KEY (code ),
  CONSTRAINT pages_modelcode_fkey FOREIGN KEY (modelcode)
      REFERENCES pagemodels (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */
