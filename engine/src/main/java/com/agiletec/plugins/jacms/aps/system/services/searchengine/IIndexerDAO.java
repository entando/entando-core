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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.io.File;
import java.io.IOException;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * Data Access Object dedita alla indicizzazione di documenti.
 * @author W.Ambu
 */
public interface IIndexerDAO {
	
	/**
	 * Inizializzazione dell'indicizzatore.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @param newIndex true se è una nuova indicizzazione (ed in tal caso 
	 * cancella tutte le precedenti indicizzazioni), false in caso contrario.
	 * @throws IOException
	 */
	public void init(File dir, boolean newIndex) throws ApsSystemException;
	
	/**
	 * Aggiunge un contenuto nel db del motore di ricerca.
     * @param content Il contenuto da aggiungere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	public void add(IApsEntity entity) throws ApsSystemException;
	
	/**
     * Cancella un documento in base alla chiave mediante il quale è stato indicizzato.
     * @param name Il nome del campo Field da utilizzare per recupero del documento.
     * @param value La chiave mediante il quale 
     * è stato indicizzato il documento.
     * @throws ApsSystemException
     */
    public void delete(String name, String value) throws ApsSystemException;
    
    public void close();
	
	public void setLangManager(ILangManager langManager);
    
    public static final String CONTENT_ID_FIELD_NAME = "id";
    public static final String CONTENT_GROUP_FIELD_NAME = "group";
    
}