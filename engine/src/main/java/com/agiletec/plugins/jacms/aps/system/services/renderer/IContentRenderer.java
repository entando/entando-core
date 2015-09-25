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
package com.agiletec.plugins.jacms.aps.system.services.renderer;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.renderer.IEntityRenderer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Interfaccia base per l'implementazione dei servizi 
 * di renderizzazione contenuti.
 * @author E.Santoboni
 */
public interface IContentRenderer extends IEntityRenderer {
	
	/**
	 * Esegue il rendering del contenuto 
	 * secondo il modello specificato e nella lingua richiesta.
	 * @param content Il contenuto da renderizzare.
	 * @param modelId L'identificativo del modello di contenuto 
	 * tramite il quale effettuare la renderizzazione.
	 * @param langCode Il codice della lingua di renderizzazione.
	 * @param reqCtx Il contesto della richiesta.
	 * @return Il contenuto renderizzato.
	 */
	public String render(Content content, long modelId, String langCode, RequestContext reqCtx);
	
}
