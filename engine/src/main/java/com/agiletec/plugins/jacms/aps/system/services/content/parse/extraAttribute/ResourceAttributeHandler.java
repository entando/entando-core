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
package com.agiletec.plugins.jacms.aps.system.services.content.parse.extraAttribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.attribute.TextAttributeHandler;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe handler per l'interpretazione della porzione di xml relativo
 * all'attributo di tipo risorsa (Image o Attach).
 *
 * @author E.Santoboni
 */
public class ResourceAttributeHandler extends TextAttributeHandler {

    private static final Logger _logger = LoggerFactory.getLogger(ResourceAttributeHandler.class);

    private transient IResourceManager resourceManager;

    @Override
    public Object getAttributeHandlerPrototype() {
        ResourceAttributeHandler handler = (ResourceAttributeHandler) super.getAttributeHandlerPrototype();
        handler.setResourceManager(this.getResourceManager());
        return handler;
    }

    @Override
    public void startAttribute(Attributes attributes, String qName) throws SAXException {
        if (qName.equals("resource")) {
            this.startResource(attributes, qName);
        } else if (qName.equals("alt")) {
            this.startResourceAlt(attributes, qName);
        } else if (qName.equals("description")) {
            this.startResourceDescription(attributes, qName);
        } else if (qName.equals("legend")) {
            this.startResourceLegend(attributes, qName);
        } else if (qName.equals("title")) {
            this.startResourceTitle(attributes, qName);
        } else {
            super.startAttribute(attributes, qName);
        }
    }

    private void startResource(Attributes attributes, String qName) throws SAXException {
        String id = extractAttribute(attributes, "id", qName, true);
        String langCode = extractAttribute(attributes, "lang", qName, false);
        try {
            ResourceInterface resource = this.getResourceManager().loadResource(id);
            if (null != this.getCurrentAttr() && null != resource) {
                ((ResourceAttributeInterface) this.getCurrentAttr()).setResource(resource, langCode);
            }
        } catch (Exception e) {
            _logger.error("Error loading resource {}", id, e);
        }
    }

    @Override
    public void endAttribute(String qName, StringBuffer textBuffer) {
        if (qName.equals("resource")) {
            this.endResource();
        } else if (qName.equals("alt")) {
            this.endResourceAlt(textBuffer);
        } else if (qName.equals("description")) {
            this.endResourceDescription(textBuffer);
        } else if (qName.equals("legend")) {
            this.endResourceLegend(textBuffer);
        } else if (qName.equals("title")) {
            this.endResourceTitle(textBuffer);
        } else {
            super.endAttribute(qName, textBuffer);
        }
    }

    private void endResource() {
        return; // nulla da fare
    }

    /**
     * Restituisce il manager delle risorse.
     *
     * @return Il Manager delle risorse.
     */
    protected IResourceManager getResourceManager() {
        return this.resourceManager;
    }

    /**
     * Setta il Manager delle risorse.
     *
     * @param resourceManager Il manager delle risorse.
     */
    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    private void startResourceAlt(Attributes attributes, String qName) throws SAXException {
        this.startResourceMetadata(attributes, qName);
    }

    private void startResourceDescription(Attributes attributes, String qName) throws SAXException {
        this.startResourceMetadata(attributes, qName);
    }

    private void startResourceLegend(Attributes attributes, String qName) throws SAXException {
        this.startResourceMetadata(attributes, qName);
    }

    private void startResourceTitle(Attributes attributes, String qName) throws SAXException {
        this.startResourceMetadata(attributes, qName);
    }

    protected void startResourceMetadata(Attributes attributes, String qName) throws SAXException {
        String idLang = this.extractAttribute(attributes, "lang", qName, true);
        this.setCurrentLangId(idLang);
    }

    private void endResourceAlt(StringBuffer textBuffer) {
        if (null != textBuffer && null != this.getCurrentAttr()) {
            ((ResourceAttributeInterface) this.getCurrentAttr()).setResourceAlt(textBuffer.toString(), this.getCurrentLangId());
        }
        this.setCurrentLangId(null);
    }

    private void endResourceDescription(StringBuffer textBuffer) {
        if (null != textBuffer && null != this.getCurrentAttr()) {
            ((ResourceAttributeInterface) this.getCurrentAttr()).setResourceDescription(textBuffer.toString(), this.getCurrentLangId());
        }
        this.setCurrentLangId(null);
    }

    private void endResourceLegend(StringBuffer textBuffer) {
        if (null != textBuffer && null != this.getCurrentAttr()) {
            ((ResourceAttributeInterface) this.getCurrentAttr()).setResourceLegend(textBuffer.toString(), this.getCurrentLangId());
        }
        this.setCurrentLangId(null);
    }

    private void endResourceTitle(StringBuffer textBuffer) {
        if (null != textBuffer && null != this.getCurrentAttr()) {
            ((ResourceAttributeInterface) this.getCurrentAttr()).setResourceTitle(textBuffer.toString(), this.getCurrentLangId());
        }
        this.setCurrentLangId(null);
    }

}
