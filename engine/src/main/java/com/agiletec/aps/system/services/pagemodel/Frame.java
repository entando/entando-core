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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import java.io.Serializable;
import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

/**
 * Representation of a frame of page model
 *
 * @author E.Santoboni
 */
@XmlRootElement(name = "frame")
@XmlType(propOrder = {"pos", "description", "mainFrame", "jaxbDefaultWidget", "sketch"})
public class Frame implements Serializable {

	@XmlElement(name = "code", required = true)
	public int getPos() {
		return _pos;
	}

	public void setPos(int pos) {
		this._pos = pos;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		this._description = description;
	}

	@XmlElement(name = "mainFrame", required = false)
	public boolean isMainFrame() {
		return _mainFrame;
	}

	public void setMainFrame(boolean mainFrame) {
		this._mainFrame = mainFrame;
	}

	@XmlTransient
	public Widget getDefaultWidget() {
		if (null == this._defaultWidget && null != this._jaxbDefaultWidget && null != this._widgetTypeManager) {
			this._defaultWidget = this._jaxbDefaultWidget.createDefaultWidget(this._widgetTypeManager);
		}
		this.setJaxbDefaultWidget(null);
		this.setWidgetTypeManager(null);
		return _defaultWidget;
	}

	public void setDefaultWidget(Widget defaultWidget) {
		this._defaultWidget = defaultWidget;
	}

	@XmlElement(name = "defaultWidget", required = false)
	public JAXBDefaultWidget getJaxbDefaultWidget() {
		if (null == this._jaxbDefaultWidget && null != this._defaultWidget) {
			this._jaxbDefaultWidget = new JAXBDefaultWidget(this._defaultWidget);
		}
		return _jaxbDefaultWidget;
	}

	public void setJaxbDefaultWidget(JAXBDefaultWidget jaxbDefaultWidget) {
		this._jaxbDefaultWidget = jaxbDefaultWidget;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	@XmlElement(name = "sketch", required = false)
	public FrameSketch getSketch() {
		return _sketch;
	}

	public void setSketch(FrameSketch sketch) {
		this._sketch = sketch;
	}

	@Override
	public Frame clone() {
		Frame clone = new Frame();
		clone.setDefaultWidget(this.getDefaultWidget());
		clone.setDescription(this.getDescription());
		clone.setMainFrame(this.isMainFrame());
		clone.setPos(this.getPos());
		clone.setSketch(this.getSketch());
		return clone;
	}

	private int _pos;
	private String _description;
	private boolean _mainFrame;
	private Widget _defaultWidget;

	private JAXBDefaultWidget _jaxbDefaultWidget;
	private FrameSketch _sketch;

	@XmlTransient
	private IWidgetTypeManager _widgetTypeManager;

	@XmlRootElement(name = "defaultWidget")
	@XmlType(propOrder = {"code", "properties"})
	public static class JAXBDefaultWidget implements Serializable {

		public JAXBDefaultWidget() {
		}

		public JAXBDefaultWidget(Widget defaultWidget) {
			if (null == defaultWidget) {
				return;
			}
			WidgetType type = defaultWidget.getType();
			this.setCode(type.getCode());
			this.setProperties(defaultWidget.getConfig());
		}

		public Widget createDefaultWidget(IWidgetTypeManager widgetTypeManager) {
			WidgetType type = widgetTypeManager.getWidgetType(this.getCode());
			if (null == type) {
				return null;
			}
			Widget widget = new Widget();
			widget.setType(type);
			if (null != this.getProperties()) {
				ApsProperties properties = new ApsProperties();
				properties.putAll(this.getProperties());
				widget.setConfig(properties);
			}
			return widget;
		}

		@XmlElement(name = "code", required = true)
		public String getCode() {
			return _code;
		}

		public void setCode(String code) {
			this._code = code;
		}

		@XmlElement(name = "configuration", required = false)
		public Properties getProperties() {
			return _properties;
		}

		public void setProperties(Properties properties) {
			this._properties = properties;
		}

		private String _code;
		private Properties _properties;

	}

}
