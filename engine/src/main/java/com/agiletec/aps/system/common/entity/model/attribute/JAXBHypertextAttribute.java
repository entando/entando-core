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
package com.agiletec.aps.system.common.entity.model.attribute;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.entando.entando.aps.system.services.api.model.CDataXmlTypeAdapter;

/**
 * @author E.Santoboni
 */
public class JAXBHypertextAttribute extends DefaultJAXBAttribute {
    
    @XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
    @XmlElement(name = "htmlValue", required = false)
    public String getHtmlValue() {
        return _htmlValue;
    }
    
    public void setHtmlValue(String htmlValue) {
        this._htmlValue = htmlValue;
    }
    
    private String _htmlValue;
    
}