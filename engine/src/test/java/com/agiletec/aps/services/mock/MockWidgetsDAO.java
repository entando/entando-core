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
public class MockWidgetsDAO  extends AbstractDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(MockWidgetsDAO.class);

    /**
     * Restituisce un booleano che attesta la presenza o meno della
     * showlet con il codice dato dal parametro code.
     * @param code Codice della showlet
     * @return true se la showlet esiste, false in caso contrario.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
    public boolean exists(String code) throws ApsSystemException {
    	Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        boolean result = false;
        try {
        	conn = this.getConnection();
            stat = conn.prepareStatement("select pagecode from widgetconfig where pagecode=?");
            stat.setString(1, code);
            res = stat.executeQuery();
            result = res.next();
        } catch (Throwable t) {
            _logger.error("Error checking test widget",  t);
			throw new RuntimeException("Error checking test widget", t);
			//processDaoException(t, "Errore in controllo presenza showlet di test", "exists");
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return result;
    }
}
