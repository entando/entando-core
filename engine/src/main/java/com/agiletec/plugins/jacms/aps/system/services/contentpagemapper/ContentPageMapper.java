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
package com.agiletec.plugins.jacms.aps.system.services.contentpagemapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresentazione della mappa dei contenuti pubblicati 
 * esplicitamene nelle pagine del portale.
 * @author W.Ambu
 */
public class ContentPageMapper {
	
	/**
	 * Inizializza la mappa.
	 */
    public ContentPageMapper() {
        this._contentPageMapper = new HashMap<String, String>();
    }
    
    /**
     * Aggiunge un contenuto nella mappa dei contenuti publicati.
     * @param contentId L'id del contenuto.
     * @param pageCode Il codice della pagina 
     * nel quale è publicato il contenuto.
     */
    protected void add(String contentId, String pageCode) {
		this._contentPageMapper.put(contentId, pageCode);
	}
    
    /**
     * Restituisce la mappa dei contenuti pubblicati.
     * @return La mappa dei contenuti pubblicati.
     */
    protected Map<String, String> getContentPageMapper() {
		return _contentPageMapper;
	}
    
    /**
     * Restituisce il codice pagina nel quale è stato publicato il contenuto specificato. 
     * Restituisce null nel caso in cui l'id del contenuto non sia presente nella mappa.
     * @param contentId L'id del contenuto.
     * @return Il codice pagina nel quale è stato publicato il contenuto specificato. 
     */
    protected String getPageCode(String contentId) {
		String pageCode = (String) _contentPageMapper.get(contentId);
		return pageCode;
	}
    
    /**
     * Rimuove un'elemento dalla mappa in base all'id del contenuto.
     * @param contentId L'id del contenuto.
     */
    protected void removeContentPageMapping(String contentId) {
		this._contentPageMapper.remove(contentId);
	}
    
    /**
     * Verifica se un contenuto è stato pubblicato esplicitamente in una pagina.
     * @param contentId L'id del contenuto.
     * @return true se un contenuto è stato pubblicato 
     * esplicitamente in una pagina, false in caso contrario.
     */
    protected boolean containContent(String contentId) {
		return this._contentPageMapper.containsKey(contentId);
	}
    
    private Map<String, String> _contentPageMapper;
    
}