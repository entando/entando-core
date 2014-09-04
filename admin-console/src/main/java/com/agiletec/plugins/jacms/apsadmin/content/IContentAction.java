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
 * Interfaccia base per le classi Action della redazione contenuti.
 * @author E.Santoboni
 */
public interface IContentAction {

	/**
	 * Esegue l'azione di edit di un contenuto.
	 * @return Il codice del risultato dell'azione.
	 */
	public String edit();

	/**
	 * Esegue l'azione di copia/incolla di un contenuto.
	 * @return Il codice del risultato dell'azione.
	 */
	public String copyPaste();

	/**
	 * Esegue l'azione di associazione di una
	 * categoria al contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 * @deprecated From jAPS 2.0 version 2.1, use joinCategory of {@link IContentCategoryAction} action
	 */
	public String joinCategory();

	/**
	 * Esegue l'azione di rimozione di una
	 * categoria dal contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 * @deprecated From jAPS 2.0 version 2.1, use removeCategory of {@link IContentCategoryAction} action
	 */
	public String removeCategory();

	/**
	 * Esegue l'azione di associazione di un
	 * gruppo al contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String joinGroup();

	/**
	 * Esegue l'azione di rimozione di un
	 * gruppo dal contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeGroup();

	public String saveAndContinue();

	/**
	 * Esegue l'azione di salvataggio del contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String saveContent();

	/**
	 * Esegue l'azione di salvataggio e pubblicazione del contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String saveAndApprove();

	/**
	 * Esegue l'azione di rimozione del contenuto pubblico
	 * e salvataggio del contenuto in fase di redazione.
	 * @return Il codice del risultato dell'azione.
	 */
	public String suspend();

}