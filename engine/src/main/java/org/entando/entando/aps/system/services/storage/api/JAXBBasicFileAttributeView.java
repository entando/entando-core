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
package org.entando.entando.aps.system.services.storage.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.io.IOUtils;
import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@XmlRootElement(name = "basicFileAttributeView")
@XmlType(propOrder = {"name", "lastModifiedTime", "directory", "base64"})
public class JAXBBasicFileAttributeView implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(JAXBBasicFileAttributeView.class);

	public JAXBBasicFileAttributeView() {}

	public JAXBBasicFileAttributeView(BasicFileAttributeView file) {
		this.setName(file.getName());
		this.setLastModifiedTime(file.getLastModifiedTime());
		this.setDirectory(file.isDirectory());
	}

	public JAXBBasicFileAttributeView(BasicFileAttributeView file, InputStream inputStream) {
		this(file);
		try {
			if (null != inputStream) {
				this.setBase64(IOUtils.toByteArray(inputStream));
			} 
		}
		catch (IOException e) {
			_logger.error("Error converting inputstream");
			throw new RuntimeException(e);
		}
	}

	@XmlElement(name = "name", required = false)
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	@XmlElement(name = "lastModifiedTime", required = false)
	public Date getLastModifiedTime() {
		return _lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this._lastModifiedTime = lastModifiedTime;
	}

	@XmlElement(name = "directory", required = false)
	public Boolean getDirectory() {
		return _directory;
	}
	public void setDirectory(Boolean directory) {
		this._directory = directory;
	}

	@XmlElement(name = "base64", required = false)
	public byte[] getBase64() {
		return _base64;
	}
	public void setBase64(byte[] base64) {
		this._base64 = base64;
	}

	private String _name;
	private Date _lastModifiedTime;
	private Boolean _directory;
	private byte[] _base64;

}