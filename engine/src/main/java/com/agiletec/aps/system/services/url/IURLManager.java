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
package com.agiletec.aps.system.services.url;

import java.util.Map;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;

import javax.servlet.http.HttpServletRequest;

/**
 * Interfaccia base per i servizi di creazione di URL.
 * @author M.Diana - E.Santoboni
 */
public interface IURLManager {
	
	/**
	 * Crea e restituisce un oggetto PageURL.<br>
	 * N.B.: l'oggetto restituito deve essere utilizzato nell'ambito
	 * della richiesta corrente (non memorizzarlo in modo più persistente, ad
	 * esempio in sessione) in quanto contiene riferimenti ad altri servizi.
	 * @param reqCtx Il contesto della richiesta.
	 * @return L'oggetto creato.
	 */
	public PageURL createURL(RequestContext reqCtx);
	
	/**
	 * Crea l'URL ad una pagina del portale, sulla base 
	 * delle informazioni contenute nell'argomento passato.
	 * @param pageUrl L'oggetto contenente le informazioni sulla destinazione
	 * @param reqCtx Il contesto della richiesta dell'URL.
	 * @return La Stringa contenente l'URL.
	 */
	public String getURLString(PageURL pageUrl, RequestContext reqCtx);
	
	/**
	 * Create and return url by required page, lang and request params.
	 * @param requiredPage The required page.
	 * @param requiredLang The required lang.
	 * @param params A map of params. Could be null.
	 * @return The url.
	 */
	public String createUrl(IPage requiredPage, Lang requiredLang, Map<String, String> params);
	
	public String createUrl(IPage requiredPage, Lang requiredLang, Map<String, String> params, boolean escapeAmp);
	
	public String createUrl(IPage requiredPage, Lang requiredLang, Map<String, String> params, boolean escapeAmp, HttpServletRequest request) throws ApsSystemException;
	
	public String getApplicationBaseURL(HttpServletRequest request) throws ApsSystemException;
	
}
