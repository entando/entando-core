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

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.agiletec.aps.system.services.category.Category;

/**
 * Interfaccia base per l'implementazione degli oggetti detentori 
 * degli elementi necessari per la costruzione degli oggetti risorsa. 
 * Le classi concrete implementanti l'interfaccia sono idonee 
 * ad essere passate al Manager delle risorse per ricavare 
 * gli elementi per la costruzione di una nuova risorsa.
 * @author E.Santoboni
 */
public interface ResourceDataBean {
	
	/**
	 * Restituisce l'identificativo della risorsa.
	 * @return L'identificativo della risorsa.
	 */
	public String getResourceId();
	
	/**
	 * Restituisce il tipo di risorsa.
	 * @return Il tipo di risorsa.
	 */
	public String getResourceType();
	
	/**
	 * Restituisce il mimetype della risorsa.
	 * @return Il mimetype della risorsa.
	 */
	public String getMimeType();
	
	/**
	 * Restituisce la descrizione della risorsa.
	 * @return La descrizione della risorsa.
	 */
	public String getDescr();
	
	/**
     * Restituisce la stringa identificante 
     * il gruppo principale di cui la risorsa è membro.
     * @return Il gruppo principale di cui la risorsa è membro.
     */
	public String getMainGroup();
	
	/**
	 * Restituisce il nome del file relativo alla risorsa da aggiungere.
	 * @return Il nome del file dlla risorsa da aggiungere.
	 */
	public String getFileName();
	
	/**
	 * Restituisce l'InputStream relativo 
	 * al file della risorsa da aggiungere.
	 * @return L'InputStream del file della risorsa da aggiungere.
	 * @throws Throwable In caso di errore.
	 */
	public InputStream getInputStream() throws Throwable;
	
	/**
	 * Restituisce la dimensione del file relativo alla risorsa da aggiungere.
	 * La dimensione deve essere restituita in Kb.
	 * @return La dimensione del file relativo alla risorsa da aggiungere.
	 */
	public int getFileSize();

	/**
	 * Restituisce l'oggetto file 
	 * @return oggetto File
	 */
	public File getFile();
	
	/**
	 * Restituisce la lista di categorie associate alla risorsa.
	 * @return La lista di categorie associate alla risorsa.
	 */
	public List<Category> getCategories();
	
}
