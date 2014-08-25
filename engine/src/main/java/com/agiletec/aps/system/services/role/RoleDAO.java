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
package com.agiletec.aps.system.services.role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.authorizator.AbstractApsAutorityDAO;

/**
 * Data Access Object per gli oggetti ruolo (Role).
 * @version 1.0
 * @author E.Santoboni
 */
public class RoleDAO extends AbstractApsAutorityDAO implements IRoleDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(RoleDAO.class);
	
	/**
	 * Carica da db una mappa completa di tutti i ruoli. Nella mappa, la chiave 
	 * è costituita dal nome del ruolo. Nei ruoli sono caricati tutti i permessi 
	 * assegnati al ruolo. 
	 * @return La mappa completa di tutti i ruoli.
	 */
	public Map<String, Role> loadRoles() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<String, Role> roles = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_ROLES);
			roles = this.loadRoles(res);
		} catch (Throwable t) {
			_logger.error("Error loading roles",  t);
			throw new RuntimeException("Error loading roles", t);
			//processDaoException(t, "Error loading roles", "loadRoles");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return roles;
	}

	/**
	 * Metodo di servizio al metodo loadRoles
	 * @param result L'oggetto ResultSet precaricato.
	 * @return La mappa completa di tutti i ruoli (oggetti Role).
	 * @throws SQLException
	 */
	private Map<String, Role> loadRoles(ResultSet result) throws SQLException {
		HashMap<String, Role> roles = new HashMap<String, Role>();
		Role role = null;
		String prevRoleName = null;
		while (result.next()) {
			//1=rolename, 2=descr, 3=permissionname
			String roleName = result.getString(1);
			if (!roleName.equals(prevRoleName)) {
				if (role != null) {
					roles.put(role.getName(), role);
				}
				role = new Role();
				role.setName(roleName);
				role.setDescription(result.getString(2));
				prevRoleName = roleName;
			}
			role.addPermission(result.getString(3));
		}
		if (role != null){
			roles.put(role.getName(), role);
		}
		return roles;
	}


	/**
	 * Cancella da db tutti i permessi assegnati ad un ruolo.
	 * @param role L'oggetto che rappresenta il ruolo
	 * @param conn La connessione al db
	 * @throws ApsSystemException In caso di eccezione nell'accesso al db.
	 */
	private void deleteRolePermission(Role role, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_ROLE_PERMISSIONS);
			stat.setString(1, role.getName());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error while deleting permissions",  t);
			throw new RuntimeException("Error while deleting permissions", t);
			//processDaoException(t, "Error while deleting permissions", "deleteRolesPermission");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	/**
	 * Salva su db i permessi di un ruolo.
	 * @param role Il ruolo 
	 * @param conn La connessione al db
	 * @throws ApsSystemException In caso di eccezione nell'accesso al db.
	 */
	private void addRolePermissions(Role role, Connection conn) throws ApsSystemException {
		Set<String> permissions = role.getPermissions();
		if (permissions != null && permissions.size()>0) {
			PreparedStatement stat = null;
			try {
				String roleName = role.getName();
				Iterator<String> permissionIter = role.getPermissions().iterator();
				stat = conn.prepareStatement(ADD_ROLE_PERMISSION);
				while (permissionIter.hasNext()) {
					stat.setString(1, roleName);
					stat.setString(2, (String) permissionIter.next());
					stat.addBatch();
					stat.clearParameters();
				}
				stat.executeBatch();
			} catch (Throwable t) {
				_logger.error("Error while adding permissions to a role",  t);
				throw new RuntimeException("Error while adding permissions to a role", t);
				//processDaoException(t, "Error while adding permissions to a role", "addRolePermissions");
			} finally {
				closeDaoResources(null, stat);
			}
		}
	}

	/**
	 * Aggiunge un ruolo ad db.
	 * @param role Oggetto di tipo Role relativo al ruolo da aggiungere.
	 */
	public void addRole(Role role) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_ROLE);
			stat.setString(1, role.getName());
			stat.setString(2, role.getDescription());
			stat.executeUpdate();
			this.addRolePermissions(role, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while adding a role",  t);
			throw new RuntimeException("Error while adding a role", t);
			//processDaoException(t, "Error while adding a role", "addRole");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	/**
	 * Elimima un ruolo dal db.
	 * @param role Il ruolo (oggetto Role) da eliminare dal db.
	 */
	public void deleteRole(Role role) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteRolePermission(role, conn);
			stat = conn.prepareStatement(DELETE_ROLE);
			stat.setString(1, role.getName());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting a role",  t);
			throw new RuntimeException("Error while deleting a role", t);
			//processDaoException(t, "Error while deleting a role", "deleteRole");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	/**
	 * Aggiorna un ruolo nel db.
	 * @param role Il ruolo (oggetto Role) da aggiornare nel db.
	 */
	public void updateRole(Role role) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteRolePermission(role, conn);
			stat = conn.prepareStatement(UPDATE_ROLE);
			stat.setString(1, role.getDescription());
			stat.setString(2, role.getName());
			stat.executeUpdate();
			this.addRolePermissions(role, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while updating a role",  t);
			throw new RuntimeException("Error while updating a role", t);
			//processDaoException(t, "Error while updating a role", "updateRole");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	/**
	 * Restituisce il numero di utenti che utilizzano il ruolo immesso.
	 * @param role Il ruolo di cui trovate il numero di utenti che lo utilizzano.
	 * @return Il numero di utenti che utilizzano quel ruolo.
	 */
	public int getRoleUses(Role role) {
		Connection conn = null;
		int num = 0;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(NUMBER_OF_USER_WITH_ROLE);
			stat.setString(1, role.getName());
			res = stat.executeQuery();
			res.next();
			num = res.getInt(1);
		} catch (Throwable t) {
			_logger.error("Error getting the users sharing the same role",  t);
			throw new RuntimeException("Error getting the users sharing the same role", t);
			//processDaoException(t, "Error getting the users sharing the same role", "getNumberOfUserWithRole");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return num;
	}

	@Override
	protected String getAddUserAuthorizationQuery() {
		return ADD_USER_ROLE;
	}

	@Override
	protected String getRemoveUserAuthorizationQuery() {
		return REMOVE_USER_ROLE;
	}

	@Override
	protected String getRemoveUserAuthorizationsQuery() {
		return REMOVE_USER_ROLES;
	}

	@Override
	protected String getLoadAuthsForUserQuery() {
		return SELECT_ROLES_FOR_USER;
	}

	@Override
	protected String getUserAuthorizatedQuery() {
		return SELECT_USERS_FOR_ROLE;
	}

	private final String NUMBER_OF_USER_WITH_ROLE = 
		"SELECT COUNT(*) FROM authuserroles WHERE rolename = ? ";

	private final String LOAD_ROLES = 
		"SELECT authroles.rolename, authroles.descr,  " +
		"authrolepermissions.permissionname " +
		"FROM authroles LEFT JOIN authrolepermissions " +
		"ON authroles.rolename = authrolepermissions.rolename " +
		"ORDER BY authroles.rolename";	

	private final String DELETE_ROLE_PERMISSIONS = 
		"DELETE FROM authrolepermissions  WHERE rolename = ? ";

	private final String ADD_ROLE_PERMISSION = 
		"INSERT INTO authrolepermissions (rolename, permissionname) VALUES ( ?, ? )";

	private final String ADD_ROLE = 
		"INSERT INTO authroles (rolename, descr) VALUES ( ?, ? )";

	private final String UPDATE_ROLE =
		"UPDATE authroles SET descr = ?  WHERE rolename = ? ";

	private final String DELETE_ROLE = 
		"DELETE FROM authroles WHERE rolename = ? ";

	private final String ADD_USER_ROLE =
		"INSERT INTO authuserroles (username, rolename) VALUES ( ? , ? )";

	private final String REMOVE_USER_ROLE =
		"DELETE FROM authuserroles WHERE username = ? AND rolename = ? ";

	private final String REMOVE_USER_ROLES =
		"DELETE FROM authuserroles WHERE username = ? ";

	private final String SELECT_ROLES_FOR_USER =
		"SELECT rolename FROM authuserroles WHERE username = ? ";

	private final String SELECT_USERS_FOR_ROLE =
		"SELECT username FROM authuserroles WHERE rolename = ? ";

}
