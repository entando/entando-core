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

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Resource.TABLE_NAME)
public class Resource {
	
	public Resource() {}
	
	@DatabaseField(columnName = "resid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = false, id = true)
	private String _resourceId;
	
	@DatabaseField(columnName = "restype", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _resourceType;
	
	@DatabaseField(columnName = "descr", 
			dataType = DataType.STRING, 
			canBeNull = false)
	private String _description;
	
	@DatabaseField(columnName = "maingroup", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = false)
	private String _mainGroup;
	
	@DatabaseField(columnName = "resourcexml", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _resourceXml;
	
	@DatabaseField(columnName = "masterfilename", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _masterFileName;
	
	@DatabaseField(columnName = "creationdate", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _creationDate;
	
	@DatabaseField(columnName = "lastmodified", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _lastModified;
	
	public static final String TABLE_NAME = "resources";
	
}
/*
CREATE TABLE resources
(
  resid character varying(16) NOT NULL,
  restype character varying(30) NOT NULL,
  descr character varying(260) NOT NULL,
  maingroup character varying(20) NOT NULL,
  resourcexml character varying NOT NULL,
  masterfilename character varying(100) NOT NULL,
  creationdate date,
  lastmodified date,
  CONSTRAINT resources_pkey PRIMARY KEY (resid )
)
 */