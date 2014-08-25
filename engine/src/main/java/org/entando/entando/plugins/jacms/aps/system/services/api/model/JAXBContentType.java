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

import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.common.entity.api.JAXBEntityType;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "contentType")
public class JAXBContentType extends JAXBEntityType {
    
    public JAXBContentType() {}
    
    public JAXBContentType(Content contentType) {
        super(contentType);
    }
    
    public Integer getDefaultModelId() {
        return _defaultModelId;
    }
    public void setDefaultModelId(Integer defaultModelId) {
        this._defaultModelId = defaultModelId;
    }
    
    public Integer getListModelId() {
        return _listModelId;
    }
    public void setListModelId(Integer listModelId) {
        this._listModelId = listModelId;
    }
    
    private Integer _defaultModelId;
    private Integer _listModelId;
    
}