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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi gestori della mappa 
 * dei contenuti pubblicati nelle pagine. 
 * @author W.Ambu
 */
public interface IContentPageMapperManager {
	
	/**
     * Effettua il caricamento della mappa contenuti pubblicati / pagine
     * @throws ApsSystemException
     */
    public void reloadContentPageMapper() throws ApsSystemException;
    
    /**
     * Restituisce il codice pagina nel quale è stato publicato il contenuto specificato. 
     * Restituisce null nel caso in cui l'id del contenuto non sia presente nella mappa.
     * @param contentId L'id del contenuto.
     * @return Il codice pagina nel quale è stato publicato il contenuto specificato. 
     */
    public String getPageCode(String contentId);
    
}