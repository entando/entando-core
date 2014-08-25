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
package com.agiletec.aps.system.services.authorization.authorizator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IApsAuthority;

/**
 * Classe astratta base per gli oggetti DAO delle autorizzazioni.
 * @author E.Santoboni
 */
public abstract class AbstractApsAutorityDAO extends AbstractDAO implements IApsAuthorityDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(AbstractApsAutorityDAO.class);
	
	@Override
	public List<String> getUserAuthorizated(IApsAuthority auth) throws ApsSystemException {
		List<String> usernames = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
    	ResultSet res = null;
    	try {
    		conn = this.getConnection();
    		stat = conn.prepareStatement(this.getUserAuthorizatedQuery());
    		stat.setString(1, auth.getAuthority());
    		res = stat.executeQuery();
    		while (res.next()) {
    			String username = res.getString(1);
    			usernames.add(username);
			}
    	} catch (Throwable t) {
    		_logger.error("Error while loading authorized usernames",  t);
			throw new RuntimeException("Error while loading authorized usernames", t);
			//processDaoException(t, "Error while loading authorized usernames", "getUserAuthorizated");
    	} finally {
    		closeDaoResources(res, stat, conn);
    	}
    	return usernames;
	}
	
	@Override
	public void setUserAuthorization(String username, IApsAuthority auth) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(this.getAddUserAuthorizationQuery());
			stat.setString(1, username);
			stat.setString(2, auth.getAuthority());
    		stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error while updating user authorizations",  t);
			throw new RuntimeException("Error while updating user authorizations", t);
			//processDaoException(t, "Error while updating user authorizations", "setUserAuthorization");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void removeUserAuthorization(String username, IApsAuthority auth) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(this.getRemoveUserAuthorizationQuery());
			stat.setString(1, username);
			stat.setString(2, auth.getAuthority());
    		stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error detected while removing user authorizations",  t);
			throw new RuntimeException("Error detected while removing user authorizations", t);
			//processDaoException(t, "Error detected while removing user authorizations", "removeUserAuthorization");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void setUserAuthorizations(String username, List<IApsAuthority> auths) throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeUserAuthorizations(username, conn);
			this.addUserAuthorizations(username, auths, conn);
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Error detected while updated using user authorizations",  t);
			throw new RuntimeException("Error detected while updated using user authorizations", t);
			//processDaoException(t, "Error detected while updated using user authorizations", "setUserAuthorizations");
		} finally {
			this.closeConnection(conn);
		}
	}
	
	private void removeUserAuthorizations(String userName, Connection conn) {
		PreparedStatement stat = null;
    	try {
    		stat = conn.prepareStatement(this.getRemoveUserAuthorizationsQuery());
    		stat.setString(1, userName);
    		stat.executeUpdate();
    	} catch (Throwable t) {
    		_logger.error("Error while removing user authorizations",  t);
			throw new RuntimeException("Error while removing user authorizations", t);
			//processDaoException(t, "Error while removing user authorizations", "removeUserRoles");
    	} finally {
    		closeDaoResources(null, stat);
    	}
	}
	
	private void addUserAuthorizations(String username, List<IApsAuthority> auths, Connection conn) {
    	if (auths != null && auths.size()>0) {
    		PreparedStatement stat = null;
        	try {
        		stat = conn.prepareStatement(this.getAddUserAuthorizationQuery());
        		for (int i=0; i<auths.size(); i++) {
        			stat.setString(1, username);
                	stat.setString(2, auths.get(i).getAuthority());
                	stat.addBatch();
                	stat.clearParameters();
        		}
                stat.executeBatch();
        	} catch (Throwable t) {
        		_logger.error("Error while adding an authorization to the user",  t);
			throw new RuntimeException("Error while adding an authorization to the user", t);
			//processDaoException(t, "Error while adding an authorization to the user", "addUserAuthorizations");
        	} finally {
        		closeDaoResources(null, stat);
        	}
    	}
	}
	
	/**
	 * Returns the query to add an authority to an user.
	 * @return The query.
	 */
	protected abstract String getAddUserAuthorizationQuery();
	
	/**
	 * Returns the query to select users with an authority.
	 * @return The query.
	 */
	protected abstract String getUserAuthorizatedQuery();
	
	/**
	 * Returns the query to remove an authority to an user.
	 * @return The query.
	 */
	protected abstract String getRemoveUserAuthorizationQuery();
	
	/**
	 * Returns the query to remove all authorities to an user.
	 * @return The query.
	 */
	protected abstract String getRemoveUserAuthorizationsQuery();
	
	@Override
	public List<String> getAuthorizationNamesForUser(String username) throws ApsSystemException {
		List<String> authNames = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
    	ResultSet res = null;
    	try {
    		conn = this.getConnection();
    		stat = conn.prepareStatement(this.getLoadAuthsForUserQuery());
    		stat.setString(1, username);
    		res = stat.executeQuery();
    		while (res.next()) {
    			String auth = res.getString(1);
    			authNames.add(auth);
			}
    	} catch (Throwable t) {
    		_logger.error("Error while loading authorizations",  t);
			throw new RuntimeException("Error while loading authorizations", t);
			//processDaoException(t, "Error while loading authorizations", "getAuthorizationNamesForUser");
    	} finally {
    		closeDaoResources(res, stat, conn);
    	}
    	return authNames;
	}
	
	/**
	 * Returns the query to select the authorities of a user.
	 * @return The query.
	 */
	protected abstract String getLoadAuthsForUserQuery();
	
}