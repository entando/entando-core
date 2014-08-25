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