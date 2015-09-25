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
package com.agiletec.aps.system.common.entity.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * Event request that triggers the reload of the entity reference.
 * @author E.Santoboni
 */
public class ReloadingEntitiesReferencesEvent extends ApsEvent {
	
	/**
	 * Return the interface of the observer needed to reload entitie reverences
	 * @return Class The observer interface
	 */
	@Override
	public Class getObserverInterface() {
		return ReloadingEntitiesReferencesObserver.class;
	}
	
	@Override
	public void notify(IManager srv) {
		((ReloadingEntitiesReferencesObserver) srv).reloadEntitiesReferences(this);
	}
	
}