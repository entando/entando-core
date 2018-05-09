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

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * This class implements a text attribute that must be the same for all the
 * languages in the system.
 *
 * @author W.Ambu
 */
public class MonoTextAttribute extends AbstractTextAttribute {

    /**
     * Return the text field to index.
     *
     * @return Returns The text field to index.
     */
    @Override
    public String getIndexeableFieldValue() {
        return this.getText();
    }

    /**
     * Return the text held by this attribute.
     *
     * @return Returns the text.
     */
    @Override
    public String getText() {
        if (null != this._text) {
            return this._text;
        }
        return "";
    }

    @Override
    public String getTextForLang(String langCode) {
        return this.getText();
    }

    @Override
    public void setText(String text, String langCode) {
        this.setText(text);
    }

    @Override
    public boolean needToConvertSpecialCharacter() {
        return true;
    }

    /**
     * Associate the given text to the attribute
     *
     * @param text The text to set.
     */
    public void setText(String text) {
        this._text = text;
    }

    @Override
    public boolean isSearchableOptionSupported() {
        return true;
    }

    @Override
    public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
        if (this.getText() != null) {
            List<AttributeSearchInfo> infos = new ArrayList<>();
            String text = this.getText();
            if (text != null && text.length() >= 255) {
                text = text.substring(0, 254);
            }
            AttributeSearchInfo info = new AttributeSearchInfo(text, null, null, null);
            infos.add(info);
            return infos;
        }
        return null;
    }

    @Override
    public Element getJDOMElement() {
        Element attributeElement = this.createRootElement("attribute");
        if (null != this.getText() && this.getText().trim().length() > 0) {
            Element monotextElement = new Element("monotext");
            monotextElement.setText(this.getText().trim());
            attributeElement.addContent(monotextElement);
        }
        return attributeElement;
    }

    @Override
    public Object getValue() {
        return this.getText();
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute, String langCode) {
        super.valueFrom(jaxbAttribute, langCode);
        this.setText(((JAXBTextAttribute) jaxbAttribute).getText());
    }

    @Override
    public Status getStatus() {
        if (null != this.getText() && this.getText().trim().length() > 0) {
            return Status.VALUED;
        }
        return Status.EMPTY;
    }

    private String _text;

}
