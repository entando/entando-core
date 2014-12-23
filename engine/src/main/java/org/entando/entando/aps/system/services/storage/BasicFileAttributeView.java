/*
 * Copyright 2013-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.storage;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class BasicFileAttributeView implements Comparable<BasicFileAttributeView>, Serializable {
	
	private static final Logger _logger = LoggerFactory.getLogger(BasicFileAttributeView.class);
	
	public BasicFileAttributeView() {}
	
	public BasicFileAttributeView(File file) {
		if (null == file) {
			_logger.warn("Null file");
			return;
		}
		this.setName(file.getName());
		this.setDirectory(file.isDirectory());
		Date lastModifiedTime = new Date(file.lastModified());
		this.setLastModifiedTime(lastModifiedTime);
		this.setSize(file.length());
	}
	
	@Override
	public int compareTo(BasicFileAttributeView other) {
		if (this.isDirectory() && !other.isDirectory()) {
			return -1;
		} else if (!this.isDirectory() && other.isDirectory()) {
			return +1;
		} else {
			return this.getName().compareTo(other.getName());
		}
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	
	public Date getLastModifiedTime() {
		return _lastModifiedTime;
	}
	public void setLastModifiedTime(Date lastModifiedTime) {
		this._lastModifiedTime = lastModifiedTime;
	}
	/*
	public Date getLastAccessTime() {
		return _lastAccessTime;
	}
	public void setLastAccessTime(Date lastAccessTime) {
		this._lastAccessTime = lastAccessTime;
	}
	
	public Date getCreationTime() {
		return _creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this._creationTime = creationTime;
	}
	*/
	public Long getSize() {
		return _size;
	}
	public void setSize(Long size) {
		this._size = size;
	}
	
	public Boolean isDirectory() {
		return _directory;
	}
	public void setDirectory(Boolean directory) {
		this._directory = directory;
	}
	
	private String _name;
	private Date _lastModifiedTime;
	//private Date _lastAccessTime;
	//private Date _creationTime;
	private Long _size;
	private Boolean _directory;
	
}
