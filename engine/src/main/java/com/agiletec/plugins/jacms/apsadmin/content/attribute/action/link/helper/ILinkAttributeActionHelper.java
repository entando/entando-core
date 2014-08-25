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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.ILinkAttributeAction;

/**
 * Interfaccia della classe helper base per le action delegata 
 * alla gestione delle operazione sugli attributi link.
 * @author E.Santoboni
 */
public interface ILinkAttributeActionHelper {
	
	/**
	 * Inizializza i parametri in sessione. Usato generalmente nella fase preliminare alle operazioni su un attributo Link.
	 * @param action L'azione corrente.
	 * @param request La request.
	 */
	public void initSessionParams(ILinkAttributeAction action, HttpServletRequest request);
	
	/**
	 * Aggiunge il link.
	 * @param destinations Un array contenente i parametri di destinazione del link.
	 * @param destType Il tipo di link di destinazione.
	 * @param request La request.
	 */
	public void joinLink(String[] destinations, int destType, HttpServletRequest request);
	
	/**
	 * Rimuove il link corrente.
	 * @param request La request.
	 */
	public void removeLink(HttpServletRequest request);
	
	/**
	 * Rimuove i parametri in sessione. Usato generalmente al termine delle operazioni su un attributo Link.
	 * @param session La sessione.
	 */
	public void removeSessionParams(HttpSession session);
	
	public String buildEntryContentAnchorDest(HttpSession session);
	
	public static final String ATTRIBUTE_NAME_SESSION_PARAM = "contentAttributeName";
	public static final String LINK_LANG_CODE_SESSION_PARAM = "linkLangCode";
	public static final String LIST_ELEMENT_INDEX_SESSION_PARAM = "listElementIndex";
	public static final String INCLUDED_ELEMENT_NAME_SESSION_PARAM = "includedElementName";
	
	public static final String SYMBOLIC_LINK_SESSION_PARAM = "symbolicLinkParamName";
	
}