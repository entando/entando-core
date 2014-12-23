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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "value")
@XmlType(propOrder = {"text", "path", "resourceId", "restResourcePath"})
@XmlSeeAlso({HashMap.class})
public class JAXBResourceValue implements Serializable {
    
    public Object getText() {
        return _text;
    }
    public void setText(Object text) {
        this._text = text;
    }
    
    public Object getPath() {
        return _path;
    }
    public void setPath(Object path) {
        this._path = path;
    }
    
    public Object getResourceId() {
        return _resourceId;
    }
    public void setResourceId(Object resourceId) {
        this._resourceId = resourceId;
    }
	
	public String getRestResourcePath() {
		return _restResourcePath;
	}
	public void setRestResourcePath(String restResourcePath) {
		this._restResourcePath = restResourcePath;
	}
    
    private Object _text;
    private Object _path;
    private Object _resourceId;
	private String _restResourcePath;
    
}