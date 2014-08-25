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
package org.entando.entando.aps.system.init.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.TableInfo;

/**
 * Couple utility methods for the creating tables.
 * Rewriting of main class com.j256.ormlite.table.TableUtils 
 * for manage Oracle index name max size (30)
 * 
 * As requested, the following notes from license
 * http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_9.html#SEC72
 * 
 * This document is part of the ORMLite project.
 * 
 * Permission to use, copy, modify, and/or distribute this software for any purpose 
 * with or without fee is hereby granted, provided that this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD 
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. 
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, 
 * OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, 
 * DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, 
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE. 
 * 
 * @author graywatson - E.Santoboni
 */
public class ApsTableUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ApsTableUtils.class);
	private static final FieldType[] noFieldTypes = new FieldType[0];

	/**
	 * For static methods only.
	 */
	private ApsTableUtils() {}

	/**
	 * Issue the database statements to create the table associated with a class.
	 * 
	 * @param connectionSource Associated connection source.
	 * @param dataClass The class for which a table will be created.
	 * @return The number of statements executed to do so.
	 */
	public static <T> int createTable(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
		return createTable(connectionSource, dataClass, false);
	}
	
	/**
	 * Create a table if it does not already exist. This is not supported by all databases.
	 */
	public static <T> int createTableIfNotExists(ConnectionSource connectionSource, Class<T> dataClass)
			throws SQLException {
		return createTable(connectionSource, dataClass, true);
	}
	
	private static <T, ID> int createTable(ConnectionSource connectionSource, Class<T> dataClass, boolean ifNotExists)
			throws SQLException {
		Dao<T, ID> dao = DaoManager.createDao(connectionSource, dataClass);
		if (dao instanceof BaseDaoImpl<?, ?>) {
			return doCreateTable(connectionSource, ((BaseDaoImpl<?, ?>) dao).getTableInfo(), ifNotExists);
		} else {
			TableInfo<T, ID> tableInfo = new TableInfo<T, ID>(connectionSource, null, dataClass);
			return doCreateTable(connectionSource, tableInfo, ifNotExists);
		}
	}
	
	/**
	 * Generate and return the list of statements to create a database table and any associated features.
	 */
	private static <T, ID> void addCreateTableStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo,
			List<String> statements, List<String> queriesAfter, boolean ifNotExists) throws SQLException {
		StringBuilder sb = new StringBuilder(256);
		sb.append("CREATE TABLE ");
		if (ifNotExists && databaseType.isCreateIfNotExistsSupported()) {
			sb.append("IF NOT EXISTS ");
		}
		databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
		sb.append(" (");
		List<String> additionalArgs = new ArrayList<String>();
		List<String> statementsBefore = new ArrayList<String>();
		List<String> statementsAfter = new ArrayList<String>();
		// our statement will be set here later
		boolean first = true;
		for (FieldType fieldType : tableInfo.getFieldTypes()) {
			// skip foreign collections
			if (fieldType.isForeignCollection()) {
				continue;
			} else if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			String columnDefinition = fieldType.getColumnDefinition();
			if (columnDefinition == null) {
				// we have to call back to the database type for the specific create syntax
				databaseType.appendColumnArg(tableInfo.getTableName(), sb, fieldType, additionalArgs, statementsBefore,
						statementsAfter, queriesAfter);
			} else {
				// hand defined field
				databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
				sb.append(' ').append(columnDefinition).append(' ');
			}
		}
		// add any sql that sets any primary key fields
		databaseType.addPrimaryKeySql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter,
				queriesAfter);
		// add any sql that sets any unique fields
		databaseType.addUniqueComboSql(tableInfo.getFieldTypes(), additionalArgs, statementsBefore, statementsAfter,
				queriesAfter);
		for (String arg : additionalArgs) {
			// we will have spat out one argument already so we don't have to do the first dance
			sb.append(", ").append(arg);
		}
		sb.append(") ");
		databaseType.appendCreateTableSuffix(sb);
		statements.addAll(statementsBefore);
		statements.add(sb.toString());
		statements.addAll(statementsAfter);
		addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, false);
		addCreateIndexStatements(databaseType, tableInfo, statements, ifNotExists, true);
	}

	private static <T, ID> void addCreateIndexStatements(DatabaseType databaseType, TableInfo<T, ID> tableInfo,
			List<String> statements, boolean ifNotExists, boolean unique) {
		// run through and look for index annotations
		Map<String, List<String>> indexMap = new HashMap<String, List<String>>();
		for (FieldType fieldType : tableInfo.getFieldTypes()) {
			String indexName;
			if (unique) {
				indexName = fieldType.getUniqueIndexName();
			} else {
				indexName = fieldType.getIndexName();
			}
			if (indexName == null) {
				continue;
			}
			indexName = checkOracleIndexKeyName(databaseType, indexName, indexMap);
			List<String> columnList = indexMap.get(indexName);
			if (columnList == null) {
				columnList = new ArrayList<String>();
				indexMap.put(indexName, columnList);
			}
			columnList.add(fieldType.getColumnName());
		}
		StringBuilder sb = new StringBuilder(128);
		for (Map.Entry<String, List<String>> indexEntry : indexMap.entrySet()) {
			logger.info("creating index '{}' for table '{}", indexEntry.getKey(), tableInfo.getTableName());
			sb.append("CREATE ");
			if (unique) {
				sb.append("UNIQUE ");
			}
			sb.append("INDEX ");
			if (ifNotExists && databaseType.isCreateIndexIfNotExistsSupported()) {
				sb.append("IF NOT EXISTS ");
			}
			String indexKey = indexEntry.getKey();
			databaseType.appendEscapedEntityName(sb, indexKey);
			sb.append(" ON ");
			databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
			sb.append(" ( ");
			boolean first = true;
			for (String columnName : indexEntry.getValue()) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				databaseType.appendEscapedEntityName(sb, columnName);
			}
			sb.append(" )");
			statements.add(sb.toString());
			sb.setLength(0);
		}
	}
	
	/**
	 * Extension for Oracle Index max length.
	 * @param databaseType The database type
	 * @param indexName The index name to check
	 * @param indexMap the index map.
	 * @return The processed index name.
	 */
	private static String checkOracleIndexKeyName(DatabaseType databaseType, String indexName, Map<String, List<String>> indexMap) {
		if (!ApsOracleDatabaseType.DATABASE_NAME.equals(databaseType.getDatabaseName())) {
			return indexName;
		}
		String newIndexName = null;
		if (indexName.trim().length() > 28 
				&& databaseType.getDatabaseName().equals(ApsOracleDatabaseType.DATABASE_NAME)) {
			newIndexName = indexName.substring(0, 27);
			int index = 0;
			while (null != indexMap.get(newIndexName + index)) {
				++index;
			}
			newIndexName += index;
		} else {
			newIndexName = indexName;
		}
		return newIndexName;
	}
	
	private static <T, ID> int doCreateTable(ConnectionSource connectionSource, TableInfo<T, ID> tableInfo,
			boolean ifNotExists) throws SQLException {
		DatabaseType databaseType = connectionSource.getDatabaseType();
		logger.info("creating table '{}'", tableInfo.getTableName());
		List<String> statements = new ArrayList<String>();
		List<String> queriesAfter = new ArrayList<String>();
		addCreateTableStatements(databaseType, tableInfo, statements, queriesAfter, ifNotExists);
		DatabaseConnection connection = connectionSource.getReadWriteConnection();
		try {
			int stmtC =
					doStatements(connection, "create", statements, false, databaseType.isCreateTableReturnsNegative(),
							databaseType.isCreateTableReturnsZero());
			stmtC += doCreateTestQueries(connection, databaseType, queriesAfter);
			return stmtC;
		} finally {
			connectionSource.releaseConnection(connection);
		}
	}

	private static int doStatements(DatabaseConnection connection, String label, Collection<String> statements,
			boolean ignoreErrors, boolean returnsNegative, boolean expectingZero) throws SQLException {
		int stmtC = 0;
		for (String statement : statements) {
			int rowC = 0;
			CompiledStatement compiledStmt = null;
			try {
				compiledStmt = connection.compileStatement(statement, StatementBuilder.StatementType.EXECUTE, noFieldTypes);
				rowC = compiledStmt.runExecute();
				logger.info("executed {} table statement changed {} rows: {}", label, rowC, statement);
			} catch (SQLException e) {
				if (ignoreErrors) {
					logger.info("ignoring {} error '{}' for statement: {}", label, e, statement);
				} else {
					throw SqlExceptionUtil.create("SQL statement failed: " + statement, e);
				}
			} finally {
				if (compiledStmt != null) {
					compiledStmt.close();
				}
			}
			// sanity check
			if (rowC < 0) {
				if (!returnsNegative) {
					throw new SQLException("SQL statement " + statement + " updated " + rowC
							+ " rows, we were expecting >= 0");
				}
			} else if (rowC > 0 && expectingZero) {
				throw new SQLException("SQL statement updated " + rowC + " rows, we were expecting == 0: " + statement);
			}
			stmtC++;
		}
		return stmtC;
	}

	private static int doCreateTestQueries(DatabaseConnection connection, DatabaseType databaseType,
			List<String> queriesAfter) throws SQLException {
		int stmtC = 0;
		// now execute any test queries which test the newly created table
		for (String query : queriesAfter) {
			CompiledStatement compiledStmt = null;
			try {
				compiledStmt = connection.compileStatement(query, StatementBuilder.StatementType.SELECT, noFieldTypes);
				// we don't care about an object cache here
				DatabaseResults results = compiledStmt.runQuery(null);
				int rowC = 0;
				// count the results
				for (boolean isThereMore = results.first(); isThereMore; isThereMore = results.next()) {
					rowC++;
				}
				logger.info("executing create table after-query got {} results: {}", rowC, query);
			} catch (SQLException e) {
				// we do this to make sure that the statement is in the exception
				throw SqlExceptionUtil.create("executing create table after-query failed: " + query, e);
			} finally {
				// result set is closed by the statement being closed
				if (compiledStmt != null) {
					compiledStmt.close();
				}
			}
			stmtC++;
		}
		return stmtC;
	}
	
}