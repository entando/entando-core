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

import java.util.Map;

import com.agiletec.aps.util.ApsProperties;

/**
 * Interfaccia base per Data Access Object degli oggetti label (I18n).
 * @author E.Santoboni
 */
public interface II18nDAO {

	/**
	 * Carica la mappa che contiene tutti i gruppi delle label in tutte le lingue.
	 * @return La mappa contenente tutte le label.
	 */
	public Map<String, ApsProperties> loadLabelGroups();

	/**
	 * Aggiunge un gruppo di label.
	 * @param key La chiave del gruppo da aggiungere.
	 * @param labels La mappa delle labels indicizzate per codice lingua.
	 */
	public void addLabelGroup(String key, ApsProperties labels);

	/**
	 * Aggiorna un gruppo di label.
	 * @param key La chiave del gruppo da aggiornare.
	 * @param labels La mappa delle labels indicizzate per codice lingua.
	 */
	public void updateLabelGroup(String key, ApsProperties labels);

	/**
	 * Cancella un gruppo di label.
	 * @param key La chiave del gruppo da cancellare.
	 */
	public void deleteLabelGroup(String key);

}