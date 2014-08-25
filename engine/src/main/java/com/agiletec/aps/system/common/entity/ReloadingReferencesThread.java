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
package com.agiletec.aps.system.common.entity;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread Class used to reload all entity references. 
 * @author E.Santoboni
 */
public class ReloadingReferencesThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(ReloadingReferencesThread.class);
	
	/**
	 * Setup the thread for the references reloading
	 * @param entityManager the service that handles the entities
	 * @param typeCode The type Code of entities to reload. If null, reload all entities.
	 */
	public ReloadingReferencesThread(ApsEntityManager entityManager, String typeCode) {
		this._entityManager = entityManager;
		this._typeCode = typeCode;
	}
	
	@Override
	public void run() {
		if (null != this._typeCode) {
			this.reloadEntityTypeReferences(this._typeCode);
		} else {
			List<String> typeCodes = this._entityManager.getEntityTypeCodes();
			Iterator<String> iter = typeCodes.iterator();
			while (iter.hasNext()) {
				String typeCode = (String) iter.next();
				this.reloadEntityTypeReferences(typeCode);
			}
		}
	}
	
	protected void reloadEntityTypeReferences(String typeCode) {
		try {
			this._entityManager.reloadEntitySearchReferencesByType(typeCode);
		} catch (Throwable t) {
			_logger.error("reloadEntityTypeReferences of type : {}", typeCode, t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntityTypeReferences of type : " + typeCode);
		}
	}
	
	private ApsEntityManager _entityManager;
	private String _typeCode;
	
}