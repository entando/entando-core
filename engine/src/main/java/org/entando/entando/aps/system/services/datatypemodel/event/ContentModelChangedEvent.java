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
package org.entando.entando.aps.system.services.datatypemodel.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.aps.system.services.datatypemodel.ContentModel;

/**
 * Evento specifico da rilanciare in corrispondenza di modifica di un modello di
 * contenuto.
 *
 * @author E.Santoboni - C.Siddi
 */
public class ContentModelChangedEvent extends ApsEvent {

	@Override
	public void notify(IManager srv) {
		((ContentModelChangedObserver) srv).updateFromContentModelChanged(this);
	}

	@Override
	public Class getObserverInterface() {
		return ContentModelChangedObserver.class;
	}

	public ContentModel getContentModel() {
		return _contentModel;
	}

	public void setContentModel(ContentModel contentModel) {
		this._contentModel = contentModel;
	}

	/**
	 * Restituisce il codice dell'operazione che si stà eseguendo sul modello di
	 * contenuto.
	 *
	 * @return Il codice dell'operazione.
	 */
	public int getOperationCode() {
		return _operationCode;
	}

	/**
	 * Setta il codice dell'operazione che si stà eseguendo sul modello di
	 * contenuto.
	 *
	 * @param operationCode Il codice dell'operazione.
	 */
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	private ContentModel _contentModel;

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
