/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.common.entity.model.attribute;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.util.SelectItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.common.entity.model.attribute.util.EnumeratorMapAttributeItemsExtractor;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class describes an "EnumeratorMap" Attribute.
 * @author E.Santoboni
 */
public class EnumeratorMapAttribute extends EnumeratorAttribute {

	private static final Logger _logger =  LoggerFactory.getLogger(EnumeratorMapAttribute.class);
	
	@Override
    public Object getAttributePrototype() {
        EnumeratorMapAttribute prototype = (EnumeratorMapAttribute) super.getAttributePrototype();
        prototype.setMap(this.getMap());
        prototype.setMapItems(this.getMapItems());
        return prototype;
    }
	
	@Override
    protected void initItems() {
        if (null != this.getStaticItems() && this.getStaticItems().trim().length() > 0) {
			this.setMapItems(this.extractStaticItems());
        }
        if (null != this.getExtractorBeanName()) {
            try {
                EnumeratorMapAttributeItemsExtractor extractor = (EnumeratorMapAttributeItemsExtractor) this.getBeanFactory().getBean(this.getExtractorBeanName(), EnumeratorMapAttributeItemsExtractor.class);
                if (null != extractor) {
                    List<SelectItem> items = extractor.getMapItems();
                    if (items != null && items.size() > 0) {
                        this.addExtractedItems(items);
                    }
                }
            } catch (Throwable t) {
            	_logger.error("Error while extract items from bean extractor '{}'", this.getExtractorBeanName(), t);
            }
        }
		/*
        if (null != this.getMapItems()) {
            SelectItem[] items = new SelectItem[this.getMapItems().length];
            for (int i = 0; i < this.getMapItems().length; i++) {
                if (null != this.getMapItems()[i]) {
                    items[i] = this.getMapItems()[i];
                }
            }
            this.setMapItems(items);
        }
		*/
		if (null != this.getMapItems()) {
			for (int i = 0; i < this.getMapItems().length; i++) {
				if (null != this.getMapItems()[i]) {
                    SelectItem item = this.getMapItems()[i];
					this.getMap().put(item.getKey(), item.getValue());
                }
            }
		}
    }
    
    private void addExtractedItems(List<SelectItem> items) {
        SelectItem[] values = null;
        if (null == this.getMapItems() || this.getMapItems().length == 0) {
            values = new SelectItem[items.size()];
            for (int i = 0; i < items.size(); i++) {
                SelectItem item = items.get(i);
                values[i] = item;
            }
        } else {
            values = new SelectItem[this.getMapItems().length + items.size()];
            for (int i = 0; i < this.getMapItems().length; i++) {
                SelectItem item = this.getMapItems()[i];
                values[i] = item;
            }
            for (int i = 0; i < items.size(); i++) {
                SelectItem item = items.get(i);
                values[i + this.getMapItems().length] = item;
            }
        }
        this.setMapItems(values);
    }
	
	private SelectItem[] extractStaticItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		if (!StringUtils.isEmpty(this.getStaticItems())) {
			String[] entries = this.getStaticItems().split(this.getCustomSeparator());
			for (String entry : entries) {
				if (!StringUtils.isEmpty(entry) && entry.contains(DEFAULT_KEY_VALUE_SEPARATOR)) {
					String[] keyValue = entry.split(DEFAULT_KEY_VALUE_SEPARATOR);
					if (keyValue.length == 2) {
						items.add(new SelectItem(keyValue[0].trim(), keyValue[1].trim()));
					}
				}
			}
		}
		BeanComparator c = new BeanComparator("value");
		Collections.sort(items, c);
		SelectItem[] array = new SelectItem[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = items.get(i);
		}
		return array;
	}
    
    @Override
    public Element getJDOMElement() {
		Element rootElement = this.createRootElement("attribute");
        if (StringUtils.isNotEmpty(this.getMapKey())) {
            Element keyElement = new Element("key");
            keyElement.setText(this.getMapKey().trim());
            rootElement.addContent(keyElement);
            Element valueElement = new Element("value");
			if (StringUtils.isNotEmpty(this.getMapValue())) {
				valueElement.setText(this.getMapValue().trim());
			}
            rootElement.addContent(valueElement);
        }
        return rootElement;
    }
    
	public String getMapKey() {
		return super.getText();
	}
	public String getMapValue() {
		String key = this.getMapKey();
		if (StringUtils.isEmpty(key)) {
			return key;
		}
		String value = this.getMap().get(key);
		if (StringUtils.isEmpty(value)) {
			return "";
		}
		return value;
	}
    
	@Override
	protected AbstractJAXBAttribute getJAXBAttributeInstance() {
		return new JAXBEnumeratorMapAttribute();
	}
	
	@Override
	public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
		JAXBEnumeratorMapAttribute jaxbAttribute = (JAXBEnumeratorMapAttribute) super.createJAXBAttribute(langCode);
		if (null == jaxbAttribute) return null;
		JAXBEnumeratorMapValue value = new JAXBEnumeratorMapValue();
		value.setKey(this.getMapKey());
		value.setValue(this.getMapValue());
		jaxbAttribute.setMapValue(value);
		return jaxbAttribute;
	}
	
	@Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute) {
		if (null == jaxbAttribute) {
			return;
		}
		JAXBEnumeratorMapValue value = ((JAXBEnumeratorMapAttribute) jaxbAttribute).getMapValue();
		if (null != value) {
			this.setText(value.getValue());
		}
    }
	
	public SelectItem[] getMapItems() {
		return _mapItems;
	}
	public void setMapItems(SelectItem[] mapItems) {
		this._mapItems = mapItems;
	}
	
	protected Map<String, String> getMap() {
		return _map;
	}
	protected void setMap(Map<String, String> map) {
		this._map = map;
	}
    
    private SelectItem[] _mapItems;
    private Map<String, String> _map = new HashMap<String, String>();
    private final String DEFAULT_KEY_VALUE_SEPARATOR = "=";
	
}