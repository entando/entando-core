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

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Wrapper del Servizio I18N utilizzato nel contesto di Velocity per il parsing dei modelli.
 * Viene passato a Velocity già inizializzato con la lingua da utilizzare, perché per i 
 * modelli di contenuto la lingua deve essere "trasparente". 
 * Il servizio base richiede invece la specificazione della lingua ad ogni richiesta.
 * @author S.Didaci
 */
public class I18nManagerWrapper {

	/**
	 * Inizializzazione del Wrapper.
	 * @param langCode La lingua tramite il quale restituire la label.
	 * @param i18nManager Il manager gestore delle etichette.
	 */
	public I18nManagerWrapper(String langCode, II18nManager i18nManager) {
		this._lang = langCode;
		this._i18nManager = i18nManager;
	}

	/**
	 * Restituisce la label data la chiave. 
	 * @param key La chiave tramite il quele estrarre la label.
	 * @return La label cercata.
	 * @throws ApsSystemException in caso di errori di parsing.
	 */
	public String getLabel(String key) throws ApsSystemException {
		String label = null;
		if (key != null) {
			label = this._i18nManager.getLabel(key, this._lang);
		}
		return label;
	}

	private String _lang = null;

	private II18nManager _i18nManager;

}
