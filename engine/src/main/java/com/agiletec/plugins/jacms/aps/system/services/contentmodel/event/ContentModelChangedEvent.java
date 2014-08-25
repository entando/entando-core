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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;

/**
 * Evento specifico da rilanciare in corrispondenza 
 * di modifica di un modello di contenuto.
 * @author E.Santoboni - C.Siddi
 */
public class ContentModelChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((ContentModelChangedObserver) srv).updateFromContentModelChanged(this);
	}
	
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
	 * Restituisce il codice dell'operazione 
	 * che si stà eseguendo sul modello di contenuto.
	 * @return Il codice dell'operazione.
	 */
	public int getOperationCode() {
		return _operationCode;
	}
	
	/**
	 * Setta il codice dell'operazione 
	 * che si stà eseguendo sul modello di contenuto.
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
