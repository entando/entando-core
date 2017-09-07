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

import com.agiletec.aps.system.ApsSystemUtils;
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
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import java.util.Set;
import org.entando.entando.aps.system.services.dataobject.event.PublicDataChangedEvent;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;

/**
 * DataObjects manager. This implements all the methods needed to create and
 * manage the DataObjects.
 *
 * @author M.Diana - E.Santoboni
 */
public class DataObjectManager extends ApsEntityManager implements IDataObjectManager, GroupUtilizer, CategoryUtilizer {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectManager.class);

	@Override
	public void init() throws Exception {
		super.init();
		this.createSmallContentTypes();
		_logger.debug("{} ready. Initialized {} DataObject types", this.getClass().getName(), super.getEntityTypes().size());
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
	public DataObject createDataObject(String typeCode) {
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
	public List<SmallDataType> getSmallDataTypes() {
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
	public Map<String, SmallDataType> getSmallDataTypesMap() {
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
	public DataObject loadDataObject(String id, boolean onLine) throws ApsSystemException {
		return this.loadDataObject(id, onLine, false);
	}

	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#id)", condition = "#onLine and #cacheable")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsCsv(#id)")
	public DataObject loadDataObject(String id, boolean onLine, boolean cacheable) throws ApsSystemException {
		DataObject content = null;
		try {
			DataObjectRecordVO contentVo = this.loadDataObjectVO(id);
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
	public DataObjectRecordVO loadDataObjectVO(String id) throws ApsSystemException {
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
	public void saveDataObject(DataObject content) throws ApsSystemException {
		this.addContent(content);
	}

	@Override
	public void saveDataObjectAndContinue(DataObject content) throws ApsSystemException {
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
	public void insertOnLineDataObject(DataObject content) throws ApsSystemException {
		try {
			content.setLastModified(new Date());
			if (null == content.getId()) {
				content.setCreated(new Date());
				this.saveDataObject(content);
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
	public List<String> getAllDataObjectsId() throws ApsSystemException {
		return super.getAllEntityId();
	}

	@Override
	public void reloadEntityReferences(String entityId) {
		try {
			DataObjectRecordVO contentVo = this.loadDataObjectVO(entityId);
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
	public void removeOnLineDataObject(DataObject content) throws ApsSystemException {
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
		event.setDataObject(content);
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
	public void deleteDataObject(DataObject content) throws ApsSystemException {
		try {
			this.getDataObjectDAO().deleteEntity(content.getId());
		} catch (Throwable t) {
			_logger.error("Error while deleting content {}", content.getId(), t);
			throw new ApsSystemException("Error while deleting content " + content.getId(), t);
		}
	}

	@Override
	public List<String> loadPublicDataObjectsId(String contentType, String[] categories, EntitySearchFilter[] filters,
			Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadPublicDataObjectsId(contentType, categories, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadPublicDataObjectsId(String contentType, String[] categories, boolean orClauseCategoryFilter,
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
	public List<String> loadPublicDataObjectsId(String[] categories,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadPublicDataObjectsId(categories, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadPublicDataObjectsId(String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = this.getPublicDataObjectSearcherDAO().loadPublicDataObjectsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
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
	public List<String> loadDataObjectsId(String[] categories, EntitySearchFilter[] filters,
			Collection<String> userGroupCodes, boolean onlyOwner) throws ApsSystemException {
		throw new ApsSystemException("'loadContentsId' method deprecated From jAPS 2.0 version 2.0.9. Use loadWorkContentsId or loadPublicContentsId");
	}

	@Override
	public List<String> loadWorkDataObjectsId(EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadWorkDataObjectsId(null, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadWorkDataObjectsId(String[] categories, EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		return this.loadWorkDataObjectsId(categories, false, filters, userGroupCodes);
	}

	@Override
	public List<String> loadWorkDataObjectsId(String[] categories, boolean orClauseCategoryFilter,
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = this.getWorkDataObjectSearcherDAO().loadDataObjectsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
		} catch (Throwable t) {
			_logger.error("Error while loading work contents", t);
			throw new ApsSystemException("Error while loading work contents", t);
		}
		return contentsId;
	}

	@Override
	public List getCategoryUtilizers(String resourceId) throws ApsSystemException {
		List<String> contentIds = null;
		try {
			contentIds = this.getDataObjectDAO().getCategoryUtilizers(resourceId);
		} catch (Throwable t) {
			throw new ApsSystemException("Error while loading referenced contents : category " + resourceId, t);
		}
		return contentIds;
	}

	@Override
	public void reloadCategoryReferences(String categoryCode) {
		try {
			this.getContentUpdaterService().reloadCategoryReferences(categoryCode);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "reloadCategoryReferences");
		}
	}

	@Override
	public List getCategoryUtilizersForReloadReferences(String categoryCode) {
		List<String> contentIdToReload = new ArrayList<String>();
		try {
			Set<String> contents = this.getContentUpdaterService().getDataObjectsId(categoryCode);
			if (null != contents) {
				contentIdToReload.addAll(contents);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCategoryUtilizersForReloadReferences");
		}
		return contentIdToReload;
	}

	@Override
	public List getGroupUtilizers(String groupName) throws ApsSystemException {
		List<String> contentIds = null;
		try {
			contentIds = this.getDataObjectDAO().getGroupUtilizers(groupName);
		} catch (Throwable t) {
			throw new ApsSystemException("Error while loading referenced contents : group " + groupName, t);
		}
		return contentIds;
	}

	@Override
	public boolean isSearchEngineUser() {
		return true;
	}

	@Override
	public DataObjectsStatus getDataObjectsStatus() {
		DataObjectsStatus status = null;
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
	protected IDataObjectDAO getDataObjectDAO() {
		return _dataObjectDao;
	}

	public void setDataObjectDAO(IDataObjectDAO dao) {
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

	protected IWorkDataObjectSearcherDAO getWorkDataObjectSearcherDAO() {
		return _workDataObjectSearcherDAO;
	}

	public void setWorkDataObjectSearcherDAO(IWorkDataObjectSearcherDAO workDataObjectSearcherDAO) {
		this._workDataObjectSearcherDAO = workDataObjectSearcherDAO;
	}

	public IPublicDataObjectSearcherDAO getPublicDataObjectSearcherDAO() {
		return _publicDataObjectSearcherDAO;
	}

	public void setPublicDataObjectSearcherDAO(IPublicDataObjectSearcherDAO publicDataObjectSearcherDAO) {
		this._publicDataObjectSearcherDAO = publicDataObjectSearcherDAO;
	}

	protected IDataObjectUpdaterService getContentUpdaterService() {
		return _contentUpdaterService;
	}

	public void setContentUpdaterService(IDataObjectUpdaterService contentUpdaterService) {
		this._contentUpdaterService = contentUpdaterService;
	}

	@Override
	public IApsEntity getEntity(String entityId) throws ApsSystemException {
		return this.loadDataObject(entityId, false);
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

	private IDataObjectDAO _dataObjectDao;

	private IWorkDataObjectSearcherDAO _workDataObjectSearcherDAO;

	private IPublicDataObjectSearcherDAO _publicDataObjectSearcherDAO;

	private IDataObjectUpdaterService _contentUpdaterService;

}
