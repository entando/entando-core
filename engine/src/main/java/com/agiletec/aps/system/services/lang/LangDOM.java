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
package com.agiletec.aps.system.services.lang;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe di supporto all'interpretazione dell'XML indicante le lingue del sistema.
 * La classe viene utilizzata anche per l'interpretazione dell'xml 
 * delle lingue "assegnabili" al sistema.
 * @author E.Santoboni
 */
public class LangDOM {

	private static final Logger logger = LoggerFactory.getLogger(LangDOM.class);
	
	private Document doc;
	
	/**
	 * Costruttore base della classe dom.
	 */
	public LangDOM() {
		this.doc = new Document();
		Element elementRoot = new Element("Langs");
		this.doc.setRootElement(elementRoot);
	}
	
	/**
	 * Costruttore della classe dom.
	 * @param xmlText L'xml delle lingue tramite inizializzare il cocumento.
	 * @throws ApsSystemException In caso di errore nell'interpretazione del documento.
	 */
	public LangDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	/**
	 * Aggiunge una lista di lingue al documento.
	 * @param langs La lista di lingue da aggiungere.
	 */
	public void addLangs(List<Lang> langs) {
		for (Lang lang : langs) {
			this.addLang(lang);
		}
	}
	
	private void addLang(Lang lang) {
		Element langElement = new Element("Lang");
		Element codeElement = new Element("code");
		codeElement.setText(lang.getCode());
		langElement.addContent(codeElement);
		Element descrElement = new Element("descr");
		descrElement.setText(lang.getDescr());
		langElement.addContent(descrElement);
		if (lang.isDefault()) {
			Element defaultElement = new Element("default");
			defaultElement.setText(new Boolean(lang.isDefault()).toString());
			langElement.addContent(defaultElement);
		}
		this.doc.getRootElement().addContent(langElement);
	}
	
	/**
	 * Restituisce la lista di lingue contenute nel documento.
	 * @return La lista di lingue contenute nel documento.
	 */
	public List<Lang> getLangs() {
		List<Lang> langs = new ArrayList<Lang>();
		List<Element> langElements = this.doc.getRootElement().getChildren();
		for (Element langElement : langElements) {
			Lang lang = new Lang();
			Element codeElement = langElement.getChild("code");
			if (null != codeElement) {
				String code = codeElement.getText();
				lang.setCode(code);
			}
			Element descrElement = langElement.getChild("descr");
			if (null != descrElement) {
				String descr = descrElement.getText();
				lang.setDescr(descr);
			}
			Element defaultElement = langElement.getChild("default");
			if (null != defaultElement) {
				String isDefaultString = defaultElement.getText();
				lang.setDefault(new Boolean(isDefaultString).booleanValue());
			} else {
				lang.setDefault(false);
			}
			langs.add(lang);
		}
		return langs;
	}
	
	/**
	 * Restutuisce l'xml del documento.
	 * @return L'xml del documento.
	 */
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		out.setFormat(format);
		String xml = out.outputString(this.doc);
		return xml;
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			this.doc = builder.build(reader);
		} catch (Throwable t) {
			logger.error("Error while parsing xml : {}", xmlText, t);
			throw new ApsSystemException("Error detected while parsing the XML", t);
		}
	}

}
