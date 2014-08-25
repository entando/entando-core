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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
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