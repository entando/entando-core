/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
     * Return the id of the content published in the widget, if any.
     * @return The id of the published content 
     */
    public String getPublishedContent() {
        return _publishedContent;
    }
    
    /**
     * Set the id of the content to publish in the current widget.
     * @param publishedContent The id of the content to publish
     */
    public void setPublishedContent(String publishedContent) {
        this._publishedContent = publishedContent;
    }
	
	/**
	 * The type of the widget
	 */
	private WidgetType _type;
	
	/**
	 * The configuration properties; the configuration may be null
	 */
	private ApsProperties _config;
	
	/**
	 * id of the content published in this widget
	 */
	private String _publishedContent;
	
}
