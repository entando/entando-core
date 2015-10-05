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
package org.entando.entando.aps.system.services.api.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author E.Santoboni
 */
public abstract class AbstractApiResponseResult implements Serializable {
    
    public abstract Object getResult();
    
    protected Object getMainResult() {
        return this._mainResult;
    }
    public void setMainResult(Object mainResult) {
        this._mainResult = mainResult;
    }
    
    public void setHtml(String html) {
        if (null == html) {
            html = "";
        }
        this._html = html;
    }
    
	@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
    @XmlElement(name = "html", required = true)
    public String getHtml() {
         return this._html;
    }
    
    private Object _mainResult;
    
    private String _html;
    
}
