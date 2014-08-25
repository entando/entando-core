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

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.ApiException;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "attributeType")
public class JAXBEnumeratorAttributeType extends DefaultJAXBAttributeType {
    
    public String getStaticItems() {
        return _staticItems;
    }
    public void setStaticItems(String staticItems) {
        this._staticItems = staticItems;
    }
    
    public String getExtractorBeanName() {
        return _extractorBeanName;
    }
    public void setExtractorBeanName(String extractorBeanName) {
        this._extractorBeanName = extractorBeanName;
    }
    
    public String getCustomSeparator() {
        return _customSeparator;
    }
    public void setCustomSeparator(String customSeparator) {
        this._customSeparator = customSeparator;
    }
    
    @Override
    public AttributeInterface createAttribute(Map<String, AttributeInterface> attributes) throws ApiException {
        EnumeratorAttribute attribute = (EnumeratorAttribute) super.createAttribute(attributes);
        attribute.setStaticItems(this.getStaticItems());
        attribute.setExtractorBeanName(this.getExtractorBeanName());
        attribute.setCustomSeparator(this.getCustomSeparator());
        return attribute;
    }
    
    private String _staticItems;
    private String _extractorBeanName;
    private String _customSeparator;
    
}
