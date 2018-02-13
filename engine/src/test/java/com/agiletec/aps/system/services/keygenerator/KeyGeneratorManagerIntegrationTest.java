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
public class KeyGeneratorManagerIntegrationTest extends BaseTestCase {

    private int currentKey;
    private IKeyGeneratorManager keyGeneratorManager = null;

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
        uniqueKeyCurrentValue = keyGeneratorManager.getUniqueKeyCurrentValue();
        int expectedUniqueKeyCurrentValue = currentKey + 1;
        assertEquals(expectedUniqueKeyCurrentValue,uniqueKeyCurrentValue); 
        uniqueKeyCurrentValue = keyGeneratorManager.getUniqueKeyCurrentValue();
        expectedUniqueKeyCurrentValue = currentKey + 2;
        assertEquals(expectedUniqueKeyCurrentValue,uniqueKeyCurrentValue);
        uniqueKeyCurrentValue = keyGeneratorManager.getUniqueKeyCurrentValue();
        expectedUniqueKeyCurrentValue = currentKey + 3;
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
            currentKey = result.getInt(1);
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
            prepStat.setInt(1, currentKey);
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
            keyGeneratorManager = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
