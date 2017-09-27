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
package org.entando.entando.aps.system.services.dataobjectmodel.event;

import com.agiletec.aps.system.common.notify.ObserverService;

/**
 * Interfaccia base per l'implementazione dei servizi destinatari della
 * notificazione di eventi di modifica di un modello di datatype.
 *
 * @author C.Siddi - E.Santoboni
 */
public interface DataObjectModelChangedObserver extends ObserverService {

    /**
     * Aggiorna il servizio di conseguenza alla notifica di un evento di
     * modifica modello di datatype.
     *
     * @param event L'evento notificato.
     */
    public void updateFromDataObjectModelChanged(DataObjectModelChangedEvent event);

}
