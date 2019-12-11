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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This attribute represent an information of type boolean. Obviously this
 * attribute does not support multiple languages.
 *
 * @author E.Santoboni
 */
public class BooleanAttribute extends AbstractAttribute {

    @Override
    public Element getJDOMElement() {
        Element attributeElement = this.createRootElement("attribute");
        if (this.saveBooleanJDOMElement()) {
            Element booleanElement = new Element("boolean");
            booleanElement.setText(this.getValue().toString());
            attributeElement.addContent(booleanElement);
        }
        return attributeElement;
    }

    protected boolean saveBooleanJDOMElement() {
        return true;
    }

    /**
     * Return the object characterizing the attribute.
     *
     * @return The boolean
     */
    @Override
    public Boolean getValue() {
        if (null != this._boolean) {
            return this._boolean.booleanValue();
        }
        return false;
    }

    /**
     * Return the object characterizing the attribute.
     *
     * @return The boolean
     */
    public Boolean getBooleanValue() {
        return _boolean;
    }

    /**
     * Set up the boolean for the current attribute
     *
     * @param booleanObject The boolean
     */
    public void setBooleanValue(Boolean booleanObject) {
        this._boolean = booleanObject;
    }

    @Override
    public boolean isSearchableOptionSupported() {
        return true;
    }

    @Override
    public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
        List<AttributeSearchInfo> infos = new ArrayList<>();
        if (this.addSearchInfo()) {
            AttributeSearchInfo info = new AttributeSearchInfo(String.valueOf(this.getValue()), null, null, null);
            infos.add(info);
        }
        return infos;
    }

    protected boolean addSearchInfo() {
        return true;
    }

    @Override
    public String getIndexingType() {
        return IndexableAttributeInterface.INDEXING_TYPE_NONE;
    }

    @Override
    protected AbstractJAXBAttribute getJAXBAttributeInstance() {
        return new JAXBBooleanAttribute();
    }

    @Override
    public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
        JAXBBooleanAttribute jaxbAttribute = (JAXBBooleanAttribute) super.createJAXBAttribute(langCode);
        if (null == jaxbAttribute) {
            return null;
        }
        jaxbAttribute.setBoolean(this.getValue());
        return jaxbAttribute;
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute, String langCode) {
        super.valueFrom(jaxbAttribute, langCode);
        Boolean value = ((JAXBBooleanAttribute) jaxbAttribute).getBoolean();
        if (null != value) {
            this.setBooleanValue(value);
        }
    }

    @Override
    public Status getStatus() {
        return Status.VALUED;
    }

    private Boolean _boolean;

}
