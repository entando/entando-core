/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import java.util.Properties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

/**
 * Representation of a frame of page model
 * @author E.Santoboni
 */
@XmlRootElement(name = "frame")
@XmlType(propOrder = {"pos", "description", "mainFrame", "jaxbDefaultWidget"})
public class Frame {
	
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
	
	@Override
	public Frame clone() {
		Frame clone = new Frame();
		clone.setDefaultWidget(this.getDefaultWidget());
		clone.setDescription(this.getDescription());
		clone.setMainFrame(this.isMainFrame());
		clone.setPos(this.getPos());
		return clone;
	}
	
	private int _pos;
	private String _description;
	private boolean _mainFrame;
	private Widget _defaultWidget;
	
	private JAXBDefaultWidget _jaxbDefaultWidget;
	
	@XmlTransient
	private IWidgetTypeManager _widgetTypeManager;
	
	@XmlRootElement(name = "defaultWidget")
	@XmlType(propOrder = {"code", "properties"})
	public static class JAXBDefaultWidget {
		
		public JAXBDefaultWidget() {}
		
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