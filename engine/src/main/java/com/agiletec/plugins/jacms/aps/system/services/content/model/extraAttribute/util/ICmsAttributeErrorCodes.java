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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util;

/**
 * @author E.Santoboni
 */
public interface ICmsAttributeErrorCodes {
    
    /**
     * Codice messaggio : INVALID_PAGE - Link a Pagina referenziata non corretta.
     */
    public static final String INVALID_PAGE = "INVALID_PAGE";
    
    /**
     * Codice messaggio : VOID_PAGE - Link a Pagina referenziata vuota.
     */
    public static final String VOID_PAGE = "VOID_PAGE";
    
    /**
     * Codice messaggio : INVALID_PAGE_GROUPS - Link a Pagina referenziata con gruppo incompatibile.
     * L'errore corrisponde al caso in cui la pagina lincata appartiene ad un gruppo che la rende 
     * non accessibile ad uno solo dei gruppi (sia gruppo proprietario che extra) a cui è associato il contenuto.
     */
    public static final String INVALID_PAGE_GROUPS = "INVALID_PAGE_GROUPS";
    
    /**
     * Codice messaggio : INVALID_CONTENT - Link a Contenuto referenziato non corretto.
     */
    public static final String INVALID_CONTENT = "INVALID_CONTENT";
    
    /**
     * Codice messaggio : INVALID_CONTENT_GROUPS - Link a Contenuto referenziata con gruppi incompatibili.
     * L'errore corrisponde al caso in cui il contenuto lincato appartiene ad un gruppo che lo rende 
     * non accessibile ad uno solo dei gruppi (sia gruppo proprietario che extra) a cui è associato il contenuto principale.
     */
    public static final String INVALID_CONTENT_GROUPS = "INVALID_CONTENT_GROUPS";
    
    /**
     * Codice messaggio : INVALID_RESOURCE_GROUPS - Risorsa referenziata con gruppi incompatibili.
     * L'errore corrisponde al caso in cui la risorsa referenziata appartiene ad un gruppo che lo rende 
     * non accessibile ad uno solo dei gruppi (sia gruppo proprietario che extra) a cui è associato il contenuto principale.
     */
    public static final String INVALID_RESOURCE_GROUPS = "INVALID_RESOURCE_GROUPS";
    
}
