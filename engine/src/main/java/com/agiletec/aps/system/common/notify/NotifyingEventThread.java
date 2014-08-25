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
package com.agiletec.aps.system.common.notify;

/**
 * Thread Class delegate to notify event.
 * @author E.Santoboni
 */
public class NotifyingEventThread extends Thread {
	
	/**
	 * Inizializza il thread delegato notificazione eventi.
	 * @param notifyManager Il servizio gestore notifica eventi.
	 * @param event l'evento da notificare.
	 */
	public NotifyingEventThread(NotifyManager notifyManager, ApsEvent event) {
		this._notifyManager = notifyManager;
		this._event = event;
	}
	
	@Override
	public void run() {
		this._notifyManager.notify(this._event);
	}
	
	private NotifyManager _notifyManager;
	private ApsEvent _event;
	
}
