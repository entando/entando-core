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
package com.agiletec.aps.system.services.page.events;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.services.page.IPage;

/**
 * Evento specifico da rilanciare in corispondenza 
 * di modifica di una pagina del portale.
 * @author E.Santoboni - M.Diana
 */
public class PageChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((PageChangedObserver) srv).updateFromPageChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return PageChangedObserver.class;
	}
	
	/**
	 * Restituisce la pagina modificata.
	 * @return La pagina modificata.
	 */
	public IPage getPage() {
		return _page;
	}

	/**
	 * Setta la pagina modificata.
	 * @param page La pagina modificata.
	 */
	public void setPage(IPage page) {
		this._page = page;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public int getFramePosition() {
		return _framePosition;
	}
	public void setFramePosition(int framePosition) {
		this._framePosition = framePosition;
	}
	
	private IPage _page;
	
	private int _operationCode;
	private int _framePosition;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;
	public static final int EDIT_FRAME_OPERATION_CODE = 4;
	
}
