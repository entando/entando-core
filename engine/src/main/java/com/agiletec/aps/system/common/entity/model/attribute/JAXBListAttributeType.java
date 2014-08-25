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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "attributeType")
@XmlType(propOrder = {"nestedType"})
@XmlSeeAlso({DefaultJAXBAttributeType.class, JAXBCompositeAttributeType.class, JAXBEnumeratorAttributeType.class})
public class JAXBListAttributeType extends DefaultJAXBAttributeType {
    
    @Override
    public AttributeInterface createAttribute(Map<String, AttributeInterface> attributes) throws ApiException {
        AbstractListAttribute listAttribute = (AbstractListAttribute) super.createAttribute(attributes);
        DefaultJAXBAttributeType jaxbNestedType = this.getNestedType();
        if (null == jaxbNestedType) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "List Attribute '" + this.getName() + "' - Missing Nested Type");
        }
        AttributeInterface masterNestedType = attributes.get(jaxbNestedType.getType());
        if (null == masterNestedType) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "List Attribute '" + this.getName() + "' - Nested Type '" + jaxbNestedType.getType() + "' does not exist");
        }
        listAttribute.setNestedAttributeType(jaxbNestedType.createAttribute(attributes));
        return listAttribute;
    }
    
    public DefaultJAXBAttributeType getNestedType() {
        return _nestedType;
    }
    public void setNestedType(DefaultJAXBAttributeType nestedType) {
        this._nestedType = nestedType;
    }
    
    private DefaultJAXBAttributeType _nestedType;
    
}