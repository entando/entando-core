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
package org.entando.entando.apsadmin.dataobject.helper;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Interfaccia per gli Helper della DatObjectAction.
 *
 * @author E.Santoboni
 */
public interface IDataObjectActionHelper extends IEntityActionHelper {

	public void updateDataObject(IApsEntity entity, boolean updateMainGroup, HttpServletRequest request);

	/**
	 * Verifica che l'utente corrente possegga i diritti di accesso al DatObject
	 * selezionato.
	 *
	 * @param dataObject Il DataObject.
	 * @param currentUser Il DataObject corrente.
	 * @return True nel caso che l'utente corrente abbia i permessi di
	 * lettura/scrittura sul DatObject, false in caso contrario.
	 */
	public boolean isUserAllowed(DataObject dataObject, UserDetails currentUser);

	public Map getReferencingObjects(DataObject dataObject, HttpServletRequest request) throws ApsSystemException;

	public EntitySearchFilter getOrderFilter(String groupBy, String lastOrder);

	public ActivityStreamInfo createActivityStreamInfo(DataObject dataObject, int strutsAction, boolean addLink);

}
