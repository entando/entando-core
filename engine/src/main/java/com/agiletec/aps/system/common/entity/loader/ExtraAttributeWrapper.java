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
package com.agiletec.aps.system.common.entity.loader;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * The Wrapper Class of the extra attribute.
 * @author E.Santoboni
 */
public class ExtraAttributeWrapper {
	
	public AttributeInterface getAttribute() {
		return _attribute;
	}
	public void setAttribute(AttributeInterface attribute) {
		this._attribute = attribute;
	}
	
	/**
	 * (**DEPRECATED since Entando 3.0.1** Use setEntityManagerNameDest) Set the entity manager destination.
	 * @param entityManagerDest The entity manager destination.
	 * @deprecated Since Entando 3.0.1. To avoid circolar references. Use setEntityManagerNameDest
	 */
	public void setEntityManagerDest(IEntityManager entityManagerDest) {
		String name = ((IManager) entityManagerDest).getName();
		this.setEntityManagerNameDest(name);
	}
	
	protected String getEntityManagerNameDest() {
		return _entityManagerNameDest;
	}
	public void setEntityManagerNameDest(String entityManagerNameDest) {
		this._entityManagerNameDest = entityManagerNameDest;
	}
	
	private AttributeInterface _attribute;
	private String _entityManagerNameDest;
	
}