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
package org.entando.entando.aps.system.services.controller.preview.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;

/**
 * Implementazione del un sottoservizio di controllo che verifica la validità
 * della richiesta del client. Viene verificata la correttezza formale della
 * richiesta tramite la corrispondenza con una maschera. La richiesta deve
 * essere nella forma:<br>
 * <code>/lingua/pagina.wp</code> oppure <code>/lingua/path_pagina/</code><br>
 * dove lingua è un codice lingua configurato, pagina una pagina del portale e
 * path_pagina il path (stile breadcrumbs) della pagina. Se la richiesta è
 * valida, l'oggetto lingua e l'oggetto pagina corrispondenti alla richiesta
 * sono inseriti nel contesto di richiesta sotto forma di extra parametri, con i
 * nomi "currentLang" e "currentPage", ed il metodo service restituisce
 * Controller.CONTINUE. Se la richiesta non è valida, viene restituito lo stato
 * di errore.
 *
 * @author M.Diana - E.Santoboni
 */
public class PreviewRequestValidator extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(PreviewRequestValidator.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	/**
	 * Esecuzione. Le operazioni sono descritte nella documentazione della
	 * classe.
	 *
	 * @param reqCtx Il contesto di richiesta
	 * @param status Lo stato di uscita del servizio precedente
	 * @return Lo stato di uscita
	 */
	@Override
	public int service(RequestContext reqCtx, int status) {
		_logger.debug("{} invoked", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		// Se si è verificato un errore in un altro sottoservizio, termina
		// subito
		if (status == ControllerManager.ERROR) {
			return ControllerManager.SYS_ERROR;
		}
		try { // non devono essere rilanciate eccezioni
			boolean ok = this.isRightPath(reqCtx);
			if (!ok || null == reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE) || null == reqCtx.getExtraParam(
					SystemConstants.EXTRAPAR_CURRENT_LANG)) {
				retStatus = ControllerManager.SYS_ERROR;
			} else {
				retStatus = ControllerManager.CONTINUE;
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
		if (this.getResourcePath(reqCtx).equals("/preview")) {
			resourcePath = getFullResourcePath(reqCtx);
			matcher = this._pattern.matcher(resourcePath);
			if (matcher.lookingAt()) {
				ok = true;
				String sect1 = matcher.group(1);
				String sect2 = matcher.group(2);
				lang = getLangManager().getLang(sect1);
				page = this.getDesiredPage(sect2);
			}
		}
		if (!ok) {
			return false;
		}
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, page);
		return true;
	}

	private Page getDesiredPage(String pageCode) {
		Page page = null;
		IPage currentPage = this.getPageManager().getDraftPage(pageCode);
		if (null != currentPage) {
			page = new Page();
			page.setCode(currentPage.getCode());
			page.setParentCode(currentPage.getParentCode());
			page.setGroup(currentPage.getGroup());
			PageMetadata metadata = currentPage.getMetadata();
			page.setMetadata(metadata);
			String[] children = currentPage.getChildrenCodes();
			page.setChildrenCodes(children);
			Widget[] widgets = currentPage.getWidgets();
			page.setWidgets(widgets);
		}
		return page;
	}

	/**
	 * Recupera il ServletPath richiesto dal client.
	 *
	 * @param reqCtx Il contesto di richiesta
	 * @return Il ServletPath
	 */
	protected String getResourcePath(RequestContext reqCtx) {
		return reqCtx.getRequest().getServletPath();
	}

	protected String getFullResourcePath(RequestContext reqCtx) {
		return this.getResourcePath(reqCtx) + reqCtx.getRequest().getPathInfo();
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

	private ILangManager _langManager;
	private IPageManager _pageManager;

	protected Pattern _pattern = Pattern.compile("^/preview/(\\w+)/((\\w+)*)");

}
