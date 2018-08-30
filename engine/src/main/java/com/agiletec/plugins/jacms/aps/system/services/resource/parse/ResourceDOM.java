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

import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe JDOM per la scrittura di un oggetto tipo Resource in xml.
 *
 * @author E.Santoboni
 */
public class ResourceDOM {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private Document _doc;
    protected Element _root;

    public final static String ROOT = "resource";
    public final static String TAG_DESCR = "descr";
    public final static String TAG_GROUPS = "groups";
    public final static String TAG_CATEGORIES = "categories";
    public final static String TAG_MASTER_FILE = "masterfile";
    public final static String TAG_METADATA = "metadata-list";

    public final static String[] TAGS = {TAG_DESCR, TAG_GROUPS, TAG_CATEGORIES, TAG_MASTER_FILE, TAG_METADATA};

    /**
     * Inizializza il documento.
     */
    public ResourceDOM() {
        this.buildDOM();
    }

    /**
     * Setta l'identificativo della risorsa.
     *
     * @param id L'identificativo della risorsa.
     */
    public void setId(String id) {
        this._root.setAttribute("id", id);
    }

    /**
     * Setta il tipo della risorsa.
     *
     * @param type Il tipo della risorsa.
     */
    public void setTypeCode(String type) {
        this._root.setAttribute("typecode", type);
    }

    /**
     * Setta la descrizione della risorsa.
     *
     * @param descr La descrizione della risorsa.
     * @deprecated use setDescription
     */
    public void setDescr(String descr) {
        this.setDescription(descr);
    }

    public void setDescription(String description) {
        this._root.getChild(TAG_DESCR).setText(description);
    }

    /**
     * Setta il gruppo principale di cui il contenuto èmembro.
     *
     * @param group Il gruppo principale.
     */
    public void setMainGroup(String group) {
        this._root.getChild(TAG_GROUPS).setAttribute("mainGroup", group);
    }

    public void setMasterFileName(String masterFileName) {
        if (null == masterFileName) {
            return;
        }
        this._root.getChild(TAG_MASTER_FILE).setText(masterFileName);
    }

    /**
     * Aggiunge una categoria alla risorsa.
     *
     * @param category La categoria da aggiungere.
     */
    public void addCategory(String category) {
        Element tag = new Element("category");
        tag.setAttribute("id", category);
        this._root.getChild(TAG_CATEGORIES).addContent(tag);
    }

    /**
     * Aggiunge un'oggetto elemento corrispondente all'istanza di rirsa da
     * aggiungere.
     *
     * @param instance L'elemento istanza da aggiungere.
     */
    public void addInstance(Element instance) {
        this._root.addContent(instance);
    }

    private void buildDOM() {
        this._doc = new Document();
        this._root = new Element(ROOT);
        for (int i = 0; i < TAGS.length; i++) {
            Element tag = new Element(TAGS[i]);
            this._root.addContent(tag);
        }
        this._doc.setRootElement(this._root);
    }

    /**
     * Restituisce la stringa xml corrispondente alla risorsa.
     *
     * @return La stringa xml corrispondente alla risorsa.
     */
    public String getXMLDocument() {
        XMLOutputter out = new XMLOutputter();
        String xml = out.outputString(this._doc);
        return xml;
    }
    
    /**
     * Aggiunge i metadati alla risorsa.
     *
     * @param metadata il metadata da aggiungere.
     */
    public void addMetadata(Map<String, String> metadata) {
        if (null != metadata) {
            metadata.forEach((k, v) -> {
                Element metadataElement = new Element("metadata");
                metadataElement.setAttribute("id", k.trim());
                if (null != v) {
                    try {
                        metadataElement.setText(v.trim());
                    } catch (Exception ex) {
                        logger.error("error setting metadata value for id {}, set value to empty string {}", k.trim() , ex);
                        metadataElement.setText("");
                    }
                } else {
                    metadataElement.setText("");
                }
                this._root.getChild(TAG_METADATA).addContent(metadataElement);
            });
        }
    }
}
