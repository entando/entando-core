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
package com.agiletec.plugins.jacms.aps.system.services.content;

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
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.IReferenceableAttribute;

/**
 * DAO class for objects of type content. 
 * @author M.Diana - E.Santoboni - S.Didaci
 */
public class ContentDAO extends AbstractEntityDAO implements IContentDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(ContentDAO.class);
	
	@Override
	protected String getLoadEntityRecordQuery() {
		return LOAD_CONTENT_VO;
	}
	
	@Override
	protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		ContentRecordVO contentVo = new ContentRecordVO();
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
		this.addWorkContentRelationsRecord((Content) entity, conn);
	}
	
	@Override
	protected String getAddEntityRecordQuery() {
		return ADD_CONTENT;
	}
	
	@Override
	protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		Content content = (Content) entity;
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
	public void updateContent(Content content, boolean updateDate) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeUpdateContent(content, updateDate, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating content",  t);
			throw new RuntimeException("Error updating content", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void executeUpdateContent(Content content, boolean updateDate, Connection conn) throws Throwable {
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
		this.addWorkContentRelationsRecord((Content) entity, conn);
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
		Content content = (Content) entity;
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
	 * @param content the content to publish.
	 */
	@Override
	public void insertOnLineContent(Content content) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeInsertOnLineContent(content, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error publish content {} ", content.getId(),  t);
			throw new RuntimeException("Error publish content - " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void executeInsertOnLineContent(Content content, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteEntitySearchRecord(content.getId(), conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_REL_RECORD, conn);
		this.updateContentRecordForInsertOnLine(content, conn);
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
			_logger.error("Error on adding public content search records",  t);
			throw new RuntimeException("Error on adding public content search records", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected void updateContentRecordForInsertOnLine(Content content, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(INSERT_ONLINE_CONTENT);
			stat.setString(1, content.getTypeCode());
			stat.setString(2, content.getDescription());
			stat.setString(3, content.getStatus());
			String xml = content.getXML();
			stat.setString(4, xml);
			stat.setString(5, DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
			stat.setString(6, xml);
			stat.setString(7, content.getMainGroup());
			stat.setString(8, content.getVersion());
			stat.setString(9, content.getLastEditor());
			stat.setString(10, content.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating for insert onLine content {}", content.getId(),  t);
			throw new RuntimeException("Error updating for insert onLine content " + content.getId(), t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Updates the references of a published content
	 * @param content the published content
	 */
	@Override
	public void reloadPublicContentReferences(Content content) {
		if (content.isOnLine()) {
			Connection conn = null;
			try {
				conn = this.getConnection();
				conn.setAutoCommit(false);
				this.executeReloadPublicContentReferences(content, conn);
				conn.commit();
			} catch (Throwable t) {
				this.executeRollback(conn);
				_logger.error("Error reloading references - Content {}", content.getId(),  t);
				throw new RuntimeException("Error reloading references - Content " + content.getId(), t);
			} finally {
				this.closeConnection(conn);
			}
		}
	}
	
	protected void executeReloadPublicContentReferences(Content content, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		this.addPublicContentSearchRecord(content.getId(), content, conn);
		this.addContentRelationsRecord(content, conn);
		this.addContentAttributeRoleRecord(content.getId(), content, ADD_ATTRIBUTE_ROLE_RECORD, conn);
	}
	
	/**
	 * Updates the references of a content
	 * @param content the content
	 */
	@Override
	public void reloadWorkContentReferences(Content content) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeReloadWorkContentReferences(content, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in reloading references - Work Content {}", content.getId(),  t);
			throw new RuntimeException("Errore in reloading references - Work Content " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	protected void executeReloadWorkContentReferences(Content content, Connection conn) throws Throwable {
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_WORK_ATTRIBUTE_ROLE_RECORD, conn);
		super.deleteEntitySearchRecord(content.getId(), conn);
		super.addEntitySearchRecord(content.getId(), content, conn);
		this.addWorkContentRelationsRecord(content, conn);
		this.addContentAttributeRoleRecord(content.getId(), content, ADD_WORK_ATTRIBUTE_ROLE_RECORD, conn);
	}
	
	/**
	 * Unpublish a content, preventing it from being displayed in the portal. Obviously the content itslef is not deleted.
	 * @param content the content to unpublish.
	 */
	@Override
	public void removeOnLineContent(Content content) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.executeRemoveOnLineContent(content, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error removing online content {}", content.getId(),  t);
			throw new RuntimeException("Error removing online content - " + content.getId(), t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	/**
	 * Unpublish a content, preventing it from being displayed in the portal. Obviously
	 * the content itslef is not deleted.
	 * @param content the content to unpublish.
	 * @param conn the connection to the DB.
	 * @throws ApsSystemException when connection errors to the database are detected.
	 */
	protected void executeRemoveOnLineContent(Content content, Connection conn) throws ApsSystemException {
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_SEARCH_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_CONTENT_REL_RECORD, conn);
		super.deleteRecordsByEntityId(content.getId(), DELETE_ATTRIBUTE_ROLE_RECORD, conn);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(REMOVE_ONLINE_CONTENT);
			stat.setString(1, null);
			stat.setString(2, content.getStatus());
			stat.setString(3, content.getXML());
			stat.setString(4, DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT));
			stat.setString(5, content.getVersion());
			stat.setString(6, content.getLastEditor());
			stat.setString(7, content.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing online content {}", content.getId(),  t);
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
	
	private void addCategoryRelationsRecord(Content content, boolean isPublicRelations, PreparedStatement stat) throws ApsSystemException {
		if (content.getCategories().size()>0) {
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
				_logger.error("Error saving content relation record for content {}", content.getId(),  e.getNextException());
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
	
	private void addGroupRelationsRecord(Content content, PreparedStatement stat) throws ApsSystemException {
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
			_logger.error("Error saving group relation record for content {}", content.getId(),  t);
			throw new RuntimeException("Error saving group relation record for content " + content.getId(), t);
		}
	}
	
	/**
	 * Add a record in the table 'contentrelations' for every resource, page, other content,
	 * role and category associated to the given content).
	 * @param content The current content.
	 * @param conn The connection to the database.
	 * @throws ApsSystemException when connection error are detected.
	 */
	protected void addContentRelationsRecord(Content content, Connection conn) throws ApsSystemException{
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_CONTENT_REL_RECORD);
			this.addCategoryRelationsRecord(content, true, stat);
			this.addGroupRelationsRecord(content, stat);
			EntityAttributeIterator attributeIter = new EntityAttributeIterator(content);
			while (attributeIter.hasNext()) {
				AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
				if (currAttribute instanceof IReferenceableAttribute) {
					IReferenceableAttribute cmsAttribute = (IReferenceableAttribute) currAttribute;
					List<CmsAttributeReference> refs = cmsAttribute.getReferences(this.getLangManager().getLangs());
					for (int i=0; i<refs.size(); i++) {
						CmsAttributeReference ref = refs.get(i);
						stat.setString(1, content.getId());
						stat.setString(2, ref.getRefPage());
						stat.setString(3, ref.getRefContent());
						stat.setString(4, ref.getRefResource());
						stat.setString(5, null);
						stat.setString(6, null);
						stat.addBatch();
						stat.clearParameters();
					}
				}
			}
			stat.executeBatch();
		} catch (BatchUpdateException e) {
			_logger.error("Error saving record into contentrelations {}", content.getId(), e.getNextException());
			throw new RuntimeException("Error saving record into contentrelations " + content.getId(), e.getNextException());
		} catch (Throwable t) {
			_logger.error("Error saving record into contentrelations {}", content.getId(),  t);
			throw new RuntimeException("Error saving record into contentrelations " + content.getId(), t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected void addWorkContentRelationsRecord(Content content, Connection conn) throws ApsSystemException{
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
			_logger.error("Error on adding content attribute role records",  t);
			throw new RuntimeException("Error on adding content attribute role records", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public List<String> getContentUtilizers(String contentId) {
		List<String> contentIds = null;
		try {
			contentIds = this.getUtilizers(contentId, LOAD_REFERENCED_CONTENTS_FOR_CONTENT);
		} catch (Throwable t) {
			_logger.error("Error loading referenced contents for content {}", contentId,  t);
			throw new RuntimeException("Error loading referenced contents for content" + contentId, t);
		}
		return contentIds;
	}
	
	@Override
	public List<String> getPageUtilizers(String pageCode) {
		List<String> contentIds = null;
		try {
			contentIds = this.getUtilizers(pageCode, LOAD_REFERENCED_CONTENTS_FOR_PAGE);
		} catch (Throwable t) {
			_logger.error("Error loading referenced contents for page {}", pageCode,  t);
			throw new RuntimeException("Error loading referenced contents for page" + pageCode, t);
		}
		return contentIds;
	}
	
	@Override
	public List<String> getGroupUtilizers(String groupName) {
		List<String> contentIds = null;
		try {
			contentIds = this.getUtilizers(groupName, LOAD_REFERENCED_CONTENTS_FOR_GROUP);
		} catch (Throwable t) {
			_logger.error("Error loading referenced contents for group {}", groupName,  t);
			throw new RuntimeException("Error loading referenced contents for group " + groupName, t);
		}
		return contentIds;
	}
	
	@Override
	public List<String> getResourceUtilizers(String resourceId) {
		List<String> contentIds = null;
		try {
			contentIds = this.getUtilizers(resourceId, LOAD_REFERENCED_CONTENTS_FOR_RESOURCE);
		} catch (Throwable t) {
			_logger.error("Error loading referenced contents for resource {}", resourceId,  t);
			throw new RuntimeException("Error loading referenced contents for resource " + resourceId, t);
		}
		return contentIds;
	}
	
	@Override
	public List<String> getCategoryUtilizers(String categoryCode) {
		List<String> contentIds = null;
		try {
			contentIds = this.getUtilizers(categoryCode, LOAD_REFERENCED_CONTENTS_FOR_CATEGORY);
		} catch (Throwable t) {
			_logger.error("Error loading referenced contents for category {}", categoryCode,  t);
			throw new RuntimeException("Error loading referenced contents for category " + categoryCode, t);
		}
		return contentIds;
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
	
	private final String DELETE_CONTENT =
		"DELETE FROM contents WHERE contentid = ? ";
	
	private final String DELETE_CONTENT_REL_RECORD =
		"DELETE FROM contentrelations WHERE contentid = ? ";
	
	private final String DELETE_WORK_CONTENT_REL_RECORD =
		"DELETE FROM workcontentrelations WHERE contentid = ? ";

	private final String ADD_CONTENT_SEARCH_RECORD =
		"INSERT INTO contentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) " +
		"VALUES ( ? , ? , ? , ? , ? , ? )";
	
	private final String DELETE_CONTENT_SEARCH_RECORD =
		"DELETE FROM contentsearch WHERE contentid = ? ";
	
	private final String ADD_WORK_CONTENT_SEARCH_RECORD =
		"INSERT INTO workcontentsearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) " +
		"VALUES ( ? , ? , ? , ? , ? , ? )";
	
	private final String DELETE_WORK_CONTENT_SEARCH_RECORD =
		"DELETE FROM workcontentsearch WHERE contentid = ? ";
	
	private final String ADD_CONTENT_REL_RECORD =
		"INSERT INTO contentrelations " +
		"(contentid, refpage, refcontent, refresource, refcategory, refgroup) " +
		"VALUES ( ? , ? , ? , ? , ? , ? )";
	
	private final String ADD_WORK_CONTENT_REL_RECORD =
		"INSERT INTO workcontentrelations (contentid, refcategory) VALUES ( ? , ? )";
	
	private final String LOAD_CONTENTS_ID_MAIN_BLOCK = 
		"SELECT DISTINCT contents.contentid FROM contents ";
	
	private final String LOAD_REFERENCED_CONTENTS_FOR_PAGE = 
		LOAD_CONTENTS_ID_MAIN_BLOCK + 
		" RIGHT JOIN contentrelations ON contents.contentid = contentrelations.contentid WHERE refpage = ? " +
		"ORDER BY contents.contentid";
	
	private final String LOAD_REFERENCED_CONTENTS_FOR_CONTENT = 
		LOAD_CONTENTS_ID_MAIN_BLOCK + 
		" RIGHT JOIN contentrelations ON contents.contentid = contentrelations.contentid WHERE refcontent = ? " +
		"ORDER BY contents.contentid";
	
	private final String LOAD_REFERENCED_CONTENTS_FOR_GROUP = 
		LOAD_CONTENTS_ID_MAIN_BLOCK + 
		" RIGHT JOIN contentrelations ON contents.contentid = contentrelations.contentid WHERE refgroup = ? " +
		"ORDER BY contents.contentid";
	
	private final String LOAD_REFERENCED_CONTENTS_FOR_RESOURCE = 
		LOAD_CONTENTS_ID_MAIN_BLOCK + 
		" RIGHT JOIN contentrelations ON contents.contentid = contentrelations.contentid WHERE refresource = ? " +
		"ORDER BY contents.contentid";
	
	private final String LOAD_REFERENCED_CONTENTS_FOR_CATEGORY = 
		LOAD_CONTENTS_ID_MAIN_BLOCK + 
		" RIGHT JOIN contentrelations ON contents.contentid = contentrelations.contentid WHERE refcategory = ? " +
		"ORDER BY contents.contentid";
	
	private final String LOAD_CONTENTS_VO_MAIN_BLOCK = 
		"SELECT contents.contentid, contents.contenttype, contents.descr, contents.status, " +
		"contents.workxml, contents.created, contents.lastmodified, contents.onlinexml, contents.maingroup, " + 
		"contents.currentversion, contents.firsteditor, contents.lasteditor " +
		"FROM contents ";
	
	private final String LOAD_CONTENT_VO = 
		LOAD_CONTENTS_VO_MAIN_BLOCK + " WHERE contents.contentid = ? ";
	
	private final String ADD_CONTENT =
		"INSERT INTO contents (contentid, contenttype, descr, status, workxml, " + 
		"created, lastmodified, maingroup, currentversion, firsteditor, lasteditor) " +
		"VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
	
	private final String INSERT_ONLINE_CONTENT =
		"UPDATE contents SET contenttype = ? , descr = ? , status = ? , " +
		"workxml = ? , lastmodified = ? , onlinexml = ? , maingroup = ? , currentversion = ? , lasteditor = ? " +
		"WHERE contentid = ? ";
	
	private final String REMOVE_ONLINE_CONTENT = 
		"UPDATE contents SET onlinexml = ? , status = ? , " +
		"workxml = ? , lastmodified = ? , currentversion = ? , lasteditor = ? WHERE contentid = ? ";
	
	private final String UPDATE_CONTENT =
		"UPDATE contents SET contenttype = ? , descr = ? , status = ? , " +
		"workxml = ? , lastmodified = ? , maingroup = ? , currentversion = ? , lasteditor = ? " +
		"WHERE contentid = ? ";
	
	private final String UPDATE_CONTENT_WITHOUT_DATE =
		"UPDATE contents SET contenttype = ? , descr = ? , status = ? , " +
		"workxml = ? , maingroup = ? , currentversion = ? , lasteditor = ? " +
		"WHERE contentid = ? ";
	
	private final String LOAD_ALL_CONTENTS_ID = 
		"SELECT contentid FROM contents";
	
	private final String ADD_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO contentattributeroles (contentid, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM contentattributeroles WHERE contentid = ? ";
	
	private final String ADD_WORK_ATTRIBUTE_ROLE_RECORD =
		"INSERT INTO workcontentattributeroles (contentid, attrname, rolename) VALUES ( ? , ? , ? )";
	
	private final String DELETE_WORK_ATTRIBUTE_ROLE_RECORD =
		"DELETE FROM workcontentattributeroles WHERE contentid = ? ";
	
}
