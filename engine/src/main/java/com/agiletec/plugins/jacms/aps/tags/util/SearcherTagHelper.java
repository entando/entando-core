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
package com.agiletec.plugins.jacms.aps.tags.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;

/**
 * Classe helper per i tag esecutori di 
 * ricerche contenuti in base a parola chiave.
 * @author E.Santoboni
 */
public class SearcherTagHelper {
	
	/**
	 * Carica una lista di identificativi di contenuto in base ad una ricerca 
	 * effettuata in funzione ad una parila chiave specificata.
	 * @param word La parola con cui effettuare la ricerca.
	 * @param reqCtx Il contesto della richiesta.
	 * @return La lista di identificativi di contenuto.
	 * @throws ApsSystemException
	 */
	public List<String> executeSearch(String word, RequestContext reqCtx) throws ApsSystemException {
		List<String> result = new ArrayList<String>();
		if (null != word && word.trim().length() > 0) {
			UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
	    	ICmsSearchEngineManager searchEngine = (ICmsSearchEngineManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.SEARCH_ENGINE_MANAGER, reqCtx.getRequest());
	    	IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, reqCtx.getRequest());
			List<Group> groups = authManager.getUserGroups(currentUser);
			Set<String> userGroups = new HashSet<String>();
			Iterator<Group> iter = groups.iterator();
	    	while (iter.hasNext()) {
	    		Group group = iter.next();
	    		userGroups.add(group.getName());
	    	}
	    	Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			result = searchEngine.searchEntityId(currentLang.getCode(), word, userGroups);
		}
		return result;
	}
	
}
