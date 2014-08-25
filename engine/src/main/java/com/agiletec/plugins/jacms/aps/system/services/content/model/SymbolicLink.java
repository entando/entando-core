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
 * i metodi getDestType, getPageDest, getContentId, getUrlDest.
 * @author E.Santoboni-S.Didaci
 */
@XmlRootElement(name = "symbolicLink")
@XmlType(propOrder = {"contentDest", "pageDest", "symbolicDestination"})
public class SymbolicLink implements Serializable {
	
	/**
	 * Imposta il link come "link a url". Riservato tipicamente a link esterni al portale 
	 * @param url L'URL completo della destinazione del link (esempio: "http;//www.google.com")
	 */
	public void setDestinationToUrl(String url) {
		_destType = URL_TYPE;
		_urlDest = url;
	}
	
	/**
	 * Imposta il link come "link a pagina", interno al portale.
	 * @param pageCode Il codice della pagina di destinazione.
	 */
	public void setDestinationToPage(String pageCode){
		_destType = PAGE_TYPE;
		_pageDest = pageCode;
	}
	
	/**
	 * Imposta il link come "link a contenuto", interno al portale.
	 * @param contentId Il codice del contenuto di destinazione.
	 */
	public void setDestinationToContent(String contentId){
		_destType = CONTENT_TYPE;
		_contentDest = contentId;
	}
	
	/**
	 * Imposta il link come "link a contenuto su pagina specificata", interno al portale.
	 * @param contentId Il codice del contenuto di destinazione.
	 * @param pageCode Il codice della pagina di destinazione.
	 */
	public void setDestinationToContentOnPage(String contentId, String pageCode){
		_destType = CONTENT_ON_PAGE_TYPE;
		_contentDest = contentId;
		_pageDest = pageCode;
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
		StringBuffer dest = new StringBuffer();
		dest.append(SymbolicLink.SYMBOLIC_DEST_PREFIX);
		switch(_destType){
		case URL_TYPE:
			dest.append("U;").append(_urlDest);
			break;
		case PAGE_TYPE:
			dest.append("P;").append(_pageDest);
			break;
		case CONTENT_TYPE:
			dest.append("C;").append(_contentDest);
			break;
		case CONTENT_ON_PAGE_TYPE:
			dest.append("O;").append(_contentDest).append(';').append(_pageDest);
			break;
		}
		dest.append(SymbolicLink.SYMBOLIC_DEST_POSTFIX);
		return dest.toString();
	}

	private String[] extractParams(String symbolicDestination) {
		String params[] = null;
		if(symbolicDestination.startsWith(SymbolicLink.SYMBOLIC_DEST_PREFIX)
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
		int[] types = {URL_TYPE, PAGE_TYPE, CONTENT_TYPE, CONTENT_ON_PAGE_TYPE};
		return types;
	}
	
	private int _destType;
	private String _pageDest;
	private String _contentDest;
	private String _urlDest;
	
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
	 * La stringa prefisso del link simbolico.
	 */
	public static final String SYMBOLIC_DEST_PREFIX = "#!";
	
	/**
	 * La stringa suffisso del link simbolico.
	 */
	public static final String SYMBOLIC_DEST_POSTFIX = "!#";
	
}
