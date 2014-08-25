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
import org.entando.entando.aps.system.init.model.portdb.Page;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ContentRelation.TABLE_NAME)
public class ContentRelation implements ExtendedColumnDefinition {
	
	public ContentRelation() {}
	
	@DatabaseField(foreign = true, columnName = "contentid", 
			width = 16, 
			canBeNull = false, index = true)
	private Content _content;
	
	@DatabaseField(foreign = true, columnName = "refpage", 
			width = 30)
	private Page _page;
	
	@DatabaseField(foreign = true, columnName = "refcontent", 
			width = 16)
	private Content _refContent;
	
	@DatabaseField(foreign = true, columnName = "refresource", 
			width = 16)
	private Resource _resource;
	
	@DatabaseField(foreign = true, columnName = "refcategory", 
			width = 30, index = true)
	private Category _category;
	
	@DatabaseField(columnName = "refgroup", 
			dataType = DataType.STRING, 
			width = 20, index = true)
	private String _group;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String contentTableName = Content.TABLE_NAME;
		String pageTableName = Page.TABLE_NAME;
		String resourceTableName = Resource.TABLE_NAME;
		String categoryTableName = Category.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + TABLE_NAME + "`";
			contentTableName = "`" + contentTableName + "`";
			pageTableName = "`" + pageTableName + "`";
			resourceTableName = "`" + resourceTableName + "`";
			categoryTableName = "`" + categoryTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_contid_fkey FOREIGN KEY (contentid) "
				+ "REFERENCES " + contentTableName + " (contentid)",
				"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_refcat_fkey FOREIGN KEY (refcategory) "
				+ "REFERENCES " + categoryTableName + " (catcode)",
				"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_refcont_fkey FOREIGN KEY (refcontent) "
				+ "REFERENCES " + contentTableName + " (contentid)",
				"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_refpage_fkey FOREIGN KEY (refpage) "
				+ "REFERENCES " + pageTableName + " (code)",
				"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_refres_fkey FOREIGN KEY (refresource) "
				+ "REFERENCES " + resourceTableName + " (resid)"};
	}
	
	public static final String TABLE_NAME = "contentrelations";
	
}
/*
CREATE TABLE contentrelations
(
  contentid character varying(16) NOT NULL,
  refpage character varying(30),
  refcontent character varying(16),
  refresource character varying(16),
  refcategory character varying(30),
  refgroup character varying(20),
  CONSTRAINT contentrelations_contentid_fkey FOREIGN KEY (contentid)
      REFERENCES contents (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT contentrelations_refcategory_fkey FOREIGN KEY (refcategory)
      REFERENCES categories (catcode) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT contentrelations_refcontent_fkey FOREIGN KEY (refcontent)
      REFERENCES contents (contentid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT contentrelations_refpage_fkey FOREIGN KEY (refpage)
      REFERENCES pages (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT contentrelations_refresource_fkey FOREIGN KEY (refresource)
      REFERENCES resources (resid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */