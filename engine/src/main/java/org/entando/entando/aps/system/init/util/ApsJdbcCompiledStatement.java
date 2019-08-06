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

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.jdbc.JdbcDatabaseResults;
import com.j256.ormlite.jdbc.TypeValMapper;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseResults;
import java.sql.Statement;

public class ApsJdbcCompiledStatement implements CompiledStatement {

    private final Statement statement;
    private String query;
    private final PreparedStatement preparedStatement;
    private final StatementType type;
    private ResultSetMetaData metaData = null;

    public ApsJdbcCompiledStatement(PreparedStatement preparedStatement, StatementType type) {
        this.preparedStatement = preparedStatement;
        this.statement = null;
        this.type = type;
    }

    public ApsJdbcCompiledStatement(Statement statement, String query, StatementType type) {
        this.preparedStatement = null;
        this.statement = statement;
        this.query = query;
        this.type = type;
    }

    @Override
    public int getColumnCount() throws SQLException {
        if (metaData == null) {
            metaData = preparedStatement.getMetaData();
        }
        return metaData.getColumnCount();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        if (metaData == null) {
            metaData = preparedStatement.getMetaData();
        }
        return metaData.getColumnName(column + 1);
    }

    @Override
    public int runUpdate() throws SQLException {
        // this can be a UPDATE, DELETE, or ... just not a SELECT
        if (!type.isOkForUpdate()) {
            throw new IllegalArgumentException("Cannot call update on a " + type + " statement");
        }
        if (null != preparedStatement) {
            return preparedStatement.executeUpdate();
        } else {
            return statement.executeUpdate(query);
        }

    }

    @Override
    public DatabaseResults runQuery(ObjectCache objectCache) throws SQLException {
        if (!type.isOkForQuery()) {
            throw new IllegalArgumentException("Cannot call query on a " + type + " statement");
        }
        if (null != preparedStatement) {
            return new JdbcDatabaseResults(preparedStatement, preparedStatement.executeQuery(), objectCache);
        } else {
            //return new JdbcDatabaseResults(statement, statement.executeQuery(query), objectCache);
            return null;
        }
    }

    @Override
    public int runExecute() throws SQLException {
        if (!type.isOkForExecute()) {
            throw new IllegalArgumentException("Cannot call execute on a " + type + " statement");
        }
        if (null != preparedStatement) {
            preparedStatement.execute();
            return preparedStatement.getUpdateCount();
        } else {
            statement.execute(query);
            return statement.getUpdateCount();
        }
    }

    @Override
    public void close() throws SQLException {
        if (null != preparedStatement) {
            preparedStatement.close();
        } else {
            statement.close();
        }
    }

    @Override
    public void setObject(int parameterIndex, Object obj, SqlType sqlType) throws SQLException {
        if (obj == null) {
            preparedStatement.setNull(parameterIndex + 1, TypeValMapper.getTypeValForSqlType(sqlType));
        } else {
            preparedStatement.setObject(parameterIndex + 1, obj, TypeValMapper.getTypeValForSqlType(sqlType));
        }
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        if (null != preparedStatement) {
            preparedStatement.setMaxRows(max);
        } else {
            statement.setMaxRows(max);
        }
    }

    @Override
    public void setQueryTimeout(long millis) throws SQLException {
        if (null != preparedStatement) {
            preparedStatement.setQueryTimeout(Long.valueOf(millis).intValue() / 1000);
        } else {
            statement.setQueryTimeout(Long.valueOf(millis).intValue() / 1000);
        }
    }

    /**
     * Called by {@link JdbcDatabaseResults#next()} to get more results into the
     * existing ResultSet.
     */
    boolean getMoreResults() throws SQLException {
        if (null != preparedStatement) {
            return preparedStatement.getMoreResults();
        } else {
            return statement.getMoreResults();
        }
    }
}
