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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

/**
 * Interfaccia base per i Manager dei contenuti.
 * @author M.Diana - E.Santoboni
 */
public interface IContentManager extends IEntityManager {
    
    /**
     * Crea una nuova istanza di un contenuto del tipo richiesto. Il nuovo contenuto
     * è istanziato mediante clonazione del prototipo corrispondente.
     * @param typeCode Il codice del tipo di contenuto richiesto, come definito in configurazione.
     * @return Il contenuto istanziato (vuoto).
     */
    public Content createContentType(String typeCode);
    
    /**
     * Return a list of the of the content types in a 'small form'. 'Small form' mans that
     * the contents returned are purged from all unnecessary information (eg. attributes).
     * @return The list of the types in a (small form).
     * @deprecated From Entando 4.1.2, use getSmallEntityTypes() method
     */
    public List<SmallContentType> getSmallContentTypes();
    
    /**
     * Restituisce la mappa dei prototipi dei tipi di contenuti
     * il oggetti SmallContentType, indicizzati in base al codice del tipo.
     * @return La mappa dei prototipi dei tipi di contenuti
     * il oggetti SmallContentType.
     */
    public Map<String, SmallContentType> getSmallContentTypesMap();
    
    /**
     * Restituisce il codice della pagina di default per la visualizzazione
     * di un contenuto. La pagina di default è definita a livello di tipo di
     * contenuto; il tipo è desunto dal codice in base alla convenzione
     * di codifica.
     * @param contentId L'identificaore di un contenuto
     * @return Il codice della pagina.
     */
    public String getViewPage(String contentId);
    
    /**
     * Restituisce il codice del modello di default per un contenuto.
     * @param contentId Il codice del contenuto
     * @return Il codice del modello.
     */
    public String getDefaultModel(String contentId);
    
    /**
     * Restituisce il codice del modello da usare nelle liste per un contenuto.
     * @param contentId Il codice del contenuto
     * @return Il codice del modello.
     */
    public String getListModel(String contentId);
    
    /**
     * Restituisce un contenuto completo in base al suo indice id ed in base a che si desideri quello
     * nell'area di lavoro o quello onLine. Include come ritorno anche i dati contenuti sotto forma di xml.
     * @param id L'dentificativo del contenuto da restituire.
     * @param onLine Specifica quale contenuto deve caricare, true carica il contenuto online,
     * false carica il contenuto libero.
     * @return Il contenuto OnLine.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public Content loadContent(String id, boolean onLine) throws ApsSystemException;
	
	public Content loadContent(String id, boolean onLine, boolean cacheable) throws ApsSystemException;
    
    /**
     * Restituisce un VO contenente le informazioni del record su db
     * corrispondente al contenuto di cui all'id inserito.
     * @param id L'identificativo del contenuto.
     * @return L'oggetto VO corrispondente al contenuto cercato.
     * @throws ApsSystemException In caso di errore in accesso al db.
     */
    public ContentRecordVO loadContentVO(String id) throws ApsSystemException;
    
    /**
     * Salva un contenuto sul DB.
     * Il metodo viene utilizzato sia nel caso di salvataggio di un
     * nuovo contenuto (in tal caso l'id del contenuto nuovo sarà nullo)
     * o di aggiornamento di contenuto già esistente (id non nullo).
     * @param content Il contenuto da aggiungere o modificare.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public void saveContent(Content content) throws ApsSystemException;
    
    public void saveContentAndContinue(Content content) throws ApsSystemException;
    
    /**
     * Save a content in the DB.
     * @param content The content to add.
     * @throws ApsSystemException in case of error.
     */
    public void addContent(Content content) throws ApsSystemException;
    
    /**
     * Inserisce il contenuto OnLine.
     * @param content Il contenuto da rendere visibile online.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public void insertOnLineContent(Content content) throws ApsSystemException;
    
    /**
     * Rimuove un contenuto OnLine. L'operazione non cancella il contenuto ma ne
     * rimuove la possibilita' di visualizzazione nel portale.
     * Il contenuto ancora presente verrà messo in stato cancellato.
     * @param content Il contenuto da rimuovere.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public void removeOnLineContent(Content content) throws ApsSystemException;
    
    /**
     * Cancella un contenuto dal db.
     * @param content Il contenuto da cancellare.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public void deleteContent(Content content) throws ApsSystemException;
    
    /**
     * Carica una lista di identificativi di contenuti publici in base ai parametri immessi.
     * @param contentType Il codice dei tipi di contenuto da cercare.
     * @param categories La categorie dei contenuti da cercare.
     * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
     * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista.
     * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno
     * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente
     * il codice del gruppo degli amministratori, non sarà applicato alcun il filtro sul gruppo.
     * @return La lista degli id dei contenuti cercati.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public List<String> loadPublicContentsId(String contentType, String[] categories,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    public List<String> loadPublicContentsId(String contentType, String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    /**
     * Carica una lista di identificativi di contenuti publici in base ai parametri immessi.
     * @param categories La categorie dei contenuti da cercare.
     * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
     * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista.
     * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno
     * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente
     * il codice del gruppo degli amministratori, non sarà applicato alcun il filtro sul gruppo.
     * @return La lista degli id dei contenuti cercati.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    public List<String> loadPublicContentsId(String[] categories,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    public List<String> loadPublicContentsId(String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    /**
     * Carica una lista di identificativi di contenuti in base ai parametri immessi.
     * @param categories La categorie dei contenuti da cercare.
     * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
     * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista.
     * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno
     * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente
     * il codice del gruppo degli amministratori, non sarà applicato alcun filtro sul gruppo.
     * @param onlyOwner Implica se il filtro sulla ricerca và applicato
     * anche sui gruppi extra dei contenuti e non esclusivamente sul gruppo proprietario.
     * @return La lista degli id dei contenuti cercati.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     * @deprecated From jAPS 2.0 version 2.0.9. Use loadWorkContentsId or loadPublicContentsId
     */
    public List<String> loadContentsId(String[] categories, EntitySearchFilter[] filters,
            Collection<String> userGroupCodes, boolean onlyOwner) throws ApsSystemException;
    
    public List<String> loadWorkContentsId(EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    public List<String> loadWorkContentsId(String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    public List<String> loadWorkContentsId(String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
    
    /**
     * Restituisce la lista di tutti identificativi dei contenuti.
     * @return La lista di tutti identificativi dei contenuti.
     * @throws ApsSystemException In caso di errore
     * @deprecated From jAPS 2.0 version 2.0.9, use searchId(EntitySearchFilter[]) method
     */
    public List<String> getAllContentsId() throws ApsSystemException;
    
    /**
     * Restituisce lo stato del servizio.
     * @return Lo stato del servizio.
     * @deprecated From jAPS 2.0 version 2.0.9, use getStatus
     */
    public int getState();
    
    /**
     * Identificativo stato servizio: stato pronto.
     * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntityManager} constants
     */
    public static final int ID_STATE_READY = STATUS_READY;
    
    /**
     * Identificativo stato servizio: stato ricaricamento referenze in progress.
     * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntityManager} constants
     */
    public static final int ID_RELOAD_REFERENCES_IN_PROGRESS = STATUS_RELOADING_REFERENCES_IN_PROGRESS;
    
    public static final String CONTENT_DESCR_FILTER_KEY = "descr";
    
    public static final String CONTENT_STATUS_FILTER_KEY = "status";
    
    public static final String CONTENT_CREATION_DATE_FILTER_KEY = "created";
    
    public static final String CONTENT_MODIFY_DATE_FILTER_KEY = "modified";
    
    public static final String CONTENT_ONLINE_FILTER_KEY = "online";
    
    public static final String CONTENT_MAIN_GROUP_FILTER_KEY = "maingroup";
    
    public static final String CONTENT_CURRENT_VERSION_FILTER_KEY = "currentversion";
    
    public static final String CONTENT_FIRST_EDITOR_FILTER_KEY = "firsteditor";
    
    public static final String CONTENT_LAST_EDITOR_FILTER_KEY = "lasteditor";
    
}
