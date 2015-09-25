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
package com.agiletec.plugins.jacms.apsadmin.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class ResourceIconUtil {
	
	/**
	 * Return an icon filename by resource filename
	 * @param fileName The file of resource
	 * @return The icon filename
	 * @deprecated use getIconByFilename(String filename)
	 */
	public String getIconFile(String fileName) {
		return this.getDefaultResourceIcon();
	}
	
	public String getIconByFilename(String filename) {
		String extension = filename.substring(filename.lastIndexOf('.')+1).trim();
		return this.getIconByExtension(extension);
	}
	
	public String getIconByExtension(String extension) {
		if (null != extension && extension.length()>0) {
			Iterator<String> iter = this.getResourceTypesIconFiles().keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				List<String> extensions = Arrays.asList(key.split(","));
				if (extensions.contains(extension)) {
					return this.getResourceTypesIconFiles().get(key);
				}
			}
		}
		return this.getDefaultResourceIcon();
	}
	
	protected Map<String, String> getResourceTypesIconFiles() {
		return _resourceTypesIconFiles;
	}
	public void setResourceTypesIconFiles(Map<String, String> resourceTypesIconFiles) {
		this._resourceTypesIconFiles = resourceTypesIconFiles;
	}
	
	protected String getDefaultResourceIcon() {
		return _defaultResourceIcon;
	}
	public void setDefaultResourceIcon(String defaultResourceIcon) {
		this._defaultResourceIcon = defaultResourceIcon;
	}
	
	private Map<String, String> _resourceTypesIconFiles;
	private String _defaultResourceIcon;
	
}