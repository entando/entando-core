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
package com.agiletec.aps.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rappresenta il contesto relativo ad una richiesta del client.
 * Contiene i riferimenti alla request, alla response, al contesto di sistema
 * e ad una mappa di parametri extra destinati ad usi vari.
 * @author M.Diana
 */
public class RequestContext {
	
	/**
	 * Imposta la mappa dei parametri extra 
	 * e memorizza il riferimento al contesto del sistema.
	 */
	public RequestContext(){
		this._extraParams = new HashMap<String, Object>();
	}
	
	/**
	 * Restituisce un parametro extra.
	 * @param name Il nome del parametro
	 * @return Il parametro richiesto
	 */
	public Object getExtraParam(String name) {
		return _extraParams.get(name);
	}

	/**
	 * Aggiunge un parametro extra a questo oggetto
	 * @param name Il nome del parametro
	 * @param param Il parametro da aggiungere
	 */
	public void addExtraParam(String name, Object param) {
		this._extraParams.put(name, param);
	}

	/**
	 * Elimina un parametro extra.
	 * @param name Il nome del parametro
	 */
	public void removeExtraParam(String name) {
		this._extraParams.remove(name);
	}
	
	/**
	 * Restituisce la request della servlet.
	 * @return La request della servlet.
	 */
	public HttpServletRequest getRequest() {
		return _request;
	}

	/**
	 * Imposta la request della servlet.
	 * @param request La request da impostare.
	 */
	public void setRequest(HttpServletRequest request) {
		this._request = request;
	}

	/**
	 * Restituisce la response della servlet.
	 * @return La response della servlet.
	 */
	public HttpServletResponse getResponse() {
		return _response;
	}

	/**
	 * Imposta la response della servlet.
	 * @param response La response da impostare.
	 */
	public void setResponse(HttpServletResponse response) {
		this._response = response;
	}

	/**
	 * Imposta due extra parameter con la specifica di un errore HTTP.  
	 * @param errorCode Il codice di errore HTTP, una delle costanti di HTTPServletResponse.
	 */
	public void setHTTPError(int errorCode) {
		this.addExtraParam("errorType", "HTTP");
		this.addExtraParam("errorCode", new Integer(errorCode));
	}
	
	/**
	 * Elimina eventuali extra parametri di errore precedentemente impostati.
	 */
	public void clearError() {
		this.removeExtraParam("errorType");
		this.removeExtraParam("errorCode");
	}
	
	/**
	 * Riferimento alla request della servlet.
	 */
	private HttpServletRequest _request;
	
	/**
	 * Riferimento alla response della servlet.
	 */
	private HttpServletResponse _response;
	
	/**
	 * Mappa dei parametri extra.
	 */
	private Map<String, Object> _extraParams;
	
	/**
	 * Nome parametro di redirezione.
	 */
	public static final String EXTRAPAR_REDIRECT_URL = "redirectUrl";
	
	/**
	 * Nome parametro: flag per riconoscimento loop di redirezione
	 */
	public static final String PAR_REDIRECT_FLAG = "redirectflag";
	
	/**
	 * Nome attributo del requestContext nella request.
	 */
	public static final String REQCTX = "reqCtx";
	
}
