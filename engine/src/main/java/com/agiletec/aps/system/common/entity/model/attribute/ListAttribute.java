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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This class represents the Attribute of type "Multi-language List", composed by several
 * homogeneous attributes; there is a list for every language in the system.
 * @author M.Diana
 */
public class ListAttribute extends AbstractListAttribute {

	private static final Logger _logger = LoggerFactory.getLogger(ListAttribute.class);
	
	/**
     * Initialize the data structure.
     */
    public ListAttribute() {
        this._listMap = new HashMap<String, List<AttributeInterface>>();
    }

    /**
     * Add a new empty attribute to the list in the specified language.
     * @param langCode The code of the language.
     * @return The attribute added to the list, ready to be populated with
     * the data.
     */
    public AttributeInterface addAttribute(String langCode) {
        AttributeInterface newAttr = (AttributeInterface) this.getNestedAttributeType().getAttributePrototype();
        newAttr.setDefaultLangCode(this.getDefaultLangCode());
        newAttr.setParentEntity(this.getParentEntity());
        List<AttributeInterface> attrList = this.getAttributeList(langCode);
        attrList.add(newAttr);
        return newAttr;
    }

    /**
     * Return the list of attributes of the desired language.
     * @param langCode The language code.
     * @return A list of homogeneous attributes.
     */
    public List<AttributeInterface> getAttributeList(String langCode) {
        List<AttributeInterface> attrList = (List<AttributeInterface>) _listMap.get(langCode);
        if (attrList == null) {
            attrList = new ArrayList<AttributeInterface>();
            this._listMap.put(langCode, attrList);
        }
        return attrList;
    }

    /**
	 * Return the list of attributes in the current rendering language.
     * @return A list of homogeneous attributes.
     */
	@Override
    public Object getRenderingAttributes() {
        List<AttributeInterface> attrList = this.getAttributeList(this.getRenderingLang());
        return attrList;
    }

    /**
     * Remove from the list one of the attributes of the given language.
     * @param langCode The code of the language of the list where to delete the attribute from.
     * @param index The index of the attribute in the list.
     */
    public void removeAttribute(String langCode, int index) {
        List<AttributeInterface> attrList = this.getAttributeList(langCode);
        attrList.remove(index);
        if (attrList.isEmpty()) {
            this._listMap.remove(langCode);
        }
    }

    @Override
    public void setDefaultLangCode(String langCode) {
        super.setDefaultLangCode(langCode);
        Iterator<List<AttributeInterface>> values = this._listMap.values().iterator();
        while (values.hasNext()) {
            List<AttributeInterface> elementList = values.next();
            Iterator<AttributeInterface> attributes = elementList.iterator();
            while (attributes.hasNext()) {
                AttributeInterface attribute = attributes.next();
                attribute.setDefaultLangCode(langCode);
            }
        }
    }

    @Override
    public Element getJDOMElement() {
		Element listElement = this.createRootElement("list");
        listElement.setAttribute("nestedtype", this.getNestedAttributeTypeCode());
        Iterator<String> langIter = _listMap.keySet().iterator();
        while (langIter.hasNext()) {
            String langCode = langIter.next();
            Element listLangElement = new Element("listlang");
            if (null != langCode) {
                listLangElement.setAttribute("lang", langCode);
                List<AttributeInterface> attributeList = this.getAttributeList(langCode);
                Iterator<AttributeInterface> attributeListIter = attributeList.iterator();
                while (attributeListIter.hasNext()) {
                    AttributeInterface attribute = attributeListIter.next();
                    Element attributeElement = attribute.getJDOMElement();
                    listLangElement.addContent(attributeElement);
                }
                listElement.addContent(listLangElement);
            }
        }
        return listElement;
    }

    /**
     * Return a Map containing all the localized versions of the associated list.
     * @return A map indexed by the language code.
     */
    public Map<String, List<AttributeInterface>> getAttributeListMap() {
        return _listMap;
    }

    @Override
    public List<AttributeInterface> getAttributes() {
        List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
        Iterator<List<AttributeInterface>> values = this.getAttributeListMap().values().iterator();
        while (values.hasNext()) {
            attributes.addAll(values.next());
        }
        return attributes;
    }

    @Override
    public Object getValue() {
        return this.getAttributeListMap();
    }

    @Override
    protected Object getJAXBValue(String langCode) {
        if (null == langCode) {
            if (null == this.getAttributeListMap()) {
                return null;
            }
            Map<String, List<DefaultJAXBAttribute>> map = new HashMap<String, List<DefaultJAXBAttribute>>();
            Iterator<String> langCodesIter = this.getAttributeListMap().keySet().iterator();
            while (langCodesIter.hasNext()) {
                String listLangCode = langCodesIter.next();
                List<AttributeInterface> attributes = this.getAttributeListMap().get(listLangCode);
                List<DefaultJAXBAttribute> jaxrAttributes = this.extractJAXBListAttributes(attributes, langCode);
                map.put(listLangCode, jaxrAttributes);
            }
            return map;
        }
        List<AttributeInterface> attributes = this.getAttributeList(langCode);
        if (null == attributes) {
            return null;
        }
        return this.extractJAXBListAttributes(attributes, langCode);
    }
    
    private List<DefaultJAXBAttribute> extractJAXBListAttributes(List<AttributeInterface> attributes, String langCode) {
        List<DefaultJAXBAttribute> jaxrAttributes = new ArrayList<DefaultJAXBAttribute>();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface attribute = attributes.get(i);
            jaxrAttributes.add(attribute.getJAXBAttribute(langCode));
        }
        return jaxrAttributes;
    }
    
    @Override
    public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
        JAXBListAttribute jaxbListAttribute = (JAXBListAttribute) jaxbAttribute;
        if (null == jaxbListAttribute) return;
        List<DefaultJAXBAttribute> attributes = jaxbListAttribute.getAttributes();
        if (null == attributes) return;
        for (int i = 0; i < attributes.size(); i++) {
            DefaultJAXBAttribute jaxbAttributeElement = attributes.get(i);
            AttributeInterface attribute = this.addAttribute(this.getDefaultLangCode());
            attribute.valueFrom(jaxbAttributeElement);
        }
    }
    
	@Override
	public Status getStatus() {
		ILangManager langManager = (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
		Lang defaultLang = langManager.getDefaultLang();
		List<AttributeInterface> attributeList = this.getAttributeList(defaultLang.getCode());
		boolean valued = (null != attributeList && !attributeList.isEmpty());
		if (valued) {
			return Status.VALUED;
		} else {
			return Status.EMPTY;
		}
	}
    
    @Override
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            ILangManager langManager = (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
            List<Lang> langs = langManager.getLangs();
            for (int i = 0; i < langs.size(); i++) {
                Lang lang = langs.get(i);
                List<AttributeInterface> attributeList = this.getAttributeList(lang.getCode());
                for (int j = 0; j < attributeList.size(); j++) {
                    AttributeInterface attributeElement = attributeList.get(j);
                    AttributeTracer elementTracer = (AttributeTracer) tracer.clone();
                    elementTracer.setListElement(true);
                    elementTracer.setListLang(lang);
                    elementTracer.setListIndex(j);
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
            }
        } catch (Throwable t) {
            //ApsSystemUtils.logThrowable(t, this, "validate");
            _logger.error("Error validating list attribute", t);
            throw new RuntimeException("Error validating list attribute", t);
        }
        return errors;
    }
    
    private Map<String, List<AttributeInterface>> _listMap;
    
}
