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
package org.entando.entando.aps.system.services.dataobject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;

/**
 * Abstract Data access object used to search dataObjects.
 *
 * @author E.Santoboni
 */
public abstract class AbstractDataObjectSearcherDAO extends AbstractEntitySearcherDAO {

    private static final Logger _logger = LoggerFactory.getLogger(AbstractDataObjectSearcherDAO.class);

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        if (metadataFieldKey.equals(IDataObjectManager.ENTITY_ID_FILTER_KEY)) {
            return this.getEntityMasterTableIdFieldName();
        } else if (metadataFieldKey.equals(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY)) {
            return this.getEntityMasterTableIdTypeFieldName();
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY)) {
            return "descr";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY)) {
            return "status";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY)) {
            return "created";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_MODIFY_DATE_FILTER_KEY)) {
            return "lastmodified";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY)) {
            return "onlinexml";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY)) {
            return "maingroup";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_CURRENT_VERSION_FILTER_KEY)) {
            return "currentversion";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_FIRST_EDITOR_FILTER_KEY)) {
            return "firsteditor";
        } else if (metadataFieldKey.equals(IDataObjectManager.DATA_OBJECT_LAST_EDITOR_FILTER_KEY)) {
            return "lasteditor";
        } else {
            throw new RuntimeException("Chiave di ricerca '" + metadataFieldKey + "' non riconosciuta");
        }
    }

    protected PreparedStatement buildStatement(EntitySearchFilter[] filters,
            Collection<String> userGroupCodes, boolean selectAll, Connection conn) {
        return this.buildStatement(filters, null, false, userGroupCodes, selectAll, conn);
    }

    protected PreparedStatement buildStatement(EntitySearchFilter[] filters,
            String[] categories, Collection<String> userGroupCodes, boolean selectAll, Connection conn) {
        return this.buildStatement(filters, categories, false, userGroupCodes, selectAll, conn);
    }

    protected PreparedStatement buildStatement(EntitySearchFilter[] filters,
            String[] categories, boolean orClauseCategoryFilter,
            Collection<String> userGroupCodes, boolean selectAll, Connection conn) {
        Collection<String> groupsForSelect = this.getGroupsForSelect(userGroupCodes);
        String query = this.createQueryString(filters, categories, orClauseCategoryFilter, groupsForSelect, selectAll);
        //System.out.println("QUERY : " + query);
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(query);
            int index = 0;
            index = super.addAttributeFilterStatementBlock(filters, index, stat);
            index = this.addMetadataFieldFilterStatementBlock(filters, index, stat);
            if (groupsForSelect != null) {
                index = this.addGroupStatementBlock(groupsForSelect, index, stat);
            }
            if (categories != null) {
                index = this.addCategoryStatementBlock(categories, index, stat);
            }
        } catch (Throwable t) {
            _logger.error("Errore in fase di creazione statement", t);
            throw new RuntimeException("Errore in fase di creazione statement", t);
        }
        return stat;
    }

    protected int addGroupStatementBlock(Collection<String> groupCodes, int index, PreparedStatement stat) throws Throwable {
        Iterator<String> groupIter = groupCodes.iterator();
        while (groupIter.hasNext()) {
            String groupName = groupIter.next();
            stat.setString(++index, groupName);
        }
        return index;
    }

    protected int addCategoryStatementBlock(String[] categories, int index, PreparedStatement stat) throws Throwable {
        if (null == categories) {
            return index;
        }
        for (int i = 0; i < categories.length; i++) {
            stat.setString(++index, categories[i]);
        }
        return index;
    }

    protected String createQueryString(EntitySearchFilter[] filters, Collection<String> groupsForSelect, boolean selectAll) {
        return this.createQueryString(filters, null, false, groupsForSelect, selectAll);
    }

    protected String createQueryString(EntitySearchFilter[] filters,
            String[] categories, Collection<String> groupsForSelect, boolean selectAll) {
        return this.createQueryString(filters, categories, false, groupsForSelect, selectAll);
    }

    protected String createQueryString(EntitySearchFilter[] filters,
            String[] categories, boolean orClauseCategoryFilter, Collection<String> groupsForSelect, boolean selectAll) {
        StringBuffer query = this.createBaseQueryBlock(filters, selectAll);
        boolean hasAppendWhereClause = this.appendFullAttributeFilterQueryBlocks(filters, query, false);
        hasAppendWhereClause = this.appendMetadataFieldFilterQueryBlocks(filters, query, hasAppendWhereClause);
        if (null != groupsForSelect && !groupsForSelect.isEmpty()) {
            hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
            this.addGroupsQueryBlock(query, groupsForSelect);
        }
        if (null != categories && categories.length > 0) {
            hasAppendWhereClause = this.verifyWhereClauseAppend(query, hasAppendWhereClause);
            this.addCategoriesQueryBlock(query, categories, !orClauseCategoryFilter);
        }
        boolean ordered = appendOrderQueryBlocks(filters, query, false);
        //System.out.println("********** " + query.toString());
        return query.toString();
    }

    protected void addGroupsQueryBlock(StringBuffer query, Collection<String> userGroupCodes) {
        query.append(" ( ");
        int size = userGroupCodes.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                query.append("OR ");
            }
            query.append("dataobjects.maingroup = ? ");
        }
        query.append(") ");
    }

    protected void addCategoriesQueryBlock(StringBuffer query, String[] categories, boolean andClause) {
        if (categories != null && categories.length > 0) {
            query.append(" ( ");
            for (int i = 0; i < categories.length; i++) {
                if (i > 0) {
                    if (andClause) {
                        query.append(" AND ");
                    } else {
                        query.append(" OR ");
                    }
                }
                query.append(" dataobjects.dataid IN (SELECT dataid FROM ")
                        .append(this.getContentRelationsTableName()).append(" WHERE ")
                        .append(this.getContentRelationsTableName()).append(".refcategory = ? ) ");
            }
            query.append(" ) ");
        }
    }

    protected Collection<String> getGroupsForSelect(Collection<String> userGroupCodes) {
        if (userGroupCodes != null && userGroupCodes.contains(Group.ADMINS_GROUP_NAME)) {
            return null;
        } else {
            Collection<String> groupsForSelect = new HashSet<String>();
            if (userGroupCodes != null) {
                groupsForSelect.addAll(userGroupCodes);
            }
            return groupsForSelect;
        }
    }

    @Override
    protected ApsEntityRecord createRecord(ResultSet result) throws Throwable {
        DataObjectRecordVO contentVo = new DataObjectRecordVO();
        contentVo.setId(result.getString("dataid"));
        contentVo.setTypeCode(result.getString("datatype"));
        contentVo.setDescription(result.getString("descr"));
        contentVo.setStatus(result.getString("status"));
        String xmlWork = result.getString("workxml");
        contentVo.setCreate(DateConverter.parseDate(result.getString("created"), this.DATE_FORMAT));
        contentVo.setModify(DateConverter.parseDate(result.getString("lastmodified"), this.DATE_FORMAT));
        String xmlOnLine = result.getString("onlinexml");
        contentVo.setOnLine(null != xmlOnLine && xmlOnLine.length() > 0);
        contentVo.setSync(xmlWork.equals(xmlOnLine));
        String mainGroupCode = result.getString("maingroup");
        contentVo.setMainGroupCode(mainGroupCode);
        contentVo.setXmlWork(xmlWork);
        contentVo.setXmlOnLine(xmlOnLine);
        contentVo.setVersion(result.getString("currentversion"));
        contentVo.setLastEditor(result.getString("lasteditor"));
        return contentVo;
    }

    @Override
    protected String getEntityMasterTableName() {
        return "dataobjects";
    }

    @Override
    protected String getEntityMasterTableIdFieldName() {
        return "dataid";
    }

    @Override
    protected String getEntityMasterTableIdTypeFieldName() {
        return "datatype";
    }

    protected abstract String getContentRelationsTableName();

    protected final String DATE_FORMAT = "yyyyMMddHHmmss";

}
