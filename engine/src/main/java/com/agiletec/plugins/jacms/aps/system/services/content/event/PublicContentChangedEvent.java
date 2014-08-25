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
package com.agiletec.plugins.jacms.aps.system.services.content.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Evento specifico da rilanciare in corrispondenza 
 * di approvazione o disapprovazione di un contenuto.
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
	 * @return Il contenuto modificato.
	 */
	public Content getContent() {
		return _content;
	}

	/**
	 * Setta il contenuto modificato, approvato o disapprovato.
	 * @param content Il contenuto modificato.
	 */
	public void setContent(Content content) {
		this._content = content;
	}
	
	/**
	 * Restituisce il codice dell'operazione 
	 * che si stà eseguendo sul contenuto pubblico.
	 * @return Il codice dell'operazione.
	 */
	public int getOperationCode() {
		return _operationCode;
	}

	/**
	 * Setta il codice dell'operazione 
	 * che si stà eseguendo sul contenuto pubblico.
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
