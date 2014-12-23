/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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