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
package com.agiletec.aps.system.services.i18n;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

/**
 * Interfaccia base per i servizi fornitori stringhe "localizzate".
 * @author S.Didaci - E.Santoboni - S.Puddu
 */
public interface II18nManager {
	
	/**
	 * Restituisce una label in base alla chiave ed alla lingua specificata.
	 * @param key La chiave
	 * @param langCode Il codice della lingua.
	 * @return La label richiesta.
	 * @throws ApsSystemException
	 */
	public String getLabel(String key, String langCode) throws ApsSystemException;
	
	public ApsProperties getLabelGroup(String key) throws ApsSystemException;
	
	/**
	 * Add a group of labels on db.
	 * @param key The key of the labels.
	 * @param labels Th labels to add.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void addLabelGroup(String key, ApsProperties labels) throws ApsSystemException;
	
	/**
	 * Delete a group of labels from db.
	 * @param key The key of the labels to delete.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void deleteLabelGroup(String key) throws ApsSystemException;
	
	/**
	 * Update a group of labels on db.
	 * @param key The key of the labels.
	 * @param labels The key of the labels to update.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void updateLabelGroup(String key, ApsProperties labels) throws ApsSystemException;
	
	/**
	 * Restituisce la lista di chiavi di gruppi di labels 
	 * in base ai parametri segbalati.
	 * @param insertedText Il testo tramite il quale effettuare la ricerca.
	 * @param doSearchByKey Specifica se effettuare la ricerca sulle chiavi, 
	 * in base al testo inserito.
	 * @param doSearchByLang Specifica se effettuare la ricerca 
	 * sul testo di una lingua, in base al testo inserito.
	 * @param langCode Specifica la lingua della label sulla quale effettuare 
	 * la ricerca, in base al testo inserito.
	 * @return La lista di chiavi di gruppi di labels 
	 * in base ai parametri segbalati.
	 */
	public List<String> searchLabelsKey(String insertedText, boolean doSearchByKey, 
			boolean doSearchByLang, String langCode);
	
	/**
	 * Return the group of labels.
	 * @return The group of labels.
	 */
	public Map<String, ApsProperties> getLabelGroups();
	
}
