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
package com.agiletec.plugins.jacms.aps.system.services.linkresolver;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * Interfaccia base per i Servizi gestori della risoluzione dei link sinbolici.
 * @author M.Diana - E.Santoboni
 */
public interface ILinkResolverManager {
	
	/**
	 * Sostituisce nel testo i link simbolici con URL reali.
	 * @param text Il testo che può contenere link simbolici.
	 * @param contentId The id of content that contains the link to resolve
	 * @param reqCtx Il contesto di richiesta
	 * @return Il testo in cui i link simbolici sono sostituiti con URL reali.
	 */
	public String resolveLinks(String text, String contentId, RequestContext reqCtx);
	
	public String resolveLinks(String text, RequestContext reqCtx);
	
	/**
	 * Restituisce il link risolto sulla base del symbolic link.
	 * @param symbolicLink Il symbolic linj da risolvere.
	 * @param contentId The id of content that contains the link to resolve
	 * @param reqCtx Il contesto della richiesta. Può essere null. 
	 * @return Il link generato.
	 */
	public String resolveLink(SymbolicLink symbolicLink, String contentId, RequestContext reqCtx);
	
	public String resolveLink(SymbolicLink symbolicLink, RequestContext reqCtx);
	
}