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

import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;

/**
 * Base abstract class for the complex attributes of type List and Monolist. 
 * @author E.Santoboni
 */
public abstract class AbstractListAttribute extends AbstractComplexAttribute 
		implements ListAttributeInterface {
    
    @Override
    public Object getAttributePrototype() {
        ListAttributeInterface clone = (ListAttributeInterface) super.getAttributePrototype();
        clone.setNestedAttributeType(this.getNestedAttributeType());
        return clone;
    }
    
	@Override
    public void setComplexAttributeConfig(Element attributeElement, Map<String, AttributeInterface> attrTypes) throws ApsSystemException {
        Element nestedTypeRootElement = attributeElement.getChild("nestedtype");
        if (null == nestedTypeRootElement) {
            this.setOldComplexAttributeConfig(attributeElement, attrTypes);
            return;
        }
        List<Element> nestedTypeAttributeElements = nestedTypeRootElement.getChildren();
        if (nestedTypeAttributeElements == null || nestedTypeAttributeElements.size() != 1) {
            throw new ApsSystemException("Wrong list attribute element detected: Wrong number of nested type in attribute list " + this.getName());
        }
        Element nestedTypeAttributeElement = nestedTypeAttributeElements.get(0);
        String nestedTypeCode = this.extractXmlAttribute(nestedTypeAttributeElement, "attributetype", true);
        AttributeInterface nestedType = (AttributeInterface) attrTypes.get(nestedTypeCode);
        if (nestedType == null) {
            throw new ApsSystemException("Wrong list attribute element detected: "
                    + nestedType + ", in attribute list " + this.getName());
        }
        nestedType = (AttributeInterface) nestedType.getAttributePrototype();
        nestedType.setAttributeConfig(nestedTypeAttributeElement);
        if (!nestedType.isSimple()) {
            ((AbstractComplexAttribute) nestedType).setComplexAttributeConfig(nestedTypeAttributeElement, attrTypes);
        }
		nestedType.setName(super.getName());
        this.setNestedAttributeType(nestedType);
    }
    
    @Deprecated(/** INSERTED to guaranted compatibility with previsous version of jAPS 2.0.12 */)
    private void setOldComplexAttributeConfig(Element attributeElement, Map<String, AttributeInterface> attrTypes) throws ApsSystemException {
        String nestedTypeCode = this.extractXmlAttribute(attributeElement, "nestedtype", true);
        AttributeInterface nestedType = (AttributeInterface) attrTypes.get(nestedTypeCode);
        if (nestedType == null) {
            throw new ApsSystemException("Wrong list attribute element detected: "
                    + nestedType + ", in attribute list " + this.getName());
        }
        nestedType = (AttributeInterface) nestedType.getAttributePrototype();
        nestedType.setAttributeConfig(attributeElement);
        if (!nestedType.isSimple()) {
            ((AbstractComplexAttribute) nestedType).setComplexAttributeConfig(attributeElement, attrTypes);
        }
        this.setNestedAttributeType(nestedType);
    }
    
    @Override
    public Element getJDOMConfigElement() {
        Element configElement = super.getJDOMConfigElement();
        Element nestedTypeRootElement = new Element("nestedtype");
        configElement.addContent(nestedTypeRootElement);
        Element nestedTypeElement = this.getNestedAttributeType().getJDOMConfigElement();
        nestedTypeRootElement.addContent(nestedTypeElement);
        return configElement;
    }
    
    @Override
    protected String getTypeConfigElementName() {
        return "list";
    }

    /**
     * Set up the attribute to utilize as prototype for the creation of the elements to add to
     * the list of attributes.
     * @param attribute The prototype attribute.
     */
    @Override
    public void setNestedAttributeType(AttributeInterface attribute) {
        this._nestedType = attribute;
    }

    /**
     * Return the attribute to utilize as prototype for the creation of the elements to add to
     * the list of attributes.
     * @return The prototype attribute.
     */
    protected AttributeInterface getNestedAttributeType() {
        return this._nestedType;
    }

    /**
     * Return the string which identifies the type of the attributes which will be 
     * held in this class.
     * @return The code of the Attribute Type.
     */
    @Override
    public String getNestedAttributeTypeCode() {
        return this.getNestedAttributeType().getType();
    }

    /**
     * This method overrides the method of the abstract class it derives from, because
     * this kind of attribute can never be "indexable" and so it always returns the constant 
     * 'INDEXING_TYPE_NONE', belonging to 'AttributeInterface', which declares it not searchable.
     * Declaring a complex attribute indexable will result in its constituting elements to be considered
     * as such.
     * @see com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface#getIndexingType()
     */
    @Override
    public String getIndexingType() {
        return IndexableAttributeInterface.INDEXING_TYPE_NONE;
    }
	
    /**
     * This method overrides the method of the abstract class it derives from, because
     * this kind of attribute can never be "searchable" and so it always return a 'false' value.
     * @return Return always false.
     * @see com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface#isSearchable()
     */
    @Override
    public boolean isSearchable() {
        return false;
    }
	
	@Override
	protected AbstractJAXBAttribute getJAXBAttributeInstance() {
		return new JAXBListAttribute();
	}
	
	protected List<AbstractJAXBAttribute> extractJAXBListAttributes(List<AttributeInterface> attributes, String langCode) {
        List<AbstractJAXBAttribute> jaxbAttributes = new ArrayList<AbstractJAXBAttribute>();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface attribute = attributes.get(i);
            jaxbAttributes.add(attribute.getJAXBAttribute(langCode));
        }
        return jaxbAttributes;
    }
	
    @Override
    public JAXBListAttributeType getJAXBAttributeType() {
        JAXBListAttributeType jaxbAttribute = (JAXBListAttributeType) super.getJAXBAttributeType();
        AttributeInterface nestedType = this.getNestedAttributeType();
        if (null != nestedType) {
            jaxbAttribute.setNestedType(nestedType.getJAXBAttributeType());
        }
        return jaxbAttribute;
    }
    
    @Override
    protected DefaultJAXBAttributeType getJAXBAttributeTypeInstance() {
        return new JAXBListAttributeType();
    }
    
    private AttributeInterface _nestedType;
    
}
