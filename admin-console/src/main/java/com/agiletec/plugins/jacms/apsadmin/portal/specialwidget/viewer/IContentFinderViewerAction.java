/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer;

import com.agiletec.plugins.jacms.apsadmin.content.IContentFinderAction;

/**
 * Interfaccia base per la classe Action che cerca i contenuti per 
 * la configurazione dei widget di tipo "Pubblica contenuto singolo".
 * @author E.Santoboni
 */
public interface IContentFinderViewerAction extends IContentFinderAction {
	
	/**
	 * Esegue l'operazione di richiesta associazione di un contenuto alla showlet.
	 * La richiesta viene effettuata nell'interfaccia di ricerca risorse e viene redirezionata 
	 * alla action che gestisce la configurazione della showlet di pubblicazione contenuto.
	 * @return Il codice del risultato dell'azione.
	 */
	public String joinContent();
	
}
