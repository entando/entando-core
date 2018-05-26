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

import com.agiletec.aps.system.SystemConstants;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.AbstractEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;

/**
 * DAO class for objects of type dataobject.
 *
 * @author M.Diana - E.Santoboni - S.Didaci
 */
public class DataObjectDAO extends AbstractEntityDAO implements IDataObjectDAO {

    private static final Logger _logger = LoggerFactory.getLogger(DataObjectDAO.class);

    @Override
    protected String getLoadEntityRecordQuery() {
        return LOAD_DATAOBJECT_VO;
    }

    @Override
    protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
        DataObjectRecordVO dataobjectVo = new DataObjectRecordVO();
        dataobjectVo.setId(res.getString(1));
        dataobjectVo.setTypeCode(res.getString(2));
        dataobjectVo.setDescription(res.getString(3));
        dataobjectVo.setStatus(res.getString(4));
        String xmlWork = res.getString(5);
        dataobjectVo.setCreate(DateConverter.parseDate(res.getString(6), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
        dataobjectVo.setModify(DateConverter.parseDate(res.getString(7), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
        String xmlOnLine = res.getString(8);
        dataobjectVo.setOnLine(null != xmlOnLine && xmlOnLine.length() > 0);
        dataobjectVo.setSync(xmlWork.equals(xmlOnLine));
        String mainGroupCode = res.getString(9);
        dataobjectVo.setMainGroupCode(mainGroupCode);
        dataobjectVo.setXmlWork(xmlWork);
        dataobjectVo.setXmlOnLine(xmlOnLine);
        dataobjectVo.setVersion(res.getString(10));
        dataobjectVo.setFirstEditor(res.getString(11));
        dataobjectVo.setLastEditor(res.getString(12));
        return dataobjectVo;
    }

    @Override
    protected void executeAddEntity(IApsEntity entity, Connection conn) throws Throwable {
        super.executeAddEntity(entity, conn);
        this.addDataObjectRelationsRecord((DataObject) entity, conn);
    }

    @Override
    protected String getAddEntityRecordQuery() {
        return ADD_DATAOBJECT;
    }

    @Override
    protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
        DataObject dataobject = (DataObject) entity;
        stat.setString(1, dataobject.getId());
        stat.setString(2, dataobject.getTypeCode());
        stat.setString(3, dataobject.getDescription());
        stat.setString(4, dataobject.getStatus());
        stat.setString(5, dataobject.getXML());
        String currentDate = DateConverter.getFormattedDate(new Date(), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT);
        stat.setString(6, currentDate);
        stat.setString(7, currentDate);
        stat.setString(8, dataobject.getXML());
        stat.setString(9, dataobject.getMainGroup());
        stat.setString(10, dataobject.getVersion());
        stat.setString(11, dataobject.getFirstEditor());
        stat.setString(12, dataobject.getLastEditor());
    }

    @Override
    public void updateDataObject(DataObject dataobject, boolean updateDate) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.executeUpdateData(dataobject, updateDate, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error updating dataobject", t);
            throw new RuntimeException("Error updating dataobject", t);
        } finally {
            this.closeConnection(conn);
        }
    }

    protected void executeUpdateData(DataObject dataobject, boolean updateDate, Connection conn) throws Throwable {
        PreparedStatement stat = null;
        try {
            this.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_REL_RECORD, conn);
            this.deleteRecordsByEntityId(dataobject.getId(), this.getRemovingSearchRecordQuery(), conn);
            this.deleteRecordsByEntityId(dataobject.getId(), this.getRemovingAttributeRoleRecordQuery(), conn);
            if (updateDate) {
                stat = conn.prepareStatement(this.getUpdateEntityRecordQuery());
            } else {
                stat = conn.prepareStatement(this.getUpdateEntityRecordQueryWithoutDate());
            }
            this.buildUpdateEntityStatement(dataobject, updateDate, stat);
            stat.executeUpdate();
            this.addEntitySearchRecord(dataobject.getId(), dataobject, conn);
            this.addEntityAttributeRoleRecord(dataobject.getId(), dataobject, conn);
            this.addDataObjectRelationsRecord(dataobject, conn);
        } catch (Throwable t) {
            throw t;
        } finally {
            this.closeDaoResources(null, stat);
        }
    }

    @Override
    protected void executeUpdateEntity(IApsEntity entity, Connection conn) throws Throwable {
        this.deleteRecordsByEntityId(entity.getId(), DELETE_DATAOBJECT_REL_RECORD, conn);
        super.executeUpdateEntity(entity, conn);
        this.addDataObjectRelationsRecord((DataObject) entity, conn);
    }

    @Override
    protected String getUpdateEntityRecordQuery() {
        return UPDATE_DATAOBJECT;
    }

    protected String getUpdateEntityRecordQueryWithoutDate() {
        return UPDATE_DATAOBJECT_WITHOUT_DATE;
    }

    @Override
    protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
        this.buildUpdateEntityStatement(entity, true, stat);
    }

    protected void buildUpdateEntityStatement(IApsEntity entity, boolean updateDate, PreparedStatement stat) throws Throwable {
        DataObject dataobject = (DataObject) entity;
        int index = 1;
        stat.setString(index++, dataobject.getTypeCode());
        stat.setString(index++, dataobject.getDescription());
        stat.setString(index++, dataobject.getStatus());
        stat.setString(index++, dataobject.getXML());
        if (updateDate) {
            stat.setString(index++, DateConverter.getFormattedDate(new Date(), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
        }
        stat.setString(index++, dataobject.getMainGroup());
        stat.setString(index++, dataobject.getVersion());
        stat.setString(index++, dataobject.getLastEditor());
        stat.setString(index++, dataobject.getId());
    }

    /**
     * This publishes a dataobject.
     *
     * @param dataobject the dataobject to publish.
     */
    @Override
    public void insertDataObject(DataObject dataobject) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.executeInsertDataObject(dataobject, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error publish dataobject {} ", dataobject.getId(), t);
            throw new RuntimeException("Error publish dataobject - " + dataobject.getId(), t);
        } finally {
            this.closeConnection(conn);
        }
    }

    protected void executeInsertDataObject(DataObject dataobject, Connection conn) throws Throwable {
        this.executeInsertDataObject(dataobject, true, conn);
    }

    protected void executeInsertDataObject(DataObject dataobject, boolean updateDate, Connection conn) throws Throwable {
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_SEARCH_RECORD, conn);
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
        super.deleteEntitySearchRecord(dataobject.getId(), conn);
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_REL_RECORD, conn);
        this.updateDataObjectRecordForInsert(dataobject, updateDate, conn);
        this.addDataObjectSearchRecord(dataobject.getId(), dataobject, conn);
        super.addEntitySearchRecord(dataobject.getId(), dataobject, conn);
        this.addDataObjectAttributeRoleRecord(dataobject.getId(), dataobject, ADD_ATTRIBUTE_ROLE_RECORD, conn);
        this.addDataObjectRelationsRecord(dataobject, conn);
    }

    protected void addDataObjectSearchRecord(String id, IApsEntity entity, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_DATAOBJECT_SEARCH_RECORD);
            this.addEntitySearchRecord(id, entity, stat);
        } catch (Throwable t) {
            _logger.error("Error on adding public dataobject search records", t);
            throw new RuntimeException("Error on adding public dataobject search records", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    protected void updateDataObjectRecordForInsert(DataObject dataobject, Connection conn) throws ApsSystemException {
        this.updateDataObjectRecordForInsert(dataobject, true, conn);
    }

    protected void updateDataObjectRecordForInsert(DataObject dataobject, boolean updateDate, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            int index = 1;
            if (updateDate) {
                stat = conn.prepareStatement(INSERT_DATAOBJECT);
            } else {
                stat = conn.prepareStatement(INSERT_DATAOBJECT_WITHOUT_DATE);
            }
            stat.setString(index++, dataobject.getTypeCode());
            stat.setString(index++, dataobject.getDescription());
            stat.setString(index++, dataobject.getStatus());
            String xml = dataobject.getXML();
            stat.setString(index++, xml);
            if (updateDate) {
                stat.setString(index++, DateConverter.getFormattedDate(new Date(), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
            }
            stat.setString(index++, xml);
            stat.setString(index++, dataobject.getMainGroup());
            stat.setString(index++, dataobject.getVersion());
            stat.setString(index++, dataobject.getLastEditor());
            stat.setString(index++, dataobject.getId());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error updating for insert onLine dataobject {}", dataobject.getId(), t);
            throw new RuntimeException("Error updating for insert onLine dataobject " + dataobject.getId(), t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    /**
     * Updates the references of a published dataobject
     *
     * @param dataobject the published dataobject
     */
    @Override
    public void reloadDataObjectReferences(DataObject dataobject) {
        if (dataobject.isOnLine()) {
            Connection conn = null;
            try {
                conn = this.getConnection();
                conn.setAutoCommit(false);
                this.executeReloadDataObjectReferences(dataobject, conn);
                conn.commit();
            } catch (Throwable t) {
                this.executeRollback(conn);
                _logger.error("Error reloading references - Data {}", dataobject.getId(), t);
                throw new RuntimeException("Error reloading references - Data " + dataobject.getId(), t);
            } finally {
                this.closeConnection(conn);
            }
        }
    }

    protected void executeReloadDataObjectReferences(DataObject dataobject, Connection conn) throws Throwable {
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_SEARCH_RECORD, conn);
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_REL_RECORD, conn);
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
        this.addDataObjectSearchRecord(dataobject.getId(), dataobject, conn);
        this.addDataObjectRelationsRecord(dataobject, conn);
        this.addDataObjectAttributeRoleRecord(dataobject.getId(), dataobject, ADD_ATTRIBUTE_ROLE_RECORD, conn);
    }

    /**
     * Unpublish a dataobject, preventing it from being displayed in the portal.
     * Obviously the dataobject itslef is not deleted.
     *
     * @param dataobject the dataobject to unpublish.
     */
    @Override
    public void removeDataObject(DataObject dataobject) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.executeRemoveDataObject(dataobject, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error removing online dataobject {}", dataobject.getId(), t);
            throw new RuntimeException("Error removing online dataobject - " + dataobject.getId(), t);
        } finally {
            this.closeConnection(conn);
        }
    }

    protected void executeRemoveDataObject(DataObject dataobject, Connection conn) {
        this.executeRemoveDataObject(dataobject, true, conn);
    }

    /**
     * Unpublish a dataobject, preventing it from being displayed in the portal.
     * Obviously the dataobject itslef is not deleted.
     *
     * @param dataobject the dataobject to unpublish.
     * @param updateDate
     * @param conn the connection to the DB.
     */
    protected void executeRemoveDataObject(DataObject dataobject, boolean updateDate, Connection conn) {
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_SEARCH_RECORD, conn);
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_DATAOBJECT_REL_RECORD, conn);
        super.deleteRecordsByEntityId(dataobject.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
        PreparedStatement stat = null;
        try {
            if (updateDate) {
                stat = conn.prepareStatement(REMOVE_DATAOBJECT);
            } else {
                stat = conn.prepareStatement(REMOVE_DATAOBJECT_WITHOUT_DATE);
            }
            int index = 1;
            stat.setString(index++, null);
            stat.setString(index++, dataobject.getStatus());
            stat.setString(index++, dataobject.getXML());
            if (updateDate) {
                stat.setString(index++, DateConverter.getFormattedDate(new Date(), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT));
            }
            stat.setString(index++, dataobject.getVersion());
            stat.setString(index++, dataobject.getLastEditor());
            stat.setString(index++, dataobject.getId());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error removing online dataobject {}", dataobject.getId(), t);
            throw new RuntimeException("Error removing online dataobject - " + dataobject.getId(), t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    @Override
    protected String getDeleteEntityRecordQuery() {
        return DELETE_DATAOBJECT;
    }

    @Override
    protected void executeDeleteEntity(String entityId, Connection conn) throws Throwable {
        super.deleteRecordsByEntityId(entityId, DELETE_DATAOBJECT_SEARCH_RECORD, conn);
        super.deleteRecordsByEntityId(entityId, DELETE_DATAOBJECT_REL_RECORD, conn);
        super.deleteRecordsByEntityId(entityId, DELETE_ATTRIBUTE_ROLE_RECORD, conn);
        super.executeDeleteEntity(entityId, conn);
    }

    private void addCategoryRelationsRecord(DataObject dataobject, boolean isPublicRelations, PreparedStatement stat) throws ApsSystemException {
        if (dataobject.getCategories().size() > 0) {
            try {
                Set<String> codes = new HashSet<String>();
                Iterator<Category> categoryIter = dataobject.getCategories().iterator();
                while (categoryIter.hasNext()) {
                    Category category = (Category) categoryIter.next();
                    this.addCategoryCode(category, codes);
                }
                Iterator<String> codeIter = codes.iterator();
                while (codeIter.hasNext()) {
                    String code = codeIter.next();
                    int i = 1;
                    stat.setString(i++, dataobject.getId());
                    /*
					if (isPublicRelations) {
						stat.setString(i++, null);
						stat.setString(i++, null);
						stat.setBigDecimal(i++, null);
					}
                     */
                    stat.setString(i++, code);
                    if (isPublicRelations) {
                        stat.setString(i++, null);
                    }
                    stat.addBatch();
                    stat.clearParameters();
                }
            } catch (SQLException e) {
                _logger.error("Error saving dataobject relation record for dataobject {}", dataobject.getId(), e.getNextException());
                throw new RuntimeException("Error saving dataobject relation record for dataobject " + dataobject.getId(), e.getNextException());
            }
        }
    }

    private void addCategoryCode(Category category, Set<String> codes) {
        codes.add(category.getCode());
        Category parentCategory = (Category) category.getParent();
        if (null != parentCategory && !parentCategory.getCode().equals(parentCategory.getParentCode())) {
            this.addCategoryCode(parentCategory, codes);
        }
    }

    private void addGroupRelationsRecord(DataObject dataobject, PreparedStatement stat) throws ApsSystemException {
        try {
            dataobject.addGroup(dataobject.getMainGroup());
            Iterator<String> groupIter = dataobject.getGroups().iterator();
            while (groupIter.hasNext()) {
                String groupName = groupIter.next();
                stat.setString(1, dataobject.getId());
                /*
				stat.setString(2, null);
				stat.setString(3, null);
				stat.setBigDecimal(4, null);
                 */
                stat.setString(2, null);
                stat.setString(3, groupName);
                stat.addBatch();
                stat.clearParameters();
            }
        } catch (Throwable t) {
            _logger.error("Error saving group relation record for dataobject {}", dataobject.getId(), t);
            throw new RuntimeException("Error saving group relation record for dataobject " + dataobject.getId(), t);
        }
    }

    /**
     * Add a record in the table 'dataobjectrelations' for every resource, page,
     * other dataobject, role and category associated to the given dataobject).
     *
     * @param dataobject The current dataobject.
     * @param conn The connection to the database.
     * @throws ApsSystemException when connection error are detected.
     */
    protected void addDataObjectRelationsRecord(DataObject dataobject, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_DATAOBJECT_REL_RECORD);
            this.addCategoryRelationsRecord(dataobject, true, stat);
            this.addGroupRelationsRecord(dataobject, stat);
            EntityAttributeIterator attributeIter = new EntityAttributeIterator(dataobject);
            while (attributeIter.hasNext()) {
                AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
            }
            stat.executeBatch();
        } catch (BatchUpdateException e) {
            _logger.error("Error saving record into dataobjectrelations {}", dataobject.getId(), e.getNextException());
            throw new RuntimeException("Error saving record into dataobjectrelations " + dataobject.getId(), e.getNextException());
        } catch (Throwable t) {
            _logger.error("Error saving record into dataobjectrelations {}", dataobject.getId(), t);
            throw new RuntimeException("Error saving record into dataobjectrelations " + dataobject.getId(), t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    protected void addDataObjectAttributeRoleRecord(String id, IApsEntity entity, String query, Connection conn) throws ApsSystemException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(query);
            super.addEntityAttributeRoleRecord(id, entity, stat);
        } catch (Throwable t) {
            _logger.error("Error on adding dataobject attribute role records", t);
            throw new RuntimeException("Error on adding dataobject attribute role records", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    protected List<String> getUtilizers(String referencedObjectCode, String query) throws Throwable {
        Connection conn = null;
        List<String> dataids = new ArrayList<String>();
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(query);
            stat.setString(1, referencedObjectCode);
            res = stat.executeQuery();
            while (res.next()) {
                String id = res.getString(1);
                dataids.add(id);
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return dataids;
    }

    @Override
    public DataObjectsStatus loadDataObjectsStatus() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        DataObjectsStatus status = null;
        try {
            conn = this.getConnection();
            int online = this.loadDataObjectStatus(conn, COUNT_DATAOBJECTS);
            int offline = this.loadDataObjectStatus(conn, COUNT_OFFLINE_DATAOBJECTS);
            int withDiffs = this.loadDataObjectStatus(conn, COUNT_DATAOBJECTS_WITH_DIFFS);
            Date lastModified = this.loadDataObjectStatusLastModified(conn, LOAD_LAST_MODIFIED);
            status = new DataObjectsStatus();
            status.setDraft(offline);
            status.setOnline(online);
            status.setOnlineWithChanges(withDiffs);
            status.setLastUpdate(lastModified);
        } catch (Throwable t) {
            _logger.error("Error loading dataobjects status", t);
            throw new RuntimeException("Error loading dataobjects status", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return status;
    }

    private int loadDataObjectStatus(Connection conn, String query) {
        PreparedStatement stat = null;
        ResultSet res = null;
        int count = 0;
        try {
            stat = conn.prepareStatement(query);
            res = stat.executeQuery();
            if (res.next()) {
                count = res.getInt(1);
            }
        } catch (Throwable t) {
            _logger.error("Error loading dataobjects status. If you are runing Entando backed by Apache Derby it's a known issue");
        } finally {
            closeDaoResources(res, stat);
        }
        return count;
    }

    private Date loadDataObjectStatusLastModified(Connection conn, String query) {
        PreparedStatement stat = null;
        ResultSet res = null;
        Date lastModified = null;
        try {
            stat = conn.prepareStatement(query);
            res = stat.executeQuery();
            if (res.next()) {
                String lastMod = res.getString("lastmodified");
                lastModified = DateConverter.parseDate(lastMod, "yyyyMMddHHmmss");
            }
        } catch (Throwable t) {
            _logger.error("Error loading dataobjects status last modified date", t);
            throw new RuntimeException("Error loading dataobjects status last modified date", t);
        } finally {
            closeDaoResources(res, stat);
        }
        return lastModified;
    }

    @Override
    protected String getAddingSearchRecordQuery() {
        return ADD_DATAOBJECT_SEARCH_RECORD;
    }

    @Override
    protected String getRemovingSearchRecordQuery() {
        return DELETE_DATAOBJECT_SEARCH_RECORD;
    }

    @Override
    protected String getAddingAttributeRoleRecordQuery() {
        return ADD_ATTRIBUTE_ROLE_RECORD;
    }

    @Override
    protected String getRemovingAttributeRoleRecordQuery() {
        return DELETE_ATTRIBUTE_ROLE_RECORD;
    }

    @Override
    protected String getExtractingAllEntityIdQuery() {
        return LOAD_ALL_DATAOBJECTS_ID;
    }

    private final String DELETE_DATAOBJECT = "DELETE FROM dataobjects WHERE dataid = ? ";

    private final String DELETE_DATAOBJECT_REL_RECORD = "DELETE FROM dataobjectrelations WHERE dataid = ? ";

    private final String ADD_DATAOBJECT_SEARCH_RECORD = "INSERT INTO dataobjectsearch (dataid, attrname, textvalue, datevalue, numvalue, langcode) "
            + "VALUES ( ? , ? , ? , ? , ? , ? )";

    private final String DELETE_DATAOBJECT_SEARCH_RECORD = "DELETE FROM dataobjectsearch WHERE dataid = ? ";

    private final String ADD_DATAOBJECT_REL_RECORD = "INSERT INTO dataobjectrelations "
            + "(dataid, refcategory, refgroup) VALUES ( ? , ? , ? )";

    private final String LOAD_DATAOBJECTS_ID_MAIN_BLOCK = "SELECT DISTINCT dataobjects.dataid FROM dataobjects ";

    private final String LOAD_REFERENCED_DATAOBJECTS_FOR_GROUP = LOAD_DATAOBJECTS_ID_MAIN_BLOCK
            + " RIGHT JOIN dataobjectrelations ON dataobjects.dataid = dataobjectrelations.dataid WHERE dataobjectrelations.refgroup = ? "
            + "ORDER BY dataobjects.dataid";

    private final String LOAD_REFERENCED_DATAOBJECTS_FOR_CATEGORY = LOAD_DATAOBJECTS_ID_MAIN_BLOCK
            + " RIGHT JOIN dataobjectrelations ON dataobjects.dataid = dataobjectrelations.dataid WHERE dataobjectrelations.refcategory = ? "
            + "ORDER BY dataobjects.dataid";

    private final String LOAD_DATAOBJECTS_VO_MAIN_BLOCK = "SELECT dataobjects.dataid, dataobjects.datatype, dataobjects.descr, dataobjects.status, "
            + "dataobjects.workxml, dataobjects.created, dataobjects.lastmodified, dataobjects.onlinexml, dataobjects.maingroup, "
            + "dataobjects.currentversion, dataobjects.firsteditor, dataobjects.lasteditor FROM dataobjects ";

    private final String LOAD_DATAOBJECT_VO = LOAD_DATAOBJECTS_VO_MAIN_BLOCK + " WHERE dataobjects.dataid = ? ";

    private final String ADD_DATAOBJECT = "INSERT INTO dataobjects (dataid, datatype, descr, status, workxml, "
            + "created, lastmodified, onlinexml, maingroup, currentversion, firsteditor, lasteditor) "
            + "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ?)";

    private final String INSERT_DATAOBJECT = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
            + "workxml = ? , lastmodified = ? , onlinexml = ? , maingroup = ? , currentversion = ? , lasteditor = ? "
            + "WHERE dataid = ? ";

    private final String INSERT_DATAOBJECT_WITHOUT_DATE = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
            + "workxml = ? , onlinexml = ? , maingroup = ? , currentversion = ? , lasteditor = ? " + "WHERE dataid = ? ";

    private final String REMOVE_DATAOBJECT = "UPDATE dataobjects SET onlinexml = ? , status = ? , "
            + "workxml = ? , lastmodified = ? , currentversion = ? , lasteditor = ? WHERE dataid = ? ";

    private final String REMOVE_DATAOBJECT_WITHOUT_DATE = "UPDATE dataobjects SET onlinexml = ? , status = ? , "
            + "workxml = ? , currentversion = ? , lasteditor = ? WHERE dataid = ? ";

    private final String UPDATE_DATAOBJECT = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
            + "workxml = ? , lastmodified = ? , maingroup = ? , currentversion = ? , lasteditor = ? " + "WHERE dataid = ? ";

    private final String UPDATE_DATAOBJECT_WITHOUT_DATE = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
            + "workxml = ? , maingroup = ? , currentversion = ? , lasteditor = ? " + "WHERE dataid = ? ";

    private final String LOAD_ALL_DATAOBJECTS_ID = "SELECT dataid FROM dataobjects";

    private final String ADD_ATTRIBUTE_ROLE_RECORD = "INSERT INTO dataobjectattributeroles (dataid, attrname, rolename) VALUES ( ? , ? , ? )";

    private final String DELETE_ATTRIBUTE_ROLE_RECORD = "DELETE FROM dataobjectattributeroles WHERE dataid = ? ";

    private static final String COUNT_OFFLINE_DATAOBJECTS = "SELECT count(dataid) FROM dataobjects WHERE onlinexml IS NULL";

    private static final String COUNT_DATAOBJECTS = "SELECT count(dataid) FROM dataobjects "
            + "WHERE onlinexml IS NOT NULL AND onlinexml = workxml";

    private static final String COUNT_DATAOBJECTS_WITH_DIFFS = "SELECT count(dataid) FROM dataobjects "
            + "WHERE onlinexml IS NOT NULL AND onlinexml <> workxml";

    private final String LOAD_LAST_MODIFIED = LOAD_DATAOBJECTS_VO_MAIN_BLOCK + "order by dataobjects.lastmodified desc";

    @Override
    public List<String> getGroupUtilizers(String groupName) {
        List<String> dataids = null;
        try {
            dataids = this.getUtilizers(groupName, LOAD_REFERENCED_DATAOBJECTS_FOR_GROUP);
        } catch (Throwable t) {
            _logger.error("Error loading referenced datatypes for group {}", groupName, t);
            throw new RuntimeException("Error loading referenced datatypes for group " + groupName, t);
        }
        return dataids;
    }

    @Override
    public List<String> getCategoryUtilizers(String categoryCode) {
        List<String> dataids = null;
        try {
            dataids = this.getUtilizers(categoryCode, LOAD_REFERENCED_DATAOBJECTS_FOR_CATEGORY);
        } catch (Throwable t) {
            _logger.error("Error loading referenced datatypes for category {}", categoryCode, t);
            throw new RuntimeException("Error loading referenced datatypes for category " + categoryCode, t);
        }
        return dataids;
    }

}
