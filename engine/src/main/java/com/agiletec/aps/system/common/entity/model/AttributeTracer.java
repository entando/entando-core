/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
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
package com.agiletec.aps.system.common.entity.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This class implements the 'tracer' for the Entando Attributes. This class is
 * used, with the singles attributes, to trace the position inside any 'complex'
 * attributes. This class is involved during the update and validation process
 * of the Attribute and, furthermore, it guarantees the correct construction of
 * the form in the content edit interface.
 * @author E.Santoboni
 */
public class AttributeTracer {
    
	@Override
    public AttributeTracer clone() {
        AttributeTracer clone = null;
        try {
            Class attributeClass = Class.forName(this.getClass().getName());
            clone = (AttributeTracer) attributeClass.newInstance();
            clone.setMonoListElement(this._monoListElement);
            clone.setListElement(this._listElement);
            clone.setListLang(this._listLang);
            clone.setListIndex(this._listIndex);
            clone.setCompositeElement(this._compositeElement);
            clone.setParentAttribute(this._parentAttribute);
            clone.setLang(this._lang);
        } catch (Exception e) {
            throw new RuntimeException("Error cloning Attribute tracer", e);
        }
        return clone;
    }
    
    public AttributeTracer getMonoListElementTracer(int index) {
        AttributeTracer tracer = (AttributeTracer) this.clone();
        tracer.setMonoListElement(true);
        tracer.setListIndex(index);
        return tracer;
    }
    
    public AttributeTracer getListElementTracer(Lang lang, int index) {
        AttributeTracer tracer = (AttributeTracer) this.clone();
        tracer.setListElement(true);
        tracer.setListLang(lang);
        tracer.setListIndex(index);
        return tracer;
    }
    
    public AttributeTracer getCompositeTracer(AttributeInterface parentAttribute) {
        AttributeTracer tracer = (AttributeTracer) this.clone();
        tracer.setCompositeElement(true);
        tracer.setParentAttribute(parentAttribute);
        return tracer;
    }
    
    /**
     * Return the name of the field related to the given attribute.
     * The name is built considering the position of the attribute itself
     * tracked by the tracer.
     * @param attribute The attribute whose name must be returned.
     * @return the name of the field associated to the attribute. 
     */
    public String getFormFieldName(AttributeInterface attribute) {
        StringBuilder formFieldName = new StringBuilder();
		StringBuilder formFieldPrefix = new StringBuilder();
        String langModule = "";
        if (null != this.getLang() && attribute.isMultilingual()) {
            langModule = this.getLang().getCode() + "_";
        }
        if (this.isMonoListElement()) {
			formFieldPrefix.append("Monolist:");
            if (this.isCompositeElement()) {
				formFieldPrefix.append("Composite:");
                formFieldName.append(langModule).append(this.getParentAttribute().getName())
						.append("_").append(attribute.getName()).append("_").append(this.getListIndex());
            } else {
                formFieldName.append(langModule).append(attribute.getName())
						.append("_").append(this.getListIndex());
            }
			formFieldPrefix.append(attribute.getType()).append(":");
        } else if (this.isCompositeElement()) {
			formFieldPrefix.append("Composite:").append(attribute.getType()).append(":");
            formFieldName.append(langModule).append(this.getParentAttribute().getName())
					.append("_").append(attribute.getName());
        } else if (this.isListElement()) {
			formFieldPrefix.append("List:").append(attribute.getType()).append(":");
            formFieldName.append(this.getListLang().getCode())
					.append("_").append(attribute.getName()).append("_").append(this.getListIndex());
        } else {
			formFieldPrefix.append(attribute.getType()).append(":");
            formFieldName.append(langModule).append(attribute.getName());
        }
        return formFieldPrefix.toString() + formFieldName.toString();
    }
	
	public String getMonolistElementFieldName(AttributeInterface attribute) {
		if (!this.isMonoListElement()) {
			return null;
		}
		StringBuilder fieldName = new StringBuilder();
		fieldName.append("Monolist:").append(attribute.getType()).append(":");
		fieldName.append(attribute.getName()).append("_").append(this.getListIndex());
		return fieldName.toString();
	}
    
    public String getPositionMessage(AttributeInterface attribute) {
        StringBuilder buffer = new StringBuilder("Attribute ");
        if (this.isMonoListElement()) {
            if (this.isCompositeElement()) {
                buffer.append(this.getParentAttribute().getName())
                        .append(" - element ").append(String.valueOf(this.getListIndex() + 1))
                        .append(" - Included Attribute ").append(attribute.getName());
            } else {
                buffer.append(attribute.getName()).append(" - element ")
                        .append(String.valueOf(this.getListIndex() + 1));
            }
        } else if (this.isCompositeElement()) {
            buffer.append(this.getParentAttribute().getName())
                    .append(" - Included Attribute ").append(attribute.getName());
        } else if (this.isListElement()) {
            buffer.append(attribute.getName())
                    .append(" - lang ").append(this.getListLang().getDescr())
                    .append(" - element ").append(String.valueOf(this.getListIndex() + 1));
        } else {
            buffer.append(attribute.getName());
        }
        return buffer.toString();
    }
    
    public Lang getLang() {
        return _lang;
    }
    public void setLang(Lang lang) {
        this._lang = lang;
    }
    
    public boolean isCompositeElement() {
        return _compositeElement;
    }
    public void setCompositeElement(boolean compositeElement) {
        this._compositeElement = compositeElement;
    }
    
    public AttributeInterface getParentAttribute() {
        return _parentAttribute;
    }
    public void setParentAttribute(AttributeInterface parentAttribute) {
        this._parentAttribute = parentAttribute;
    }
    
    /**
     * Determine whether the attribute belongs to an element
     * of a 'monolist' Attribute or not.
     * @return true if the attribute belongs to a 'Monolist' Attribute, false otherwise.
     */
    public boolean isMonoListElement() {
        return _monoListElement;
    }
    
    /**
     * Set the membership of the attribute to a 'Monolist' Attribute.
     * @param monoListElement true if the element belongs to a 'Monolist' Attribute,
     * false otherwise. 
     */
    public void setMonoListElement(boolean monoListElement) {
        this._monoListElement = monoListElement;
    }
    
    /**
     * Determine whether the attribute belongs to a 'Multi-Language List' Attribute element
     * @return true if the element belongs to a 'Multi-Language List' Attribute, false otherwise.
     */
    public boolean isListElement() {
        return _listElement;
    }

    /**
     * Set the membership of the attribute to a 'Multi-Language List' Attribute element.
     * @param listElement set true if the attribute belongs to a 'Multi-Language List' Attribute element,
     * false otherwise.
     */
    public void setListElement(boolean listElement) {
        this._listElement = listElement;
    }
    
    /**
     * Return the language of the list when the current attribute is
     * an element of a 'Multi-language List' attribute.
     * @return The language of the list.
     */
    public Lang getListLang() {
        return _listLang;
    }
    
    /**
     * Set the language of the list when the current attribute is an element belonging to a 
     * multi-language list attribute.
     * @param lang The language of the list.
     */
    public void setListLang(Lang lang) {
        this._listLang = lang;
    }
    
    /**
     * Return the index of the position of the attribute within a multi-language
     * list or a monolist.
     * @return The index of the position of the attribute inside a list 
     */
    public int getListIndex() {
        return _listIndex;
    }
    
    /**
     * Setta l'indice relativo alla posizione dell'attributo elemento 
     * all'interno di una lista multilingua o di una monolista.
     * @param listIndex Set the index of the position of the attribute.
     */
    public void setListIndex(int listIndex) {
        this._listIndex = listIndex;
    }
    
    private Lang _lang;
    private boolean _monoListElement = false;
    private boolean _listElement = false;
    private Lang _listLang = null;
    private int _listIndex = -1;
    private boolean _compositeElement = false;
    private AttributeInterface _parentAttribute = null;
    
}
