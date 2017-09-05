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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.entando.entando.aps.system.services.dataobject.event.PublicDataChangedEvent;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;

/**
 * Contents manager. This implements all the methods needed to create and manage
 * the contents.
 *
 * @author M.Diana - E.Santoboni
 */
public class DataObjectManager extends ApsEntityManager implements IContentManager {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectManager.class);

	@Override
	public void init() throws Exception {
		super.init();
		this.createSmallContentTypes();
		_logger.debug("{} ready. Initialized {} content types", this.getClass().getName(), super.getEntityTypes().size());
	}

	@Override
	protected String getConfigItemName() {
		return "dataTypeDefinitions";
	}

	private void createSmallContentTypes() {
		this._smallContentTypes = new HashMap<String, SmallDataType>(this.getEntityTypes().size());
		List<IApsEntity> types = new ArrayList<IApsEntity>(this.getEntityTypes().values());
		for (int i = 0; i < types.size(); i++) {
			DataObject contentPrototype = (DataObject) types.get(i);
			SmallDataType smallContentType = new SmallDataType();
			smallContentType.setCode(contentPrototype.getTypeCode());
			smallContentType.setDescription(contentPrototype.getTypeDescription());
			this._smallContentTypes.put(smallContentType.getCode(), smallContentType);
		}
	}

	@Override
	public void updateEntityPrototype(IApsEntity entityType) throws ApsSystemException {
		super.updateEntityPrototype(entityType);
		this.createSmallContentTypes();
	}

	/**
	 * Create a new instance of the requested content. The new content is forked
	 * (or cloned) from the corresponding prototype, and it's returned empty.
	 *
	 * @param typeCode The code of the requested (proto)type, as declared in the
	 * configuration.
	 * @return The new content.
	 */
	@Override
	public DataObject createContentType(String typeCode) {
		DataObject content = (DataObject) super.getEntityPrototype(typeCode);
		return content;
	}

	/**
	 * Return a list of the of the content types in a 'small form'. 'Small form'
	 * mans that the contents returned are purged from all unnecessary
	 * information (eg. attributes).
	 *
	 * @return The list of the types in a (small form).
	 * @deprecated From Entando 4.1.2, use getSmallEntityTypes() method
	 */
	@Override
	public List<SmallDataType> getSmallContentTypes() {
		List<SmallDataType> smallContentTypes = new ArrayList<SmallDataType>();
		smallContentTypes.addAll(this._smallContentTypes.values());
		Collections.sort(smallContentTypes);
		return smallContentTypes;
	}

	/**
	 * Return the map of the prototypes of the contents types. Return a map,
	 * index by the code of the type, of the prototypes of the available content
	 * types.
	 *
	 * @return The map of the prototypes of the content types in a
	 * 'SmallContentType' objects.
	 */
	@Override
	public Map<String, SmallDataType> getSmallContentTypesMap() {
		return this._smallContentTypes;
	}

	/**
	 * Return the code of the default page used to display the given content.
	 * The default page is defined at content type level; the type is
	 * extrapolated from the code built following the conventions.
	 *
	 * @param contentId The content ID
	 * @return The page code.
	 */
	@Override
	public String getViewPage(String contentId) {
		DataObject type = this.getTypeById(contentId);
		String pageCode = type.getViewPage();
		return pageCode;
	}

	/**
	 * Return the code of the default model of content.
	 *
	 * @param contentId The content code
	 * @return Il requested model code
	 */
	@Override
	public String getDefaultModel(String contentId) {
		DataObject type = this.getTypeById(contentId);
		String defaultModel = type.getDefaultModel();
		return defaultModel;
	}

	/**
	 * Return the code of the model to be used when the content is rendered in
	 * list
	 *
	 * @param contentId The code of the content
	 * @return The code of the model
	 */
	@Override
	public String getListModel(String contentId) {
		DataObject type = this.getTypeById(contentId);
		String defaultListModel = type.getListModel();
		return defaultListModel;
	}

	/**
	 * Return a complete content given its ID; it is possible to choose to
	 * return the published -unmodifiable!- content or the working copy. It also
	 * returns the data in the form of XML.
	 *
	 * @param id The ID of the content
	 * @param onLine Specifies the type of the content to return: 'true'
	 * references the published content, 'false' the freely modifiable one.
	 * @return The requested content.
	 * @throws ApsSystemException In case of error.
	 */
	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#id)", condition = "#onLine")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsCsv(#id)")
	public DataObject loadContent(String id, boolean onLine) throws ApsSystemException {
		return this.loadContent(id, onLine, false);
	}

	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#id)", condition = "#onLine and #cacheable")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsCsv(#id)")
	public DataObject loadContent(String id, boolean onLine, boolean cacheable) throws ApsSystemException {
		DataObject content = null;
		try {
			DataObjectRecordVO contentVo = this.loadContentVO(id);
			content = this.createContent(contentVo, onLine);
		} catch (ApsSystemException e) {
			_logger.error("Error while loading content : id {}", id, e);
			throw new ApsSystemException("Error while loading content : id " + id, e);
		}
		return content;
	}

	protected DataObject createContent(DataObjectRecordVO contentVo, boolean onLine) throws ApsSystemException {
		DataObject content = null;
		try {
			if (contentVo != null) {
				String xmlData;
				if (onLine) {
					xmlData = contentVo.getXmlOnLine();
				} else {
					xmlData = contentVo.getXmlWork();
				}
				if (xmlData != null) {
					content = (DataObject) this.createEntityFromXml(contentVo.getTypeCode(), xmlData);
					content.setId(contentVo.getId());
					content.setTypeCode(contentVo.getTypeCode());
					content.setDescription(contentVo.getDescription());
					content.setOnLine(contentVo.isOnLine());
					content.setMainGroup(contentVo.getMainGroupCode());
					if (null == content.getVersion()) {
						content.setVersion(contentVo.getVersion());
					}
					if (null == content.getFirstEditor()) {
						content.setFirstEditor(contentVo.getFirstEditor());
					}
					if (null == content.getLastEditor()) {
						content.setLastEditor(contentVo.getLastEditor());
					}
					if (null == content.getCreated()) {
						content.setCreated(contentVo.getCreate());
					}
					if (null == content.getLastModified()) {
						content.setLastModified(contentVo.getModify());
					}
					if (null == content.getStatus()) {
						content.setStatus(contentVo.getStatus());
					}
				}
			}
		} catch (ApsSystemException e) {
			_logger.error("Error while creating content by vo", e);
			throw new ApsSystemException("Error while creating content by vo", e);
		}
		return content;
	}

	/**
	 * Return a {@link DataObjectRecordVO} (shortly: VO) containing the all
	 * content informations stored in the DB.
	 *
	 * @param id The id of the requested content.
	 * @return The VO object corresponding to the wanted content.
	 * @throws ApsSystemException in case of error.
	 */
	@Override
	public DataObjectRecordVO loadContentVO(String id) throws ApsSystemException {
		DataObjectRecordVO contentVo = null;
		try {
			contentVo = (DataObjectRecordVO) this.getDataObjectDAO().loadEntityRecord(id);
		} catch (Throwable t) {
			_logger.error("Error while loading content vo : id {}", id, t);
			throw new ApsSystemException("Error while loading content vo : id " + id, t);
		}
		return contentVo;
	}

	/**
	 * Save a content in the DB.
	 *
	 * @param content The content to add.
	 * @throws ApsSystemException in case of error.
	 */
	@Override
	public void saveContent(DataObject content) throws ApsSystemException {
		this.addContent(content);
	}

	@Override
	public void saveContentAndContinue(DataObject content) throws ApsSystemException {
		this.addUpdateContent(content, false);
	}

	/**
	 * Save a content in the DB. Hopefully this method has no annotation
	 * attached
	 *
	 * @param content
	 * @throws ApsSystemException
	 */
	@Override
	public void addContent(DataObject content) throws ApsSystemException {
		this.addUpdateContent(content, true);
	}

	private void addUpdateContent(DataObject content, boolean updateDate) throws ApsSystemException {
		try {
			content.setLastModified(new Date());
			if (updateDate) {
				content.incrementVersion(false);
			}
			String status = content.getStatus();
			if (null == status) {
				content.setStatus(DataObject.STATUS_DRAFT);
			} else if (status.equals(DataObject.STATUS_PUBLIC)) {
				content.setStatus(DataObject.STATUS_READY);
			}
			if (null == content.getId()) {
				IKeyGeneratorManager keyGenerator = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
				int key = keyGenerator.getUniqueKeyCurrentValue();
				String id = content.getTypeCode() + key;
				content.setId(id);
				this.getDataObjectDAO().addEntity(content);
			} else {
				this.getDataObjectDAO().updateContent(content, updateDate);
			}
		} catch (Throwable t) {
			_logger.error("Error while saving content", t);
			throw new ApsSystemException("Error while saving content", t);
		}
	}

	/**
	 * Publish a content.
	 *
	 * @param content The ID associated to the content to be displayed in the
	 * portal.
	 * @throws ApsSystemException in case of error.
	 */
	@Override
	//@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
	//@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
	public void insertOnLineContent(DataObject content) throws ApsSystemException {
		try {
			content.setLastModified(new Date());
			if (null == content.getId()) {
				content.setCreated(new Date());
				this.saveContent(content);
			}
			content.incrementVersion(true);
			content.setStatus(DataObject.STATUS_PUBLIC);
			this.getDataObjectDAO().insertOnLineContent(content);
			int operationEventCode = -1;
			if (content.isOnLine()) {
				operationEventCode = PublicDataChangedEvent.UPDATE_OPERATION_CODE;
			} else {
				operationEventCode = PublicDataChangedEvent.INSERT_OPERATION_CODE;
			}
			this.notifyPublicContentChanging(content, operationEventCode);
		} catch (Throwable t) {
			_logger.error("Error while inserting content on line", t);
			throw new ApsSystemException("Error while inserting content on line", t);
		}
	}

	/**
	 * Return the list of all the content IDs.
	 *
	 * @return The list of all the content IDs.
	 * @throws ApsSystemException In case of error
	 * @deprecated Since Entando 2.0 version 2.0.9, use
	 * searchId(EntitySearchFilter[]) method
	 */
	@Override
	public List<String> getAllContentsId() throws ApsSystemException {
		return super.getAllEntityId();
	}

	@Override
	public void reloadEntityReferences(String entityId) {
		try {
			DataObjectRecordVO contentVo = this.loadContentVO(entityId);
			DataObject content = this.createContent(contentVo, true);
			if (content != null) {
				this.getDataObjectDAO().reloadPublicContentReferences(content);
			}
			DataObject workcontent = this.createContent(contentVo, false);
			if (workcontent != null) {
				this.getDataObjectDAO().reloadWorkContentReferences(workcontent);
			}
			_logger.debug("Reloaded content references for content {}", entityId);
		} catch (Throwable t) {
			_logger.error("Error while reloading content references for content {}", entityId, t);
		}
	}

	/**
	 * Unpublish a content, preventing it from being displayed in the portal.
	 * Obviously the content itself is not deleted.
	 *
	 * @param content the content to unpublish.
	 * @throws ApsSystemException in case of error
	 */
	@Override
	//@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
	//@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
	public void removeOnLineContent(DataObject content) throws ApsSystemException {
		try {
			content.setLastModified(new Date());
			content.incrementVersion(false);
			if (null != content.getStatus() && content.getStatus().equals(DataObject.STATUS_PUBLIC)) {
				content.setStatus(DataObject.STATUS_READY);
			}
			this.getDataObjectDAO().removeOnLineContent(content);
			this.notifyPublicContentChanging(content, PublicDataChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error while removing onLine content", t);
			throw new ApsSystemException("Error while removing onLine content", t);
		}
	}

	/**
	 * Notify the modification of a published content.
	 *
	 * @param content The modified content.
	 * @param operationCode the operation code to notify.
	 * @exception ApsSystemException in caso of error.
	 */
	private void notifyPublicContentChanging(DataObject content, int operationCode) throws ApsSystemException {
		PublicDataChangedEvent event = new PublicDataChangedEvent();
		event.setContent(content);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	/**
	 * Return the content type from the given ID code. The code is extracted
	 * following the coding conventions: the first three characters are the type
	 * of the code.
	 *
	 * @param contentId the content ID whose content type is extracted.
	 * @return The content type requested
	 */
	private DataObject getTypeById(String contentId) {
		String typeCode = contentId.substring(0, 3);
		DataObject type = (DataObject) super.getEntityTypes().get(typeCode);
		return type;
	}

	/**
	 * Deletes a content from the DB.
	 *
	 * @param content The content to delete.
	 * @throws ApsSystemException in case of error.
	 */
	@Override
	//@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
	//@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
	public void deleteContent(DataObject content) throws ApsSystemException {
		try {
			this.getDataObjectDAO().deleteEntity(content.getId());
		} catch (Throwable t) {
			_logger.error("Error while deleting content {}", content.getId(), t);
			throw new ApsSystemException("Error while deleting content " + content.getId(), t);
		}
	}

	@Override
	public List<String> loadPublicContentsId(String contentType, String[] categories, EntitySearchFilter[] filters,
			Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadPublicContentsId(contentType, categories, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadPublicContentsId(String contentType, String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = this.getPublicDataObjectSearcherDAO().loadPublicContentsId(contentType, categories, orClauseCategoryFilter, filters, userGroupCodes);
		} catch (Throwable t) {
			_logger.error("Error while loading contents", t);
			throw new ApsSystemException("Error while loading contents", t);
		}
		return contentsId;
	}

	@Override
	public List<String> loadPublicContentsId(String[] categories,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadPublicContentsId(categories, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadPublicContentsId(String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = this.getPublicDataObjectSearcherDAO().loadPublicContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
		} catch (Throwable t) {
			_logger.error("Error while loading contents", t);
			throw new ApsSystemException("Error while loading contents", t);
		}
		return contentsId;
	}

	/**
	 * @deprecated From jAPS 2.0 version 2.0.9. Use loadWorkContentsId or
	 * loadPublicContentsId
	 */
	@Override
	public List<String> loadContentsId(String[] categories, EntitySearchFilter[] filters,
			Collection<String> userGroupCodes, boolean onlyOwner) throws ApsSystemException {
		throw new ApsSystemException("'loadContentsId' method deprecated From jAPS 2.0 version 2.0.9. Use loadWorkContentsId or loadPublicContentsId");
	}

	@Override
	public List<String> loadWorkContentsId(EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadWorkContentsId(null, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadWorkContentsId(String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadWorkContentsId(categories, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadWorkContentsId(String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = this.getWorkDataObjectSearcherDAO().loadContentsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
		} catch (Throwable t) {
			_logger.error("Error while loading work contents", t);
			throw new ApsSystemException("Error while loading work contents", t);
		}
		return contentsId;
	}

	@Override
	public boolean isSearchEngineUser() {
		return true;
	}

	@Override
	public ContentsStatus getContentsStatus() {
		ContentsStatus status = null;
		try {
			status = this.getDataObjectDAO().loadContentStatus();
		} catch (Throwable t) {
			_logger.error("error in getContentsStatus");
		}
		return status;
	}

	/**
	 * Return the DAO which handles all the operations on the contents.
	 *
	 * @return The DAO managing the contents.
	 */
	protected IContentDAO getDataObjectDAO() {
		return _dataObjectDao;
	}

	public void setDataObjectDAO(IContentDAO dao) {
		this._dataObjectDao = dao;
	}

	@Override
	protected IEntitySearcherDAO getEntitySearcherDao() {
		return this.getWorkDataObjectSearcherDAO();
	}

	@Override
	protected IEntityDAO getEntityDao() {
		return this.getDataObjectDAO();
	}

	protected IWorkContentSearcherDAO getWorkDataObjectSearcherDAO() {
		return _workDataObjectSearcherDAO;
	}

	public void setWorkDataObjectSearcherDAO(IWorkContentSearcherDAO workDataObjectSearcherDAO) {
		this._workDataObjectSearcherDAO = workDataObjectSearcherDAO;
	}

	public IPublicContentSearcherDAO getPublicDataObjectSearcherDAO() {
		return _publicDataObjectSearcherDAO;
	}

	public void setPublicDataObjectSearcherDAO(IPublicContentSearcherDAO publicDataObjectSearcherDAO) {
		this._publicDataObjectSearcherDAO = publicDataObjectSearcherDAO;
	}

	protected IContentUpdaterService getContentUpdaterService() {
		return _contentUpdaterService;
	}

	public void setContentUpdaterService(IContentUpdaterService contentUpdaterService) {
		this._contentUpdaterService = contentUpdaterService;
	}

	@Override
	public IApsEntity getEntity(String entityId) throws ApsSystemException {
		return this.loadContent(entityId, false);
	}

	/**
	 * @deprecated From jAPS 2.0 version 2.0.9, use getStatus()
	 */
	@Override
	public int getState() {
		return super.getStatus();
	}

	/**
	 * Map of the prototypes of the content types in the so called 'small form',
	 * indexed by the type code.
	 */
	private Map<String, SmallDataType> _smallContentTypes;

	private IContentDAO _dataObjectDao;

	private IWorkContentSearcherDAO _workDataObjectSearcherDAO;

	private IPublicContentSearcherDAO _publicDataObjectSearcherDAO;

	private IContentUpdaterService _contentUpdaterService;

}
