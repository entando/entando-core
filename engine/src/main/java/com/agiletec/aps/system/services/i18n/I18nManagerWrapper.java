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
