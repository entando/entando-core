/*
 * Copyright 2013-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.aps.system.init.portdb;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.model.portdb.Category;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ResourceRelation.TABLE_NAME)
public class ResourceRelation implements ExtendedColumnDefinition {
	
	public ResourceRelation() {}
	
	@DatabaseField(foreign = true, columnName = "resid", 
			width = 16, 
			canBeNull = false)
	private Resource _resource;
	
	@DatabaseField(foreign = true, columnName = "refcategory", 
			width = 30, 
			canBeNull = false)
	private Category _category;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String categoryTableName = Category.TABLE_NAME;
		String resourceTableName = Resource.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			categoryTableName = "`" + categoryTableName + "`";
			resourceTableName = "`" + resourceTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_refcat_fkey FOREIGN KEY (refcategory) "
				+ "REFERENCES " + categoryTableName + " (catcode)", 
			"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_resid_fkey FOREIGN KEY (resid) "
				+ "REFERENCES " + resourceTableName + " (resid)"};
	}
	
	public static final String TABLE_NAME = "resourcerelations";
	
}
/*
CREATE TABLE resourcerelations
(
  resid character varying(16) NOT NULL,
  refcategory character varying(30),
  CONSTRAINT resourcerelations_refcategory_fkey FOREIGN KEY (refcategory)
      REFERENCES categories (catcode) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT resourcerelations_resid_fkey FOREIGN KEY (resid)
      REFERENCES resources (resid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */