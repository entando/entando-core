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
package org.entando.entando.aps.system.services.dataobject.widget;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.dataobject.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;

/**
 * Interfaccia base per le classi helper per le showlet erogatori contenuti.
 * @author E.Santoboni
 */
public interface IContentViewerHelper {
    
    /**
     * Restituisce il contenuto da visualizzare nella showlet.
     * @param contentId L'identificativo del contenuto ricavato dal tag.
     * @param modelId Il modello del contenuto ricavato dal tag.
     * @param reqCtx Il contesto della richiesta.
     * @return Il contenuto da visualizzare nella showlet.
     * @throws ApsSystemException In caso di errore
     */
	public String getRenderedContent(String contentId, String modelId, RequestContext reqCtx) throws ApsSystemException;

	public String getRenderedContent(String contentId, String modelId, boolean publishExtraTitle, RequestContext reqCtx) throws ApsSystemException;
	
	public ContentRenderizationInfo getRenderizationInfo(String contentId, String modelId, boolean publishExtraTitle, RequestContext reqCtx) throws ApsSystemException;
	
	public PublicContentAuthorizationInfo getAuthorizationInfo(String contentId, RequestContext reqCtx) throws ApsSystemException;
	
}