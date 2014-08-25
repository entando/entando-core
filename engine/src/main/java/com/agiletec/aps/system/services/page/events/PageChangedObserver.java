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
package com.agiletec.aps.system.services.page.events;

import com.agiletec.aps.system.common.notify.ObserverService;

/**
 * Interfaccia base per l'implementazione dei servizi destinatari 
 * della notificazione di eventi di modifica pagina.
 * @author E.Santoboni - M.Diana
 */
public interface PageChangedObserver extends ObserverService {
	
	/**
	 * Aggiorna il servizio di conseguenza alla notifica 
	 * di un evento di modifica pagina.
	 * @param event L'evento notificato.
	 */
	public void updateFromPageChanged(PageChangedEvent event);
	
}
