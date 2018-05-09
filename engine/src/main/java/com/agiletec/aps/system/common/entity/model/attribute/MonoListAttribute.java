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

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;

/**
 * This class implements a list of homogeneous attributes.
 *
 * @author M.Diana
 */
public class MonoListAttribute extends AbstractListAttribute {

    private static final Logger _logger = LoggerFactory.getLogger(MonoListAttribute.class);

    /**
     * Initialize the list of attributes.
     */
    public MonoListAttribute() {
        this._attributes = new ArrayList<>();
    }

    /**
     * Add a new empty attribute to the list. The attribute was previously
     * defined using the 'setNestedAttributeType' method.
     *
     * @return the empty attribute added to the list ready to be populated with
     * the data.
     */
    public AttributeInterface addAttribute() {
        AttributeInterface newAttr = (AttributeInterface) this.getNestedAttributeType().getAttributePrototype();
        newAttr.setDefaultLangCode(this.getDefaultLangCode());
        newAttr.setParentEntity(this.getParentEntity());
        this.getAttributes().add(newAttr);
        return newAttr;
    }

    /**
     * Return one of the elements in the list identified by the position.
     *
     * @param index The index, starting from 0, of the attribute to return.
     * @return The requested attribute.
     */
    public AttributeInterface getAttribute(int index) {
        return (AttributeInterface) this.getAttributes().get(index);
    }

    /**
     * Return the list of attributes contained in this object.
     *
     * @return A list of homogeneous attributes.
     */
    @Override
    public List<AttributeInterface> getAttributes() {
        return _attributes;
    }

    /**
     * Delete the attribute in list in the desired position
     *
     * @param index The position index of the attribute to remove.
     */
    public void removeAttribute(int index) {
        this.getAttributes().remove(index);
    }

    /**
     * Set up the language for the renderization. Note: the attributes in the
     * list may support several languages (depending on the attributes
     * themselves)
     *
     * @param langCode The language code for the rendering process.
     */
    @Override
    public void setRenderingLang(String langCode) {
        super.setRenderingLang(langCode);
        for (int i = 0; i < this.getAttributes().size(); i++) {
            AttributeInterface attribute = (AttributeInterface) this.getAttributes().get(i);
            attribute.setRenderingLang(langCode);
        }
    }

    /**
     * Return an Element that represents a list of homogeneous attributes, that
     * may be empty The list is the same for all the available languages, but it
     * may contain attributes which, in turn, my support several languages.
     *
     * @return The JDOM representing a list of homogeneous attributes, with none
     * or multiple elements.
     */
    @Override
    public Element getJDOMElement() {
        Element monolistElement = this.createRootElement("list");
        monolistElement.setAttribute("nestedtype", this.getNestedAttributeTypeCode());
        for (int i = 0; i < this.getAttributes().size(); i++) {
            AttributeInterface attribute = (AttributeInterface) this.getAttributes().get(i);
            Element attributeElement = attribute.getJDOMElement();
            monolistElement.addContent(attributeElement);
        }
        return monolistElement;
    }

    @Override
    public Object getRenderingAttributes() {
        if (this.getNestedAttributeType().isSimple()) {
            return this.getAttributes();
        } else {
            List<Object> attributes = new ArrayList<>();
            for (int i = 0; i < this.getAttributes().size(); i++) {
                AbstractComplexAttribute complexAttr = (AbstractComplexAttribute) this.getAttributes().get(i);
                attributes.add(complexAttr.getRenderingAttributes());
            }
            return attributes;
        }
    }

    @Override
    public Object getValue() {
        return this.getAttributes();
    }

    @Override
    public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
        JAXBListAttribute jaxbAttribute = (JAXBListAttribute) super.createJAXBAttribute(langCode);
        if (null == jaxbAttribute) {
            return null;
        }
        List<AttributeInterface> attributes = this.getAttributes();
        if (null == attributes) {
            return null;
        }
        List<AbstractJAXBAttribute> jaxbList = this.extractJAXBListAttributes(attributes, langCode);
        jaxbAttribute.setAttributes(jaxbList);
        return jaxbAttribute;
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute, String langCode) {
        JAXBListAttribute jaxbListAttribute = (JAXBListAttribute) jaxbAttribute;
        if (null == jaxbListAttribute) {
            return;
        }
        List<AbstractJAXBAttribute> attributes = jaxbListAttribute.getAttributes();
        if (null == attributes) {
            return;
        }
        for (int i = 0; i < attributes.size(); i++) {
            AbstractJAXBAttribute jaxbAttributeElement = attributes.get(i);
            AttributeInterface attribute = this.addAttribute();
            attribute.valueFrom(jaxbAttributeElement, langCode);
        }
    }

    @Override
    public Status getStatus() {
        List<AttributeInterface> attributes = this.getAttributes();
        if (null != attributes && attributes.size() > 0) {
            return Status.VALUED;
        }
        return Status.EMPTY;
    }

    @Override
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            List<AttributeInterface> attributes = this.getAttributes();
            for (int i = 0; i < attributes.size(); i++) {
                AttributeInterface attributeElement = attributes.get(i);
                AttributeTracer elementTracer = (AttributeTracer) tracer.clone();
                elementTracer.setListIndex(i);
                elementTracer.setMonoListElement(true);
                Status elementStatus = attributeElement.getStatus();
                if (elementStatus.equals(Status.EMPTY)) {
                    errors.add(new AttributeFieldError(attributeElement, FieldError.INVALID, elementTracer));
                } else {
                    List<AttributeFieldError> elementErrors = attributeElement.validate(elementTracer);
                    if (null != elementErrors) {
                        errors.addAll(elementErrors);
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("Error validating monolist attribute", t);
            throw new RuntimeException("Error validating monolist attribute", t);
        }
        return errors;
    }

    private List<AttributeInterface> _attributes;

}
