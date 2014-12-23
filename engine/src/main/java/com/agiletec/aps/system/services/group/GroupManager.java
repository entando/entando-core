/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.AbstractService;

/**
 * Servizio gestore dei gruppi.
 * @author E.Santoboni
 */
public class GroupManager extends AbstractService implements IGroupManager  {
	
	private static final Logger _logger = LoggerFactory.getLogger(GroupManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadGroups();
		_logger.debug("{} ready. Initialized {}  user groups", this.getClass().getName(), _groups.size());
	}
	
	private void loadGroups() throws ApsSystemException {
		try {
			_groups = this.getGroupDAO().loadGroups();
		} catch (Throwable t) {
			throw new ApsSystemException("Error loading the group list", t);
		}
	}
	
	/**
	 * Aggiunge un gruppo nel sistema.
	 * @param group Il gruppo da aggiungere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	@Override
	public void addGroup(Group group) throws ApsSystemException {
		try {
			this.getGroupDAO().addGroup(group);
			_groups.put(group.getName(), group );
		} catch (Throwable t) {
			_logger.error("Error detected while adding a group", t);
			throw new ApsSystemException("Error detected while adding a group", t);
		}
	}
	
	/**
	 * Rimuove un gruppo dal sistema.
	 * @param group Il gruppo da rimuovere.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	@Override
	public void removeGroup(Group group) throws ApsSystemException {
		try {
			this.getGroupDAO().deleteGroup(group);
			_groups.remove(group.getName());
		} catch (Throwable t) {
			_logger.error("Error while removing a group", t);
			throw new ApsSystemException("Error while removing a group", t);
		}
	}
	
	/**
	 * Aggiorna un gruppo di sistema.
	 * @param group Il gruppo da aggiornare.
	 * @throws ApsSystemException In caso di errori in accesso al db.
	 */
	@Override
	public void updateGroup(Group group) throws ApsSystemException {
		try {
			this.getGroupDAO().updateGroup(group);
			Group groupInMap = this.getGroup(group.getName());
			groupInMap.setDescr(group.getDescr());
		} catch (Throwable t) {
			_logger.error("Error updating a group", t);
			throw new ApsSystemException("Error updating a group", t);
		}
	}
	
	/**
	 * Restituisce la lista dei gruppi presenti nel sistema.
	 * @return La lista dei gruppi presenti nel sistema.
	 */
	@Override
	public List<Group> getGroups() {
		return new ArrayList<Group>(_groups.values());
	}
	
	/**
	 * Restituisce la mappa dei gruppi presenti nel sistema. 
	 * La mappa è indicizzata in base al nome del gruppo.
	 * @return La mappa dei gruppi presenti nel sistema.
	 */
	@Override
	public Map<String, Group> getGroupsMap() {
		return _groups;
	}
	
	/**
	 * Restituisce un gruppo in base al nome.
	 * @param groupName Il nome del gruppo.
	 * @return Il gruppo cercato.
	 */
	@Override
	public Group getGroup(String groupName) {
		return (Group) this._groups.get(groupName);
	}
	
	protected IGroupDAO getGroupDAO() {
		return this._groupDao;
	}
	public void setGroupDAO(IGroupDAO groupDao) {
		this._groupDao = groupDao;
	}
	
	/**
	 * La mappa dei gruppi presenti nel sistema 
	 * indicizzata in base al nome del gruppo.
	 */
	private Map<String, Group> _groups;
	
	private IGroupDAO _groupDao;
	
}
