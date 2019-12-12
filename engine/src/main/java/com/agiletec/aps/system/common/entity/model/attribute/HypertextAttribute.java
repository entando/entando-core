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

import com.agiletec.aps.util.HtmlHandler;

import java.util.Iterator;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.CDATA;
import org.jdom.Element;
import org.apache.commons.lang.StringUtils;

/**
 * This class represents a 'Hypertext' Attribute.
 *
 * @author W.Ambu
 */
public class HypertextAttribute extends TextAttribute {

    @Override
    public boolean needToConvertSpecialCharacter() {
        return false;
    }

    /**
     * Return the field to index after having eventually removed the HTML tags.
     *
     * @return The text field to index
     */
    @Override
    public String getIndexeableFieldValue() {
        HtmlHandler htmlhandler = new HtmlHandler();
        String parsedText = htmlhandler.getParsedText(super.getText());
        return StringEscapeUtils.unescapeHtml(parsedText);
    }

    /**
     * Return the requested number of characters of the text associated to this
     * attribute, in the current language purged by the HTML tags, if any.
     *
     * @param n The number of characters to return
     * @return The string of text with the desired length.
     * @deprecated It might return less characters than requested. Use the
     * getHeadEscaped instead
     */
    public String getHead(int n) {
        HtmlHandler htmlhandler = new HtmlHandler();
        String parsedText = htmlhandler.getParsedText(super.getText());
        String head = parsedText;
        if (n < parsedText.length()) {
            while ((Character.isLetterOrDigit(parsedText.charAt(n)) || (parsedText.charAt(n) == ';')) && (n < parsedText.length())) {
                n++;
            }
            head = parsedText.substring(0, n);
        }
        return head;
    }

    /**
     * Return the requested number of characters rounded on word boundary of the
     * text associated to this attribute, in the current language, stripping
     * HTML tags, if any.
     *
     * @param n The minimum number of characters to return
     * @return The string of text with the desired length.
     */
    public String getEscapedHead(int n) {
        String parsedText = super.getText().replaceAll("<[^<>]+>", "").trim();
        String head = parsedText;

        if (n < parsedText.length()) {
            while ((Character.isLetterOrDigit(parsedText.charAt(n)) || (parsedText.charAt(n) == ';')) && (n < parsedText.length())) {
                n++;
            }
            head = parsedText.substring(0, n);
        }
        return head;
    }

    @Override
    public Element getJDOMElement() {
        Element attributeElement = this.createRootElement("attribute");
        Iterator<String> langIter = this.getTextMap().keySet().iterator();
        while (langIter.hasNext()) {
            String currentLangCode = langIter.next();
            String hypertext = (String) this.getTextMap().get(currentLangCode);
            if (null != hypertext && hypertext.trim().length() > 0) {
                Element hypertextElement = new Element("hypertext");
                hypertextElement.setAttribute("lang", currentLangCode);
                CDATA cdata = new CDATA(hypertext);
                hypertextElement.addContent(cdata);
                attributeElement.addContent(hypertextElement);
            }
        }
        return attributeElement;
    }

    @Override
    public boolean isSearchableOptionSupported() {
        return false;
    }

    @Override
    protected JAXBHypertextAttribute getJAXBAttributeInstance() {
        return new JAXBHypertextAttribute();
    }

    @Override
    public JAXBHypertextAttribute getJAXBAttribute(String langCode) {
        JAXBHypertextAttribute jaxbHypertexAttribute = (JAXBHypertextAttribute) super.createJAXBAttribute(langCode);
        if (null == jaxbHypertexAttribute) {
            return null;
        }
        String text = this.getTextForLang(langCode);
        if (StringUtils.isNotEmpty(text)) {
            jaxbHypertexAttribute.setHtmlValue(text);
        }
        return jaxbHypertexAttribute;
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute, String langCode) {
        JAXBHypertextAttribute jaxbHypertextAttribute = (JAXBHypertextAttribute) jaxbAttribute;
        String value = jaxbHypertextAttribute.getHtmlValue();
        if (null != value) {
            String langToSet = (null != langCode) ? langCode : this.getDefaultLangCode();
            this.getTextMap().put(langToSet, value);
        }
    }

}
