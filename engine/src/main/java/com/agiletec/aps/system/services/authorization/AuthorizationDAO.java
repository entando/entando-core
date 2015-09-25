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
package com.agiletec.aps.system.services.authorization;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class AuthorizationDAO extends AbstractSearcherDAO implements IAuthorizationDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(AuthorizationDAO.class);
	
	@Override
	public void addUserAuthorization(String username, Authorization authorization) {
		if (null == authorization || null == username) return;
		String groupName = (null != authorization.getGroup()) ? authorization.getGroup().getName() : null;
		String roleName = (null != authorization.getRole()) ? authorization.getRole().getName() : null;
		super.executeQueryWithoutResultset(ADD_AUTHORIZATION, username, groupName, roleName);
	}
	
	@Override
	public void addUserAuthorizations(String username, List<Authorization> authorizations) {
		this.addUpdateUserAuthorizations(username, authorizations, false);
	}
	
	@Override
	public void updateUserAuthorizations(String username, List<Authorization> authorizations) {
		this.addUpdateUserAuthorizations(username, authorizations, true);
	}
	
	protected void addUpdateUserAuthorizations(String username, List<Authorization> authorizations, boolean update) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			if (update) {
				super.executeQueryWithoutResultset(conn, DELETE_USER_AUTHORIZATIONS, username);
			}
			this.addUserAuthorizations(username, authorizations, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error detected while addind user authorizations",  t);
			throw new RuntimeException("Error detected while addind user authorizations", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void addUserAuthorizations(String username, List<Authorization> authorizations, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_AUTHORIZATION);
			for (int i=0; i<authorizations.size(); i++) {
				Authorization auth = authorizations.get(i);
				if (null == auth) continue;
				stat.setString(1, username);
				if (null != auth.getGroup()) {
					stat.setString(2, auth.getGroup().getName());
				} else {
					stat.setNull(2, Types.VARCHAR);
				}
				if (null != auth.getRole()) {
					stat.setString(3, auth.getRole().getName());
				} else {
					stat.setNull(3, Types.VARCHAR);
				}
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			_logger.error("Error detected while addind user authorizations",  t);
			throw new RuntimeException("Error detected while addind user authorizations", t);
		} finally {
			this.closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void deleteUserAuthorization(String username, String groupname, String rolename) {
		super.executeQueryWithoutResultset(DELETE_AUTHORIZATION, username, groupname, rolename);
	}
	
	@Override
	public List<Authorization> getUserAuthorizations(String username, Map<String, Group> groups, Map<String, Role> roles) {
		Connection conn = null;
		List<Authorization> authorizations = new ArrayList<Authorization>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_USER_AUTHORIZATIONS);
			stat.setString(1, username);
			res = stat.executeQuery();
			while (res.next()) {
				String groupname = res.getString(1);
				Group group = (null != groupname) ? groups.get(groupname) : null;
				String rolename = res.getString(2);
				Role role = (null != rolename) ? roles.get(rolename) : null;
				Authorization authorization = new Authorization(group, role);
				if (!authorizations.contains(authorization)) {
					authorizations.add(authorization);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading user authorization",  t);
			throw new RuntimeException("Error loading user authorization", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return authorizations;
	}
	
	@Override
	public void deleteUserAuthorizations(String username) {
		super.executeQueryWithoutResultset(DELETE_USER_AUTHORIZATIONS, username);
	}
	
	@Override
	public List<String> getUsersByAuthorities(List<String> groupNames, List<String> roleNames) {
		FieldSearchFilter[] filters = {};
		if (CollectionUtils.isNotEmpty(groupNames)) {
			FieldSearchFilter filter = new FieldSearchFilter("groupname", groupNames, false);
			filters = super.addFilter(filters, filter);
		}
		if (CollectionUtils.isNotEmpty(roleNames)) {
			FieldSearchFilter filter = new FieldSearchFilter("rolename", roleNames, false);
			filters = super.addFilter(filters, filter);
		}
		return super.searchId(filters);
	}
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "authusergrouprole";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "username";
	}
	
	private final String ADD_AUTHORIZATION =
		"INSERT INTO authusergrouprole(username, groupname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_USER_AUTHORIZATIONS =
		"DELETE FROM authusergrouprole WHERE username = ?";
	
	private final String DELETE_AUTHORIZATION =
		DELETE_USER_AUTHORIZATIONS + " AND groupname = ? AND rolename = ? ";
	
	private final String GET_USER_AUTHORIZATIONS = 
		"SELECT groupname, rolename FROM authusergrouprole WHERE username = ? ";
	
}
