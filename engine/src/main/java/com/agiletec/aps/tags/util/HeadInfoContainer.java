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
package com.agiletec.aps.tags.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contenitore di informazioni da inserire nella testata della pagina html.
 * @author 
 */
public class HeadInfoContainer {
	
	/**
	 * Inizializzazione del container.
	 */
	public HeadInfoContainer(){
		this._container = new HashMap<String, List<Object>>();
	}
	
	/**
	 * Inserisce nel contenitore un'informazione di un dato tipo.
	 * Nel caso dei fogli di stile, il tipo è "StyleSheet" e l'informazione
	 * è una stringa contenente il nome del foglio di stile.
	 * @param type Il tipo di informazione da aggiungere.
	 * @param info L'informazione da aggiungere.
	 */
	public void addInfo(String type, Object info){
		List<Object> infos = this._container.get(type);
		if (infos == null) {
			infos = new ArrayList<Object>();
			this._container.put(type, infos);
		}
		if (!infos.contains(info)) {
			infos.add(info);
		}
	}
	
	/**
	 * Restituisce una collezione di informazioni in base al tipo.
	 * @param type Il tipo delle informazioni richieste.
	 * @return Una collezione di informazioni.
	 */
	public List<Object> getInfos(String type) {
		return this._container.get(type);
	}
	
	private Map<String, List<Object>> _container;
	
}