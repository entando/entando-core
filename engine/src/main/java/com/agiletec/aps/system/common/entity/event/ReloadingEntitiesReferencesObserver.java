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

import com.agiletec.aps.system.common.notify.ObserverService;

/**
 * Interface for event observers required to reload the entity references.
 * The interface is implemented by the basic service manager entity.
 * @author E.Santoboni
 */
public interface ReloadingEntitiesReferencesObserver extends ObserverService {
	
	/**
	 * Reload the entity references.
	 * @param event The event of required reloading entity references.
	 */
	public void reloadEntitiesReferences(ReloadingEntitiesReferencesEvent event);
	
}