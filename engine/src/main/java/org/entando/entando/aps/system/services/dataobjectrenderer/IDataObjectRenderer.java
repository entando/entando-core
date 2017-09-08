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
package org.entando.entando.aps.system.services.dataobjectrenderer;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.renderer.IEntityRenderer;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Interfaccia base per l'implementazione dei servizi di renderizzazione
 * dataobject.
 *
 * @author E.Santoboni
 */
public interface IDataObjectRenderer extends IEntityRenderer {

	/**
	 * Esegue il rendering del dataobject secondo il modello specificato e nella
	 * lingua richiesta.
	 *
	 * @param dataobject Il dataobject da renderizzare.
	 * @param modelId L'identificativo del modello di dataobject tramite il
	 * quale effettuare la renderizzazione.
	 * @param langCode Il codice della lingua di renderizzazione.
	 * @param reqCtx Il contesto della richiesta.
	 * @return Il dataobject renderizzato.
	 */
	public String render(DataObject dataobject, long modelId, String langCode, RequestContext reqCtx);

}
