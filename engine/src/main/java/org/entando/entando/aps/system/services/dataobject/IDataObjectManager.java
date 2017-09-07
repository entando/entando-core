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
package org.entando.entando.aps.system.services.dataobject;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;

/**
 * Interfaccia base per i Manager dei DataObject.
 *
 * @author M.Diana - E.Santoboni
 */
public interface IDataObjectManager extends IEntityManager {

	/**
	 * Crea una nuova istanza di un DataObject del tipo richiesto. Il nuovo
	 * DataObject è istanziato mediante clonazione del prototipo corrispondente.
	 *
	 * @param typeCode Il codice del tipo di DataObject richiesto, come definito
	 * in configurazione.
	 * @return Il DataObject istanziato (vuoto).
	 */
	public DataObject createDataObject(String typeCode);

	/**
	 * Return a list of the of the DataObject types in a 'small form'. 'Small
	 * form' mans that the DataObjects returned are purged from all unnecessary
	 * information (eg. attributes).
	 *
	 * @return The list of the types in a (small form).
	 * @deprecated From Entando 4.1.2, use getSmallEntityTypes() method
	 */
	public List<SmallDataType> getSmallDataTypes();

	/**
	 * Restituisce la mappa dei prototipi dei tipi di DataObject in oggetti
	 * SmallContentType, indicizzati in base al codice del tipo.
	 *
	 * @return La mappa dei prototipi dei tipi di DataObject il oggetti
	 * SmallDataType.
	 */
	public Map<String, SmallDataType> getSmallDataTypesMap();

	/**
	 * Restituisce il codice della pagina di default per la visualizzazione di
	 * un DataObject. La pagina di default è definita a livello di tipo di
	 * DataObject; il tipo è desunto dal codice in base alla convenzione di
	 * codifica.
	 *
	 * @param contentId L'identificaore di un DataObject
	 * @return Il codice della pagina.
	 */
	public String getViewPage(String contentId);

	/**
	 * Restituisce il codice del modello di default per un DataObject.
	 *
	 * @param contentId Il codice del DataObject
	 * @return Il codice del modello.
	 */
	public String getDefaultModel(String contentId);

	/**
	 * Restituisce il codice del modello da usare nelle liste per un DataObject.
	 *
	 * @param contentId Il codice del DataObject
	 * @return Il codice del modello.
	 */
	public String getListModel(String contentId);

	/**
	 * Restituisce un DataObject completo in base al suo indice id ed in base a
	 * che si desideri quello nell'area di lavoro o quello onLine. Include come
	 * ritorno anche i dati contenuti sotto forma di xml.
	 *
	 * @param id L'dentificativo del DataObject da restituire.
	 * @param onLine Specifica quale DataObject deve caricare, true carica il
	 * DataObject online, false carica il contenuto libero.
	 * @return Il DataObject OnLine.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public DataObject loadDataObject(String id, boolean onLine) throws ApsSystemException;

	public DataObject loadDataObject(String id, boolean onLine, boolean cacheable) throws ApsSystemException;

	/**
	 * Restituisce un VO contenente le informazioni del record su db
	 * corrispondente al DataObject di cui all'id inserito.
	 *
	 * @param id L'identificativo del DataObject.
	 * @return L'oggetto VO corrispondente al DataObject cercato.
	 * @throws ApsSystemException In caso di errore in accesso al db.
	 */
	public DataObjectRecordVO loadDataObjectVO(String id) throws ApsSystemException;

	/**
	 * Salva un DataObject sul DB. Il metodo viene utilizzato sia nel caso di
	 * salvataggio di un nuovo DataObject (in tal caso l'id del contenuto nuovo
	 * sarà nullo) o di aggiornamento di DataObject già esistente (id non
	 * nullo).
	 *
	 * @param dataObject Il DataObject da aggiungere o modificare.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public void saveDataObject(DataObject dataObject) throws ApsSystemException;

	public void saveDataObjectAndContinue(DataObject dataObject) throws ApsSystemException;

	/**
	 * Save a DataObject in the DB.
	 *
	 * @param dataObject The DataObject to add.
	 * @throws ApsSystemException in case of error.
	 */
	public void addContent(DataObject dataObject) throws ApsSystemException;

	/**
	 * Inserisce il DataObject OnLine.
	 *
	 * @param dataObject Il DataObject da rendere visibile online.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public void insertOnLineDataObject(DataObject dataObject) throws ApsSystemException;

	/**
	 * Rimuove un DataObject OnLine. L'operazione non cancella il DataObject ma
	 * ne rimuove la possibilita' di visualizzazione nel portale. Il DataObject
	 * ancora presente verrà messo in stato cancellato.
	 *
	 * @param content Il DataObject da rimuovere.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public void removeOnLineDataObject(DataObject content) throws ApsSystemException;

	/**
	 * Cancella un DataObject dal db.
	 *
	 * @param dataObject Il DataObject da cancellare.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public void deleteDataObject(DataObject dataObject) throws ApsSystemException;

	/**
	 * Carica una lista di identificativi di DataObject publici in base ai
	 * parametri immessi.
	 *
	 * @param dataType Il codice dei tipi di DataObject da cercare.
	 * @param categories La categorie dei DataObject da cercare.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca
	 * deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente
	 * la lista. Se la collezione è vuota o nulla, gli identificativi di
	 * DataObject erogati saranno relativi al gruppo definito "ad accesso
	 * libero". Nel caso nella collezione sia presente il codice del gruppo
	 * degli amministratori, non sarà applicato alcun il filtro sul gruppo.
	 * @return La lista degli id dei DataObject cercati.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public List<String> loadPublicDataObjectsId(String dataType, String[] categories,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	public List<String> loadPublicDataObjectsId(String dataType, String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	/**
	 * Carica una lista di identificativi di DataObject publici in base ai
	 * parametri immessi.
	 *
	 * @param categories La categorie dei DataObject da cercare.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca
	 * deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente
	 * la lista. Se la collezione è vuota o nulla, gli identificativi di
	 * DataObject erogati saranno relativi al gruppo definito "ad accesso
	 * libero". Nel caso nella collezione sia presente il codice del gruppo
	 * degli amministratori, non sarà applicato alcun il filtro sul gruppo.
	 * @return La lista degli id dei DataObject cercati.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public List<String> loadPublicDataObjectsId(String[] categories,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	public List<String> loadPublicDataObjectsId(String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	/**
	 * Carica una lista di identificativi di DataObject in base ai parametri
	 * immessi.
	 *
	 * @param categories La categorie dei DataObject da cercare.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca
	 * deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente
	 * la lista. Se la collezione è vuota o nulla, gli identificativi di
	 * DataObject erogati saranno relativi al gruppo definito "ad accesso
	 * libero". Nel caso nella collezione sia presente il codice del gruppo
	 * degli amministratori, non sarà applicato alcun filtro sul gruppo.
	 * @param onlyOwner Implica se il filtro sulla ricerca và applicato anche
	 * sui gruppi extra dei DataObject e non esclusivamente sul gruppo
	 * proprietario.
	 * @return La lista degli id dei DataObject cercati.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 * @deprecated From jAPS 2.0 version 2.0.9. Use loadWorkContentsId or
	 * loadPublicDataObjectsId
	 */
	public List<String> loadDataObjectsId(String[] categories, EntitySearchFilter[] filters,
			Collection<String> userGroupCodes, boolean onlyOwner) throws ApsSystemException;

	public List<String> loadWorkDataObjectsId(EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	public List<String> loadWorkDataObjectsId(String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	public List<String> loadWorkDataObjectsId(String[] categories, boolean orClauseCategoryFilter, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;

	public DataObjectsStatus getDataObjectsStatus();

	/**
	 * Restituisce la lista di tutti identificativi dei DataObject.
	 *
	 * @return La lista di tutti identificativi dei DataObject.
	 * @throws ApsSystemException In caso di errore
	 * @deprecated From jAPS 2.0 version 2.0.9, use
	 * searchId(EntitySearchFilter[]) method
	 */
	public List<String> getAllDataObjectsId() throws ApsSystemException;

	/**
	 * Restituisce lo stato del servizio.
	 *
	 * @return Lo stato del servizio.
	 * @deprecated From jAPS 2.0 version 2.0.9, use getStatus
	 */
	public int getState();

	/**
	 * Identificativo stato servizio: stato pronto.
	 *
	 * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntityManager}
	 * constants
	 */
	public static final int ID_STATE_READY = STATUS_READY;

	/**
	 * Identificativo stato servizio: stato ricaricamento referenze in progress.
	 *
	 * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntityManager}
	 * constants
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
