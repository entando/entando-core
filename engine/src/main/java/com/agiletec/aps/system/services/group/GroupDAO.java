/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object per gli oggetti Group. 
 * @author E.Santoboni
 */
public class GroupDAO extends AbstractDAO implements IGroupDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(GroupDAO.class);
	
	/**
	 * Carica la mappa dei gruppi presenti nel sistema 
	 * indicizzandola in base al nome del gruppo.
	 * @return La mappa dei gruppi presenti nel sistema.
	 */
	@Override
	public Map<String, Group> loadGroups() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<String, Group> groups = new HashMap<String, Group>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(ALL_GROUPS);
			while (res.next()) {
				Group group = new Group();
				group.setName(res.getString(1));
				group.setDescr(res.getString(2));
				groups.put(group.getName(), group);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading groups",  t);
			throw new RuntimeException("Error while loading groups", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return groups;
	}
	
	/**
	 * Aggiunge un gruppo nel db.
	 * @param group Il gruppo da aggiungere.
	 */
	@Override
	public void addGroup(Group group) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_GROUP);
			stat.setString(1, group.getName());
			stat.setString(2, group.getDescr());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a group",  t);
			throw new RuntimeException("Error while adding a group", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	/**
	 * Aggiorna un gruppo nel db.
	 * @param group Il gruppo da aggiornare.
	 */
	@Override
	public void updateGroup(Group group) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_GROUP);
			stat.setString(1, group.getDescr());
			stat.setString(2, group.getName());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a group",  t);
			throw new RuntimeException("Error while updating a group", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	/**
	 * Rimuove un gruppo dal db.
	 * @param group Il gruppo da rimuovere.
	 */
	@Override
	public void deleteGroup(Group group) {
		this.deleteGroup(group.getName());
	}
	
	/**
	 * Rimuove un gruppo dal sistema.
	 * @param groupName Il nome del gruppo da rimuovere.
	 */
	@Override
	public void deleteGroup(String groupName) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_GROUP);
			stat.setString(1, groupName );
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting a group",  t);
			throw new RuntimeException("Error while deleting a group", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	/*
	@Override
	protected String getLoadAuthsForUserQuery() {
		return SELECT_GROUPS_FOR_USER;
	}
	
	@Override
	protected String getAddUserAuthorizationQuery() {
		return ADD_USER_GROUP;
	}
	
	@Override
	protected String getRemoveUserAuthorizationQuery() {
		return REMOVE_USER_GROUP;
	}
	
	@Override
	protected String getRemoveUserAuthorizationsQuery() {
		return REMOVE_USER_GROUPS;
	}
	
	@Override
	protected String getUserAuthorizatedQuery() {
		return SELECT_USERS_FOR_GROUP;
	}
	*/
	private final String ALL_GROUPS =
		"SELECT groupname, descr FROM authgroups";
	/*
	private final String SELECT_GROUPS_FOR_USER =
		"SELECT groupname FROM authusergroups WHERE username = ? ";
	
	private final String SELECT_USERS_FOR_GROUP =
		"SELECT username FROM authusergroups WHERE groupname = ? ";
	*/
	private final String DELETE_GROUP =
		"DELETE FROM authgroups WHERE groupname = ? ";
	
	private final String ADD_GROUP =
		"INSERT INTO authgroups (groupname ,descr ) VALUES ( ? , ? )";
	
	private final String UPDATE_GROUP =
		"UPDATE authgroups SET descr= ? WHERE groupname = ? ";	
	/*
	private final String ADD_USER_GROUP =
		"INSERT INTO authusergroups (username, groupname) VALUES ( ? , ? )";
	
	private final String REMOVE_USER_GROUP =
		"DELETE FROM authusergroups WHERE username = ? AND groupname = ? ";
	
	private final String REMOVE_USER_GROUPS =
		"DELETE FROM authusergroups WHERE username = ? ";
	*/
	
}
