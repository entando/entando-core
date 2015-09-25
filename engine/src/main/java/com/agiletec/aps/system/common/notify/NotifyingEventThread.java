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
