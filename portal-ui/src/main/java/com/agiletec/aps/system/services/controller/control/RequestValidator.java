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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageUtils;

/**
 * Implementazione del un sottoservizio di controllo che
 * verifica la validità della richiesta del client.
 * Viene verificata la correttezza formale della richiesta tramite la
 * corrispondenza con una maschera. La richiesta deve essere nella forma:<br>
 * <code>/lingua/pagina.wp</code> oppure <code>/lingua/path_pagina/</code><br>
 * dove lingua è un codice lingua configurato, pagina una pagina del portale e 
 * path_pagina il path (stile breadcrumbs) della pagina.
 * Se la richiesta è valida, l'oggetto lingua e l'oggetto pagina
 * corrispondenti alla richiesta sono inseriti nel contesto di richiesta
 * sotto forma di extra parametri, con i nomi "currentLang" e "currentPage",
 * ed il metodo service restituisce Controller.CONTINUE.
 * Se la richiesta non è valida, viene restituito lo stato di errore.
 * @author M.Diana - E.Santoboni
 */
public class RequestValidator extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(RequestValidator.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	/**
	 * Esecuzione. Le operazioni sono descritte nella documentazione della classe.
	 * @param reqCtx Il contesto di richiesta
	 * @param status Lo stato di uscita del servizio precedente
	 * @return Lo stato di uscita
	 */
	@Override
	public int service(RequestContext reqCtx, int status) {
		_logger.debug("{} invoked", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		// Se si è verificato un errore in un altro sottoservizio, termina subito
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try { // non devono essere rilanciate eccezioni
			boolean ok = this.isRightPath(reqCtx);
			if (ok) {
				if (null == reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE)) {
					retStatus = this.redirect(this.getNotFoundPageCode(), reqCtx);
				} else if (null == reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG)) {
					retStatus = this.redirect(this.getErrorPageCode(), reqCtx);
				} else {
					retStatus = ControllerManager.CONTINUE;
				}
			} else {
				retStatus = this.redirect(this.getErrorPageCode(), reqCtx);
			}
		} catch (Throwable t) {
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			_logger.error("Error while validating the client request", t);
		}
		return retStatus;
	}
	
	private boolean isRightPath(RequestContext reqCtx) {
		boolean ok = false;
		String resourcePath;
		Matcher matcher;
		Lang lang = null;
		IPage page = null;
		if (this.getResourcePath(reqCtx).equals("/pages")) {
			resourcePath = getFullResourcePath(reqCtx);
			matcher = this._patternFullPath.matcher(resourcePath);
			if (matcher.lookingAt()) {
				ok = true;
				String sect1 = matcher.group(1);
				lang = getLangManager().getLang(sect1);
				page = this.getPage(matcher);
			}
		} else {
			resourcePath = getResourcePath(reqCtx);
			matcher = this._pattern.matcher(resourcePath);
			if (matcher.lookingAt()) {
				ok = true;
				String sect1 = matcher.group(1);
				String sect2 = matcher.group(2);
				lang = getLangManager().getLang(sect1);
				page = this.getPageManager().getPage(sect2);
			} else {
				//to preserve url with ".wp" suffix
				matcher = this._oldPattern.matcher(resourcePath);
				if (matcher.lookingAt()) {
					ok = true;
					String sect1 = matcher.group(1);
					String sect2 = matcher.group(2);
					lang = getLangManager().getLang(sect1);
					page = this.getPageManager().getPage(sect2);
				}
			}
		}
		if (!ok) return false;
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, page);
		return true;
	}
	
	/**
	 * Qualora si usasse il mapping /pages/*
	 * restituisce un'oggetto IPage solo nel caso
	 * in cui il path completo della pagina risulti corretto.
	 * Qualora il path sia di lunghezza pari a zero
	 * verrà restituita l'homepage.
	 * @param Matcher il matcher valorizzato come segue<br>
	 * matcher.group(1) -> lang_code<br>
	 * matcher.group(2) -> /paginaX/paginaY<br>
	 * matcher.group(3) -> /paginaY<br>
	 * @return un oggetto Page oppure null
	 */
	private IPage getPage(Matcher matcher) {
		IPage page = null;
		String rootCode = this.getPageManager().getRoot().getCode();
		String path = matcher.group(2);
		//Se il path è di tipo /it o /it/ o /it/homepage
		if(path.trim().length() == 0  || path.substring(1).equals(rootCode) ){
			return this.getPageManager().getRoot();
		}
		String pageCode = matcher.group(3).substring(1);
		IPage tempPage = this.getPageManager().getPage(pageCode);
		if (null != tempPage) {
			//la pagina esiste ed è di livello 1
			//if(tempPage.getParentCode().equals(rootCode)) return tempPage;
			//la pagina è di livello superiore al primo e il path è corretto
			String fullPath = matcher.group(2).substring(1).trim();
			String createdlFullPath = PageUtils.getFullPath(tempPage, "/").toString();
			if (null != tempPage && createdlFullPath.equals(fullPath) ){
				page = tempPage;
			}
		}
		return page;
	}
	
	/**
	 * Recupera il ServletPath richiesto dal client.
	 * @param reqCtx Il contesto di richiesta
	 * @return Il ServletPath
	 */
	protected String getResourcePath(RequestContext reqCtx) {
		return reqCtx.getRequest().getServletPath();
	}
	
	protected String getFullResourcePath(RequestContext reqCtx) {
		return this.getResourcePath(reqCtx) + reqCtx.getRequest().getPathInfo();
	}
	
	protected String getErrorPageCode() {
		return this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE);
	}
	
	protected String getNotFoundPageCode() {
		return this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE);
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return configManager;
	}
	public void setConfigManager(ConfigInterface configService) {
		this.configManager = configService;
	}
	
	private ILangManager _langManager;
	private IPageManager _pageManager;
	private ConfigInterface configManager;
	
	@Deprecated
	protected Pattern _oldPattern = Pattern.compile("^/(\\w+)/(\\w+)\\Q.wp\\E");
	
	protected Pattern _pattern = Pattern.compile("^/(\\w+)/(\\w+)\\Q.page\\E");
	
	protected Pattern _patternFullPath = Pattern.compile("^/pages/(\\w+)((/\\w+)*)");
	
}
