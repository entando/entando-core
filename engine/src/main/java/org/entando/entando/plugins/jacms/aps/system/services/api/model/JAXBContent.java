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
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.agiletec.aps.system.common.entity.model.JAXBEntity;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBHypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBListAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "content")
@XmlType(propOrder = {"created", "lastModified", "version", "lastEditor"})
@XmlSeeAlso({ArrayList.class, HashMap.class, JAXBHypertextAttribute.class, JAXBListAttribute.class, JAXBResourceValue.class, JAXBLinkValue.class, SymbolicLink.class})
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