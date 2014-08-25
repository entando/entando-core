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
package com.agiletec.plugins.jacms.aps.system.services.resource.parse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * Classe JDOM per la scrittura di un oggetto tipo Resource in xml.
 * @author E.Santoboni
 */
public class ResourceDOM {
	
	/**
	 * Inizializza il documento.
	 */
	public ResourceDOM() {
		this.buildDOM();
	}
	
	/**
	 * Setta l'identificativo della risorsa.
	 * @param id L'identificativo della risorsa.
	 */
	public void setId(String id) {
		this._root.setAttribute("id", id);
	}
	
	/**
	 * Setta il tipo della risorsa.
	 * @param type Il tipo della risorsa.
	 */
	public void setTypeCode(String type) {
		this._root.setAttribute("typecode", type);
	}
	
	/**
	 * Setta la descrizione della risorsa.
	 * @param descr La descrizione della risorsa.
	 */
	public void setDescr(String descr) {
		this._root.getChild(TAG_DESCR).setText(descr);
	}
	
	/**
	 * Setta il gruppo principale di cui il contenuto èmembro.
	 * @param group Il gruppo principale.
	 */
	public void setMainGroup(String group) {
		this._root.getChild(TAG_GROUPS).setAttribute("mainGroup", group);
	}
	
	public void setMasterFileName(String masterFileName) {
		if (null == masterFileName) return;
		this._root.getChild(TAG_MASTER_FILE).setText(masterFileName);
	}
	
	/**
	 * Aggiunge una categoria alla risorsa.
	 * @param category La categoria da aggiungere.
	 */
	public void addCategory(String category) {
		Element tag = new Element("category");
		tag.setAttribute("id", category);
		this._root.getChild(TAG_CATEGORIES).addContent(tag);
	}
	
	/**
	 * Aggiunge un'oggetto elemento corrispondente all'istanza di rirsa da aggiungere.
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
	 * @return La stringa xml corrispondente alla risorsa.
	 */
	public String getXMLDocument() {
		XMLOutputter out = new XMLOutputter();
		String xml = out.outputString(this._doc);
		return xml;
	}
	
	private Document _doc;
	protected Element _root;
	
	private final static String ROOT = "resource";
	private final static String TAG_DESCR = "descr";
	private final static String TAG_GROUPS = "groups";
	private final static String TAG_CATEGORIES = "categories";
	private final static String TAG_MASTER_FILE = "masterfile";
	private final static String[] TAGS = {TAG_DESCR, TAG_GROUPS, TAG_CATEGORIES, TAG_MASTER_FILE};
	
}
