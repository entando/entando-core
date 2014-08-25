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
package com.agiletec.aps.system.services.page.widget;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Rappresenta un obbiettivo di navigazione, da utilizzare come voce di un menu.
 * E' un wrapper della classe Page alla quale aggiunge un livello (relativo),
 * per poter definire menu a più livelli. 
 * @author M.Diana - E.Santoboni
 */
public class NavigatorTarget {
	
	/**
	 * Costruttore.
	 * @param page La pagina da wrappare.
	 * @param level Il livello.
	 */
	public NavigatorTarget(IPage page, int level) {
		this._page = page;
		this._level = level;
	}
	
	/**
	 * Restituisce il titolo della pagina nella lingua corrente.
	 * Nel caso il titolo nella lingua corrente sia assente, viene restituito il 
	 * titolo nella lingua di default.
	 * @return Il titolo della pagina.
	 */
	public String getTitle() {
		Lang lang = (Lang) this._reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		String title = this.getPage().getTitle(lang.getCode());
		if (title == null || title.trim().equals("")) {
			ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this._reqCtx.getRequest());
			title = this.getPage().getTitle(langManager.getDefaultLang().getCode());
		}
		if (title == null || title.trim().equals("")) {
			title = this.getPage().getCode();
		}
		return title;
	}
	
	/**
	 * Restituisce il codice della pagina.
	 * @return Il codice della pagina.
	 */
	public String getCode() {
		return this.getPage().getCode();
	}
	
	/**
	 * Verifica se il target corrente è vuoto (nessun frame configurato).
	 * Analizza il target corrente e se i frame della pagina non contengono nessuna showlet 
	 * restituisce true, false in caso contrario.
	 * @return true se la pagina è vuota, false in caso contrario.
	 * @deprecated compatibility with JSP specification 2.1 - "void" is a reserved word
	 */
	public boolean isVoid() {
		return this.isVoidPage();
	}
	
	/**
	 * Verifica se il target corrente è vuoto (nessun frame configurato).
	 * Analizza il target corrente e se i frame della pagina non contengono nessuna showlet 
	 * restituisce true, false in caso contrario.
	 * @return true se la pagina è vuota, false in caso contrario.
	 */
	public boolean isVoidPage() {
		boolean isVoid = true;
		Widget[] showlets = this.getPage().getWidgets();
		for (int i = 0; i < showlets.length; i++) {
			if (null != showlets[i]) {
				isVoid = false;
				break;
			}
		}
		return isVoid;
	}
	
	/**
	 * Restituisce il link alla pagina corrente.
	 * @return Il link alla pagina.
	 */
	public String getUrl() {
		IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this._reqCtx.getRequest());
		PageURL pageUrl = urlManager.createURL(this._reqCtx);
		pageUrl.setPage(this.getPage());
		String urlString = pageUrl.getURL();
		return urlString;
	}
	
	/**
	 * Check if the current target has children. 
	 * Analyze the current target and return true if it has at least one child, false otherwise.
	 * @return true if the current target has children, false otherwise.
	 */
	public boolean isParent() {
		IPage page = this.getPage();
		boolean isParent = (null != page && null != page.getChildren() && page.getChildren().length > 0);
		return isParent;
	}
	
	/**
	 * Setta il contesto della richiesta.
	 * @param reqCtx Il contesto della richiesta.
	 */
	public void setRequestContext(RequestContext reqCtx) {
		this._reqCtx = reqCtx;
	}
	
	/**
	 * Restituisce il valore dell'attributo livello.
	 * @return Il livello
	 */
	public int getLevel() {
		return _level;
	}
	
	/**
	 * Restituisce l'oggetto pagina sottostante.
	 * @return La pagina
	 */
	public IPage getPage() {
		return _page;
	}
	
	private int _level;
	private IPage _page;
	private RequestContext _reqCtx;
	
}