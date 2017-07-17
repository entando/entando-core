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
package org.entando.entando.aps.system.init.model.portdb;

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Mezzano
 */
@DatabaseTable(tableName = PageMetadataDraft.TABLE_NAME)
public class PageMetadataDraft extends AbstractPageMetadata implements ExtendedColumnDefinition {
	
	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}
	
	public static final String TABLE_NAME = "pages_metadata_draft";
	
}
