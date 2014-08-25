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
package com.agiletec.aps.services.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author M.Casari
 */
public class MockUniqueKeysDAO extends AbstractDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(MockUniqueKeysDAO.class);
	
    /**
     * @param id L'id del contatore.
     * @return chiave univoca corrente.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
    public int getCurrentKey(int id) throws ApsSystemException {
    	Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        int current = -1;
        try {
        	conn = this.getConnection();
            stat = conn.prepareStatement("select keyvalue from uniquekeys where id=?");
            stat.setInt(1, id);
            res = stat.executeQuery();
            if (res.next()) {
				current = res.getInt("keyvalue");
			}
        } catch (Throwable t) {
            _logger.error("Error loading unique key",  t);
			throw new RuntimeException("Error loading unique key", t);
			//processDaoException(t, "Errore in controllo presenza showlet di test", "exists");
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return current;
    }
}
