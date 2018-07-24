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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.ILinkAttributeAction;

import java.util.Map;

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
	public void joinLink(String[] destinations, int destType, Map<String,String> properties, HttpServletRequest request);
	
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

    public static final String LINK_PROPERTIES_MAP_SESSION_PARAM = "linkPropertiesMap";

}