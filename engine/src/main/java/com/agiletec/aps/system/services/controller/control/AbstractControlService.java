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
package com.agiletec.aps.system.services.controller.control;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;

/**
 * Classe di utilità che implementa un metodo per impostare una redirezione ed
 * un metodo per recuperare un parametro singolo dall'HttpServletRequest.
 * @author M.Diana - E.Santoboni
 */
public abstract class AbstractControlService implements ControlServiceInterface {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractControlService.class);
	
	/**
	 * Imposta i parametri di una redirezione.
	 * @param redirDestPage Il codice della pagina su cui si vuole redirezionare.
	 * @param reqCtx Il contesto di richiesta.
	 * @return L'indicativo del tipo di redirezione in uscita del controlService. 
	 * Può essere una delle costanti definite in ControllerManager.
	 */
	protected int redirect(String redirDestPage, RequestContext reqCtx) {
		return this.redirect(redirDestPage, null, reqCtx);
	}
	
	protected int redirect(String redirDestPage, Map<String, String> params, RequestContext reqCtx) {
		int retStatus;
		try {
			String redirPar = this.getParameter(RequestContext.PAR_REDIRECT_FLAG, reqCtx);
			if (redirPar == null || "".equals(redirPar)) {
				PageURL url = this.getUrlManager().createURL(reqCtx);
				url.setPageCode(redirDestPage);
				if (null != params && !params.isEmpty()) {
					Iterator<String> iter = params.keySet().iterator();
					while (iter.hasNext()) {
						String key = iter.next();
						url.addParam(key, params.get(key));
					}
				}
				url.addParam(RequestContext.PAR_REDIRECT_FLAG, "1");
				url.setEscapeAmp(false);
				String redirUrl = url.getURL();
				_logger.debug("Redirecting to {}", redirUrl);
				return this.redirectUrl(redirUrl, reqCtx);
			} else {
				reqCtx.setHTTPError(HttpServletResponse.SC_BAD_REQUEST);
				retStatus = ControllerManager.ERROR;
			}
		} catch (Throwable t) {
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			_logger.error("Error on creation redirect to page {}", redirDestPage, t);
		}
		return retStatus;
	}
	
	protected int redirectUrl(String urlDest, RequestContext reqCtx) {
		int retStatus;
		try {
			reqCtx.clearError();
			reqCtx.addExtraParam(RequestContext.EXTRAPAR_REDIRECT_URL, urlDest);
			retStatus = ControllerManager.REDIRECT;
		} catch (Throwable t) {
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			_logger.error("Error on creation redirect to url {}", urlDest, t);
		}
		return retStatus;
	}
	
	/**
	 * Recupera un parametro della richiesta.
	 * @param name Il nome del parametro.
	 * @param reqCtx Il contesto di richiesta.
	 * @return Il valore del parametro
	 */
	protected String getParameter(String name, RequestContext reqCtx){
		String param = reqCtx.getRequest().getParameter(name);
		return param;
	}
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	/**
	 * Il log di sistema.
	 */
	@Deprecated
	protected Logger _log = ApsSystemUtils.getLogger();
	
	/**
	 * Riferimento all'Url Manager.
	 */
	private IURLManager _urlManager;
	
}
