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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.entando.entando.aps.system.services.widgettype.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;

/**
 * Representation of a frame of page template
 */
@XmlRootElement(name = "frame")
@XmlType(propOrder = {"pos", "description", "mainFrame", "jaxbDefaultWidget", "sketch"})
public class Frame implements Serializable {

	private int pos;
	private String description;
	private boolean mainFrame;
	private Widget defaultWidget;

	private JAXBDefaultWidget jaxbDefaultWidget;
	private FrameSketch sketch;

	private transient IWidgetTypeManager widgetTypeManager;

	@XmlElement(name = "code", required = true)
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "mainFrame", required = false)
	public boolean isMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(boolean mainFrame) {
		this.mainFrame = mainFrame;
	}

	@XmlTransient
	public Widget getDefaultWidget() {
		if (shouldUseDefaultWidget()) {
			defaultWidget = jaxbDefaultWidget.createDefaultWidget(widgetTypeManager);
		}

		jaxbDefaultWidget = null;
		widgetTypeManager = null;
		return defaultWidget;
	}

	private boolean shouldUseDefaultWidget() {
		return (defaultWidget == null) &&
			   (jaxbDefaultWidget != null) &&
			   (widgetTypeManager != null);
	}

	public void setDefaultWidget(Widget defaultWidget) {
		this.defaultWidget = defaultWidget;
	}

	@XmlElement(name = "defaultWidget", required = false)
	public JAXBDefaultWidget getJaxbDefaultWidget() {
		if (needToCreateNewJaxbDefaultWidget()) {
			jaxbDefaultWidget = new JAXBDefaultWidget(defaultWidget);
		}
		return jaxbDefaultWidget;
	}

	private boolean needToCreateNewJaxbDefaultWidget() {
		return (jaxbDefaultWidget == null) && (defaultWidget != null);
	}

	public void setJaxbDefaultWidget(JAXBDefaultWidget jaxbDefaultWidget) {
		this.jaxbDefaultWidget = jaxbDefaultWidget;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this.widgetTypeManager = widgetTypeManager;
	}

	@XmlElement(name = "sketch", required = false)
	public FrameSketch getSketch() {
		return sketch;
	}

	public void setSketch(FrameSketch sketch) {
		this.sketch = sketch;
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

	@XmlRootElement(name = "defaultWidget")
	@XmlType(propOrder = {"code", "properties"})
	public static class JAXBDefaultWidget implements Serializable {

		private String code;
		private Properties properties;

                public JAXBDefaultWidget() {
                }
                
		public JAXBDefaultWidget(Widget defaultWidget) {
			if (defaultWidget == null) {
				return;
			}
			WidgetType type = defaultWidget.getType();
			code = type.getCode();
			properties = defaultWidget.getConfig();
		}

		public Widget createDefaultWidget(IWidgetTypeManager widgetTypeManager) {
			WidgetType type = widgetTypeManager.getWidgetType(code);
			if (type == null) {
				return null;
			}
			Widget widget = new Widget();
			widget.setType(type);
			if (properties != null) {
				ApsProperties apsProperties = new ApsProperties(properties);
				widget.setConfig(apsProperties);
			}
			return widget;
		}

		@XmlElement(name = "code", required = true)
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@XmlElement(name = "configuration", required = false)
		public Properties getProperties() {
			return properties;
		}

		public void setProperties(Properties properties) {
			this.properties = properties;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Frame frame = (Frame) o;
		return pos == frame.pos &&
			   mainFrame == frame.mainFrame &&
			   Objects.equals(description, frame.description) &&
			   Objects.equals(defaultWidget, frame.defaultWidget) &&
			   Objects.equals(jaxbDefaultWidget, frame.jaxbDefaultWidget) &&
			   Objects.equals(sketch, frame.sketch) &&
			   Objects.equals(widgetTypeManager, frame.widgetTypeManager);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pos, description, mainFrame, defaultWidget, jaxbDefaultWidget, sketch, widgetTypeManager);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("pos", pos)
				.append("description", description)
				.append("mainFrame", mainFrame)
				.append("defaultWidget", defaultWidget)
				.append("jaxbDefaultWidget", jaxbDefaultWidget)
				.append("sketch", sketch)
				.append("widgetTypeManager", widgetTypeManager)
				.toString();
	}
}
