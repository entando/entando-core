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
package com.agiletec.aps.system.common.entity.model;

/**
 * This class represents an entity record.
 * @author E.Santoboni
 */
public class ApsEntityRecord {

	@Override
	public boolean equals(Object rec) {
		if (rec instanceof ApsEntityRecord) {
			return this.getId().equals(((ApsEntityRecord)rec).getId());
		}
		return super.equals(rec);
	}

	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	public String getXml() {
		return _xml;
	}
	public void setXml(String xml) {
		this._xml = xml;
	}

	private String _id;
	private String _typeCode;
	private String _xml;

}
