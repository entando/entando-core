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
package com.agiletec.aps.system.services.lang;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia base per i servizi di gestione delle lingue.
 * @author E.Santoboni
 */
public interface ILangManager {
	
	/**
	 * Return the list of assignable langs to system ordered by lang's description.
	 * @return The List of assignable langs.
	 * @throws ApsSystemException 
	 */
	public List<Lang> getAssignableLangs() throws ApsSystemException;
	
	/**
	 * Add a lang on system.
	 * @param code The code of the lang to add.
	 * @throws ApsSystemException In case of error on update config.
	 */
	public void addLang(String code) throws ApsSystemException;
	
	/**
	 * Update the description of a system langs.
	 * @param code The code of the lang to update.
	 * @param descr The new description.
	 * @throws ApsSystemException In case of error on update config.
	 */
	public void updateLang(String code, String descr) throws ApsSystemException;
	
	/**
	 * Remove a lang from the system.
	 * @param code The code of the lang to remove.
	 * @throws ApsSystemException In case of error on update config.
	 */
	public void removeLang(String code) throws ApsSystemException;
	
	/**
	 * Restituisce un oggetto lingua in base al codice
	 * @param code Il codice della lingua
	 * @return La lingua richiesta
	 */
	public Lang getLang(String code);
	
	/**
	 * Return the default lang.
	 * @return The default lang.
	 */
	public Lang getDefaultLang();
	
	/**
	 * Restituisce la lista (ordinata) delle lingue. La lingua di
	 * default è in prima posizione.
	 * @return La lista delle lingue
	 */
	public List<Lang> getLangs();
	
}
