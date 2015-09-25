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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.Serializable;

/**
 * Classe rappresentante una dimensione di resize di una risorsa immagine.
 * @author E.Santoboni
 */
public class ImageResourceDimension implements Serializable {

    /**
     * Setta l'identificativo del resize.
     * @return Returns L'identificativo del resize.
     */
    public int getIdDim() {
    	return _idDim;
    }

    /**
     * Restituisce l'identificativo del resize.
     * @param idDim L'identificativo del resize.
     */
    public void setIdDim(int idDim) {
    	this._idDim = idDim;
    }

    /**
     * Restituisce la dimensione lungo l'asse x in px.
     * @return La dimensione lungo l'asse x in px.
     */
    public int getDimx() {
        return _dimx;
    }

    /**
     * Setta la dimensione lungo l'asse x in px.
     * @param dimx La dimensione lungo l'asse x in px.
     */
    public void setDimx(int dimx) {
        this._dimx = dimx;
    }

    /**
     * Restituisce la dimensione lungo l'asse y in px.
     * @return La dimensione lungo l'asse y in px.
     */
    public int getDimy() {
        return _dimy;
    }

    /**
     * Setta la dimensione lungo l'asse y in px.
     * @param dimy La dimensione lungo l'asse y in px.
     */
    public void setDimy(int dimy) {
        this._dimy = dimy;
    }
    
    private int _idDim;
    private int _dimx;
    private int _dimy;

}