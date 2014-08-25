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
package com.agiletec.aps.system.services.keygenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @version 1.0
 * @author W.Ambu
 */
public class TestKeyGeneratorManager extends BaseTestCase {

	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this.extractSequenceNumber();
    }

    protected void tearDown() throws Exception {
    	this.updateSequenceNumber();
        super.tearDown();
    }

    /*
     * Controllo che stia correttamente incrementando di uno la sequence
     */
    public void testGetUniqueKeyCurrentValueWithRightIncrement() throws ApsSystemException{
        int uniqueKeyCurrentValue = 0;
        uniqueKeyCurrentValue = _keyGeneratorManager.getUniqueKeyCurrentValue();      
        int expectedUniqueKeyCurrentValue = _currentKey + 1;
        assertEquals(expectedUniqueKeyCurrentValue,uniqueKeyCurrentValue); 
        uniqueKeyCurrentValue = _keyGeneratorManager.getUniqueKeyCurrentValue();
        expectedUniqueKeyCurrentValue = _currentKey + 2;
        assertEquals(expectedUniqueKeyCurrentValue,uniqueKeyCurrentValue);
        uniqueKeyCurrentValue = _keyGeneratorManager.getUniqueKeyCurrentValue();
        expectedUniqueKeyCurrentValue = _currentKey + 3;
        assertEquals(expectedUniqueKeyCurrentValue,uniqueKeyCurrentValue);
    }
    
    private void extractSequenceNumber() throws Exception {
    	DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		String SELECT_KEY = "SELECT keyvalue FROM uniquekeys WHERE id = 1";
    	Statement prepStat = null;
    	ResultSet result = null;
    	Connection conn = null;
    	try {
    		conn = dataSource.getConnection();
    		prepStat = conn.createStatement();
    		result = prepStat.executeQuery(SELECT_KEY);
    		result.next();
    		_currentKey = result.getInt(1);
    	} catch (Throwable t) {
    		throw new Exception(t);
    	} finally {
    		closeDaoStatement(result, prepStat);
    		conn.close();
    	}
    }
    
    /*
     * Riallinea il dato sul db con quello estratto precedentemente.
     */
    private void updateSequenceNumber() throws Exception {
    	DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		String UPDATE_KEY = "UPDATE uniquekeys SET keyvalue = ? WHERE id = 1";
        PreparedStatement prepStat = null;
        ResultSet result = null;
        Connection conn = null;
        try {
        	conn = dataSource.getConnection();
            prepStat = conn.prepareStatement(UPDATE_KEY);
            prepStat.setInt(1, _currentKey);
            prepStat.executeUpdate();
        } catch (Throwable t) {
    		throw new Exception(t);
        } finally {
            closeDaoStatement(result, prepStat);
            conn.close();
        }
    }
    
    private void closeDaoStatement(ResultSet res, Statement stat) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException e) { }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) { }
        }
    }
    
    private void init() throws Exception {
    	try {
    		_keyGeneratorManager = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private int _currentKey;
    
    private IKeyGeneratorManager _keyGeneratorManager = null;
    
}