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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Rappresentazione della mappa dei datatype pubblicati esplicitamene nelle
 * pagine del portale.
 */
public class DataObjectPageMapper implements Serializable {

	private Map<String, String> dataTypePageMapper;
	
    /**
     * Inizializza la mappa.
     */
    public DataObjectPageMapper() {
        this.dataTypePageMapper = new HashMap<>();
    }

    /**
     * Aggiunge un datatype nella mappa dei datatypes publicati.
     *
     * @param dataId L'id del datatype.
     * @param pageCode Il codice della pagina nel quale è publicato il datatype.
     */
    public void add(String dataId, String pageCode) {
        this.dataTypePageMapper.put(dataId, pageCode);
    }

    /**
     * Restituisce la mappa dei datatypes pubblicati.
     *
     * @return La mappa dei datatypes pubblicati.
     */
    protected Map<String, String> getDataObjectPageMapper() {
        return dataTypePageMapper;
    }

    /**
     * Restituisce il codice pagina nel quale è stato publicato il datatype
     * specificato. Restituisce null nel caso in cui l'id del datatype non sia
     * presente nella mappa.
     *
     * @param dataId L'id del datatype.
     * @return Il codice pagina nel quale è stato publicato il datatype
     * specificato.
     */
    public String getPageCode(String dataId) {
        return this.dataTypePageMapper.get(dataId);
    }

    /**
     * Rimuove un'elemento dalla mappa in base all'id del datatype.
     *
     * @param dataId L'id del datatype.
     */
    protected void removeDataObjectPageMapping(String dataId) {
        this.dataTypePageMapper.remove(dataId);
    }

    /**
     * Verifica se un datatype è stato pubblicato esplicitamente in una pagina.
     *
     * @param dataId L'id del datatype.
     * @return true se un datatype è stato pubblicato esplicitamente in una
     * pagina, false in caso contrario.
     */
    protected boolean containDataObject(String dataId) {
        return this.dataTypePageMapper.containsKey(dataId);
	}

}
