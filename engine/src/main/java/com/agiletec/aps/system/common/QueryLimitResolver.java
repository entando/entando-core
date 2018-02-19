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

package com.agiletec.aps.system.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryLimitResolver {

    //TODO MOVE
    private static final String JDBC_DRIVER_DERBY_EMBEDDED = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_DRIVER_POSTGRES = "org.postgresql.Driver";

    private static final Logger logger = LoggerFactory.getLogger(QueryLimitResolver.class);

    @SuppressWarnings("rawtypes")
    public static String createLimitBlock(FieldSearchFilter filter, DataSource dataSource) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return createLimitBlock(filter.getOffset(), filter.getLimit(), dataSource);
    }

    public static String createLimitBlock(Integer offset, Integer limit, DataSource dataSource) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String limitBlock = null;
        String driverClassName = extractDriverClassName(dataSource);
        logger.trace("detected driver: {}", driverClassName);
        if (driverClassName.equalsIgnoreCase(JDBC_DRIVER_DERBY_EMBEDDED)) {
            limitBlock = String.format(" OFFSET %d ROWS FETCH NEXT %d ROWS ONLY ", new Object[]{offset, limit});
        } else if (driverClassName.equalsIgnoreCase(JDBC_DRIVER_POSTGRES)) {
            limitBlock = String.format(" OFFSET %d ROWS FETCH NEXT %d ROWS ONLY ", new Object[]{offset, limit});
        } else if (driverClassName.equalsIgnoreCase("TODO")) {
            throw new UnsupportedOperationException(driverClassName + " not implemented!");
        }  else {
            throw new UnsupportedOperationException(driverClassName + " not implemented!");
        }
        logger.trace("sql limit: {}", limitBlock);
        return limitBlock;
    }
    

    /**
     * TODO MAKE IT SMARTER AND JBOSS COMPLIANT
     * @param dataSource
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private static String extractDriverClassName(DataSource dataSource) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = dataSource.getClass().getDeclaredMethod("getDriverClassName");
        String driver = (String) method.invoke(dataSource);
        return driver;
    }

}
