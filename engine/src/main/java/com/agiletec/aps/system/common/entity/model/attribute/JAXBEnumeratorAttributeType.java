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
