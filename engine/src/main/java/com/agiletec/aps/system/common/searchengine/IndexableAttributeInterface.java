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
package com.agiletec.aps.system.common.searchengine;

/**
 * Interfaccia per l'implementazione degli attributi 
 * indicizzabili dal motore di ricerca.
 * @version 1.0
 * @author W.Ambu
 */
public interface IndexableAttributeInterface {
    
	/**
	 * Restituisce il testo caratterizzante 
	 * un attributo ai fini della ricerca su db.
	 * @return La stringa corretta per l'interrogazione. 
	 */
    public String getIndexeableFieldValue();
    
	public final String INDEXING_TYPE_NONE = "NONE";
	public final String INDEXING_TYPE_KEYWORD = "KEYWORD";
	public final String INDEXING_TYPE_UNSTORED = "UNSTORED";
	public final String INDEXING_TYPE_TEXT = "TEXT";
	
}
