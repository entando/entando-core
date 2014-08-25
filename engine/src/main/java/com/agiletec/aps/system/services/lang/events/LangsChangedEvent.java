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
package com.agiletec.aps.system.services.lang.events;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * Evento specifico da rilanciare in corispondenza 
 * di modifica delle lingue di sistema.
 * @author E.Santoboni - M.Diana
 */
public class LangsChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((LangsChangedObserver) srv).updateFromLangsChanged(this);
	}
	
	public Class getObserverInterface() {
		return LangsChangedObserver.class;
	}
	
}
