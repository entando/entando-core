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
package com.agiletec.apsadmin.system.plugin;

/**
 * @author E.Santoboni
 */
public final class PluginSubMenuContainer {
	
	public String getSubMenuFilePath() {
		return _subMenuFilePath;
	}
	public void setSubMenuFilePath(String subMenuFilePath) {
		this._subMenuFilePath = subMenuFilePath;
	}
	
	public int getPriority() {
		return _priority;
	}
	public void setPriority(int priority) {
		this._priority = priority;
	}
	
	private String _subMenuFilePath;
	private int _priority;
	
}