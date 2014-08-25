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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer;

import com.agiletec.apsadmin.portal.specialwidget.ISimpleWidgetConfigAction;

/**
 * Interfaccia base per le action gestori della configurazione della showlet erogatore contenuto singolo.
 * @author E.Santoboni
 */
public interface IContentViewerWidgetAction extends ISimpleWidgetConfigAction {
	
	/**
	 * Esegue l'operazione di associazione di un contenuto alla showlet.
	 * L'operazione ha l'effetto di inserire il riferimento del contenuto desiderato 
	 * (ricavato dai parametri della richiesta) nei parametri di configurazione della showlet.
	 * @return Il codice del risultato dell'azione.
	 */
	public String joinContent();
	
}
