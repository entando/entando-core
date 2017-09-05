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
package org.entando.entando.aps.system.services.datatype;

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
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import org.entando.entando.aps.system.services.datatype.model.DataObject;
import org.entando.entando.aps.system.services.datatype.model.DataObjectRecordVO;

/**
 * DAO class for objects of type content.
 *
 * @author M.Diana - E.Santoboni - S.Didaci
 */
public class ContentDAO extends AbstractEntityDAO implements IContentDAO {

	private static final Logger _logger = LoggerFactory.getLogger(ContentDAO.class);

	@Override
	protected String getLoadEntityRecordQuery() {
		return LOAD_CONTENT_VO;
	}

	@Override
	protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		DataObjectRecordVO contentVo = new DataObjectRecordVO();
		contentVo.setId(res.getString(1));
		contentVo.setTypeCode(res.getString(2));
		contentVo.setDescription(res.getString(3));
		contentVo.setStatus(res.getString(4));
		String xmlWork = res.getString(5);
		contentVo.setCreate(DateConverter.parseDate(res.getString(6), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
		contentVo.setModify(DateConverter.parseDate(res.getString(7), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
		String xmlOnLine = res.getString(8);
		contentVo.setOnLine(null != xmlOnLine && xmlOnLine.length() > 0);
		contentVo.setSync(xmlWork.equals(xmlOnLine));
		String mainGroupCode = res.getString(9);
		contentVo.setMainGroupCode(mainGroupCode);
		contentVo.setXmlWork(xmlWork);
		contentVo.setXmlOnLine(xmlOnLine);
		contentVo.setVersion(res.getString(10));
		contentVo.setFirstEditor(res.getString(11));
		contentVo.setLastEditor(res.getString(12));
		return contentVo;
	}

	@Override
	protected void executeAddEntity(IApsEntity entity, Connection conn) throws Throwable {
		super.executeAddEntity(entity, conn);
		this.addWorkContentRelationsRecord((DataObject) entity, conn);
	}

	@Override
	protected String getAddEntityRecordQuery() {
		return ADD_CONTENT;
	}

	@Override
	protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		DataObject content = (DataObject) entity;
		stat.setString(1, content.getId());
		stat.setString(2, content.getTypeCode());
		stat.setString(3, content.getDescription());
		stat.setString(4, content.getStatus());
		stat.setString(5, content.getXML());
		String currentDate = DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
		stat.setString(6, currentDate);
		stat.setString(7, currentDate);
		stat.setString(8, content.getMainGroup());
		stat.setString(9, content.getVersion());
		stat.setString(10, content.getFirstEditor());
		stat.setString(11, content.getLastEditor());
	}

	@Override
	public void updateContent(DataObject content, boolean updateDate) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeUpdateContent(content, updateDate, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating content", t);
			throw new RuntimeException("Error updating content", t);
		} finally {
			this.closeConnection(conn);
		}
	}

	protected void executeUpdateContent(DataObject content, boolean updateDate, Connection conn) throws Throwable {
		PreparedStatement stat = null;
		try {
			this.deleteRecordsByEntityId(content.getId(), DELETE_WORK_CONTENT_REL_RECORD, conn);
			this.deleteRecordsByEntityId(content.getId(), this.getRemovingSearchRecordQuery(), conn);
			this.deleteRecordsByEntityId(content.getId(), this.getRemovingAttributeRoleRecordQuery(), conn);
			if (updateDate) {
				stat = conn.prepareStatement(this.getUpdateEntityRecordQuery());
			} else {
				stat = conn.prepareStatement(this.getUpdateEntityRecordQueryWithoutDate());
			}
			this.buildUpdateEntityStatement(content, updateDate, stat);
			stat.executeUpdate();
			this.addEntitySearchRecord(content.getId(), content, conn);
			this.addEntityAttributeRoleRecord(content.getId(), content, conn);
			this.addWorkContentRelationsRecord(content, conn);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.closeDaoResources(null, stat);
		}
	}

	@Override
	protected void executeUpdateEntity(IApsEntity entity, Connection conn) throws Throwable {
		this.deleteRecordsByEntityId(entity.getId(), DELETE_WORK_CONTENT_REL_RECORD, conn);
		super.executeUpdateEntity(entity, conn);
		this.addWorkContentRelationsRecord((DataObject) entity, conn);
	}

	@Override
	protected String getUpdateEntityRecordQuery() {
		return UPDATE_CONTENT;
	}

	protected String getUpdateEntityRecordQueryWithoutDate() {
		return UPDATE_CONTENT_WITHOUT_DATE;
	}

	@Override
	protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		this.buildUpdateEntityStatement(entity, true, stat);
	}

	protected void buildUpdateEntityStatement(IApsEntity entity, boolean updateDate, PreparedStatement stat) throws Throwable {
		DataObject content = (DataObject) entity;
		int index = 1;
		stat.setString(index++, content.getTypeCode());
		stat.setString(index++, content.getDescription());
		stat.setString(index++, content.getStatus());
		stat.setString(index++, content.getXML());
		if (updateDate) {
			stat.setString(index++, DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
		}
		stat.setString(index++, content.getMainGroup());
		stat.setString(index++, content.getVersion());
		stat.setString(index++, content.getLastEditor());
		stat.setString(index++, content.getId());
	}

	/**
	 * This publishes a content.
	 *
	 * @param content the content to publish.
	 */
	@Override
	public void insertOnLineContent(DataObject content) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeInsertOnLineContent(content, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error publish content {} ", content.getId(), t);
			throw new RuntimeException("Error publish content - " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}

	protected void executeInsertOnLineContent(DataObject content, Connection conn) throws Throwable {
		this.executeInsertOnLineContent(content, true, conn);
	}

	protected void executeInsertOnLineContent(DataObject content, boolean updateDate, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteEntitySearchRecord(content.getId(), conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_REL_RECORD, conn);
		this.updateContentRecordForInsertOnLine(content, updateDate, conn);
		this.addPublicContentSearchRecord(content.getId(), content, conn);
		super.addEntitySearchRecord(content.getId(), content, conn);
		this.addContentAttributeRoleRecord(content.getId(), content, ADD_ATTRIBUTE_ROLE_RECORD, conn);
		this.addWorkContentRelationsRecord(content, conn);
		this.addContentRelationsRecord(content, conn);
		this.addContentAttributeRoleRecord(content.getId(), content, ADD_WORK_ATTRIBUTE_ROLE_RECORD, conn);
	}

	@Deprecated
	protected void deletePublicContentSearchRecord(String id, Connection conn) throws ApsSystemException {
		super.deleteRecordsByEntityId(id, DELETE_CONTENT_SEARCH_RECORD, conn);
	}

	protected void addPublicContentSearchRecord(String id, IApsEntity entity, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_CONTENT_SEARCH_RECORD);
			this.addEntitySearchRecord(id, entity, stat);
		} catch (Throwable t) {
			_logger.error("Error on adding public content search records", t);
			throw new RuntimeException("Error on adding public content search records", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected void updateContentRecordForInsertOnLine(DataObject content, Connection conn) throws ApsSystemException {
		this.updateContentRecordForInsertOnLine(content, true, conn);
	}

	protected void updateContentRecordForInsertOnLine(DataObject content, boolean updateDate, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			int index = 1;
			if (updateDate) {
				stat = conn.prepareStatement(INSERT_ONLINE_CONTENT);
			} else {
				stat = conn.prepareStatement(INSERT_ONLINE_CONTENT_WITHOUT_DATE);
			}
			stat.setString(index++, content.getTypeCode());
			stat.setString(index++, content.getDescription());
			stat.setString(index++, content.getStatus());
			String xml = content.getXML();
			stat.setString(index++, xml);
			if (updateDate) {
				stat.setString(index++, DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
			}
			stat.setString(index++, xml);
			stat.setString(index++, content.getMainGroup());
			stat.setString(index++, content.getVersion());
			stat.setString(index++, content.getLastEditor());
			stat.setString(index++, content.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating for insert onLine content {}", content.getId(), t);
			throw new RuntimeException("Error updating for insert onLine content " + content.getId(), t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	/**
	 * Updates the references of a published content
	 *
	 * @param content the published content
	 */
	@Override
	public void reloadPublicContentReferences(DataObject content) {
		if (content.isOnLine()) {
			Connection conn = null;
			try {
				conn = this.getConnection();
				conn.setAutoCommit(false);
				this.executeReloadPublicContentReferences(content, conn);
				conn.commit();
			} catch (Throwable t) {
				this.executeRollback(conn);
				_logger.error("Error reloading references - Content {}", content.getId(), t);
				throw new RuntimeException("Error reloading references - Content " + content.getId(), t);
			} finally {
				this.closeConnection(conn);
			}
		}
	}

	protected void executeReloadPublicContentReferences(DataObject content, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		this.addPublicContentSearchRecord(content.getId(), content, conn);
		this.addContentRelationsRecord(content, conn);
		this.addContentAttributeRoleRecord(content.getId(), content, ADD_ATTRIBUTE_ROLE_RECORD, conn);
	}

	/**
	 * Updates the references of a content
	 *
	 * @param content the content
	 */
	@Override
	public void reloadWorkContentReferences(DataObject content) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeReloadWorkContentReferences(content, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in reloading references - Work Content {}", content.getId(), t);
			throw new RuntimeException("Errore in reloading references - Work Content " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}

	protected void executeReloadWorkContentReferences(DataObject content, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteEntitySearchRecord(content.getId(), conn);
		super.addEntitySearchRecord(content.getId(), content, conn);
		this.addWorkContentRelationsRecord(content, conn);
		this.addContentAttributeRoleRecord(content.getId(), content, ADD_WORK_ATTRIBUTE_ROLE_RECORD, conn);
	}

	/**
	 * Unpublish a content, preventing it from being displayed in the portal.
	 * Obviously the content itslef is not deleted.
	 *
	 * @param content the content to unpublish.
	 */
	@Override
	public void removeOnLineContent(DataObject content) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeRemoveOnLineContent(content, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing online content {}", content.getId(), t);
			throw new RuntimeException("Error removing online content - " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}

	protected void executeRemoveOnLineContent(DataObject content, Connection conn) {
		this.executeRemoveOnLineContent(content, true, conn);
	}

	/**
	 * Unpublish a content, preventing it from being displayed in the portal.
	 * Obviously the content itslef is not deleted.
	 *
	 * @param content the content to unpublish.
	 * @param updateDate
	 * @param conn the connection to the DB.
	 */
	protected void executeRemoveOnLineContent(DataObject content, boolean updateDate, Connection conn) {
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		PreparedStatement stat = null;
		try {
			if (updateDate) {
				stat = conn.prepareStatement(REMOVE_ONLINE_CONTENT);
			} else {
				stat = conn.prepareStatement(REMOVE_ONLINE_CONTENT_WITHOUT_DATE);
			}
			int index = 1;
			stat.setString(index++, null);
			stat.setString(index++, content.getStatus());
			stat.setString(index++, content.getXML());
			if (updateDate) {
				stat.setString(index++, DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
			}
			stat.setString(index++, content.getVersion());
			stat.setString(index++, content.getLastEditor());
			stat.setString(index++, content.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing online content {}", content.getId(), t);
			throw new RuntimeException("Error removing online content - " + content.getId(), t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	@Override
	protected String getDeleteEntityRecordQuery() {
		return DELETE_CONTENT;
	}

	@Override
	protected void executeDeleteEntity(String entityId, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(entityId, DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(entityId, DELETE_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(entityId, DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteRecordsByEntityId(entityId, DELETE_WORK_CONTENT_REL_RECORD, conn);
		super.executeDeleteEntity(entityId, conn);
	}

	private void addCategoryRelationsRecord(DataObject content, boolean isPublicRelations, PreparedStatement stat) throws ApsSystemException {
		if (content.getCategories().size() > 0) {
			try {
				Set<String> codes = new HashSet<String>();
				Iterator<Category> categoryIter = content.getCategories().iterator();
				while (categoryIter.hasNext()) {
					Category category = (Category) categoryIter.next();
					this.addCategoryCode(category, codes);
				}
				Iterator<String> codeIter = codes.iterator();
				while (codeIter.hasNext()) {
					String code = codeIter.next();
					int i = 1;
					stat.setString(i++, content.getId());
					if (isPublicRelations) {
						stat.setString(i++, null);
						stat.setString(i++, null);
						stat.setBigDecimal(i++, null);
					}
					stat.setString(i++, code);
					if (isPublicRelations) {
						stat.setString(i++, null);
					}
					stat.addBatch();
					stat.clearParameters();
				}
			} catch (SQLException e) {
				_logger.error("Error saving content relation record for content {}", content.getId(), e.getNextException());
				throw new RuntimeException("Error saving content relation record for content " + content.getId(), e.getNextException());
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

	private void addGroupRelationsRecord(DataObject content, PreparedStatement stat) throws ApsSystemException {
		try {
			content.addGroup(content.getMainGroup());
			Iterator<String> groupIter = content.getGroups().iterator();
			while (groupIter.hasNext()) {
				String groupName = groupIter.next();
				stat.setString(1, content.getId());
				stat.setString(2, null);
				stat.setString(3, null);
				stat.setBigDecimal(4, null);
				stat.setString(5, null);
				stat.setString(6, groupName);
				stat.addBatch();
				stat.clearParameters();
			}
		} catch (Throwable t) {
			_logger.error("Error saving group relation record for content {}", content.getId(), t);
			throw new RuntimeException("Error saving group relation record for content " + content.getId(), t);
		}
	}

	/**
	 * Add a record in the table 'contentrelations' for every resource, page,
	 * other content, role and category associated to the given content).
	 *
	 * @param content The current content.
	 * @param conn The connection to the database.
	 * @throws ApsSystemException when connection error are detected.
	 */
	protected void addContentRelationsRecord(DataObject content, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_CONTENT_REL_RECORD);
			this.addCategoryRelationsRecord(content, true, stat);
			this.addGroupRelationsRecord(content, stat);
			EntityAttributeIterator attributeIter = new EntityAttributeIterator(content);
			while (attributeIter.hasNext()) {
				AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
			}
			stat.executeBatch();
		} catch (BatchUpdateException e) {
			_logger.error("Error saving record into contentrelations {}", content.getId(), e.getNextException());
			throw new RuntimeException("Error saving record into contentrelations " + content.getId(), e.getNextException());
		} catch (Throwable t) {
			_logger.error("Error saving record into contentrelations {}", content.getId(), t);
			throw new RuntimeException("Error saving record into contentrelations " + content.getId(), t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected void addWorkContentRelationsRecord(DataObject content, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_WORK_CONTENT_REL_RECORD);
			this.addCategoryRelationsRecord(content, false, stat);
			stat.executeBatch();
		} catch (BatchUpdateException e) {
			_logger.error("Error saving record into workcontentrelations {}", content.getId(), e.getNextException());
			throw new RuntimeException("Error saving record into workcontentrelations " + content.getId(), e);
		} catch (Throwable t) {
			_logger.error("Error saving record into workcontentrelations {}", content.getId(), t);
			throw new RuntimeException("Error saving record into workcontentrelations " + content.getId(), t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected void addContentAttributeRoleRecord(String id, IApsEntity entity, String query, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			super.addEntityAttributeRoleRecord(id, entity, stat);
		} catch (Throwable t) {
			_logger.error("Error on adding content attribute role records", t);
			throw new RuntimeException("Error on adding content attribute role records", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected List<String> getUtilizers(String referencedObjectCode, String query) throws Throwable {
		Connection conn = null;
		List<String> contentIds = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(query);
			stat.setString(1, referencedObjectCode);
			res = stat.executeQuery();
			while (res.next()) {
				String id = res.getString(1);
				contentIds.add(id);
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return contentIds;
	}

	@Override
	public ContentsStatus loadContentStatus() {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		ContentsStatus status = null;
		try {
			conn = this.getConnection();
			int online = this.loadContentStatus(conn, COUNT_ONLINE_CONTENTS);
			int offline = this.loadContentStatus(conn, COUNT_OFFLINE_CONTENTS);
			int withDiffs = this.loadContentStatus(conn, COUNT_ONLINE_CONTENTS_WITH_DIFFS);
			Date lastModified = this.loadContentStatusLastModified(conn, LOAD_LAST_MODIFIED);
			status = new ContentsStatus();
			status.setDraft(offline);
			status.setOnline(online);
			status.setOnlineWithChanges(withDiffs);
			status.setLastUpdate(lastModified);
		} catch (Throwable t) {
			_logger.error("Error loading contents status", t);
			throw new RuntimeException("Error loading contents status", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return status;
	}

	private int loadContentStatus(Connection conn, String query) {
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
			_logger.error("Error loading contents status. If you are runing Entando backed by Apache Derby it's a known issue");
		} finally {
			closeDaoResources(res, stat);
		}
		return count;
	}

	private Date loadContentStatusLastModified(Connection conn, String query) {
		PreparedStatement stat = null;
		ResultSet res = null;
		Date lastModified = null;
		try {
			stat = conn.prepareStatement(query);
			res = stat.executeQuery();
			if (res.next()) {
				String lastMod = res.getString("lastmodified");
				lastModified = DateConverter.parseDate(lastMod, JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
			}
		} catch (Throwable t) {
			_logger.error("Error loading contents status last modified date", t);
			throw new RuntimeException("Error loading contents status last modified date", t);
		} finally {
			closeDaoResources(res, stat);
		}
		return lastModified;
	}

	@Override
	protected String getAddingSearchRecordQuery() {
		return ADD_WORK_CONTENT_SEARCH_RECORD;
	}

	@Override
	protected String getRemovingSearchRecordQuery() {
		return DELETE_WORK_CONTENT_SEARCH_RECORD;
	}

	@Override
	protected String getAddingAttributeRoleRecordQuery() {
		return ADD_WORK_ATTRIBUTE_ROLE_RECORD;
	}

	@Override
	protected String getRemovingAttributeRoleRecordQuery() {
		return DELETE_WORK_ATTRIBUTE_ROLE_RECORD;
	}

	@Override
	protected String getExtractingAllEntityIdQuery() {
		return LOAD_ALL_CONTENTS_ID;
	}

	private final String DELETE_CONTENT = "DELETE FROM dataobjects WHERE contentid = ? ";

	private final String DELETE_CONTENT_REL_RECORD = "DELETE FROM datatyperelations WHERE contentid = ? ";

	private final String DELETE_WORK_CONTENT_REL_RECORD = "DELETE FROM workdatatyperelations WHERE contentid = ? ";

	private final String ADD_CONTENT_SEARCH_RECORD = "INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) "
			+ "VALUES ( ? , ? , ? , ? , ? , ? )";

	private final String DELETE_CONTENT_SEARCH_RECORD = "DELETE FROM datatypesearch WHERE contentid = ? ";

	private final String ADD_WORK_CONTENT_SEARCH_RECORD = "INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) "
			+ "VALUES ( ? , ? , ? , ? , ? , ? )";

	private final String DELETE_WORK_CONTENT_SEARCH_RECORD = "DELETE FROM workdatatypesearch WHERE contentid = ? ";

	private final String ADD_CONTENT_REL_RECORD = "INSERT INTO datatyperelations "
			+ "(contentid, refpage, refcontent, refresource, refcategory, refgroup) VALUES ( ? , ? , ? , ? , ? , ? )";

	private final String ADD_WORK_CONTENT_REL_RECORD = "INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ( ? , ? )";

	private final String LOAD_CONTENTS_VO_MAIN_BLOCK = "SELECT dataobjects.contentid, dataobjects.datatype, dataobjects.descr, dataobjects.status, "
			+ "dataobjects.workxml, dataobjects.created, dataobjects.lastmodified, dataobjects.onlinexml, dataobjects.maingroup, "
			+ "dataobjects.currentversion, dataobjects.firsteditor, dataobjects.lasteditor FROM dataobjects ";

	private final String LOAD_CONTENT_VO = LOAD_CONTENTS_VO_MAIN_BLOCK + " WHERE dataobjects.contentid = ? ";

	private final String ADD_CONTENT = "INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, "
			+ "created, lastmodified, maingroup, currentversion, firsteditor, lasteditor) "
			+ "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";

	private final String INSERT_ONLINE_CONTENT = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
			+ "workxml = ? , lastmodified = ? , onlinexml = ? , maingroup = ? , currentversion = ? , lasteditor = ? "
			+ "WHERE contentid = ? ";

	private final String INSERT_ONLINE_CONTENT_WITHOUT_DATE = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
			+ "workxml = ? , onlinexml = ? , maingroup = ? , currentversion = ? , lasteditor = ? " + "WHERE contentid = ? ";

	private final String REMOVE_ONLINE_CONTENT = "UPDATE dataobjects SET onlinexml = ? , status = ? , "
			+ "workxml = ? , lastmodified = ? , currentversion = ? , lasteditor = ? WHERE contentid = ? ";

	private final String REMOVE_ONLINE_CONTENT_WITHOUT_DATE = "UPDATE dataobjects SET onlinexml = ? , status = ? , "
			+ "workxml = ? , currentversion = ? , lasteditor = ? WHERE contentid = ? ";

	private final String UPDATE_CONTENT = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
			+ "workxml = ? , lastmodified = ? , maingroup = ? , currentversion = ? , lasteditor = ? " + "WHERE contentid = ? ";

	private final String UPDATE_CONTENT_WITHOUT_DATE = "UPDATE dataobjects SET datatype = ? , descr = ? , status = ? , "
			+ "workxml = ? , maingroup = ? , currentversion = ? , lasteditor = ? " + "WHERE contentid = ? ";

	private final String LOAD_ALL_CONTENTS_ID = "SELECT contentid FROM dataobjects";

	private final String ADD_ATTRIBUTE_ROLE_RECORD = "INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ( ? , ? , ? )";

	private final String DELETE_ATTRIBUTE_ROLE_RECORD = "DELETE FROM datatypeattributeroles WHERE contentid = ? ";

	private final String ADD_WORK_ATTRIBUTE_ROLE_RECORD = "INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ( ? , ? , ? )";

	private final String DELETE_WORK_ATTRIBUTE_ROLE_RECORD = "DELETE FROM workdatatypeattributeroles WHERE contentid = ? ";

	private static final String COUNT_OFFLINE_CONTENTS = "SELECT count(contentid) FROM dataobjects WHERE onlinexml IS NULL";

	private static final String COUNT_ONLINE_CONTENTS = "SELECT count(contentid) FROM dataobjects "
			+ "WHERE onlinexml IS NOT NULL AND onlinexml = workxml";

	private static final String COUNT_ONLINE_CONTENTS_WITH_DIFFS = "SELECT count(contentid) FROM dataobjects "
			+ "WHERE onlinexml IS NOT NULL AND onlinexml <> workxml";

	private final String LOAD_LAST_MODIFIED = LOAD_CONTENTS_VO_MAIN_BLOCK + "order by dataobjects.lastmodified desc";

}
