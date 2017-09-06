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
package org.entando.entando.aps.system.services.dataobject.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;

import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Represents the authorization information of a DataType. The enhanced object
 * is cached by alphanumeric identifier produced by the identifier of the
 * DataType.
 *
 * @author E.Santoboni
 */
public class PublicDataTypeAuthorizationInfo implements Serializable {

	private static final long serialVersionUID = -5241592759371755368L;

	@Deprecated
	public PublicDataTypeAuthorizationInfo(DataObject content) {
		this(content, null);
	}

	public PublicDataTypeAuthorizationInfo(DataObject dataObject, List<Lang> langs) {
		this._contentId = dataObject.getId();
		this._dataType = dataObject.getTypeCode();
		this._mainGroup = dataObject.getMainGroup();
		String[] allowedGroups = new String[1 + dataObject.getGroups().size()];
		allowedGroups[0] = dataObject.getMainGroup();
		int index = 1;
		Iterator<String> iterGroup = dataObject.getGroups().iterator();
		while (iterGroup.hasNext()) {
			allowedGroups[index++] = iterGroup.next();
		}
		this.setAllowedGroups(allowedGroups);
	}

	/**
	 * Setta l'array dei codici dei gruppi autorizzati alla visualizzazione del
	 * dataType.
	 *
	 * @param allowedGroups L'array dei codici dei gruppi autorizzati.
	 */
	protected void setAllowedGroups(String[] allowedGroups) {
		this._allowedGroups = allowedGroups;
	}

	public boolean isUserAllowed(Collection<String> userGroupCodes) {
		if (null == userGroupCodes) {
			userGroupCodes = new ArrayList<String>();
		}
		if (userGroupCodes.contains(Group.ADMINS_GROUP_NAME)) {
			return true;
		}
		for (int i = 0; i < _allowedGroups.length; i++) {
			String allowedGroup = _allowedGroups[i];
			if (Group.FREE_GROUP_NAME.equals(allowedGroup)
					|| userGroupCodes.contains(allowedGroup)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica i permessi dell'utente in accesso al dataType. Restituisce true
	 * se l'utente specificato è abilitato ad accedere al dataType, false in
	 * caso contrario.
	 *
	 * @param userGroups I gruppi dell'utente di cui verificarne l'abilitazione.
	 * @return true se l'utente specificato è abilitato ad accedere al dataType,
	 * false in caso contrario.
	 */
	public boolean isUserAllowed(List<Group> userGroups) {
		if (null == userGroups) {
			userGroups = new ArrayList<Group>();
		}
		Set<String> codes = new HashSet<String>();
		for (int i = 0; i < userGroups.size(); i++) {
			Group group = userGroups.get(i);
			codes.add(group.getAuthority());
		}
		return this.isUserAllowed(codes);
	}

	public String getContentId() {
		return _contentId;
	}

	public String getDataType() {
		return _dataType;
	}

	public void setDataType(String dataType) {
		this._dataType = dataType;
	}

	public String getMainGroup() {
		return _mainGroup;
	}

	private String _contentId;
	private String _dataType;

	private String _mainGroup;

	private String[] _allowedGroups = new String[0];

}
