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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "value")
@XmlType(propOrder = {"text", "url", "symbolikLink"})
@XmlSeeAlso({SymbolicLink.class, HashMap.class})
public class JAXBLinkValue implements Serializable {
    
    @XmlElement(name = "text", required = true)
    public String getText() {
        return _text;
    }
    public void setText(String text) {
        this._text = text;
    }
    
    @XmlElement(name = "url", required = true)
    public String getUrl() {
        return _url;
    }
    public void setUrl(String url) {
        this._url = url;
    }
    
    @XmlElement(name = "symbolikLink", required = false)
    public SymbolicLink getSymbolikLink() {
        return _symbolikLink;
    }
    public void setSymbolikLink(SymbolicLink symbolikLink) {
        this._symbolikLink = symbolikLink;
    }
    
    private String _text;
    private String _url;
    private SymbolicLink _symbolikLink;
    
}