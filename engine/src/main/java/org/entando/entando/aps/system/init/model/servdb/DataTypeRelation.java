/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.init.model.servdb;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.model.portdb.Category;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = DataTypeRelation.TABLE_NAME)
public class DataTypeRelation implements ExtendedColumnDefinition {

	public DataTypeRelation() {
	}

	@DatabaseField(foreign = true, columnName = "contentid",
			width = 16,
			canBeNull = false, index = true)
	private DataTypeTable _content;

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
		String categoryTableName = Category.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + TABLE_NAME + "`";
			categoryTableName = "`" + categoryTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
			+ "ADD CONSTRAINT " + TABLE_NAME + "_refcat_fkey FOREIGN KEY (refcategory) "
			+ "REFERENCES " + categoryTableName + " (catcode)"};
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
