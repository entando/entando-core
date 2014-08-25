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
package com.agiletec.plugins.jacms.aps.system.services.resource.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * The event of resource changing
 * @author E.Santoboni - M.Diana
 */
public class ResourceChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((ResourceChangedObserver) srv).updateFromResourceChanged(this);
	}
	
	public Class getObserverInterface() {
		return ResourceChangedObserver.class;
	}
	
	public ResourceInterface getResource() {
		return _resource;
	}
	public void setResource(ResourceInterface resource) {
		this._resource = resource;
	}
	
	private ResourceInterface _resource;
	
}