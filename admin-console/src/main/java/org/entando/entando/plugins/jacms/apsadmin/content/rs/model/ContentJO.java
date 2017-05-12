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
package org.entando.entando.plugins.jacms.apsadmin.content.rs.model;

import java.util.Date;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;

public class ContentJO {

	public ContentJO(Content content, ContentRecordVO vo) {
		this.setDescription(content.getDescription());
		this.setAuthor(content.getLastEditor());
		this.setType(content.getTypeDescription());
		this.setOnline(content.isOnLine());
		this.setChanged(!vo.isSync());
		this.setLastModified(content.getLastModified());
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		this._description = description;
	}

	public String getAuthor() {
		return _author;
	}

	public void setAuthor(String author) {
		this._author = author;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		this._type = type;
	}

	public Date getLastModified() {
		return _lastModified;
	}

	public void setLastModified(Date lastModified) {
		this._lastModified = lastModified;
	}

	public Boolean getOnline() {
		return _online;
	}

	public void setOnline(Boolean online) {
		this._online = online;
	}

	public Boolean getChanged() {
		return _changed;
	}

	public void setChanged(Boolean changed) {
		this._changed = changed;
	}

	private String _description;
	private String _author;
	private String _type;
	private Date _lastModified;
	private Boolean _online;
	private Boolean _changed;

}
