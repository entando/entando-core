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
package com.agiletec.plugins.jacms.apsadmin.content;

/**
 * Interfaccia per le action delegate alla gestione della funzione di preview contenuti.
 * @author E.Santoboni
 */
public interface IContentPreviewAction {
	
	/**
	 * Entry point dell'azione di richiesta preview del contenuto in redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String preview();
	
	/**
	 * Esegue l'azione di forward verso la pagina apposita per la preview del contenuto in redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String executePreview();
	
	public static final String PAGE_CODE_PARAM_PREFIX = "jacmsPreviewActionPageCode";
	
	public static final String LANG_CODE_PARAM_PREFIX = "jacmsPreviewActionLangCode";
	
}
