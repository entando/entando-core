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
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.aps.system.common.entity.model.JAXBEntity;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBBooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBCompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBDateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBHypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBNumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBTextAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;

import org.entando.entando.aps.system.common.entity.model.attribute.JAXBEnumeratorMapAttribute;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "content")
@XmlType(propOrder = {"created", "lastModified", "version", "lastEditor"})
@XmlSeeAlso({ArrayList.class, HashMap.class, JAXBBooleanAttribute.class, JAXBEnumeratorMapAttribute.class, JAXBCompositeAttribute.class, JAXBDateAttribute.class, JAXBHypertextAttribute.class, JAXBListAttribute.class, JAXBNumberAttribute.class, JAXBTextAttribute.class, JAXBResourceAttribute.class, JAXBLinkAttribute.class, JAXBResourceValue.class, JAXBLinkValue.class, SymbolicLink.class})
public class JAXBContent extends JAXBEntity {
    
    public JAXBContent() {
        super();
    }
    
    public JAXBContent(Content mainContent, String langCode) {
        super(mainContent, langCode);
        this.setCreated(mainContent.getCreated());
        this.setLastModified(mainContent.getLastModified());
        this.setVersion(mainContent.getVersion());
        this.setLastEditor(mainContent.getLastEditor());
    }
    
    @XmlElement(name = "created", required = true)
    public Date getCreated() {
        return _created;
    }
    public void setCreated(Date created) {
        this._created = created;
    }

    @XmlElement(name = "lastModified", required = true)
    public Date getLastModified() {
        return _lastModified;
    }
    public void setLastModified(Date lastModified) {
        this._lastModified = lastModified;
    }

    @XmlElement(name = "version", required = true)
    public String getVersion() {
        return _version;
    }
    public void setVersion(String version) {
        this._version = version;
    }

    @XmlElement(name = "lastEditor", required = true)
    public String getLastEditor() {
        return _lastEditor;
    }
    public void setLastEditor(String lastEditor) {
        this._lastEditor = lastEditor;
    }
    
    private Date _created;
    private Date _lastModified;
    private String _version;
    private String _lastEditor;
    
}