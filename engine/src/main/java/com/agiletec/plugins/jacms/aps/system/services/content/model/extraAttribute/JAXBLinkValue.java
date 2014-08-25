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
    public Object getText() {
        return _text;
    }
    public void setText(Object text) {
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
    
    private Object _text;
    private String _url;
    private SymbolicLink _symbolikLink;
    
}