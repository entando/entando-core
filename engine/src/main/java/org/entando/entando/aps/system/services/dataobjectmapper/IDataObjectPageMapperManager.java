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
package org.entando.entando.aps.system.services.dataobjectmapper;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi gestori della mappa dei datatype pubblicati
 * nelle pagine.
 *
 * @author W.Ambu
 */
public interface IDataObjectPageMapperManager {

    /**
     * Effettua il caricamento della mappa dataobject pubblicati / pagine
     *
     * @throws ApsSystemException
     */
    public void reloadDataObjectPageMapper() throws ApsSystemException;

    /**
     * Restituisce il codice pagina nel quale è stato publicato il dataobject
     * specificato. Restituisce null nel caso in cui l'id del datatype non sia
     * presente nella mappa.
     *
     * @param dataobjectId L'id del datatype.
     * @return Il codice pagina nel quale è stato publicato il datatype
     * specificato.
     */
    public String getPageCode(String dataobjectId);

}
