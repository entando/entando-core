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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.DataInstallationReport;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.init.model.TableDumpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class TableDataUtils {

	private static final Logger _logger = LoggerFactory.getLogger(TableDataUtils.class);
	
	public static void valueDatabase(String script, String databaseName, 
			DataSource dataSource, DataInstallationReport schemaReport) throws ApsSystemException {
		try {
            String[] queries = (null != script) ? QueryExtractor.extractQueries(script) : null;
			if (null == queries || queries.length == 0) {
				_logger.info("Script file for db {} void", databaseName);
				if (null != schemaReport) {
					schemaReport.getDatabaseStatus().put(databaseName, SystemInstallationReport.Status.NOT_AVAILABLE);
				}
				return;
			}
			executeQueries(dataSource, queries, true);
			if (null != schemaReport) {
				schemaReport.getDatabaseStatus().put(databaseName, SystemInstallationReport.Status.OK);
			}
		} catch (Throwable t) {
			if (null != schemaReport) {
				schemaReport.getDatabaseStatus().put(databaseName, SystemInstallationReport.Status.INCOMPLETE);
			}
			_logger.error("Error executing script into db {} ", databaseName, t);
			//ApsSystemUtils.logThrowable(t, TableDataUtils.class, "valueDatabase", "Error executing script into db " + databaseName);
			throw new ApsSystemException("Error executing script into db " + databaseName, t);
		}
	}
	
	public static void executeQueries(DataSource dataSource, String[] queries, boolean traceException) throws ApsSystemException {
		if (null == queries || queries.length == 0) return;
		Connection conn = null;
        PreparedStatement stat = null;
		String currentQuery = null;
		try {
			conn = dataSource.getConnection();
            conn.setAutoCommit(false);
			for (int i = 0; i < queries.length; i++) {
				currentQuery = queries[i];
				stat = conn.prepareStatement(currentQuery);
				stat.execute();
			}
			conn.commit();
		} catch (Throwable t) {
			try {
				if (conn != null) {
					conn.rollback();
				}
			} catch (Throwable tr) {
				_logger.error("Error executing rollback", t);
				//ApsSystemUtils.logThrowable(tr, TableDataUtils.class,"executeQueries", "Error executing rollback");
			}
			String errorMessage = "Error executing script - QUERY:\n" + currentQuery;
			if (traceException) {
				_logger.error("Error executing script - QUERY:\n{}", currentQuery, t);
				//ApsSystemUtils.logThrowable(t, TableDataUtils.class, "executeQueries", errorMessage);
			}
			throw new ApsSystemException(errorMessage, t);
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
			} catch (Throwable t) {
				_logger.error("Error while closing the statement", t);
				//ApsSystemUtils.logThrowable(t, TableDataUtils.class, "closeDaoResources", "Error while closing the statement");
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Throwable t) {
				_logger.error("Error closing the connection", t);
				//ApsSystemUtils.logThrowable(t, TableDataUtils.class,  "closeDaoStatement", "Error closing the connection");
			}
		}
	}
	
	public static TableDumpResult dumpTable(DataSource dataSource, String tableName) throws ApsSystemException {
		TableDumpResult report = new TableDumpResult(tableName);
		StringBuilder sqlDump = new StringBuilder();
		StringBuilder scriptPrefix = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
		Connection conn = null;
        PreparedStatement stat = null;
		ResultSet res = null;
		long start = System.currentTimeMillis();
		try {
			conn = dataSource.getConnection();
			stat = conn.prepareStatement("SELECT * FROM " + tableName);
			res = stat.executeQuery();
			ResultSetMetaData metaData = res.getMetaData();
            int columnCount = metaData.getColumnCount();
			int[] types = new int[columnCount];
			for (int i = 0; i < columnCount; i++) {
				if (i>0) {
					scriptPrefix.append(", ");
				}
				int indexColumn = i+1;
				types[i] = metaData.getColumnType(indexColumn);
				scriptPrefix.append(metaData.getColumnName(indexColumn).toLowerCase());
			}
			scriptPrefix.append(") VALUES (");
			int rows = 0;
			while (res.next()) {
                sqlDump.append(scriptPrefix);
                for (int i=0; i<columnCount; i++) {
                    if (i > 0) {
                        sqlDump.append(", ");
                    }
                    Object value = getColumnValue(res, i, types);
                    if (value == null) {
                        sqlDump.append("NULL");
                    } else {
                        String outputValue = value.toString();
						outputValue = outputValue.replaceAll("'","\\''");
						if (isDataNeedsQuotes(types[i])) {
							sqlDump.append("'").append(outputValue).append("'");
						} else {
							sqlDump.append(outputValue);
						}
                    }
                }
                sqlDump.append(");\n");
				rows++;
            }
			report.setSqlDump(sqlDump.toString());
			report.setRows(rows);
		} catch (Throwable t) {
			_logger.error("Error creating backup", t);
			//ApsSystemUtils.logThrowable(t, TableDataUtils.class, 	"dumpTable", "Error creating backup");
			throw new ApsSystemException("Error creating backup", t);
		} finally {
			try {
				if (res != null) {
					res.close();
				}
			} catch (Throwable t) {
				_logger.error("Error while closing the resultset", t);
				//ApsSystemUtils.logThrowable(t, TableDataUtils.class,"dumpTable", "Error while closing the resultset");
			}
			try {
				if (stat != null) {
					stat.close();
				}
			} catch (Throwable t) {
				_logger.error("Error while closing the statement", t);
				//ApsSystemUtils.logThrowable(t, TableDataUtils.class, "dumpTable", "Error while closing the statement");
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Throwable t) {
				_logger.error("Error closing the connection", t);
				//ApsSystemUtils.logThrowable(t, TableDataUtils.class, "dumpTable", "Error closing the connection");
			}
		}
		long time = System.currentTimeMillis() - start;
		report.setRequiredTime(time);
		return report;
	}
	
	private static Object getColumnValue(ResultSet res, int columnIndex, int[] columnTypes) throws SQLException {
		int type = columnTypes[columnIndex];
		int resIndex = columnIndex + 1;
		switch (type) {
            //case Types.ARRAY:
			//	return ....;
            case Types.BIGINT:
				Object bigintObject = res.getObject(resIndex);
				if (null != bigintObject) {
					return (Integer) bigintObject;
				} else {
					return null;
				}
            //case Types.BINARY: 
			//	return ....;
            //case Types.BIT:
			//	return ....;
            case Types.BLOB:
				return res.getBlob(resIndex);
            case Types.BOOLEAN: 
				Boolean bool = res.getBoolean(resIndex);
				if (null == bool) {
					return null;
				}
				return (bool) ? 1 : 0;
            case Types.CHAR:
				return res.getString(resIndex);
            case Types.CLOB:
				return res.getString(resIndex);
            //case Types.DATALINK: 
			//	return ....;
            case Types.DATE:
				Date date = res.getDate(resIndex);
				return getDateAsString(date);
            case Types.DECIMAL:
				return res.getBigDecimal(resIndex);
            //case Types.DISTINCT: 
			//	return ....;
            case Types.DOUBLE: 
				Object doubleObject = res.getObject(resIndex);
				if (null != doubleObject) {
					return (Double) doubleObject;
				} else {
					return null;
				}
            case Types.FLOAT: 
				Object floatObject = res.getObject(resIndex);
				if (null != floatObject) {
					return (Float) floatObject;
				} else {
					return null;
				}
            case Types.INTEGER: 
				Object intObject = res.getObject(resIndex);
				if (null != intObject) {
					return (Integer) intObject;
				} else {
					return null;
				}
            //case Types.JAVA_OBJECT: 
			//	return ....;
            case Types.LONGNVARCHAR: 
				return res.getString(resIndex);
            //case Types.LONGVARBINARY: 
			//	return ....;
            case Types.LONGVARCHAR: 
				return res.getString(resIndex);
            //case Types.NCHAR: 
			//	return ....;
            case Types.NCLOB: 
				return res.getString(resIndex);
            //case Types.NULL: 
			//	return ....;
            //case Types.NUMERIC: 
			//	return ....;
            case Types.NVARCHAR: 
				return res.getString(resIndex);
            //case Types.OTHER: 
			//	return ....;
            //case Types.REAL: 
			//	return ....;
            //case Types.REF: 
			//	return ....;
            //case Types.ROWID: 
			//	return ....;
            case Types.SMALLINT:
				Object shortObject = res.getObject(resIndex);
				if (null != shortObject) {
					return (Integer) shortObject;
				} else {
					return null;
				}
            //case Types.SQLXML:
			//	return ....;
            //case Types.STRUCT:
			//	return ....;
            case Types.TIME:
				Time time = res.getTime(resIndex);
				return getTimeAsString(time);
            case Types.TIMESTAMP:
				Timestamp timestamp = res.getTimestamp(resIndex);
				return getTimestampAsString(timestamp);
            case Types.TINYINT:
				Object tinyintObject = res.getObject(resIndex);
				if (null != tinyintObject) {
					return (Integer) tinyintObject;
				} else {
					return null;
				}
            //case Types.VARBINARY:
			//	return ....;
            case Types.VARCHAR:
				return res.getString(resIndex);
            default:
				return res.getObject(resIndex);
        }
		//return null;
	}
	
	private static String getDateAsString(Date date) {
		if (null == date) {
			return null;
		}
		return DateConverter.getFormattedDate(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	private static String getTimeAsString(Time time) {
		if (null == time) {
			return null;
		}
		Date date = new Date(time.getTime());
		return getDateAsString(date);
	}
	
	private static String getTimestampAsString(Timestamp time) {
		if (null == time) {
			return null;
		}
		Date date = new Date(time.getTime());
		return getDateAsString(date);
	}
	
	private static boolean isDataNeedsQuotes(int type) {
		switch (type) {
            case Types.BIGINT: return false;
            case Types.BOOLEAN: return false;
            case Types.DECIMAL: return false;
            case Types.DOUBLE: return false;
            case Types.FLOAT: return false;
            case Types.INTEGER: return false;
            case Types.NUMERIC: return false;
            case Types.REAL: return false;
            case Types.SMALLINT: return false;
            case Types.TINYINT: return false;
            default: return true;
        }
	}
	
}
