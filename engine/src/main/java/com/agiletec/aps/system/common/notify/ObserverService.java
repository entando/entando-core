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
 * Interfaccia base per l'estensione delle interfaccie base 
 * per l'implementazione dei servizi destinatari alla notificazione 
 * di eventi particolari. L'interfaccia non deve essere implementata 
 * direttamente dai servizi ascoltatori.
 * @author E.Santoboni - M.Diana
 */
public interface ObserverService {
	
	/**
	 * Aggiorna il servizio in base all'evento notificato.
	 * @param event L'evento notificato.
	 */
	public void update(ApsEvent event);
	
}
