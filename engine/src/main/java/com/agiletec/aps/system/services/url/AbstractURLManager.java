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
package com.agiletec.aps.system.services.url;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.AbstractService;

/**
 * Servizio di creazione di URL alle risorse del sistema. Per ora è previsto
 * solo l'URL da utilizzare come link ad una pagina del portale.<br>
 * Costituisce anche la factory per gli oggetti PageURL.
 * La classe è astratta in quanto la mappatura tra url e risorse può
 * essere differente a seconda delle esigenze. Ad esempio, in un portale
 * multilingua può essere utile introdurre il codice lingua come parte
 * dell'URL delle pagine. Alcuni metodi di validità generale sono implementati.
 * @author M.Diana
 */
public abstract class AbstractURLManager extends AbstractService implements IURLManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(AbstractURLManager.class);
	
	/**
	 * Crea e restituisce un oggetto PageURL.<br>
	 * N.B.: l'oggetto restituito deve essere utilizzato nell'ambito
	 * della richiesta corrente (non memorizzarlo in modo più persistente, ad
	 * esempio in sessione) in quanto contiene riferimenti ad altri servizi.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'oggetto creato.
	 */
	@Override
	public PageURL createURL(RequestContext reqCtx) {
		return new PageURL(this, reqCtx);
	}
	
	/**
	 * Costruisce la query string a partire dai parametri passati.
	 * @param params Una mappa di parametri, indicizzata in base al nome.
	 * @return La query string; se la mappa passata è nulla o vuota restituisce
	 * una stringa vuota, se la mappa non è vuota la stringa restituita comprende
	 * il carattere ? di introduzione e il separatore & se ci sono più parametri.
	 */
	protected String createQueryString(Map<String, String> params) {
		return this.createQueryString(params, true);
	}
	
	protected String createQueryString(Map<String, String> params, boolean escapeAmp) {
		String queryString = "";
		if (params != null && !params.isEmpty()) {
			StringBuilder buf = new StringBuilder();
			buf.append("?");
			Iterator<String> keyIter = params.keySet().iterator();
			int index = 1;
			String paramSeparator = escapeAmp ? "&amp;" : "&";
			while (keyIter.hasNext()) {
				String name = keyIter.next();
				buf.append(this.encodeParam(name)).append("=").append(this.encodeParam(params.get(name)));
				if (index != params.size()) {
					buf.append(paramSeparator);
					index++;
				}
			}
			queryString = buf.toString();
		}
		return queryString;
	}
	
	protected String encodeParam(String param) {
		String value = null;
		try {
			value = URLEncoder.encode(param, ENCODING_CHARSET);
		} catch (Throwable t) {
			_logger.error("Error encoding param value: " + param);
			value = "";// Parametro vuoto
		}
		return value;	
	}
	
}
