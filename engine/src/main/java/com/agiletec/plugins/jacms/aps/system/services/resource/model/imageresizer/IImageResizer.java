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
package com.agiletec.plugins.jacms.aps.system.services.resource.model.imageresizer;

import javax.swing.ImageIcon;

import org.entando.entando.aps.system.services.storage.IStorageManager;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;

/**
 * Interfaccia base per le classi delegate al redimensionameno e salvataggio di file tipo immagine.
 * La classi concrete implementati questa interfaccia vengono utilizzate della classe Wrapper 
 * delle risorse tipo Immagine in occasione della costruzione delle sue istanze (file componenti).
 * @author E.Santoboni
 */
public interface IImageResizer {
	
	/**
	 * Effettua il redimensionameno ed il salvataggio su filesystem del file immagine specificato.
	 * @param imageIcon L'immagine master da cui ricavare l'immagine redimensionata da salvare su disco.
	 * @param filePath Il path assoluto su disco su cui deve essere salvata la risorsa.
	 * Il path è comprensivo del nome del file.
	 * @param dimension Le dimensioni del rettangolo in cui deve essere inscritta l'immagine.
	 * @throws ApsSystemException In caso di errore.
	 * @deprecated 
	 */
	public void saveResizedImage(ImageIcon imageIcon, String filePath, ImageResourceDimension dimension) throws ApsSystemException;
	
	public ResourceInstance saveResizedImage(String subPath, boolean isProtectedResource, 
			ImageIcon imageIcon, ImageResourceDimension dimension) throws ApsSystemException;
	
	public void setStorageManager(IStorageManager storageManager);
	
}