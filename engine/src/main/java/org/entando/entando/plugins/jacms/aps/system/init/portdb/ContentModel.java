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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = ContentModel.TABLE_NAME)
public class ContentModel {
	
	public ContentModel() {}
	
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
	
	public static final String TABLE_NAME = "contentmodels";
	
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