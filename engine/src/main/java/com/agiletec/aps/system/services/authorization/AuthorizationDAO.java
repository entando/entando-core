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
package com.agiletec.aps.system.services.authorization;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class AuthorizationDAO extends AbstractDAO implements IAuthorizationDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(AuthorizationDAO.class);
	
	@Override
	public void addUserAuthorization(String username, Authorization authorization) {
		if (null == authorization || null == username || null == authorization.getGroup()) return;
		if (null == authorization.getRole()) {
			super.executeQueryWithoutResultset(ADD_AUTHORIZATION_WITHOUT_ROLE, 
				username, authorization.getGroup().getName());
		} else {
			super.executeQueryWithoutResultset(ADD_AUTHORIZATION, 
				username, authorization.getGroup().getName(), authorization.getRole().getName());
		}
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
				stat.setString(2, auth.getGroup().getName());
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
				Group group = groups.get(groupname);
				String rolename = res.getString(2);
				Role role = (null != rolename) ? roles.get(rolename) : null;
				authorizations.add(new Authorization(group, role));
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
	public List<String> getUsersByAuthority(IApsAuthority authority) {
		List<String> usernames = new ArrayList<String>();
		if (null == authority) {
			return usernames;
		}
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			String query = (authority instanceof Role) ? GET_USERS_BY_ROLE : GET_USERS_BY_GROUP;
			stat = conn.prepareStatement(query);
			stat.setString(1, authority.getAuthority());
			res = stat.executeQuery();
			while (res.next()) {
				String username = res.getString(1);
				if (!usernames.contains(username)) {
					usernames.add(username);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading users by authority",  t);
			throw new RuntimeException("Error loading users by authority", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return usernames;
	}
	
	private final String ADD_AUTHORIZATION =
		"INSERT INTO authusergrouprole(username, groupname, rolename) VALUES ( ? , ? , ? )";
	
	private final String ADD_AUTHORIZATION_WITHOUT_ROLE =
		"INSERT INTO authusergrouprole(username, groupname) VALUES ( ? , ? )";
	
	private final String DELETE_USER_AUTHORIZATIONS =
		"DELETE FROM authusergrouprole WHERE username = ?";
	
	private final String DELETE_AUTHORIZATION =
		DELETE_USER_AUTHORIZATIONS + " AND groupname = ? AND rolename = ? ";
	
	private final String GET_USER_AUTHORIZATIONS = 
		"SELECT groupname, rolename FROM authusergrouprole WHERE username = ? ";
	
	private final String GET_USERS_BY_ROLE = 
		"SELECT username FROM authusergrouprole WHERE rolename = ? ";
	
	private final String GET_USERS_BY_GROUP = 
		"SELECT username FROM authusergrouprole WHERE groupname = ? ";
	
}
