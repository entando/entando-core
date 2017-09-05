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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = DataTypeModel.TABLE_NAME)
public class DataTypeModel {

	public DataTypeModel() {
	}

	@DatabaseField(columnName = "modelid",
			dataType = DataType.INTEGER,
			canBeNull = false, id = true)
	private int _modelId;

	@DatabaseField(columnName = "contenttype",
			dataType = DataType.STRING,
			width = 30,
			canBeNull = false)
	private String _contentType;

	@DatabaseField(columnName = "descr",
			dataType = DataType.STRING,
			width = 50,
			canBeNull = false)
	private String _description;

	@DatabaseField(columnName = "model",
			dataType = DataType.LONG_STRING)
	private String _model;

	@DatabaseField(columnName = "stylesheet",
			dataType = DataType.STRING,
			width = 50)
	private String _styleSheet;

	public static final String TABLE_NAME = "datatypemodels";

}
/*
CREATE TABLE contentmodels
(
  modelid integer NOT NULL,
  contenttype character varying(30) NOT NULL,
  descr character varying(50) NOT NULL,
  model character varying,
  stylesheet character varying(50),
  CONSTRAINT contentmodels_pkey PRIMARY KEY (modelid )
)
 */
