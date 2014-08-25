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
package com.agiletec.aps.system.common.entity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Abstract class extended by those DAO that perform searches on entities.
 * @author E.Santoboni
 */
public abstract class AbstractEntitySearcherDAO extends AbstractSearcherDAO implements IEntitySearcherDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractEntitySearcherDAO.class);
	
	@Override
	public List<ApsEntityRecord> searchRecords(EntitySearchFilter[] filters) {
		Connection conn = null;
		List<ApsEntityRecord> records = new ArrayList<ApsEntityRecord>();
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, true, conn);
			result = stat.executeQuery();
			this.flowRecordsResult(records, filters, result);
		} catch (Throwable t) {
			_logger.error("Error while loading records list",  t);
			throw new RuntimeException("Error while loading records list", t);
			//processDaoException(t, "Error while loading records list", "searchRecord");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return records;
	}
	
	protected void flowRecordsResult(List<ApsEntityRecord> records, EntitySearchFilter[] filters, ResultSet result) throws Throwable {
		while (result.next()) {
			ApsEntityRecord record = this.createRecord(result);
			if (!records.contains(record)) {
				if (!this.isForceTextCaseSearch() || null == filters || filters.length == 0) {
					records.add(record);
				} else {
					boolean verify = this.verifyLikeFieldFilters(result, filters);
					if (verify) {
						records.add(record);
					}
				}
			}
		}
	}
	
	protected abstract ApsEntityRecord createRecord(ResultSet result) throws Throwable;
	
	@Override
	public List<String> searchId(String typeCode, EntitySearchFilter[] filters) {
		if (typeCode != null && typeCode.trim().length()>0) {
			EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, typeCode, false);
			EntitySearchFilter[] newFilters = this.addFilter(filters, filter);
			return this.searchId(newFilters);
		}
		return this.searchId(filters);
	}
	
	@Override
	public List<String> searchId(EntitySearchFilter[] filters) {
		Connection conn = null;
		List<String> idList = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = this.buildStatement(filters, false, conn);
			result = stat.executeQuery();
			this.flowResult(idList, filters, result);
		} catch (Throwable t) {
			_logger.error("Error while loading the list of IDs",  t);
			throw new RuntimeException("Error while loading the list of IDs", t);
			//processDaoException(t, "Error while loading the list of IDs", "searchId");
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return idList;
	}
	
	protected EntitySearchFilter[] addFilter(EntitySearchFilter[] filters, EntitySearchFilter filterToAdd){
		int len = 0;
		if (filters != null) {
			len = filters.length;
		}
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for (int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}
	
	protected void flowResult(List<String> contentsId, EntitySearchFilter[] filters, ResultSet result) throws SQLException {
		while (result.next()) {
			String id = result.getString(this.getEntityMasterTableIdFieldName());
			if (contentsId.contains(id)) {
				continue;
			}
			if (!this.isForceTextCaseSearch() || null == filters || filters.length == 0) {
				contentsId.add(id);
			} else {
				boolean verify = this.verifyLikeFieldFilters(result, filters);
				if (verify) {
					contentsId.add(id);
				}
			}
		}
	}
	
	protected boolean verifyLikeFieldFilters(ResultSet result,
			EntitySearchFilter[] likeFieldFilters) throws SQLException {
		boolean verify = true;
		for (int i=0; i<likeFieldFilters.length; i++) {
			EntitySearchFilter filter = likeFieldFilters[i];
			if ((null == filter.getKey() && null == filter.getRoleName()) 
					|| !filter.isLikeOption() || !this.isForceTextCaseSearch()) {
				continue;
			}
			String fieldName = null;
			if (filter.isAttributeFilter()) {
				fieldName = this.getAttributeFieldColunm(filter)+i;
			} else {
				fieldName = this.getTableFieldName(filter.getKey());
			}
			String value = result.getString(fieldName);
			if (null != filter.getValue()) {
				verify = this.checkText((String)filter.getValue(), value, filter.getLikeOptionType());
				if (!verify) {
					break;
				}
			} else if (filter.getAllowedValues() != null && filter.getAllowedValues().size() > 0) {
				List<Object> allowedValues = filter.getAllowedValues();
				verify = this.verifyLikeAllowedValuesFilter(value, allowedValues, filter.getLikeOptionType());
				if (!verify) {
					break;
				}
			}
		}
		return verify;
	}
	
	private PreparedStatement buildStatement(EntitySearchFilter[] filters, boolean selectAll, Connection conn) {
		String query = this.createQueryString(filters, selectAll);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			int index = 0;
			index = this.addAttributeFilterStatementBlock(filters, index, stat);
			index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
		} catch (Throwable t) {
			_logger.error("Error while creating the statement",  t);
			throw new RuntimeException("Error while creating the statement", t);
			//processDaoException(t, "Error while creating the statement", "buildStatement");
		}
		return stat;
	}
	
	/**
	 * Add to the statement the filters on the entity metadata.
	 * @param filters the filters to add to the statement.
	 * @param index The current index of the statement.
	 * @param stat The statement.
	 * @return The current statement index, eventually incremented by filters.
	 * @throws Throwable In case of error.
	 */
	protected int addMetadataFieldFilterStatementBlock(EntitySearchFilter[] filters, int index, PreparedStatement stat) throws Throwable {
		if (filters == null) {
			return index;
		}
		for (int i=0; i<filters.length; i++) {
			EntitySearchFilter filter = filters[i];
			if (filter.getKey() != null && !filter.isAttributeFilter()) {
				index = this.addObjectSearchStatementBlock(filter, index, stat);
			}
		}
		return index;
	}
	
	/**
	 * Add the attribute filters to the statement.
	 * @param filters The filters on the entity filters to insert in the statement.
	 * @param index The last index used to associate the elements to the statement.
	 * @param stat The statement where the filters are applied.
	 * @return The last used index.
	 * @throws SQLException In case of error.
	 */
	protected int addAttributeFilterStatementBlock(EntitySearchFilter[] filters,
			int index, PreparedStatement stat) throws SQLException {
		if (filters == null) {
			return index;
		}
		for (int i=0; i<filters.length; i++) {
			EntitySearchFilter filter = filters[i];
			if ((null != filter.getKey() || null != filter.getRoleName()) && filter.isAttributeFilter()) {
				if (null != filter.getKey()) {
					stat.setString(++index, filter.getKey());
				} else {
					stat.setString(++index, filter.getRoleName());
				}
				index = this.addObjectSearchStatementBlock(filter, index, stat);
			}
		}
		return index;
	}
	
	/**
	 * Add to the statement a filter on a attribute.
	 * @param filter The filter on the attribute to apply in the statement.
	 * @param index The last index used to associate the elements to the statement.
	 * @param stat The statement where the filters are applied.
	 * @return The last used index.
	 * @throws SQLException In case of error.
	 */
	protected int addObjectSearchStatementBlock(EntitySearchFilter filter, int index, PreparedStatement stat) throws SQLException {
		if (filter.isAttributeFilter() && null != filter.getLangCode()) {
			stat.setString(++index, filter.getLangCode());
		}
		return super.addObjectSearchStatementBlock(filter, index, stat);
	}
	
	private String createQueryString(EntitySearchFilter[] filters, boolean selectAll) {
		StringBuffer query = this.createBaseQueryBlock(filters, selectAll);
		boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, false);
		this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);
		boolean ordered = appendOrderQueryBlocks(filters, query, false);
		return query.toString();
	}
	
	/**
	 * Create the 'base block' of the query with the eventual references to the support table.
	 * @param filters The filters defined.
	 * @param selectAll When true, this will insert all the fields in the master table in the select 
	 * of the master query.
	 * When true we select all the available fields; when false only the field addressed by the filter
	 * is selected.
	 * @return The base block of the query.
	 */
	protected StringBuffer createBaseQueryBlock(EntitySearchFilter[] filters, boolean selectAll) {
		StringBuffer query = this.createMasterSelectQueryBlock(filters, selectAll);
		this.appendJoinSerchTableQueryBlock(filters, query);
		return query;
	}
	
	protected StringBuffer createMasterSelectQueryBlock(EntitySearchFilter[] filters, boolean selectAll) {
		String masterTableName = this.getEntityMasterTableName();
		StringBuffer query = new StringBuffer("SELECT ").append(masterTableName).append(".");
		if (selectAll) {
			query.append("* ");
		} else {
			query.append(this.getEntityMasterTableIdFieldName());
		}
		if (this.isForceTextCaseSearch() && filters != null) {
			String searchTableName = this.getEntitySearchTableName();
			for (int i=0; i<filters.length; i++) {
				EntitySearchFilter filter = filters[i];
				if (!filter.isAttributeFilter() && filter.isLikeOption()) {
					String tableFieldName = this.getTableFieldName(filter.getKey());
					//check for id column already present
					if (!tableFieldName.equals(this.getMasterTableIdFieldName())) {
						query.append(", ").append(masterTableName).append(".").append(tableFieldName);
					}
				} else if (filter.isAttributeFilter() && filter.isLikeOption()) {
					String columnName = this.getAttributeFieldColunm(filter);
					query.append(", ").append(searchTableName).append(i).append(".").append(columnName);
					query.append(" AS ").append(columnName).append(i).append(" ");
				}
			}
		}
		query.append(" FROM ").append(masterTableName).append(" ");
		return query;
	}
	
	protected void appendJoinSerchTableQueryBlock(EntitySearchFilter[] filters, StringBuffer query) {
		if (filters == null) {
			return;
		}
		String masterTableName = this.getEntityMasterTableName();
		String masterTableIdFieldName = this.getEntityMasterTableIdFieldName();
		String searchTableName = this.getEntitySearchTableName();
		String searchTableIdFieldName = this.getEntitySearchTableIdFieldName();
		String attributeRoleTableName = this.getEntityAttributeRoleTableName();
		String attributeRoleTableIdFieldName = this.getEntityAttributeRoleTableIdFieldName();
		for (int i=0; i<filters.length; i++) {
			EntitySearchFilter filter = filters[i];
			if ((null != filter.getKey() || null != filter.getRoleName()) && filter.isAttributeFilter() && !filter.isNullOption()) {
				query.append("INNER JOIN ");
				query.append(searchTableName).append(" ").append(searchTableName).append(i).append(" ON ")
					.append(masterTableName).append(".").append(masterTableIdFieldName).append(" = ")
					.append(searchTableName).append(i).append(".").append(searchTableIdFieldName).append(" ");
				if (null != filter.getRoleName()) {
					query.append("INNER JOIN ");
					query.append(attributeRoleTableName).append(" ").append(attributeRoleTableName).append(i).append(" ON ")
						.append(masterTableName).append(".").append(masterTableIdFieldName).append(" = ")
						.append(attributeRoleTableName).append(i).append(".").append(attributeRoleTableIdFieldName).append(" ");
				}
			}
		}
	}
	
	protected boolean appendFullAttributeFilterQueryBlocks(EntitySearchFilter[] filters, StringBuffer query, boolean hasAppendWhereClause) {
		if (filters != null) {
			for (int i=0; i<filters.length; i++) {
				EntitySearchFilter filter = filters[i];
				if (/*filter.getKey() == null || */!filter.isAttributeFilter()) {
					continue;
				}
				//if (filter.isNullOption()) {
				if (filter.isNullOption() && filter.getKey() != null) {
					hasAppendWhereClause = this.appendNullAttributeFilterQueryBlocks(filter, query, hasAppendWhereClause);
				//} else {
				} else if (!filter.isNullOption() && (filter.getKey() != null || filter.getRoleName() != null)) {
					hasAppendWhereClause = this.appendValuedAttributeFilterQueryBlocks(filter, i, query, hasAppendWhereClause);
				}
			}
		}
		return hasAppendWhereClause;
	}
	
	private boolean appendNullAttributeFilterQueryBlocks(EntitySearchFilter filter, StringBuffer query, boolean hasAppendWhereClause) {
		hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
		query.append(this.getEntityMasterTableName()).append(".").append(this.getEntityMasterTableIdFieldName());
		query.append(" NOT IN (");
		String searchTableName = this.getEntitySearchTableName();
		query.append("SELECT ").append(searchTableName).append(".").append(this.getEntitySearchTableIdFieldName());
		query.append(" FROM ").append(searchTableName).append(" WHERE ").append(searchTableName).append(".attrname = ? ");
		this.addAttributeLangQueryBlock(searchTableName, query, filter, true);
		query.append(" AND (").append(searchTableName).append(".datevalue IS NOT NULL OR ").append(searchTableName).append(".textvalue IS NOT NULL OR ").append(searchTableName).append(".numvalue IS NOT NULL) ");
		query.append(" ) ");
		return hasAppendWhereClause;
	}
	
	private boolean appendValuedAttributeFilterQueryBlocks(EntitySearchFilter filter, int index, StringBuffer query, boolean hasAppendWhereClause) {
		String searchTableNameAlias = this.getEntitySearchTableName()+index;
		String attributeRoleTableNameAlias = this.getEntityAttributeRoleTableName()+index;
		hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
		if (null != filter.getKey()) {
			query.append(searchTableNameAlias).append(".attrname = ? ");
		} else {
			query.append(attributeRoleTableNameAlias).append(".rolename = ? ");
			query.append(" AND ").append(searchTableNameAlias).append(".attrname = ").append(attributeRoleTableNameAlias).append(".attrname ");
		}
		hasAppendWhereClause = this.addAttributeLangQueryBlock(searchTableNameAlias, query, filter, hasAppendWhereClause);
		if (filter.isLikeOption() && this.isForceTextCaseSearch()) {
			return hasAppendWhereClause;
		}
		if (filter.getAllowedValues() != null && filter.getAllowedValues().size() > 0) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			List<Object> allowedValues = filter.getAllowedValues();
			for (int j = 0; j < allowedValues.size(); j++) {
				Object allowedValue = allowedValues.get(j);
				if (j == 0) {
					query.append(" ( ");
				} else {
					query.append(" OR ");
				}
				String operator = filter.isLikeOption() ? this.getLikeClause() : "= ? ";
				query.append(searchTableNameAlias).append(".").append(this.getAttributeFieldColunm(allowedValue)).append(" ");
				query.append(operator);
				if (j == (allowedValues.size()-1)) {
					query.append(" ) ");
				}
			}
		} else if (filter.getValue() != null) {
			Object object = filter.getValue();
			String operator = filter.isLikeOption() ? this.getLikeClause() : "= ? ";
			hasAppendWhereClause = this.addAttributeObjectSearchQueryBlock(searchTableNameAlias, query, 
					object, operator, hasAppendWhereClause, filter.getLangCode());
		} else {
			//creazione blocco selezione su tabella ricerca
			if (null != filter.getStart()) {
				hasAppendWhereClause = this.addAttributeObjectSearchQueryBlock(searchTableNameAlias, query, 
						filter.getStart(), ">= ? ", hasAppendWhereClause, filter.getLangCode());
			}
			if (null != filter.getEnd()) {
				hasAppendWhereClause = this.addAttributeObjectSearchQueryBlock(searchTableNameAlias, query, 
						filter.getEnd(), "<= ? ", hasAppendWhereClause, filter.getLangCode());
			}
			if (null == filter.getStart() && null == filter.getEnd()) {
				hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
				query.append(" (").append(searchTableNameAlias).append(".datevalue IS NOT NULL OR ").append(searchTableNameAlias).append(".textvalue IS NOT NULL OR ").append(searchTableNameAlias).append(".numvalue IS NOT NULL) ");
			}
		}
		return hasAppendWhereClause;
	}
	
	protected boolean addAttributeLangQueryBlock(String searchTableName, StringBuffer query, 
			EntitySearchFilter filter, boolean hasAppendWhereClause) {
		if (filter.isAttributeFilter() && null != filter.getLangCode()) {
			hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
			query.append(searchTableName).append(".langcode = ? ");
		}
		return hasAppendWhereClause;
	}
	
	protected boolean appendMetadataFieldFilterQueryBlocks(EntitySearchFilter[] filters, StringBuffer query, boolean hasAppendWhereClause) {
		if (filters == null) {
			return hasAppendWhereClause;
		}
		for (int i=0; i<filters.length; i++) {
			EntitySearchFilter filter = filters[i];
			if (filter.getKey() != null && !filter.isAttributeFilter()) {
				hasAppendWhereClause = this.addMetadataFieldFilterQueryBlock(filter, query, hasAppendWhereClause);
			}
		}
		return hasAppendWhereClause;
	}
	
	protected boolean appendOrderQueryBlocks(EntitySearchFilter[] filters, StringBuffer query, boolean ordered) {
		if (filters == null) {
			return ordered;
		}
		for (int i=0; i<filters.length; i++) {
			EntitySearchFilter filter = filters[i];
			if ((null != filter.getKey() || null != filter.getRoleName()) && null != filter.getOrder() && !filter.isNullOption()) {
				if (!ordered) {
					query.append("ORDER BY ");
					ordered = true;
				} else {
					query.append(", ");
				}
				if (filter.isAttributeFilter()) {
					String tableName = this.getEntitySearchTableName() + i;
					this.addAttributeOrderQueryBlock(tableName, query, filter, filter.getOrder().toString());
				} else {
					String fieldName = this.getTableFieldName(filter.getKey());
					query.append(this.getEntityMasterTableName()).append(".").append(fieldName).append(" ").append(filter.getOrder());
				}
			}
		}
		return ordered;
	}
	
	protected boolean addAttributeObjectSearchQueryBlock(String searchTableName, 
			StringBuffer query, Object object, String operator, boolean hasAppendWhereClause, String langCode) {
		hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
		query.append(searchTableName).append(".").append(this.getAttributeFieldColunm(object)).append(" ");
		query.append(operator);
		return hasAppendWhereClause;
	}
	
	@Override
	protected boolean verifyWhereClauseAppend(StringBuffer query, boolean hasAppendWhereClause) {
		if (hasAppendWhereClause) {
			query.append("AND ");
		} else {
			query.append("WHERE ");
			hasAppendWhereClause = true;
		}
		return hasAppendWhereClause;
	}
	
	private void addAttributeOrderQueryBlock(String searchTableNameAlias, StringBuffer query, EntitySearchFilter filter, String order) {
		if (order == null) {
			order = "";
		}
		Object object = filter.getValue();
		if (object == null) {
			object = filter.getStart();
		}
		if (object == null) {
			object = filter.getEnd();
		}
		if (null == object) {
			query.append(searchTableNameAlias).append(".textvalue ").append(order).append(", ")
				.append(searchTableNameAlias).append(".datevalue ").append(order).append(", ")
				.append(searchTableNameAlias).append(".numvalue ").append(order);
			return;
		}
		query.append(searchTableNameAlias).append(".").append(this.getAttributeFieldColunm(object)).append(" ");
		query.append(order);
	}
	
	private String getAttributeFieldColunm(EntitySearchFilter filter) {
		Object object = null;
		if (null != filter.getAllowedValues() && filter.getAllowedValues().size() > 0) {
			object = filter.getAllowedValues().get(0);
		} else if (null != filter.getValue()) {
			object = filter.getValue();
		} else if (null != filter.getStart()) {
			object = filter.getStart();
		} else if (null != filter.getEnd()) {
			object = filter.getEnd();
		} else {
			return null;
		}
		return this.getAttributeFieldColunm(object);
	}
	
	private String getAttributeFieldColunm(Object attributeValue) {
		String columnName = null;
		if (null == attributeValue) {
			columnName = null;
		} else if (attributeValue instanceof String) {
			columnName = "textvalue";
		} else if (attributeValue instanceof Date) {
			columnName = "datevalue";
		} else if (attributeValue instanceof BigDecimal) {
			columnName = "numvalue";
		} else if (attributeValue instanceof Boolean) {
			columnName = "textvalue";
		}
		return columnName;
	}
	
	/**
	 * Return the name of the entities master table.
	 * @return The name of the master table.
	 */
	protected abstract String getEntityMasterTableName();
	
	/**
	 * Return the name of the "entity ID" field in the master entity table.
	 * @return The name of the "entity ID" field.
	 */
	protected abstract String getEntityMasterTableIdFieldName();
	
	/**
	 * Return the name of the "Entity Type code" in the master entity table.
	 * @return The name of the "Entity Type code".
	 */
	protected abstract String getEntityMasterTableIdTypeFieldName();
	
	/**
	 * Return the name of the support table used to perform search on entities.
	 * @return The name of the support table.
	 */
	protected abstract String getEntitySearchTableName();
	
	/**
	 * Return the name of the "Entity ID" in the support table used to perform search on entities.
	 * @return The name of "Entity ID" field.
	 */
	protected abstract String getEntitySearchTableIdFieldName();
	
	protected abstract String getEntityAttributeRoleTableName();
	
	protected abstract String getEntityAttributeRoleTableIdFieldName();
	
	@Override
	protected String getMasterTableIdFieldName() {
		return this.getEntityMasterTableIdFieldName();
	}
	
	@Override
	protected String getMasterTableName() {
		return this.getEntityMasterTableName();
	}
	
}