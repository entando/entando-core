/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.page;

import java.io.Serializable;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.util.ApsProperties;

/**
 * This class represent an instance of a widget configured in a page frame. 
 * @author M.Diana - E.Santoboni
 */
public class Widget implements Serializable {

	/**
	 * Return the configuration of the widget
	 * @return The configuration properties
	 */
	public ApsProperties getConfig() {
		if (null == this._config && null != this.getType()) {
			return this.getType().getConfig();
		}
		return _config;
	}

	/**
	 * Set the configuration of the widget.
	 * @param config The configuration properties to set
	 */
	public void setConfig(ApsProperties config) {
		this._config = config;
	}
	
	/**
	 * Return the type of the widget
	 * @return The type of the widget
	 */
	public WidgetType getType() {
		return _type;
	}

	/**
	 * Set the widget type
	 * @param type The of the widget 
	 */
	public void setType(WidgetType type) {
		this._type = type;
	}
	
	/**
	 * The type of the widget
	 */
	private WidgetType _type;
	
	/**
	 * The configuration properties; the configuration may be null
	 */
	private ApsProperties _config;
	
}
