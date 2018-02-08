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
package com.agiletec.aps.system.common.entity;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.cache.IEntityManagerCacheWrapper;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesEvent;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesObserver;
import com.agiletec.aps.system.common.entity.loader.AttributeDisablingCodesLoader;
import com.agiletec.aps.system.common.entity.loader.AttributeRolesLoader;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.parse.EntityHandler;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.common.entity.parse.IEntityTypeDOM;
import com.agiletec.aps.system.common.entity.parse.IEntityTypeFactory;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.DateConverter;
import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This abstract service must be extended in all those services that make use of
 * ApsEntities. By default, extending the manager, it is necessary to implement
 * the method that returns the specific category manager and, in the definition
 * of the Spring service, the configuration item where to look for the
 * definitions of the Entity Types handled by the service.
 *
 * @author E.Santoboni
 */
public abstract class ApsEntityManager extends AbstractService
                                       implements IEntityManager, IEntityTypesConfigurer, ReloadingEntitiesReferencesObserver {

    /**
     * Prefix of the thread used for references reloading.
     */
    public static final String RELOAD_REFERENCES_THREAD_NAME_PREFIX = "RELOAD_REFERENCES_INDEX_";

    private static final Logger logger = LoggerFactory.getLogger(ApsEntityManager.class);

    private IEntityTypeFactory entityTypeFactory;

    private Class entityClass;

    private String configItemName;

    private IEntityTypeDOM entityTypeDom;

    private EntityHandler entityHandler;

    private String xmlAttributeRootElementName;

    private IApsEntityDOM entityDom;

    private ICategoryManager categoryManager;

    private Map<String, AttributeRole> attributeRoles = null;

    private Map<String, String> attributeDisablingCodes = null;

    private String attributeRolesFileName;

    private String attributeDisablingCodesFileName;

    private IEntityManagerCacheWrapper cacheWrapper;

    @Override
    public void init() throws Exception {
        this.entityDom.setRootElementName(this.getXmlAttributeRootElementName());
        this.getCacheWrapper().initCache(super.getName());
        logger.debug("{} : inizializated", this.getName());
    }

    @Override
    public void refresh() throws Throwable {
        super.refresh();
        this.attributeDisablingCodes = null;
        this.attributeRoles = null;
    }

    @Override
    public Map<String, String> getAttributeDisablingCodes() {
        if (null != this.attributeDisablingCodes) {
            //codes already loaded
            return this.attributeDisablingCodes;
        }
        //codes not loaded yet
        AttributeDisablingCodesLoader loader = new AttributeDisablingCodesLoader();
        this.attributeDisablingCodes = loader.extractDisablingCodes(this.getAttributeDisablingCodesFileName(), super.getBeanFactory(), this);
        Map<String, String> clone = new HashMap<>();
        clone.putAll(this.attributeDisablingCodes);
        return clone;
    }

    @Override
    public List<AttributeRole> getAttributeRoles() {
        if (null != this.attributeRoles) {
            //roles already loaded
            return this.getOrderedAttributeRoles();
        }
        //roles not loaded yet
        this.initAttributeRoles();
        return this.getOrderedAttributeRoles();
    }

    protected void initAttributeRoles() {
        this.attributeRoles = new HashMap<>();
        AttributeRolesLoader loader = new AttributeRolesLoader();
        this.attributeRoles = loader.extractAttributeRoles(this.getAttributeRolesFileName(), super.getBeanFactory(), this);
    }

    private List<AttributeRole> getOrderedAttributeRoles() {
        List<AttributeRole> roles = new ArrayList<>(this.attributeRoles.size());
        Iterator<AttributeRole> iter = this.attributeRoles.values().iterator();
        while (iter.hasNext()) {
            AttributeRole role = iter.next();
            roles.add(role.clone());
        }
        BeanComparator comparator = new BeanComparator("name");
        Collections.sort(roles, comparator);
        return roles;
    }

    @Override
    public AttributeRole getAttributeRole(String roleName) {
        if (null == this.attributeRoles) {
            this.initAttributeRoles();
        }
        AttributeRole role = this.attributeRoles.get(roleName);
        if (null != role) {
            return role.clone();
        }
        return null;
    }

    /**
     * Create and populate the entity as specified by its type and XML
     * definition.
     *
     * @param entityTypeCode The Entity Type code.
     * @param xml The XML of the associated entity.
     * @return The populated entity.
     * @throws ApsSystemException If errors detected while retrieving the
     * entity.
     */
    protected IApsEntity createEntityFromXml(String entityTypeCode, String xml) throws ApsSystemException {
        try {
            IApsEntity entityPrototype = this.getEntityPrototype(entityTypeCode);
            SAXParserFactory parseFactory = SAXParserFactory.newInstance();
            SAXParser parser = parseFactory.newSAXParser();
            InputSource is = new InputSource(new StringReader(xml));
            EntityHandler handler = this.getEntityHandler();
            handler.initHandler(entityPrototype, this.getXmlAttributeRootElementName(), this.getCategoryManager());
            parser.parse(is, handler);
            return entityPrototype;
        } catch (ParserConfigurationException | SAXException | IOException t) {
            logger.error("Error detected while creating the entity. typecode: {} - xml: {}", entityTypeCode, xml, t);
            throw new ApsSystemException("Error detected while creating the entity", t);
        }
    }

    /**
     * Indicates whether the service makes use of the search engine. Default
     * value: false.
     *
     * @return true if the services uses the search engine, false otherwise.
     */
    @Override
    public boolean isSearchEngineUser() {
        return false;
    }

    @Override
    public IApsEntity getEntityPrototype(String typeCode) {
        IApsEntity prototype = null;
        try {
            IApsEntity mainPrototype = this.getEntityTypeFactory().extractEntityType(typeCode, this.getEntityClass(),
                                                                                     this.getConfigItemName(), this.getEntityTypeDom(), super.getName(), this.getEntityDom());
            if (null == mainPrototype) {
                return null;
            }
            prototype = mainPrototype.getEntityPrototype();
        } catch (Exception e) {
            logger.error("Error while extracting entity type {}", typeCode, e);
            throw new RuntimeException("Error while extracting entity type " + typeCode, e);
        }
        return prototype;
    }

    @Override
    public Map<String, IApsEntity> getEntityPrototypes() {
        Map<String, IApsEntity> prototypes = new HashMap<>();
        Map<String, IApsEntity> mainPrototypes = this.getEntityTypes();
        Iterator<String> iter = mainPrototypes.keySet().iterator();
        while (iter.hasNext()) {
            String code = iter.next();
            IApsEntity mainPrototype = mainPrototypes.get(code);
            prototypes.put(code, mainPrototype.getEntityPrototype());
        }
        return prototypes;
    }

    /**
     * Add a new entity prototype on the catalog.
     *
     * @param entityType The entity type to add.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public void addEntityPrototype(IApsEntity entityType) throws ApsSystemException {
        if (null == entityType) {
            throw new ApsSystemException("Invalid entity type to add");
        }
        Map<String, IApsEntity> newEntityTypes = this.getEntityTypes();
        newEntityTypes.put(entityType.getTypeCode(), entityType);
        this.updateEntityPrototypes(newEntityTypes);
        this.notifyEntityTypesChanging(null, entityType, EntityTypesChangingEvent.INSERT_OPERATION_CODE);
    }

    /**
     * Update an entity prototype on the catalog.
     *
     * @param entityType The entity type to update.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public void updateEntityPrototype(IApsEntity entityType) throws ApsSystemException {
        if (null == entityType) {
            throw new ApsSystemException("Invalid entity type to update");
        }
        Map<String, IApsEntity> entityTypes = this.getEntityTypes();
        IApsEntity oldEntityType = entityTypes.get(entityType.getTypeCode());
        if (null == oldEntityType) {
            throw new ApsSystemException("No entity type to update with code '" + entityType.getTypeCode() + "' where found");
        }
        entityTypes.put(entityType.getTypeCode(), entityType);
        this.updateEntityPrototypes(entityTypes);
        this.verifyReloadingNeeded(oldEntityType, entityType);
        this.notifyEntityTypesChanging(oldEntityType, entityType, EntityTypesChangingEvent.UPDATE_OPERATION_CODE);
    }

    protected void verifyReloadingNeeded(IApsEntity oldEntityType, IApsEntity newEntityType) {
        if (this.getStatus(newEntityType.getTypeCode()) == STATUS_NEED_TO_RELOAD_REFERENCES) {
            return;
        }
        List<AttributeInterface> attributes = oldEntityType.getAttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface oldAttribute = attributes.get(i);
            AttributeInterface newAttribute = (AttributeInterface) newEntityType.getAttribute(oldAttribute.getName());
            if ((oldAttribute.isSearchable() && null == newAttribute) || (null != newAttribute && oldAttribute.isSearchable() != newAttribute.isSearchable())) {
                this.setStatus(IEntityManager.STATUS_NEED_TO_RELOAD_REFERENCES, oldEntityType.getTypeCode());
                return;
            }
            String[] oldRoles = (null != oldAttribute.getRoles()) ? oldAttribute.getRoles() : new String[0];
            String[] newRoles = (null != newAttribute && null != newAttribute.getRoles()) ? newAttribute.getRoles() : new String[0];
            if (newRoles.length == 0 && oldRoles.length == 0) {
                continue;
            } else if (newRoles.length != oldRoles.length) {
                this.setStatus(IEntityManager.STATUS_NEED_TO_RELOAD_REFERENCES, oldEntityType.getTypeCode());
                return;
            } else {
                List<String> oldRolesList = Arrays.asList(oldRoles);
                List<String> newRolesList = Arrays.asList(newRoles);
                for (int j = 0; j < newRolesList.size(); j++) {
                    String roleName = newRolesList.get(j);
                    if (!oldRolesList.contains(roleName)) {
                        this.setStatus(IEntityManager.STATUS_NEED_TO_RELOAD_REFERENCES, oldEntityType.getTypeCode());
                        return;
                    }
                }
            }
        }
    }

    /**
     * Remove an entity type from the catalog.
     *
     * @param entityTypeCode The code of the entity type to remove.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public void removeEntityPrototype(String entityTypeCode) throws ApsSystemException {
        Map<String, IApsEntity> entityTypes = this.getEntityTypes();
        IApsEntity entityTypeToRemove = entityTypes.get(entityTypeCode);
        if (null == entityTypeToRemove) {
            throw new ApsSystemException("No entity type to remove with code '" + entityTypeCode + "' were found");
        }
        entityTypes.remove(entityTypeCode);
        this.updateEntityPrototypes(entityTypes);
        this.notifyEntityTypesChanging(entityTypeToRemove, null, EntityTypesChangingEvent.REMOVE_OPERATION_CODE);
    }

    /**
     * Update entity prototypes
     *
     * @param newEntityTypes the map, indexed by code, containing the new
     * entities.
     * @throws ApsSystemException If errors are detected during the process.
     */
    private void updateEntityPrototypes(Map<String, IApsEntity> newEntityTypes) throws ApsSystemException {
        try {
            this.getEntityTypeFactory().updateEntityTypes(newEntityTypes, this.getConfigItemName(), this.getEntityTypeDom());
            this.refresh();
        } catch (Throwable t) {
            logger.error("Error detected while updating entity prototypes", t);
            throw new ApsSystemException("Error detected while updating entity prototypes", t);
        }
    }

    private void notifyEntityTypesChanging(IApsEntity oldEntityType, IApsEntity newEntityType, int operationCode) throws ApsSystemException {
        EntityTypesChangingEvent event = new EntityTypesChangingEvent();
        event.setOperationCode(operationCode);
        event.setNewEntityType(newEntityType);
        event.setOldEntityType(oldEntityType);
        event.setEntityManagerName(this.getName());
        this.notifyEvent(event);
    }

    /**
     * Return the map of the Entity Types managed by the service.
     *
     * @return The map of the Entity Types indexed by the type code.
     */
    protected Map<String, IApsEntity> getEntityTypes() {
        Map<String, IApsEntity> types = null;
        try {
            types = this.getEntityTypeFactory().extractEntityTypes(this.getEntityClass(),
                                                                   this.getConfigItemName(), this.getEntityTypeDom(), super.getName(), this.getEntityDom());
        } catch (Exception e) {
            logger.error("Error while extracting entity types", e);
            throw new RuntimeException("Error while extracting entity types", e);
        }
        return types;
    }

    @Override
    public Map<String, AttributeInterface> getEntityAttributePrototypes() {
        Collection<AttributeInterface> attributes = this.getEntityTypeDom().getAttributeTypes().values();
        Map<String, AttributeInterface> attributeMap = new HashMap<>(attributes.size());
        Iterator<AttributeInterface> iter = attributes.iterator();
        while (iter.hasNext()) {
            AttributeInterface attributeInterface = iter.next();
            AttributeInterface clone = (AttributeInterface) attributeInterface.getAttributePrototype();
            attributeMap.put(clone.getType(), clone);
        }
        return attributeMap;
    }

    protected IEntityTypeFactory getEntityTypeFactory() {
        return this.entityTypeFactory;
    }

    /**
     * Set up the Entity Types factory. This method is used and hereby found in
     * the spring XML configuration of the service. By default, the definition
     * of the abstract service in the Spring configuration presents a standard
     * factory; such definition must be substituted in the declaration of the
     * service if specific operations are required by the particular structure
     * of the Entity Type to manage.
     *
     * @param entityTypeFactory The factory of Entity Types.
     */
    public void setEntityTypeFactory(IEntityTypeFactory entityTypeFactory) {
        this.entityTypeFactory = entityTypeFactory;
    }

    /**
     * This method is used and hereby found in the spring XML configuration of
     * the service. By default, the declaration of the abstract service in the
     * Spring configuration presents a standard class (ApsEntity); this class
     * must be substituted in the definition of the service if a different
     * class, which extends the standard ApsEntity, must be used. This method
     * checks the validity of the class.
     *
     * @param className The name of the entity class.
     */
    public void setEntityClassName(String className) {
        try {
            this.entityClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            Class check = this.entityClass;
            do {
                Class[] interfaces = check.getInterfaces();
                for (int j = 0; j < interfaces.length; j++) {
                    if (interfaces[j].equals(IApsEntity.class)) {
                        return;
                    }
                }
                check = check.getSuperclass();
            } while (!check.equals(Object.class));
            throw new RuntimeException("Invalid entity class");
        } catch (ClassNotFoundException e) {
            logger.error("Errore creating the entity class", e);
            throw new RuntimeException("Error creating the entity class", e);
        }
    }

    @Override
    public Class getEntityClass() {
        return this.entityClass;
    }

    /**
     * Return The name of the configuration item where to extract the definition
     * of the Entity types.
     *
     * @return The name of the configuration item.
     */
    protected String getConfigItemName() {
        return this.configItemName;
    }

    /**
     * Prepare the configuration item where to extract the definition of the
     * various Entity types managed by the service.
     *
     * @param confItemName The name of the configuration item where to extract
     * the definition of the Entity types
     */
    public void setConfigItemName(String confItemName) {
        this.configItemName = confItemName;
    }

    protected IEntityTypeDOM getEntityTypeDom() {
        return this.entityTypeDom;
    }

    /**
     * Prepare the DOM class that interprets the XML defining the various Entity
     * Types managed by the service. This method is used and hereby found in the
     * Spring XML configuration of the service. By default, the declaration of
     * the abstract service contains the standard DOM class, namely the
     * EntityTypeDOM; this definition can be substituted in the declaration of
     * the service if a different DOM class, implementing the IEntityTypeDOM
     * interface, is used. This is, for example, to interpret customized Entity
     * Types -all implementing the ApsEntity- in a new service.
     *
     * @param entityTypeDom The DOM class that parses the XML configuring the
     * Entity Types.
     */
    public void setEntityTypeDom(IEntityTypeDOM entityTypeDom) {
        this.entityTypeDom = entityTypeDom;
    }

    /**
     * Set up the name of the root attribute in the XML representing the single
     * entity. This method is used and found in the Spring XML definition of the
     * service. By default, the definition of the abstract service in the Spring
     * configuration, presents the name "entity"; this definition can be
     * substituted in the implementation of the service if a new name is used.
     *
     * @param xmlAttributeRootElementName The name of the root attribute.
     */
    public void setXmlAttributeRootElementName(String xmlAttributeRootElementName) {
        this.xmlAttributeRootElementName = xmlAttributeRootElementName;
    }

    protected String getXmlAttributeRootElementName() {
        return this.xmlAttributeRootElementName;
    }

    /**
     * Set up the handler class that parses the XML defining single entities.
     * This method is used and hereby found in the Spring XML configuration of
     * the service. The definition of the abstract service in the Spring
     * configuration presents a default handler class, namely the EntityHandler;
     * this definition can be changed in the declaration of the service if a
     * particular handler, which extends EntityHandler, is used to parse
     * specific entities. The class of such entities must extend the ApsEntity
     * class to be correctly managed by the service.
     *
     * @param entityHandler The handler class that parses the XML of the single
     * entities.
     */
    public void setEntityHandler(EntityHandler entityHandler) {
        this.entityHandler = entityHandler;
    }

    /**
     * Return the handler class that parses the single entity. This method
     * returns a prototype ready to be used to parse an entity.
     *
     * @return The handler class that parses the XML of the entity.
     */
    protected EntityHandler getEntityHandler() {
        return this.entityHandler.getHandlerPrototype();
    }

    protected IApsEntityDOM getEntityDom() {
        return this.entityDom;
    }

    /**
     * Set the DOM class that generates the XML that represents a single entity.
     * This method is used and hereby found in the Spring XML configuration of
     * the service. By default, the definition of the abstract service in the
     * Spring configuration, presents a standard DOM class, namely the
     * ApsEntityDOM; this definition can be substituted in the declaration of
     * the service if a different DOM class, implementing the IApsEntityDOM
     * interface, is used to generate the XML of particular entities. Such
     * entities are mapped to a class that must extend, as usual, the ApsEntity
     * class.
     *
     * @param entityDom the entity type dom
     */
    public void setEntityDom(IApsEntityDOM entityDom) {
        this.entityDom = entityDom;
    }

    /**
     * Search entities.
     *
     * @param filters The filters used to find an sort the entities ID that
     * match the given criteria.
     * @return The list of the IDs found.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public List<String> searchId(EntitySearchFilter[] filters) throws ApsSystemException {
        List<String> idList = null;
        try {
            idList = this.getEntitySearcherDao().searchId(filters);
        } catch (Throwable t) {
            logger.error("Error detected while searching entities", t);
            throw new ApsSystemException("Error detected while searching entities", t);
        }
        return idList;
    }

    /**
     * Search entities.
     *
     * @param typeCode The code of the Entity Types to look for.
     * @param filters The search filters to apply to find and sort the ID found.
     * @return The list of the ID found.
     * @throws ApsSystemException In case of error.
     */
    @Override
    public List<String> searchId(String typeCode, EntitySearchFilter[] filters) throws ApsSystemException {
        List<String> idList = null;
        try {
            idList = this.getEntitySearcherDao().searchId(typeCode, filters);
        } catch (Throwable t) {
            logger.error("Error detected while searching entities with typeCode {}", typeCode, t);
            throw new ApsSystemException("Error detected while searching entities", t);
        }
        return idList;
    }

    @Override
    public List<ApsEntityRecord> searchRecords(EntitySearchFilter[] filters) throws ApsSystemException {
        List<ApsEntityRecord> records = null;
        try {
            records = this.getEntitySearcherDao().searchRecords(filters);
        } catch (Throwable t) {
            logger.error("Error searching entity records", t);
            throw new ApsSystemException("Error searching entity records", t);
        }
        return records;
    }

    @Override
    public void reloadEntitiesReferences(ReloadingEntitiesReferencesEvent event) {
        try {
            String typeCode = null;
            this.reloadEntitiesReferences(typeCode);
        } catch (Throwable t) {
            logger.error("Error while refreshing entity refrences", t);
        }
    }

    @Override
    public Thread reloadEntitiesReferences(String typeCode) {
        ReloadingReferencesThread reloadThread = null;
        if (this.getStatus() == STATUS_READY || this.getStatus(typeCode) != STATUS_RELOADING_REFERENCES_IN_PROGRESS) {
            try {
                reloadThread = new ReloadingReferencesThread(this, typeCode);
                String threadName = RELOAD_REFERENCES_THREAD_NAME_PREFIX + this.getName() + "_" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
                reloadThread.setName(threadName);
                reloadThread.start();
                logger.info("Reloading references started");
            } catch (Throwable t) {
                logger.error("Error while starting up the reference reload procedure", t);
                throw new RuntimeException("Error while starting up the reference reload procedure", t);
            }
        } else {
            logger.info("Reloading entity references suspended: status {}", this.getStatus(typeCode));
        }
        return reloadThread;
    }

    /**
     * Reload the entity references.
     *
     * @param typeCode The type Code of entities to reload references. If null,
     * will reload all entities.
     * @throws ApsSystemException In case of error.
     */
    protected synchronized void reloadEntitySearchReferencesByType(String typeCode) throws ApsSystemException {
        if (null == typeCode) {
            throw new ApsSystemException("Error: invalid type code detected");
        }
        this.setStatus(ApsEntityManager.STATUS_RELOADING_REFERENCES_IN_PROGRESS, typeCode);
        try {
            EntitySearchFilter filter = new EntitySearchFilter(ENTITY_TYPE_CODE_FILTER_KEY, false, typeCode, false);
            EntitySearchFilter[] filters = {filter};
            List<String> entitiesId = this.getEntitySearcherDao().searchId(filters);
            for (int i = 0; i < entitiesId.size(); i++) {
                String entityId = (String) entitiesId.get(i);
                this.reloadEntityReferences(entityId);
            }
        } catch (Throwable t) {
            logger.error("Error reloading entity references of type: {}", typeCode, t);
            throw new ApsSystemException("Error reloading entity references of type: " + typeCode, t);
        } finally {
            this.setStatus(ApsEntityManager.STATUS_READY, typeCode);
        }
    }

    protected void reloadEntityReferences(String entityId) {
        try {
            IApsEntity entity = this.getEntity(entityId);
            if (entity != null) {
                this.getEntityDao().reloadEntitySearchRecords(entityId, entity);
            }
            logger.info("Entities search references reloaded {}", entityId);
        } catch (Throwable t) {
            logger.error("Error reloading the entities search references: {}", entityId, t);
        }
    }

    /**
     * Load the complete list of the entities.
     *
     * @return The complete list of entity IDs.
     * @throws ApsSystemException In case of error.
     * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntitySearcherDAO}
     * searchId(EntitySearchFilter[]) method
     */
    protected List<String> getAllEntityId() throws ApsSystemException {
        List<String> entitiesId = new ArrayList<>();
        try {
            entitiesId = this.getEntityDao().getAllEntityId();
        } catch (Throwable t) {
            logger.error("Error while loading the complete list of entity IDs", t);
            throw new ApsSystemException("Error while loading the complete list of entity IDs", t);
        }
        return entitiesId;
    }

    @Override
    public List<SmallEntityType> getSmallEntityTypes() {
        List<SmallEntityType> smallTypes = null;
        try {
            smallTypes = this.getEntityTypeFactory().extractSmallEntityTypes(this.getConfigItemName(), this.getEntityTypeDom());
            BeanComparator comparator = new BeanComparator("description");
            Collections.sort(smallTypes, comparator);
        } catch (Exception e) {
            logger.error("Error while extracting small entity types", e);
            throw new RuntimeException("Error while extracting small entity types", e);
        }
        return smallTypes;
    }

    /**
     * Return the stutus of the desired entity. If nell it returns the general
     * status.
     *
     * @param typeCode The Entity type to get the status from. It may be null.
     * @return The status of the desidered entity.
     */
    @Override
    public int getStatus(String typeCode) {
        if (typeCode == null) {
            return this.getStatus();
        }
        return this.getCacheWrapper().getEntityTypeStatus(typeCode);
    }

    @Override
    public int getStatus() {
        Set<Integer> status = new HashSet<>();
        List<String> codes = this.getEntityTypeCodes();
        for (String code : codes) {
            status.add(this.getCacheWrapper().getEntityTypeStatus(code));
        }
        if (status.contains(STATUS_RELOADING_REFERENCES_IN_PROGRESS)) {
            return STATUS_RELOADING_REFERENCES_IN_PROGRESS;
        } else if (status.contains(STATUS_NEED_TO_RELOAD_REFERENCES)) {
            return STATUS_NEED_TO_RELOAD_REFERENCES;
        }
        return STATUS_READY;
    }

    protected String getAttributeRolesFileName() {
        if (null == this.attributeRolesFileName) {
            return DEFAULT_ATTRIBUTE_ROLES_FILE_NAME;
        }
        return attributeRolesFileName;
    }

    public void setAttributeRolesFileName(String attributeRolesFileName) {
        this.attributeRolesFileName = attributeRolesFileName;
    }

    protected String getAttributeDisablingCodesFileName() {
        if (null == this.attributeDisablingCodesFileName) {
            return DEFAULT_ATTRIBUTE_DISABLING_CODES_FILE_NAME;
        }
        return attributeDisablingCodesFileName;
    }

    public void setAttributeDisablingCodesFileName(String disablingCodesFileName) {
        this.attributeDisablingCodesFileName = disablingCodesFileName;
    }

    /**
     * Imposta lo stato del tipo di entità dato. Se null imposta lo stato
     * generale. Set up the status of the given entity. If null it sets up the
     * overall status.
     *
     * @param status The status to set up.
     * @param typeCode The Entity Type where to apply the new status. If null it
     * sets up the general status.
     */
    protected void setStatus(int status, String typeCode) {
        this.getCacheWrapper().updateEntityTypeStatus(typeCode, status);
    }

    protected List<String> getEntityTypeCodes() {
        List<String> codes = new ArrayList<>();
        List<SmallEntityType> smallTypes = this.getSmallEntityTypes();
        for (SmallEntityType smallType : smallTypes) {
            codes.add(smallType.getCode());
        }
        return codes;
    }

    /**
     * Return the category manager used by the entities managed by the service.
     *
     * @return The category manager used by the entities.
     */
    protected ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    /**
     * Return the DAO to access the entity database with.
     *
     * @return The entity DAO.
     */
    protected abstract IEntityDAO getEntityDao();

    /**
     * Return the DAO used to search among entities.
     *
     * @return TheDAO used to search entities.
     */
    protected abstract IEntitySearcherDAO getEntitySearcherDao();

    protected IEntityManagerCacheWrapper getCacheWrapper() {
        return cacheWrapper;
    }

    public void setCacheWrapper(IEntityManagerCacheWrapper cacheWrapper) {
        this.cacheWrapper = cacheWrapper;
    }

}
