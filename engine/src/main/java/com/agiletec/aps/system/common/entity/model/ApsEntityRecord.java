/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
