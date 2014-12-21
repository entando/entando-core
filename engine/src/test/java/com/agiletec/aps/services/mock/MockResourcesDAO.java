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
package com.agiletec.aps.services.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author M.Casari
 */
public class MockResourcesDAO extends AbstractDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(MockResourcesDAO.class);
	
    /**
     * 
     * @param descr Codice contenuto.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
    public int deleteResource(String descr) throws ApsSystemException {
    	Connection conn = null;
        PreparedStatement stat = null;
        int res = -1;
        try {
        	conn = this.getConnection();
            stat = conn.prepareStatement("delete from resources where descr = ?");
            stat.setString(1, descr);
            res = stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error deleting resource by descr {}", descr,  t);
			throw new RuntimeException("Error deleting resource by descr " + descr, t);
			//processDaoException(t, "Errore in cancellazione risorsa ", "deleteResource");
        } finally {
            closeDaoResources(null, stat, conn);
        }
        return res;
    }    
}
