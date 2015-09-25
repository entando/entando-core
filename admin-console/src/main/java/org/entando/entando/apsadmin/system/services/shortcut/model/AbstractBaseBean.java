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
package org.entando.entando.apsadmin.system.services.shortcut.model;

import java.io.Serializable;

/**
 * @author E.Santoboni
 */
public abstract class AbstractBaseBean implements Serializable {
	
	public AbstractBaseBean(String id) {
		this.setId(id);
	}
	
	public String getId() {
		return _id;
	}
	protected void setId(String id) {
		this._id = id;
	}
	
	public String getDescriptionKey() {
		return _descriptionKey;
	}
	public void setDescriptionKey(String descriptionKey) {
		this._descriptionKey = descriptionKey;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public String getLongDescription() {
		return _longDescription;
	}
	public void setLongDescription(String longDescription) {
		this._longDescription = longDescription;
	}
	
	public String getLongDescriptionKey() {
		return _longDescriptionKey;
	}
	public void setLongDescriptionKey(String longDescriptionKey) {
		this._longDescriptionKey = longDescriptionKey;
	}
	
	private String _id;
	private String _descriptionKey;
	private String _description;
	
	private String _longDescription;
	private String _longDescriptionKey;
	
}