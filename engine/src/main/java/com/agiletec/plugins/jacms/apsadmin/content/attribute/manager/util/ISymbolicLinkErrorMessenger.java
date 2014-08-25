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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.manager.util;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;

/**
 * Interfaccia di utilità per le Classi analizzatrici della correttezza di link simbolico.
 * @author E.Santoboni
 * @deprecated Moved validation into general validation of link and hypertext attribute
 */
public interface ISymbolicLinkErrorMessenger {

	/**
	 * Analizza un link simbolico ne verifica la correttezza e restituisce
	 * un intero rappresentante il codice dell'eventuale errore riscontrato.
	 * In caso di link a pagina ed a contenuto controlla
	 * la validità dell'elemento referenziato.
	 * @param symbLink Il link simbolico da verificare.
	 * @param content Il contenuto corrente in fase di verifica.
	 * @return Il codice di errore.
	 */
	public int scan(SymbolicLink symbLink, Content content);

	/**
	 * Codice messaggio : Nessun errore riscontrato.
	 */
	public static final int MESSAGE_CODE_NO_ERROR = 0;

	/**
	 * Codice messaggio : Pagina referenziata non corretta.
	 */
	public static final int MESSAGE_CODE_INVALID_PAGE = 1;

	/**
	 * Codice messaggio : Pagina referenziata vuota.
	 */
	public static final int MESSAGE_CODE_VOID_PAGE = 2;

	/**
	 * Codice messaggio : Pagina referenziata con gruppo incompatibile.
	 * L'errore corrisponde al caso in cui la pagina lincata appartiene ad un gruppo che la rende
	 * non accessibile ad uno solo dei gruppi (sia gruppo proprietario che extra) a cui è associato il contenuto.
	 */
	public static final int MESSAGE_CODE_INVALID_PAGE_GROUPS = 3;

	/**
	 * Codice messaggio : Contenuto referenziato non corretto.
	 */
	public static final int MESSAGE_CODE_INVALID_CONTENT = 4;

	/**
	 * Codice messaggio : Contenuto referenziata con gruppi incompatibili.
	 * L'errore corrisponde al caso in cui il contenuto lincato appartiene ad un gruppo che lo rende
	 * non accessibile ad uno solo dei gruppi (sia gruppo proprietario che extra) a cui è associato il contenuto principale.
	 */
	public static final int MESSAGE_CODE_INVALID_CONTENT_GROUPS = 5;

}
