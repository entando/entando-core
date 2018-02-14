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
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
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

    private IDataObjectDAO dataObjectDao;

    private IDataObjectSearcherDAO dataObjectSearcherDAO;

    private IDataObjectUpdaterService dataobjectUpdaterService;

    @Override
    public void init() throws Exception {
        super.init();
        _logger.debug("{} ready. Initialized {} DataObject types", this.getClass().getName(), super.getEntityTypes().size());
    }

    @Override
    protected String getConfigItemName() {
        return "dataTypeDefinitions";
    }

    /**
     * Create a new instance of the requested dataobject. The new dataobject is
     * forked (or cloned) from the corresponding prototype, and it's returned
     * empty.
     *
     * @param typeCode The code of the requested (proto)type, as declared in the
     * configuration.
     * @return The new dataobject.
     */
    @Override
    public DataObject createDataObject(String typeCode) {
        return (DataObject) super.getEntityPrototype(typeCode);
    }

    /**
     * Return a list of the of the dataobject types in a 'small form'. 'Small
     * form' mans that the dataobjects returned are purged from all unnecessary
     * information (eg. attributes).
     *
     * @return The list of the types in a (small form).
     * @deprecated From Entando 4.1.2, use getSmallEntityTypes() method
     */
    @Override
    public List<SmallDataType> getSmallDataTypes() {
        List<SmallDataType> smallDataObjectTypes = new ArrayList<>();
        smallDataObjectTypes.addAll(this.getSmallDataTypesMap().values());
        Collections.sort(smallDataObjectTypes);
        return smallDataObjectTypes;
    }

    /**
     * Return the map of the prototypes of the dataobjects types. Return a map,
     * index by the code of the type, of the prototypes of the available
     * dataobject types.
     *
     * @return The map of the prototypes of the dataobject types in a
     * 'SmallDataObjectType' objects.
     */
    @Override
    public Map<String, SmallDataType> getSmallDataTypesMap() {
        Map<String, SmallDataType> smallDataTypes = new HashMap<>();
		List<SmallEntityType> entityTypes = super.getSmallEntityTypes();
		for (SmallEntityType entityType : entityTypes) {
			SmallDataType sdt = new SmallDataType();
			sdt.setCode(entityType.getCode());
			sdt.setDescription(entityType.getDescription());
			smallDataTypes.put(entityType.getCode(), sdt);
		}
        return smallDataTypes;
    }

    /**
     * Return the code of the default page used to display the given dataobject.
     * The default page is defined at dataobject type level; the type is
     * extrapolated from the code built following the conventions.
     *
     * @param dataId The dataobject ID
     * @return The page code.
     */
    @Override
    public String getViewPage(String dataId) {
        DataObject type = this.getTypeById(dataId);
        return type.getViewPage();
    }

    /**
     * Return the code of the default model of dataobject.
     *
     * @param dataId The dataobject code
     * @return Il requested model code
     */
    @Override
    public String getDefaultModel(String dataId) {
        DataObject type = this.getTypeById(dataId);
        return type.getDefaultModel();
    }

    /**
     * Return the code of the model to be used when the dataobject is rendered
     * in list
     *
     * @param dataId The code of the dataobject
     * @return The code of the model
     */
    @Override
    public String getListModel(String dataId) {
        DataObject type = this.getTypeById(dataId);
        return type.getListModel();
    }

    /**
     * Return a complete dataobject given its ID; it is possible to choose to
     * return the published -unmodifiable!- dataobject or the working copy. It
     * also returns the data in the form of XML.
     *
     * @param id The ID of the dataobject
     * @param onLine Specifies the type of the dataobject to return: 'true'
     * references the published dataobject, 'false' the freely modifiable one.
     * @return The requested dataobject.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public DataObject loadDataObject(String id, boolean onLine) throws ApsSystemException {
        return this.loadDataObject(id, onLine, false);
    }

    @Override
    public DataObject loadDataObject(String id, boolean onLine, boolean cacheable) throws ApsSystemException {
        DataObject dataobject = null;
        try {
            DataObjectRecordVO dataobjectVo = this.loadDataObjectVO(id);
            dataobject = this.createDataObject(dataobjectVo, onLine);
        } catch (ApsSystemException e) {
            _logger.error("Error while loading dataobject : id {}", id, e);
            throw new ApsSystemException("Error while loading dataobject : id " + id, e);
        }
        return dataobject;
    }

    protected DataObject createDataObject(DataObjectRecordVO dataobjectVo, boolean onLine) throws ApsSystemException {
        DataObject dataobject = null;
        try {
            if (dataobjectVo != null) {
                String xmlData;
                if (onLine) {
                    xmlData = dataobjectVo.getXmlOnLine();
                } else {
                    xmlData = dataobjectVo.getXmlWork();
                }
                if (xmlData != null) {
                    dataobject = (DataObject) this.createEntityFromXml(dataobjectVo.getTypeCode(), xmlData);
                    dataobject.setId(dataobjectVo.getId());
                    dataobject.setTypeCode(dataobjectVo.getTypeCode());
                    dataobject.setDescription(dataobjectVo.getDescription());
                    dataobject.setOnLine(dataobjectVo.isOnLine());
                    dataobject.setMainGroup(dataobjectVo.getMainGroupCode());
                    if (null == dataobject.getVersion()) {
                        dataobject.setVersion(dataobjectVo.getVersion());
                    }
                    if (null == dataobject.getFirstEditor()) {
                        dataobject.setFirstEditor(dataobjectVo.getFirstEditor());
                    }
                    if (null == dataobject.getLastEditor()) {
                        dataobject.setLastEditor(dataobjectVo.getLastEditor());
                    }
                    if (null == dataobject.getCreated()) {
                        dataobject.setCreated(dataobjectVo.getCreate());
                    }
                    if (null == dataobject.getLastModified()) {
                        dataobject.setLastModified(dataobjectVo.getModify());
                    }
                    if (null == dataobject.getStatus()) {
                        dataobject.setStatus(dataobjectVo.getStatus());
                    }
                }
            }
        } catch (ApsSystemException e) {
            _logger.error("Error while creating dataobject by vo", e);
            throw new ApsSystemException("Error while creating dataobject by vo", e);
        }
        return dataobject;
    }

    /**
     * Return a {@link DataObjectRecordVO} (shortly: VO) containing the all
     * dataobject informations stored in the DB.
     *
     * @param id The id of the requested dataobject.
     * @return The VO object corresponding to the wanted dataobject.
     * @throws ApsSystemException in case of error.
     */
    @Override
    public DataObjectRecordVO loadDataObjectVO(String id) throws ApsSystemException {
        DataObjectRecordVO dataobjectVo = null;
        try {
            dataobjectVo = (DataObjectRecordVO) this.getDataObjectDAO().loadEntityRecord(id);
        } catch (Throwable t) {
            _logger.error("Error while loading dataobject vo : id {}", id, t);
            throw new ApsSystemException("Error while loading dataobject vo : id " + id, t);
        }
        return dataobjectVo;
    }

    /**
     * Save a dataobject in the DB.
     *
     * @param dataobject The dataobject to add.
     * @throws ApsSystemException in case of error.
     */
    @Override
    public void saveDataObject(DataObject dataobject) throws ApsSystemException {
        this.addDataObject(dataobject);
    }

    @Override
    public void saveDataObjectAndContinue(DataObject dataobject) throws ApsSystemException {
        this.addUpdateDataObject(dataobject, false);
    }

    /**
     * Save a dataobject in the DB. Hopefully this method has no annotation
     * attached
     *
     * @param dataobject
     * @throws ApsSystemException
     */
    @Override
    public void addDataObject(DataObject dataobject) throws ApsSystemException {
        this.addUpdateDataObject(dataobject, true);
    }

    private void addUpdateDataObject(DataObject dataobject, boolean updateDate) throws ApsSystemException {
        try {
            dataobject.setLastModified(new Date());
            if (updateDate) {
                dataobject.incrementVersion(false);
            }
            String status = dataobject.getStatus();
            if (null == status) {
                dataobject.setStatus(DataObject.STATUS_DRAFT);
            } else if (status.equals(DataObject.STATUS_PUBLIC)) {
                dataobject.setStatus(DataObject.STATUS_READY);
            }
            if (null == dataobject.getId()) {
                IKeyGeneratorManager keyGenerator = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
                int key = keyGenerator.getUniqueKeyCurrentValue();
                String id = dataobject.getTypeCode() + key;
                dataobject.setId(id);
                this.getDataObjectDAO().addEntity(dataobject);
            } else {
                this.getDataObjectDAO().updateDataObject(dataobject, updateDate);
            }
        } catch (Throwable t) {
            _logger.error("Error while saving dataobject", t);
            throw new ApsSystemException("Error while saving dataobject", t);
        }
    }

    /**
     * Publish a dataobject.
     *
     * @param dataobject The ID associated to the dataobject to be displayed in
     * the portal.
     * @throws ApsSystemException in case of error.
     */
    @Override
    public void insertDataObject(DataObject dataobject) throws ApsSystemException {
        try {
            dataobject.setLastModified(new Date());
            if (null == dataobject.getId()) {
                dataobject.setCreated(new Date());
                this.saveDataObject(dataobject);
            }
            dataobject.incrementVersion(true);
            dataobject.setStatus(DataObject.STATUS_PUBLIC);
            this.getDataObjectDAO().insertDataObject(dataobject);
            int operationEventCode = -1;
            if (dataobject.isOnLine()) {
                operationEventCode = PublicDataChangedEvent.UPDATE_OPERATION_CODE;
            } else {
                operationEventCode = PublicDataChangedEvent.INSERT_OPERATION_CODE;
            }
            this.notifyPublicDataObjectChanging(dataobject, operationEventCode);
        } catch (Throwable t) {
            _logger.error("Error while inserting dataobject on line", t);
            throw new ApsSystemException("Error while inserting dataobject on line", t);
        }
    }

    /**
     * Return the list of all the dataobject IDs.
     *
     * @return The list of all the dataobject IDs.
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
            DataObjectRecordVO dataobjectVo = this.loadDataObjectVO(entityId);
            DataObject dataobject = this.createDataObject(dataobjectVo, true);
            if (dataobject != null) {
                this.getDataObjectDAO().reloadDataObjectReferences(dataobject);
            }
            _logger.debug("Reloaded dataobject references for dataobject {}", entityId);
        } catch (Throwable t) {
            _logger.error("Error while reloading dataobject references for dataobject {}", entityId, t);
        }
    }

    /**
     * Unpublish a dataobject, preventing it from being displayed in the portal.
     * Obviously the dataobject itself is not deleted.
     *
     * @param dataobject the dataobject to unpublish.
     * @throws ApsSystemException in case of error
     */
    @Override
    public void removeDataObject(DataObject dataobject) throws ApsSystemException {
        try {
            dataobject.setLastModified(new Date());
            dataobject.incrementVersion(false);
            if (null != dataobject.getStatus() && dataobject.getStatus().equals(DataObject.STATUS_PUBLIC)) {
                dataobject.setStatus(DataObject.STATUS_READY);
            }
            this.getDataObjectDAO().removeDataObject(dataobject);
            this.notifyPublicDataObjectChanging(dataobject, PublicDataChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error while removing onLine dataobject", t);
            throw new ApsSystemException("Error while removing onLine dataobject", t);
        }
    }

    /**
     * Notify the modification of a published dataobject.
     *
     * @param dataobject The modified dataobject.
     * @param operationCode the operation code to notify.
     * @exception ApsSystemException in caso of error.
     */
    private void notifyPublicDataObjectChanging(DataObject dataobject, int operationCode) throws ApsSystemException {
        PublicDataChangedEvent event = new PublicDataChangedEvent();
        event.setDataObject(dataobject);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    /**
     * Return the dataobject type from the given ID code. The code is extracted
     * following the coding conventions: the first three characters are the type
     * of the code.
     *
     * @param dataId the dataobject ID whose dataobject type is extracted.
     * @return The dataobject type requested
     */
    private DataObject getTypeById(String dataId) {
        String typeCode = dataId.substring(0, 3);
        DataObject type = (DataObject) super.getEntityTypes().get(typeCode);
        return type;
    }

    /**
     * Deletes a dataobject from the DB.
     *
     * @param dataobject The dataobject to delete.
     * @throws ApsSystemException in case of error.
     */
    @Override
    public void deleteDataObject(DataObject dataobject) throws ApsSystemException {
        try {
            this.getDataObjectDAO().deleteEntity(dataobject.getId());
        } catch (Throwable t) {
            _logger.error("Error while deleting dataobject {}", dataobject.getId(), t);
            throw new ApsSystemException("Error while deleting dataobject " + dataobject.getId(), t);
        }
    }

    @Override
    public List<String> loadDataObjectsId(String dataobjectType, String[] categories, EntitySearchFilter[] filters,
            Collection<String> userGroupCodes) throws ApsSystemException {
        return this.loadDataObjectsId(dataobjectType, categories, false, filters, userGroupCodes);
    }

    @Override
    public List<String> loadDataObjectsId(String dataobjectType, String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        List<String> dataobjectsId = null;
        try {
            dataobjectsId = this.getDataObjectSearcherDAO().loadDataObjectsId(dataobjectType, categories, orClauseCategoryFilter, filters, userGroupCodes);
        } catch (Throwable t) {
            _logger.error("Error while loading dataobjects", t);
            throw new ApsSystemException("Error while loading dataobjects", t);
        }
        return dataobjectsId;
    }

    @Override
    public List<String> loadDataObjectsId(String[] categories,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        return this.loadDataObjectsId(categories, false, filters, userGroupCodes);
    }

    @Override
    public List<String> loadDataObjectsId(String[] categories, boolean orClauseCategoryFilter,
            EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
        List<String> dataobjectsId = null;
        try {
            dataobjectsId = this.getDataObjectSearcherDAO().loadDataObjectsId(categories, orClauseCategoryFilter, filters, userGroupCodes);
        } catch (Throwable t) {
            _logger.error("Error while loading dataobjects", t);
            throw new ApsSystemException("Error while loading dataobjects", t);
        }
        return dataobjectsId;
    }

    /**
     * @deprecated From jAPS 2.0 version 2.0.9. Use loadWorkDataObjectsId or
     * loadPublicDataObjectsId
     */
    @Override
    public List<String> loadDataObjectsId(String[] categories, EntitySearchFilter[] filters,
            Collection<String> userGroupCodes, boolean onlyOwner) throws ApsSystemException {
        throw new ApsSystemException("'loadDataObjectsId' method deprecated From jAPS 2.0 version 2.0.9. Use loadWorkDataObjectsId or loadPublicDataObjectsId");
    }

    @Override
    public List getCategoryUtilizers(String resourceId) throws ApsSystemException {
        List<String> dataIds = null;
        try {
            dataIds = this.getDataObjectDAO().getCategoryUtilizers(resourceId);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced dataobjects : category " + resourceId, t);
        }
        return dataIds;
    }

    @Override
    public void reloadCategoryReferences(String categoryCode) {
        try {
            this.getDataObjectUpdaterService().reloadCategoryReferences(categoryCode);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "reloadCategoryReferences");
        }
    }

    @Override
    public List getCategoryUtilizersForReloadReferences(String categoryCode) {
        List<String> dataIdToReload = new ArrayList<String>();
        try {
            Set<String> dataobjects = this.getDataObjectUpdaterService().getDataObjectsId(categoryCode);
            if (null != dataobjects) {
                dataIdToReload.addAll(dataobjects);
            }
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getCategoryUtilizersForReloadReferences");
        }
        return dataIdToReload;
    }

    @Override
    public List getGroupUtilizers(String groupName) throws ApsSystemException {
        List<String> dataIds = null;
        try {
            dataIds = this.getDataObjectDAO().getGroupUtilizers(groupName);
        } catch (Throwable t) {
            throw new ApsSystemException("Error while loading referenced dataobjects : group " + groupName, t);
        }
        return dataIds;
    }

    @Override
    public boolean isSearchEngineUser() {
        return true;
    }

    @Override
    public DataObjectsStatus getDataObjectsStatus() {
        DataObjectsStatus status = null;
        try {
            status = this.getDataObjectDAO().loadDataObjectsStatus();
        } catch (Throwable t) {
            _logger.error("error in getDataObjectsStatus");
        }
        return status;
    }

    /**
     * Return the DAO which handles all the operations on the dataobjects.
     *
     * @return The DAO managing the dataobjects.
     */
    protected IDataObjectDAO getDataObjectDAO() {
        return dataObjectDao;
    }

    public void setDataObjectDAO(IDataObjectDAO dao) {
        this.dataObjectDao = dao;
    }

    @Override
    protected IEntitySearcherDAO getEntitySearcherDao() {
        return this.getDataObjectSearcherDAO();
    }

    @Override
    protected IEntityDAO getEntityDao() {
        return this.getDataObjectDAO();
    }

    public IDataObjectSearcherDAO getDataObjectSearcherDAO() {
        return dataObjectSearcherDAO;
    }

    public void setDataObjectSearcherDAO(IDataObjectSearcherDAO dataObjectSearcherDAO) {
        this.dataObjectSearcherDAO = dataObjectSearcherDAO;
    }

    protected IDataObjectUpdaterService getDataObjectUpdaterService() {
        return dataobjectUpdaterService;
    }

    public void setDataObjectUpdaterService(IDataObjectUpdaterService dataobjectUpdaterService) {
        this.dataobjectUpdaterService = dataobjectUpdaterService;
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

}
