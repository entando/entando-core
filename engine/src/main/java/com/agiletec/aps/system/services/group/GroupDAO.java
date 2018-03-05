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
package com.agiletec.aps.system.services.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object per gli oggetti Group. 
 * @author E.Santoboni
 */
public class GroupDAO extends AbstractSearcherDAO implements IGroupDAO {

    private static final Logger logger = LoggerFactory.getLogger(GroupDAO.class);
	
    @Override
    public int countGroups(FieldSearchFilter[] filters) {
        Integer groups = null;
        try {
            groups = super.countId(filters);
        } catch (Throwable t) {
            logger.error("error in count groups", t);
            throw new RuntimeException("error in count groups", t);
        }
        return groups;
    }

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
            logger.error("Error while loading groups", t);
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
            logger.error("Error while adding a group", t);
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
            logger.error("Error while updating a group", t);
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
            logger.error("Error while deleting a group", t);
			throw new RuntimeException("Error while deleting a group", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return "authgroups";
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "groupname";
    }

    private final String ALL_GROUPS =
            "SELECT groupname, descr FROM authgroups";

    private final String DELETE_GROUP =
            "DELETE FROM authgroups WHERE groupname = ? ";

    private final String ADD_GROUP =
            "INSERT INTO authgroups (groupname ,descr ) VALUES ( ? , ? )";

    private final String UPDATE_GROUP =
            "UPDATE authgroups SET descr= ? WHERE groupname = ? ";

    @Override
    public List<String> searchGroups(FieldSearchFilter[] filters) {
        List<String> groupsNames = null;
        try {
            groupsNames = super.searchId(filters);
        } catch (Throwable t) {
            logger.error("error in search groups", t);
            throw new RuntimeException("error in search groups", t);
        }
        return groupsNames;
    }
	
}
