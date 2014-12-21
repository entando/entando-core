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
package com.agiletec.apsadmin.portal.specialwidget.navigator;

import java.util.List;

import com.agiletec.aps.system.services.page.widget.NavigatorExpression;
import com.agiletec.apsadmin.portal.specialwidget.ISimpleWidgetConfigAction;

/**
 * Interfaccia per la classe action dell'interfaccia 
 * di gestione configurazione Widget tipo Navigator
 * @version 1.0
 * @author E.Santoboni
 */
public interface INavigatorWidgetConfigAction extends ISimpleWidgetConfigAction {
	
	/**
	 * Esegue l'operazione di aggiunta di una espressione 
	 * in base ai parametri di richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String addExpression();
	
	/**
	 * Esegue l'operazione di rimozione di una espressione 
	 * in base ai parametri di richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeExpression();
	
	/**
	 * Esegue l'operazione di spostamento di una espressione 
	 * in base ai parametri di richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String moveExpression();
	
	/**
	 * Restituisce la lista di espressioni associata alla showlet corrente.
	 * @return La lista di espressioni associata alla showlet corrente.
	 */
	public List<NavigatorExpression> getExpressions();
	
	public static final String MOVEMENT_UP_CODE = "UP";
	public static final String MOVEMENT_DOWN_CODE = "DOWN";
	
}