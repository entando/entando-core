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
