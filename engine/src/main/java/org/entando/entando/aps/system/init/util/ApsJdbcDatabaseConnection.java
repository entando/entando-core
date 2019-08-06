/*
 * Copyright 2019-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.jdbc.JdbcCompiledStatement;
import com.j256.ormlite.jdbc.JdbcDatabaseConnection;
import com.j256.ormlite.jdbc.TypeValMapper;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.GeneratedKeyHolder;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.Statement;
import java.sql.Types;

public class ApsJdbcDatabaseConnection extends JdbcDatabaseConnection {

    private static Logger logger = LoggerFactory.getLogger(ApsJdbcDatabaseConnection.class);

    private Connection connection;

    public ApsJdbcDatabaseConnection(Connection connection) {
        super(connection);
        this.connection = connection;
    }

    @Override
    public CompiledStatement compileStatement(String statement, StatementType type, FieldType[] argFieldTypes)
            throws SQLException {
        return compileStatement(statement, type, argFieldTypes, DEFAULT_RESULT_FLAGS);
    }

    @Override
    public CompiledStatement compileStatement(String statement, StatementType type, FieldType[] argFieldTypes,
            int resultFlags) throws SQLException {
        if (resultFlags == DatabaseConnection.DEFAULT_RESULT_FLAGS) {
            resultFlags = ResultSet.TYPE_FORWARD_ONLY;
        }
        CompiledStatement compiledStatement = null;
        if (statement.startsWith("CREATE OR REPLACE TRIGGER")) {
            compiledStatement
                    = new ApsJdbcCompiledStatement(connection.createStatement(), statement, type);
        } else {
            compiledStatement
                    = new JdbcCompiledStatement(connection.prepareStatement(statement, resultFlags,
                            ResultSet.CONCUR_READ_ONLY), type);
        }
        logger.trace("compiled statement: {}", statement);
        return compiledStatement;
    }

    @Override
    public int insert(String statement, Object[] args, FieldType[] argFieldTypes, GeneratedKeyHolder keyHolder)
            throws SQLException {
        PreparedStatement stmt;
        if (keyHolder == null) {
            stmt = connection.prepareStatement(statement);
        } else {
            stmt = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        }
        try {
            this.statementSetArgs(stmt, args, argFieldTypes);
            int rowN = stmt.executeUpdate();
            logger.trace("insert statement is prepared and executed: {}", statement);
            if (keyHolder != null) {
                ResultSet resultSet = stmt.getGeneratedKeys();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int colN = metaData.getColumnCount();
                while (resultSet.next()) {
                    for (int colC = 1; colC <= colN; colC++) {
                        // get the id column data so we can pass it back to the caller thru the keyHolder
                        Number id = this.getIdColumnData(resultSet, metaData, colC);
                        keyHolder.addKey(id);
                    }
                }
            }
            return rowN;
        } finally {
            stmt.close();
        }
    }

    private void statementSetArgs(PreparedStatement stmt, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            int typeVal = TypeValMapper.getTypeValForSqlType(argFieldTypes[i].getSqlType());
            if (arg == null) {
                stmt.setNull(i + 1, typeVal);
            } else {
                stmt.setObject(i + 1, arg, typeVal);
            }
        }
    }

    private Number getIdColumnData(ResultSet resultSet, ResultSetMetaData metaData, int columnIndex)
            throws SQLException {
        int typeVal = metaData.getColumnType(columnIndex);
        switch (typeVal) {
            case Types.BIGINT:
            case Types.DECIMAL:
            case Types.NUMERIC:
                return (Number) resultSet.getLong(columnIndex);
            case Types.INTEGER:
                return (Number) resultSet.getInt(columnIndex);
            default:
                //Modification FOR SME
                RowId rowId = resultSet.getRowId(columnIndex);
                return (Number) rowId.hashCode();
        }
    }

}
