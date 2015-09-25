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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.Serializable;

import org.jdom.Element;

/**
 * Rappresenta uno dei file costituenti una risorsa.
 * @author W.Ambu - E.Santoboni
 */
public class ResourceInstance implements Serializable{
	
	/**
     * Restituisce la lunghezza del file relativo all'istanza.
     * @return La lunghezza del file relativo all'istanza.
     */
	public String getFileLength() {
		return _fileLength;
	}
	
	/**
     * Setta la lunghezza del file relativo all'istanza.
     * @param fileLength La lunghezza del file relativo all'istanza.
     */
	public void setFileLength(String fileLength) {
		this._fileLength = fileLength;
	}
	
	/**
     * Restituisce il filename relativo al file dell'istanza.
     * @return Il filename relativo al file dell'istanza.
     */
	public String getFileName() {
		return _fileName;
	}
	
	/**
     * Setta il filename relativo al file dell'istanza.
     * @param fileName Il filename relativo al file dell'istanza.
     */
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	/**
	 * Restituisce il codice della lingua relativa all'istanza.
	 * Questo codice viene utilizzato per indicizzare la mappa delle istanza 
	 * (di risorse componenti più di una istance) nel caso che la risorsa sia multilingua.
	 * @return Il codice della lingua relativa all'istanza.
	 */
	public String getLangCode() {
		return _langCode;
	}

	/**
	 * Setta il codice della lingua relativa all'istanza.
	 * Questo codice viene utilizzato per indicizzare la mappa delle istanza 
	 * (di risorse componenti più di una istance) nel caso che la risorsa sia multilingua.
	 * @param langCode Il codice della lingua relativa all'istanza.
	 */
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	/**
     * Restituisce il mimetype relativo all'istanza.
     * @return Il mimetype relativo all'istanza.
     */
	public String getMimeType() {
		return _mimeType;
	}
	
	/**
     * Setta il mimetype relativo all'istanza.
     * @param mimeType Il mimetype relativo all'istanza.
     */
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	
	/**
	 * Setta il size relativo all'istanza.
	 * Questo identificativo viene utilizzato per indicizzare la mappa delle istanza 
	 * (di risorse componenti più di una istanze) nel caso che la risorsa non sia multilingua.
	 * @return Il size relativo all'istanza.
	 */
	public int getSize() {
		return _size;
	}
	
	/**
	 * Setta il size relativo all'istanza.
	 * Questo identificativo viene utilizzato per indicizzare la mappa delle istanza 
	 * (di risorse componenti più di una istanze) nel caso che la risorsa non sia multilingua.
	 * @param size Il size relativo all'istanza.
	 */
	public void setSize(int size) {
		this._size = size;
	}
	
	/**
	 * Restituisce l'elemento JDOM completo relativo all'istanza.
	 * Tale elemento viene richiesto dall'oggetto risorsa per la costruzione 
	 * dell'xml completo della risorsa.
	 * @return L'elemento JDOM relativo all'istanza.
	 */
	public Element getJDOMElement() {
		Element element = new Element("instance");
		if (this.getSize() != -1) {
			element.addContent(this.getSingleElement("size", String.valueOf(this.getSize())));
		}
		if (null != this.getLangCode()) {
			element.addContent(this.getSingleElement("lang", this.getLangCode()));
		}
		element.addContent(this.getSingleElement("filename", this.getFileName()));
		element.addContent(this.getSingleElement("mimetype", this.getMimeType()));
		element.addContent(this.getSingleElement("weight", this.getFileLength()));
		return element;
	}
	
	private Element getSingleElement(String tagName, String text) {
		Element tag = new Element(tagName);
		tag.setText(text);
		return tag;
	}
	
	private int _size = -1;
	private String _langCode = null;
	private String _fileName;
	private String _fileLength;
	private String _mimeType;
	
}
