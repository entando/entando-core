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
package com.agiletec.plugins.jacms.aps.system.services.resource.parse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe "handler" di supporto all'interpretazione dell'XML che rappresenta una
 * risorsa.
 *
 * @author W.Ambu - E.Santoboni
 */
public class ResourceHandler extends DefaultHandler {

    private boolean metadataTag = false;

    private ResourceInterface currentResource;
    private ResourceInstance currentInstance;
    private ICategoryManager categoryManager;
    private StringBuffer textBuffer;
    private Map<String, String> metadata = new HashMap<String, String>();

    private String metaDataKey = "";

    /**
     * Inizializzazione dell'handler.
     *
     * @param resource La risorsa prototipo da valorizzare.
     * @param categoryManager Il manager delle categorie.
     */
    public ResourceHandler(ResourceInterface resource, ICategoryManager categoryManager) {
        super();
        this.currentResource = resource;
        this.categoryManager = categoryManager;
    }

    @Override
    public void characters(char[] buf, int offset, int length) throws SAXException {
        String s = new String(buf, offset, length);
        if (this.textBuffer == null) {
            this.textBuffer = new StringBuffer(s);
        } else {
            this.textBuffer.append(s);
        }
    }

    /**
     * Sovrascrive il metodo omonimo dell'interfaccia ContentHandler.
     *
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.textBuffer = null;
        if (qName.equals("resource")) {
            this.defineResource(attributes, "resource");
        } else if (qName.equals("instance")) {
            this.defineInstance(attributes, "instance");
        } else if (qName.equals("groups")) {
            this.startGroups(attributes, qName);
        } else if (qName.equals("category")) {
            this.startCategory(attributes, "category");
        } else if (qName.equals("metadata-list")) {
            this.startMetadataList(attributes, "metadata-list");
        } else if (qName.equals("metadata")) {
            this.startMetadata(attributes, "metadata");
        }
    }

    /**
     * Sovrascrive il metodo omonimo dell'interfaccia ContentHandler.
     *
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (null != this.getTextBuffer()) {
            String text = this.getTextBuffer().toString();
            if (qName.equals("descr")) {
                this.getCurrentResource().setDescr(text.trim());
            } else if (qName.equals("masterfile")) {
                this.getCurrentResource().setMasterFileName(text.trim());
            } else if (qName.equals("groups")) {
                this.endGroups();
            } else if (qName.equals("category")) {
                this.endCategory();
            } else if (qName.equals("lang")) {
                this.currentInstance.setLangCode(text);
            } else if (qName.equals("size")) {
                this.currentInstance.setSize(Integer.parseInt(text.trim()));
            } else if (qName.equals("filename")) {
                this.currentInstance.setFileName(text);
            } else if (qName.equals("mimetype")) {
                this.currentInstance.setMimeType(text);
            } else if (qName.equals("weight")) {
                this.currentInstance.setFileLength(text);
            } else if (qName.equals("metadata")) {
                this.endMetadata();
            }
            this.textBuffer = null;
        }
        if (qName.equals("metadata-list")) {
            this.endMetadataList();
        } else if (qName.equals("instance")) {
            this.getCurrentResource().addInstance(currentInstance);
            currentInstance = null;
        }
    }

    private void defineInstance(Attributes attributes, String tagName) throws SAXException {
        this.currentInstance = new ResourceInstance();
    }

    private void defineResource(Attributes attributes, String tagName) throws SAXException {
        String id = this.extractXmlAttribute(attributes, "id", tagName, true);
        this.getCurrentResource().setId(id);
        String typecode = this.extractXmlAttribute(attributes, "typecode", tagName, true);
        this.getCurrentResource().setType(typecode);
    }

    /**
     * Recupera in modo controllato un attributo di un tag xml dall'insieme
     * degli attributi.
     *
     * @param attrs Attributi del tag xml
     * @param attributeName Nome dell'attributo richiesto
     * @param tagName Nome del tag xml
     * @param required Se true, l'attributo è considerato obbligatorio.
     * @return Il valore dell'attributo richiesto.
     * @throws SAXException Nel caso l'attributo sia dichiarato obbligatorio e
     * risulti assente.
     */
    protected String extractXmlAttribute(Attributes attrs, String attributeName,
            String tagName, boolean required) throws SAXException {
        int index = attrs.getIndex(attributeName);
        String value = attrs.getValue(index);
        if (required && value == null) {
            throw new SAXException("Attributo '" + attributeName + "' assente in tag <" + tagName + ">");
        }
        return value;
    }

    private void startGroups(Attributes attributes, String qName) throws SAXException {
        String mainGroup = this.extractXmlAttribute(attributes, "mainGroup", qName, true);
        this.getCurrentResource().setMainGroup(mainGroup);
    }

    private void endGroups() {
        return; // nulla da fare
    }

    private void startCategory(Attributes attributes, String tagName) throws SAXException {
        String categoryCode = extractXmlAttribute(attributes, "id", tagName, true);
        Category category = this.categoryManager.getCategory(categoryCode);
        if (null != category) {
            this.getCurrentResource().addCategory(category);
        }
    }

    private void endCategory() {
        return; // niente da fare
    }

    private void startMetadataList(Attributes attributes, String tagName) throws SAXException {
        setMetadataTag(true);
    }

    private void endMetadataList() {
        setMetadataTag(false);
        if (null != metadata) {
            this.getCurrentResource().setMetadata(metadata);
        }
    }

    private void startMetadata(Attributes attributes, String tagName) throws SAXException {
        if (isMetadataTag()) {
            String key = extractXmlAttribute(attributes, "id", tagName, true);
            this.metaDataKey = key;
        }

    }

    private void endMetadata() {
        if (null != this.getTextBuffer()) {
            String value = this.getTextBuffer().toString();
            metadata.put(metaDataKey, value);
        }
        metaDataKey = "";

    }

    protected ResourceInterface getCurrentResource() {
        return currentResource;
    }

    public StringBuffer getTextBuffer() {
        return textBuffer;
    }

    public void setTextBuffer(StringBuffer buffer) {
        this.textBuffer = buffer;
    }

    public boolean isMetadataTag() {
        return metadataTag;
    }

    public void setMetadataTag(boolean metadataTag) {
        this.metadataTag = metadataTag;
    }

}
