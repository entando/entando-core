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
package org.entando.entando.aps.system.services.datatype.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.aps.system.services.datatype.model.Content;

/**
 * Evento specifico da rilanciare in corrispondenza di approvazione o
 * disapprovazione di un contenuto.
 *
 * @author E.Santoboni - M.Diana
 */
public class PublicContentChangedEvent extends ApsEvent {

	@Override
	public void notify(IManager srv) {
		((PublicContentChangedObserver) srv).updateFromPublicContentChanged(this);
	}

	public Class getObserverInterface() {
		return PublicContentChangedObserver.class;
	}

	/**
	 * Restituisce il contenuto modificato, approvato o disapprovato.
	 *
	 * @return Il contenuto modificato.
	 */
	public Content getContent() {
		return _content;
	}

	/**
	 * Setta il contenuto modificato, approvato o disapprovato.
	 *
	 * @param content Il contenuto modificato.
	 */
	public void setContent(Content content) {
		this._content = content;
	}

	/**
	 * Restituisce il codice dell'operazione che si stà eseguendo sul contenuto
	 * pubblico.
	 *
	 * @return Il codice dell'operazione.
	 */
	public int getOperationCode() {
		return _operationCode;
	}

	/**
	 * Setta il codice dell'operazione che si stà eseguendo sul contenuto
	 * pubblico.
	 *
	 * @param operationCode Il codice dell'operazione.
	 */
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	private Content _content;

	private int _operationCode;

	/**
	 * Codice dell'operazione di inserimento del contenuto onLine.
	 */
	public static final int INSERT_OPERATION_CODE = 1;

	/**
	 * Codice dell'operazione di rimozione del contenuto onLine.
	 */
	public static final int REMOVE_OPERATION_CODE = 2;

	/**
	 * Codice dell'operazione di aggiornamento del contenuto onLine.
	 */
	public static final int UPDATE_OPERATION_CODE = 3;

}
