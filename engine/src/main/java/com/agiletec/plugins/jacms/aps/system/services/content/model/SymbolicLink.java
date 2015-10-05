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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Rappresenta un "link simbolico", che può rappresentare destinazioni di vario tipo,
 * interne ed esterne al portale, e le traduce in (o da) una stringa simbolica.
 * Modalità d'uso tipiche: se si desidera ottenere la stringa che rappresenta simbolicamente
 * una destinazione, si inizializza l'istanza con i metodi setDestinationToXXX, che impostano
 * il tipo e i parametri della destinazione, e successivamente si invoca il metodo
 * getSymbolicDestination. Se si desidera ottenere il tipo e i parametri di destinazione
 * a partire da una stringa simbolica (precedentemente ottenuta dalla stessa classe),
 * si inizializza l'istanza con il metodo setSymbolicDestination e si richiamano poi
 * i metodi getDestType, getPageDest, getContentId, getUrlDest, getResourceDest.
 * @author E.Santoboni - S.Didaci
 */
@XmlRootElement(name = "symbolicLink")
@XmlType(propOrder = {"contentDest", "pageDest", "resourceDest", "symbolicDestination"})
public class SymbolicLink implements Serializable {
	
	/**
	 * Imposta il link come "link a url". Riservato tipicamente a link esterni al portale 
	 * @param url L'URL completo della destinazione del link (esempio: "http;//www.google.com")
	 */
	public void setDestinationToUrl(String url) {
		this._destType = URL_TYPE;
		_urlDest = url;
	}
	
	/**
	 * Imposta il link come "link a pagina", interno al portale.
	 * @param pageCode Il codice della pagina di destinazione.
	 */
	public void setDestinationToPage(String pageCode){
		this._destType = PAGE_TYPE;
		_pageDest = pageCode;
	}
	
	/**
	 * Imposta il link come "link a contenuto", interno al portale.
	 * @param contentId Il codice del contenuto di destinazione.
	 */
	public void setDestinationToContent(String contentId){
		this._destType = CONTENT_TYPE;
		_contentDest = contentId;
	}
	
	/**
	 * Imposta il link come "link a contenuto su pagina specificata", interno al portale.
	 * @param contentId Il codice del contenuto di destinazione.
	 * @param pageCode Il codice della pagina di destinazione.
	 */
	public void setDestinationToContentOnPage(String contentId, String pageCode){
		this._destType = CONTENT_ON_PAGE_TYPE;
		_contentDest = contentId;
		_pageDest = pageCode;
	}
	
	public void setDestinationToResource(String resourceId){
		this._destType = RESOURCE_TYPE;
		this._resourceDest = resourceId;
	}

	/**
	 * Restituisce il tipo del link simbolico.
	 * @return Il tipo, una delle costanti dichiarate in questa classe.
	 */
	public int getDestType(){
		return _destType;
	}
	
	/**
	 * Restituisce l'identificativo del contenuto di destinazione. Il valore restituito
	 * è significativo nel caso che la destinazione impostata comprenda un contenuto.
	 * @return L'identificativo del contenuto di destinazione
	 */
	@XmlElement(name = "contentDestination", required = false)
	public String getContentDest(){
		return _contentDest;
	}

	/**
	 * Restituisce il codice della pagina di destinazione. Il valore restituito
	 * è significativo nel caso che la destinazione impostata comprenda una pagina.
	 * @return Il codice della pagina di destinazione.
	 */
	@XmlElement(name = "pageDestination", required = false)
	public String getPageDest(){
		return _pageDest;
	}

	/**
	 * Restituisce l'URL di destinazione. Il valore restituito
	 * è significativo nel caso che la destinazione impostata sia un URL.
	 * @return L'URL di destinazione. 
	 */
	public String getUrlDest(){
		return _urlDest;
	}
	
	@XmlElement(name = "resourceDestination", required = false)
	public String getResourceDest() {
		return _resourceDest;
	}
	
	/**
	 * Imposta la destinazione del link sulla destinazione specificata. 
	 * @param symbolicDestination Destinazione simbolica, ottenuta in precedenza
	 * tramite il corrispondente metodo get di questa stessa classe.
	 * @return True se la stringa simbolica è corretta, false se la stringa 
	 * simbolica è malformata.
	 */
	@XmlElement(name = "symbolicDestination", required = true)
	public boolean setSymbolicDestination(String symbolicDestination) {
		boolean ok = false;
		String params[] = this.extractParams(symbolicDestination);
		if (params != null){
			if (params[0].equals("U")) {
				if (params.length >= 2) {
					int length = symbolicDestination.length();
					String urlDest = symbolicDestination.substring(4, length-2);
					this.setDestinationToUrl(urlDest);
					ok = true;
				}
			} else if (params[0].equals("P")) {
				if (params.length == 2) {
					this.setDestinationToPage(params[1]);
					ok = true;
				}
			} else if (params[0].equals("C")) {
				ok = false;
				if (params.length == 2) {
					this.setDestinationToContent(params[1]);
					ok = true;
				}
			} else if (params[0].equals("O")) {
				ok = false;
				if (params.length == 3) {
					this.setDestinationToContentOnPage(params[1], params[2]);
					ok = true;
				} 
			} else if (params[0].equals("R")) {
				ok = false;
				if (params.length == 2) {
					this.setDestinationToResource(params[1]);
					ok = true;
				} 
			} else {
				ok = false;
			}
		}
		return ok;
	}
	
	/**
	 * Restituisce una stringa simbolica che rappresenta la destinazione precedentemente 
	 * impostata con uno dei metodi setDestinationToXXX. La stringa può essere successivamente 
	 * interpretata utilizzando il metodo setSymbolicDestination e richiedendo poi il tipo
	 * e le destinazioni del link tramite i metodi get.
	 * @return La strnga simbolica di destinazione.
	 */
	public String getSymbolicDestination(){
		StringBuilder dest = new StringBuilder();
		dest.append(SymbolicLink.SYMBOLIC_DEST_PREFIX);
		switch(_destType){
		case URL_TYPE:
			dest.append("U;").append(_urlDest);
			break;
		case PAGE_TYPE:
			dest.append("P;").append(this.getPageDest());
			break;
		case CONTENT_TYPE:
			dest.append("C;").append(this.getContentDest());
			break;
		case CONTENT_ON_PAGE_TYPE:
			dest.append("O;").append(this.getContentDest()).append(';').append(this.getPageDest());
			break;
		case RESOURCE_TYPE:
			dest.append("R;").append(this.getResourceDest());
			break;
		}
		dest.append(SymbolicLink.SYMBOLIC_DEST_POSTFIX);
		return dest.toString();
	}
	
	private String[] extractParams(String symbolicDestination) {
		String params[] = null;
		if (symbolicDestination.startsWith(SymbolicLink.SYMBOLIC_DEST_PREFIX)
				&& symbolicDestination.endsWith(SymbolicLink.SYMBOLIC_DEST_POSTFIX)){
			symbolicDestination = symbolicDestination.substring(2, symbolicDestination.length() - 2);
			params = symbolicDestination.split(";");
		}
		return params;
	}
	
	/**
	 * Restitusce i tipi destinazione; le chiavi dei tipi.
	 * @return I tipi delle destinazioni.
	 */
	public static int[] getDestinationTypes() {
		int[] types = {URL_TYPE, PAGE_TYPE, CONTENT_TYPE, CONTENT_ON_PAGE_TYPE, RESOURCE_TYPE};
		return types;
	}
	
	private int _destType;
	private String _pageDest;
	private String _contentDest;
	private String _urlDest;
	private String _resourceDest;
	
	/**
	 * Tipo di destinazione del link: URL esterno.
	 */
	public static final int URL_TYPE = 1;
	
	/**
	 * Tipo di destinazione del link: pagina del portale.
	 */
	public static final int PAGE_TYPE = 2;
	
	/**
	 * Tipo di destinazione del link: contenuto visualizzato sul portale.
	 */
	public static final int CONTENT_TYPE = 3;
	
	/**
	 * Tipo di destinazione del link: contenuto visualizzato su una pagina specifica del portale.
	 */
	public static final int CONTENT_ON_PAGE_TYPE = 4;
	
	/**
	 * Tipo di destinazione del link: Risorsa.
	 */
	public static final int RESOURCE_TYPE = 5;
	
	/**
	 * La stringa prefisso del link simbolico.
	 */
	public static final String SYMBOLIC_DEST_PREFIX = "#!";
	
	/**
	 * La stringa suffisso del link simbolico.
	 */
	public static final String SYMBOLIC_DEST_POSTFIX = "!#";
	
}
