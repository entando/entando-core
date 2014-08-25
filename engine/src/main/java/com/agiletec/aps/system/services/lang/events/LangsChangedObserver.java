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
package com.agiletec.aps.system.services.lang.events;

import com.agiletec.aps.system.common.notify.ObserverService;

/**
 * Interfaccia base per l'implementazione dei servizi destinatari 
 * della notificazione di eventi di modifica lingue di sistema.
 * @author E.Santoboni - M.Diana
 */
public interface LangsChangedObserver extends ObserverService {
	
	/**
	 * Aggiorna il servizio di conseguenza alla notifica 
	 * di un evento di modifica lingue di sistema.
	 * @param event L'evento notificato.
	 */
	public void updateFromLangsChanged(LangsChangedEvent event);
	
}
