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