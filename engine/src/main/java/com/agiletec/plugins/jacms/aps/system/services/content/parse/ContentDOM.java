/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.content.parse;

import java.util.Date;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.parse.ApsEntityDOM;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * Classe JDOM per la scrittura di un oggetto tipo Content in xml.
 * @author M.Morini - S.Didaci - E.Santoboni
 */
public class ContentDOM extends ApsEntityDOM {
	
	/**
	 * Setta lo stato del contenuto.
	 * @param status Lo stato del contenuto.
	 */
	public void setStatus(String status) {
		this.setAttribute(TAG_STATUS, status);
	}
	
	public void setVersion(String version) {
		this.setAttribute(TAG_VERSION, version);
	}
	
	public void setLastEditor(String lastEditor) {
		this.setAttribute(TAG_LAST_EDITOR, lastEditor);
	}
	
	public void setCreationDate(Date created) {
		if (null == created) return;
		String date = DateConverter.getFormattedDate(created, JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
		this.setAttribute(TAG_CREATED, date);
	}
	
	public void setModifyDate(Date lastModified) {
		if (null == lastModified) return;
		String date = DateConverter.getFormattedDate(lastModified, JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
		this.setAttribute(TAG_LAST_MODIFIED, date);
	}
	
	private void setAttribute(String name, String value) {
		if (null == value) return;
		if (this._root.getChild(name) == null) {
			Element tag = new Element(name);
			this._root.addContent(tag);
		}
		this._root.getChild(name).setText(value);
	}
	
	private final static String TAG_STATUS = "status";
	private final static String TAG_VERSION = "version";
	private final static String TAG_LAST_EDITOR = "lastEditor";
	private final static String TAG_CREATED = "created";
	private final static String TAG_LAST_MODIFIED = "lastModified";
	
}