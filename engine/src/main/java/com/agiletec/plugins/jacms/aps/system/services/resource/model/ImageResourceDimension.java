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