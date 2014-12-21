/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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