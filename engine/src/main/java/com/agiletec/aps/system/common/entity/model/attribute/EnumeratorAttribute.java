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

import org.jdom.CDATA;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.util.EnumeratorAttributeItemsExtractor;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * This class describes an "Enumerator" Attribute.
 *
 * @author E.Santoboni
 */
public class EnumeratorAttribute extends MonoTextAttribute implements BeanFactoryAware {

    private static final Logger _logger = LoggerFactory.getLogger(EnumeratorAttribute.class);

    @Override
    public Object getAttributePrototype() {
        EnumeratorAttribute prototype = (EnumeratorAttribute) super.getAttributePrototype();
        prototype.setBeanFactory(this.getBeanFactory());
        prototype.setItems(this.getItems());
        prototype.setStaticItems(this.getStaticItems());
        prototype.setExtractorBeanName(this.getExtractorBeanName());
        prototype.setCustomSeparator(this.getCustomSeparator());
        return prototype;
    }

    @Override
    public void setAttributeConfig(Element attributeElement) throws ApsSystemException {
        super.setAttributeConfig(attributeElement);
        String separator = this.extractXmlAttribute(attributeElement, "separator", false);
        if (null == separator || separator.trim().length() == 0) {
            separator = DEFAULT_ITEM_SEPARATOR;
        }
        this.setCustomSeparator(separator);
        String text = attributeElement.getText();
        if (null != text) {
            this.setStaticItems(text.trim());
        }
        String extractorBeanName = this.extractXmlAttribute(attributeElement, "extractorBean", false);
        this.setExtractorBeanName(extractorBeanName);
        this.initItems();
    }

    protected void initItems() {
        if (null != this.getStaticItems() && this.getStaticItems().trim().length() > 0) {
            this.setItems(this.getStaticItems().split(this.getCustomSeparator()));
        }
        if (null != this.getExtractorBeanName()) {
            try {
                EnumeratorAttributeItemsExtractor extractor = this.getExtractorBean();
                if (null != extractor) {
                    List<String> items = extractor.getItems();
                    if (items != null && items.size() > 0) {
                        this.addExtractedItems(items);
                    }
                }
            } catch (Throwable t) {
                _logger.error("Error while extract items from bean extractor '{}'", this.getExtractorBeanName(), t);
            }
        }
    }

    protected EnumeratorAttributeItemsExtractor getExtractorBean() {
        if (null != this.getBeanFactory()) {
            return this.getBeanFactory().getBean(this.getExtractorBeanName(), EnumeratorAttributeItemsExtractor.class);
        } else {
            ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            return context.getBean(this.getExtractorBeanName(), EnumeratorAttributeItemsExtractor.class);
        }
    }

    @Override
    public Element getJDOMConfigElement() {
        Element configElement = super.getJDOMConfigElement();
        this.setConfig(configElement);
        return configElement;
    }

    private void setConfig(Element configElement) {
        if (null != this.getStaticItems()) {
            CDATA cdata = new CDATA(this.getStaticItems());
            configElement.addContent(cdata);
        }
        if (null != this.getExtractorBeanName()) {
            configElement.setAttribute("extractorBean", this.getExtractorBeanName());
        }
        if (null != this.getCustomSeparator()) {
            configElement.setAttribute("separator", this.getCustomSeparator());
        }
    }

    private void addExtractedItems(List<String> items) {
        String[] values = null;
        if (null == this.getItems() || this.getItems().length == 0) {
            values = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                values[i] = item;
            }
        } else {
            values = new String[this.getItems().length + items.size()];
            for (int i = 0; i < this.getItems().length; i++) {
                String item = this.getItems()[i];
                values[i] = item;
            }
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);
                values[i + this.getItems().length] = item;
            }
        }
        this.setItems(values);
    }

    @Override
    public JAXBEnumeratorAttributeType getJAXBAttributeType() {
        JAXBEnumeratorAttributeType jaxbAttribute = (JAXBEnumeratorAttributeType) super.getJAXBAttributeType();
        jaxbAttribute.setCustomSeparator(this.getCustomSeparator());
        jaxbAttribute.setExtractorBeanName(this.getExtractorBeanName());
        jaxbAttribute.setStaticItems(this.getStaticItems());
        return jaxbAttribute;
    }

    @Override
    protected DefaultJAXBAttributeType getJAXBAttributeTypeInstance() {
        return new JAXBEnumeratorAttributeType();
    }

    public String[] getItems() {
        return _items;
    }

    public void setItems(String[] items) {
        this._items = items;
    }

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
        if (null == this._customSeparator) {
            return DEFAULT_ITEM_SEPARATOR;
        }
        return _customSeparator;
    }

    public void setCustomSeparator(String customSeparator) {
        this._customSeparator = customSeparator;
    }

    protected BeanFactory getBeanFactory() {
        return this._beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this._beanFactory = beanFactory;
    }

    @Override
    public boolean isSearchableOptionSupported() {
        return true;
    }

    private String[] _items;
    private String _staticItems;
    private String _extractorBeanName;
    private String _customSeparator;
    private final String DEFAULT_ITEM_SEPARATOR = ",";

    private transient BeanFactory _beanFactory;

}
