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

import com.agiletec.aps.system.common.entity.model.attribute.JAXBTextAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import java.io.Serializable;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlType(propOrder = {"link"})
@XmlSeeAlso({JAXBLinkValue.class, SymbolicLink.class, HashMap.class})
public class JAXBLinkAttribute extends JAXBTextAttribute implements Serializable {
    
    @XmlElement(name = "link", required = false)
    public JAXBLinkValue getLink() {
        return _link;
    }
    public void setLink(JAXBLinkValue link) {
        this._link = link;
    }
    
    private JAXBLinkValue _link;
    
}