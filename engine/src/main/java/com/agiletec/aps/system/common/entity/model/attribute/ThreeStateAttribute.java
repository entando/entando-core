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

/**
 * This attribute represent an information of type Three State. This attribute
 * does not support multiple languages.
 *
 * @author E.Santoboni
 */
public class ThreeStateAttribute extends BooleanAttribute {

    @Override
    public Boolean getValue() {
        return super.getBooleanValue();
    }

    @Override
    protected boolean saveBooleanJDOMElement() {
        return (null != super.getBooleanValue());
    }

    @Override
    protected boolean addSearchInfo() {
        return (null != super.getBooleanValue());
    }

    @Override
    public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
        JAXBBooleanAttribute jaxbAttribute = (JAXBBooleanAttribute) super.createBaseJAXBAttribute();
        jaxbAttribute.setBoolean(this.getValue());
        return jaxbAttribute;
    }

}
