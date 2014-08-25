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

	private static final Logger _logger = LoggerFactory.getLogger(LangDOM.class);
	
	/**
	 * Costruttore base della classe dom.
	 */
	public LangDOM() {
		this._doc = new Document();
		Element elementRoot = new Element("Langs");
		this._doc.setRootElement(elementRoot);
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
		for (int i=0; i<langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
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
		_doc.getRootElement().addContent(langElement);
	}
	
	/**
	 * Restituisce la lista di lingue contenute nel documento.
	 * @return La lista di lingue contenute nel documento.
	 */
	public List<Lang> getLangs() {
		List<Lang> langs = new ArrayList<Lang>();
		List<Element> langElements = this._doc.getRootElement().getChildren();
		for (int i=0; i<langElements.size(); i++) {
			Element langElement = (Element) langElements.get(i);
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
		String xml = out.outputString(_doc);
		return xml;
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_doc = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error while parsing xml : {}", xmlText, t);
			throw new ApsSystemException("Error detected while parsing the XML", t);
		}
	}
	
	private Document _doc;

}
