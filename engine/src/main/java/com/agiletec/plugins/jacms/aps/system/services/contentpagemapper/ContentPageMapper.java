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
package com.agiletec.plugins.jacms.aps.system.services.contentpagemapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresentazione della mappa dei contenuti pubblicati 
 * esplicitamene nelle pagine del portale.
 * @author W.Ambu
 */
public class ContentPageMapper {
	
	private Map<String, String> contentPageMapper;
	
	/**
	 * Inizializza la mappa.
	 */
    public ContentPageMapper() {
        this.contentPageMapper = new HashMap<String, String>();
    }
    
    /**
     * Aggiunge un contenuto nella mappa dei contenuti publicati.
     * @param contentId L'id del contenuto.
     * @param pageCode Il codice della pagina 
     * nel quale è publicato il contenuto.
     */
    public void add(String contentId, String pageCode) {
		this.contentPageMapper.put(contentId, pageCode);
	}
    
    /**
     * Restituisce la mappa dei contenuti pubblicati.
     * @return La mappa dei contenuti pubblicati.
     */
    protected Map<String, String> getContentPageMapper() {
		return contentPageMapper;
	}
    
    /**
     * Restituisce il codice pagina nel quale è stato publicato il contenuto specificato. 
     * Restituisce null nel caso in cui l'id del contenuto non sia presente nella mappa.
     * @param contentId L'id del contenuto.
     * @return Il codice pagina nel quale è stato publicato il contenuto specificato. 
     */
    public String getPageCode(String contentId) {
		return this.contentPageMapper.get(contentId);
	}
    
    /**
     * Rimuove un'elemento dalla mappa in base all'id del contenuto.
     * @param contentId L'id del contenuto.
     */
    protected void removeContentPageMapping(String contentId) {
		this.contentPageMapper.remove(contentId);
	}
    
    /**
     * Verifica se un contenuto è stato pubblicato esplicitamente in una pagina.
     * @param contentId L'id del contenuto.
     * @return true se un contenuto è stato pubblicato 
     * esplicitamente in una pagina, false in caso contrario.
     */
    protected boolean containContent(String contentId) {
		return this.contentPageMapper.containsKey(contentId);
	}
    
}