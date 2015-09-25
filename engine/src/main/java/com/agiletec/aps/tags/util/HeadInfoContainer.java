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
package com.agiletec.aps.tags.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contenitore di informazioni da inserire nella testata della pagina html.
 * @author W.Ambu
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