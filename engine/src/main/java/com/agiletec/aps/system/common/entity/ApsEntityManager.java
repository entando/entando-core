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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.beanutils.BeanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesEvent;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesObserver;
import com.agiletec.aps.system.common.entity.loader.AttributeDisablingCodesLoader;
import com.agiletec.aps.system.common.entity.loader.AttributeRolesLoader;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.parse.EntityHandler;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.common.entity.parse.IEntityTypeDOM;
import com.agiletec.aps.system.common.entity.parse.IEntityTypeFactory;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.DateConverter;

/**
 * This abstract service must be extended in all those services that make use of ApsEntities.
 * By default, extending the manager, it is necessary to implement the method that returns the specific 
 * category manager and, in the definition of the Spring service, the configuration item where to look 
 * for the definitions of the Entity Types handled by the service.
 * @author E.Santoboni
 */
public abstract class ApsEntityManager extends AbstractService 
		implements IEntityManager, IEntityTypesConfigurer, ReloadingEntitiesReferencesObserver {

	private static final Logger _logger = LoggerFactory.getLogger(ApsEntityManager.class);
	
	/**
	 * Initialize the service by loading the Entity Types form the configuration.
	 * @throws Exception If errors are detected during the service initialization.
	 */
	@Override
	public void init() throws Exception {
		this._entityDom.setRootElementName(this.getXmlAttributeRootElementName());
		String entityManagerName = super.getName();
		this._entityTypes = this._entityTypeFactory.getEntityTypes(this.getEntityClass(), 
				this.getConfigItemName(), this.getEntityTypeDom(), entityManagerName, this.getEntityDom());
		
		_logger.debug("{} : inizializated {} entity types", this.getName(), this._entityTypes.size());
	}
	
	@Override
	public void refresh() throws Throwable {
		super.refresh();
		this._attributeDisablingCodes = null;
		this._attributeRoles = null;
	}
	
	@Override
	public Map<String, String> getAttributeDisablingCodes() {
		if (null != this._attributeDisablingCodes) {
			//codes already loaded
			return this._attributeDisablingCodes;
		}
		//codes not loaded yet
		AttributeDisablingCodesLoader loader = new AttributeDisablingCodesLoader();
		this._attributeDisablingCodes = loader.extractDisablingCodes(this.getAttributeDisablingCodesFileName(), super.getBeanFactory(), this);
		Map<String, String> clone = new HashMap<String, String>();
		clone.putAll(this._attributeDisablingCodes);
		return clone;
	}
	
	@Override
	public List<AttributeRole> getAttributeRoles() {
		if (null != this._attributeRoles) {
			//roles already loaded
			return this.getOrderedAttributeRoles();
		}
		//roles not loaded yet
		this.initAttributeRoles();
		return this.getOrderedAttributeRoles();
	}
	
	protected void initAttributeRoles() {
		this._attributeRoles = new HashMap<String, AttributeRole>();
		AttributeRolesLoader loader = new AttributeRolesLoader();
		this._attributeRoles = loader.extractAttributeRoles(this.getAttributeRolesFileName(), super.getBeanFactory(), this);
	}
	
	private List<AttributeRole> getOrderedAttributeRoles() {
		List<AttributeRole> roles = new ArrayList<AttributeRole>(this._attributeRoles.size());
		Iterator<AttributeRole> iter = this._attributeRoles.values().iterator();
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
		if (null == this._attributeRoles) {
			this.initAttributeRoles();
		}
		AttributeRole role = this._attributeRoles.get(roleName);
		if (null != role) {
			return role.clone();
		}
		return null;
	}
	
	/**
	 * Create and populate the entity as specified by its type and XML definition.
	 * @param entityTypeCode The Entity Type code.
	 * @param xml The XML of the associated entity.
	 * @return The populated entity.
	 * @throws ApsSystemException If errors detected while retrieving the entity.
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
		} catch (Throwable t) {
			_logger.error("Error detected while creating the entity. typecode: {} - xml: {}", entityTypeCode, xml, t);
			//ApsSystemUtils.logThrowable(t, this, "createEntityFromXml");
			throw new ApsSystemException("Error detected while creating the entity", t);
		}
	}

	/**
	 * Indicates whether the service makes use of the search engine.
	 * Default value: false.
	 * @return true if the services uses the search engine, false otherwise.
	 */
	@Override
	public boolean isSearchEngineUser() {
		return false;
	}

	/**
	 * Create an entity prototype given the relative code.
	 * This method is for those managers that extend the ApsEntityManager.
	 * @param typeCode The entity code.
	 * @return The prototype of requested entity.
	 * @deprecated From jAPS 2.0 version 2.0.9, use getEntityPrototype(String typeCode)
	 */
	protected IApsEntity createEntityPrototype(String typeCode) {
		return this.getEntityPrototype(typeCode);
	}
	
	@Override
	public IApsEntity getEntityPrototype(String typeCode) {
		IApsEntity entityType = this.getEntityTypes().get(typeCode);
		if (null == entityType) {
			return null;
		}
		return entityType.getEntityPrototype();
	}

	@Override
	public Map<String, IApsEntity> getEntityPrototypes() {
		Collection<IApsEntity> entityTypes = this._entityTypes.values();
		Map<String, IApsEntity> entityTypesMap = new HashMap<String, IApsEntity>(entityTypes.size());
		Iterator<IApsEntity> iter = entityTypes.iterator();
		while (iter.hasNext()) {
			IApsEntity entityType = iter.next();
			IApsEntity clone = entityType.getEntityPrototype();
			entityTypesMap.put(clone.getTypeCode(), clone);
		}
		return entityTypesMap;
	}

	/**
	 * Add a new entity prototype on the catalog.
	 * @param entityType The entity type to add.
	 * @throws ApsSystemException In case of error.
	 */
	@Override
	public void addEntityPrototype(IApsEntity entityType) throws ApsSystemException {
		if (null == entityType) {
			throw new ApsSystemException("Invalid entity type to add");
		}
		Map<String, IApsEntity> newEntityTypes = new HashMap<String, IApsEntity>(this._entityTypes);
		newEntityTypes.put(entityType.getTypeCode(), entityType);
		this.updateEntityPrototypes(newEntityTypes);
		this.notifyEntityTypesChanging(null, entityType, EntityTypesChangingEvent.INSERT_OPERATION_CODE);
	}

	/**
	 * Update an entity prototype on the catalog.
	 * @param entityType The entity type to update.
	 * @throws ApsSystemException In case of error.
	 */
	@Override
	public void updateEntityPrototype(IApsEntity entityType) throws ApsSystemException {
		if (null == entityType) {
			throw new ApsSystemException("Invalid entity type to update");
		}
		IApsEntity oldEntityType = this._entityTypes.get(entityType.getTypeCode());
		if (null == oldEntityType) {
			throw new ApsSystemException("No entity type to update with code '" + entityType.getTypeCode() + "' where found");
		}
		Map<String, IApsEntity> newEntityTypes = new HashMap<String, IApsEntity>(this._entityTypes);
		newEntityTypes.put(entityType.getTypeCode(), entityType);
		this.updateEntityPrototypes(newEntityTypes);
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
			if ((newRoles.length == 0 && oldRoles.length == 0)) {
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
	 * @param entityTypeCode The code of the entity type to remove.
	 * @throws ApsSystemException In case of error.
	 */
	@Override
	public void removeEntityPrototype(String entityTypeCode) throws ApsSystemException {
		IApsEntity entityTypeToRemove = this._entityTypes.get(entityTypeCode);
		if (null == entityTypeToRemove) {
			throw new ApsSystemException("No entity type to remove with code '" + entityTypeCode + "' were found");
		}
		Map<String, IApsEntity> newEntityTypes = new HashMap<String, IApsEntity>(this._entityTypes);
		newEntityTypes.remove(entityTypeCode);
		this.updateEntityPrototypes(newEntityTypes);
		this.notifyEntityTypesChanging(entityTypeToRemove, null, EntityTypesChangingEvent.REMOVE_OPERATION_CODE);
	}

	/**
	 * Update entity prototypes
	 * @param newEntityTypes the map, indexed by code, containing the new entities.
	 * @throws ApsSystemException If errors are detected during the process.
	 */
	private void updateEntityPrototypes(Map<String, IApsEntity> newEntityTypes) throws ApsSystemException {
		try {
			this._entityTypeFactory.updateEntityTypes(newEntityTypes, this.getConfigItemName(), this.getEntityTypeDom());
			this.refresh();
		} catch (Throwable t) {
			//ApsSystemUtils.logThrowable(t, this, "updateEntityPrototypes");
			_logger.error("Error detected while updating entity prototypes", t);
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
	 * @return The map of the Entity Types indexed by the type code.
	 */
	protected Map<String, IApsEntity> getEntityTypes() {
		return this._entityTypes;
	}

	@Override
	public Map<String, AttributeInterface> getEntityAttributePrototypes() {
		Collection<AttributeInterface> attributes = this.getEntityTypeDom().getAttributeTypes().values();
		Map<String, AttributeInterface> attributeMap = new HashMap<String, AttributeInterface>(attributes.size());
		Iterator<AttributeInterface> iter = attributes.iterator();
		while (iter.hasNext()) {
			AttributeInterface attributeInterface = iter.next();
			AttributeInterface clone = (AttributeInterface) attributeInterface.getAttributePrototype();
			attributeMap.put(clone.getType(), clone);
		}
		return attributeMap;
	}
	
	/**
	 * Set up the Entity Types factory.
	 * This method is used and hereby found in the spring XML configuration of the service.
	 * By default, the definition of the abstract service in the Spring configuration presents a 
	 * standard factory; such definition must be substituted in the declaration of the service if
	 * specific operations are required by the particular structure of the Entity Type to manage.
	 * @param entityTypeFactory The factory of Entity Types.
	 */
	public void setEntityTypeFactory(IEntityTypeFactory entityTypeFactory) {
		this._entityTypeFactory = entityTypeFactory;
	}
	
	/**
	 * This method is used and hereby found in the spring XML configuration of the service.
	 * By default, the declaration of the abstract service in the Spring configuration presents a 
	 * standard class (ApsEntity); this class must be substituted in the definition of the service
	 * if a different class, which extends the standard ApsEntity, must be used.
	 * This method checks the validity of the class.
	 * @param className The name of the entity class.
	 */
	public void setEntityClassName(String className) {
		try {
			this._entityClass = Class.forName(className);
			Class check = this._entityClass;
			do {
				Class[] interfaces = check.getInterfaces();
				for (int j=0; j<interfaces.length; j++) {
					if (interfaces[j].equals(IApsEntity.class)) return;
				}
				check = check.getSuperclass();
			} while (!check.equals(Object.class));
			throw new RuntimeException("Invalid entity class");
		} catch (ClassNotFoundException e) {
			_logger.error("Errore creating the entity class", e);
			//ApsSystemUtils.logThrowable(e, this, "setEntityClassName","Error detected while creating the entity class");
			throw new RuntimeException("Error creating the entity class", e);
		}
	}
	
	/**
	 * @deprecated From jAPS 2.0 version 2.0.9, use setEntityClassName
	 */
	public void setEntityClass(String className) {
		this.setEntityClassName(className);
	}
	
	@Override
	public Class getEntityClass() {
		return this._entityClass;
	}
	
	/**
	 * Return The name of the configuration item where to extract the definition of the Entity types.
	 * @param confItemName The name of the configuration item.
	 */
	protected String getConfigItemName() {
		return this._configItemName;
	}
	
	/**
	 * Prepare the configuration item where to extract the definition of the various Entity types
	 * managed by the service.
	 * @param confItemName The name of the configuration item where to extract the definition
	 * of the Entity types
	 */
	public void setConfigItemName(String confItemName) {
		this._configItemName = confItemName;
	}
	
	protected IEntityTypeDOM getEntityTypeDom() {
		return this._entityTypeDom;
	}
	
	/**
	 * Prepare the DOM class that interprets the XML defining the various Entity Types managed by the service.
	 * This method is used and hereby found in the Spring XML configuration of the service.
	 * By default, the declaration of the abstract service contains the standard DOM class, namely the EntityTypeDOM;
	 * this definition can be substituted in the declaration of the service if a different DOM class,
	 * implementing the IEntityTypeDOM interface, is used. This is, for example, to interpret
	 * customized Entity Types -all implementing the ApsEntity- in a new service.
	 * @param entityTypeDom The DOM class that parses the XML configuring the Entity Types.
	 */
	public void setEntityTypeDom(IEntityTypeDOM entityTypeDom) {
		this._entityTypeDom = entityTypeDom;
	}
	
	/**
	 * Set up the name of the root attribute in the XML representing the single entity.
	 * This method is used and found in the Spring XML definition of the service.
	 * By default, the definition of the abstract service in the Spring configuration, presents the
	 * name "entity"; this definition can be substituted in the implementation of the service if a new name is used.
	 * @param xmlAttributeRootElementName The name of the root attribute.
	 */
	public void setXmlAttributeRootElementName(String xmlAttributeRootElementName) {
		this._xmlAttributeRootElementName = xmlAttributeRootElementName;
	}
	
	protected String getXmlAttributeRootElementName() {
		return this._xmlAttributeRootElementName;
	}
	
	/**
	 * Set up the handler class that parses the XML defining single entities.
	 * This method is used and hereby found in the Spring XML configuration of the service.
	 * The definition of the abstract service in the Spring configuration presents a default
	 * handler class, namely the EntityHandler; this definition can be changed in the declaration of the service
	 * if a particular handler, which extends EntityHandler, is used to parse specific entities.
	 * The class of such entities must extend the ApsEntity class to be correctly managed by the service.
	 * @param entityHandler The handler class that parses the XML of the single entities.
	 */
	public void setEntityHandler(EntityHandler entityHandler) {
		this._entityHandler = entityHandler;
	}

	/**
	 * Return the handler class that parses the single entity.
	 * This method returns a prototype ready to be used to parse an entity.
	 * @return The handler class that parses the XML of the entity.
	 */
	protected EntityHandler getEntityHandler() {
		return this._entityHandler.getHandlerPrototype();
	}
	
	protected IApsEntityDOM getEntityDom() {
		return this._entityDom;
	}
	
	/**
	 * Set the DOM class that generates the XML that represents a single entity.
	 * This method is used and hereby found in the Spring XML configuration of the service.
	 * By default, the definition of the abstract service in the Spring configuration, presents a standard
	 * DOM class, namely the ApsEntityDOM; this definition can be substituted in the declaration of the service 
	 * if a different DOM class, implementing the IApsEntityDOM interface, is used to generate 
	 * the XML of particular entities. Such entities are mapped to a class that must extend,
	 * as usual, the ApsEntity class.
	 */
	public void setEntityDom(IApsEntityDOM entityDom) {
		this._entityDom = entityDom;
	}

	/**
	 * Search entities.
	 * @param filters The filters used to find an sort the entities ID that match
	 * the given criteria.
	 * @return The list of the IDs found.
	 * @throws ApsSystemException In case of error.
	 */
	@Override
	public List<String> searchId(EntitySearchFilter[] filters) throws ApsSystemException {
		List<String> idList = null;
		try {
			idList = this.getEntitySearcherDao().searchId(filters);
		} catch (Throwable t) {
			_logger.error("Error detected while searching entities", t);
			//ApsSystemUtils.logThrowable(t, this, "searchId");
			throw new ApsSystemException("Error detected while searching entities", t);
		}
		return idList;
	}

	/**
	 * Search entities.
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
			_logger.error("Error detected while searching entities with typeCode {}", typeCode, t);
			//ApsSystemUtils.logThrowable(t, this, "searchId");
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
			_logger.error("Error searching entity records", t);
			//ApsSystemUtils.logThrowable(t, this, "searchRecords");
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
			_logger.error("Error while refreshing entity refrences", t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntitiesReferences", "Error while refreshing entity refrences");
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
				_logger.info("Reloading references started");
			} catch (Throwable t) {
				throw new RuntimeException("Error while starting up the reference reload procedure", t);
			}
		} else {
			_logger.info("Reloading entity references suspended: status {}",this.getStatus(typeCode));
		}
		return reloadThread;
	}

	/**
	 * Reload the entity references.
	 * @param typeCode The type Code of entities to reload references. If null, will reload all entities.
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
			for (int i=0; i<entitiesId.size(); i++) {
				String entityId = (String) entitiesId.get(i);
				this.reloadEntityReferences(entityId);
			}
		} catch (Throwable t) {
			_logger.error("Error reloading entity references of type: {}", typeCode, t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntitySearchReferencesByType");
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
			_logger.info("Entities search references reloaded {}", entityId);
		} catch (Throwable t) {
			_logger.error("Error reloading the entities search references: {}", entityId, t);
			//ApsSystemUtils.logThrowable(t, this, "reloadEntityReferences", "Error reloading the entities search references: " + entityId);
		}
	}
	
	/**
	 * Load the complete list of the entities.
	 * @return The complete list of entity IDs.
	 * @throws ApsSystemException In case of error.
	 * @deprecated From jAPS 2.0 version 2.0.9, use {@link IEntitySearcherDAO} searchId(EntitySearchFilter[]) method 
	 */
	protected List<String> getAllEntityId() throws ApsSystemException {
		List<String> entitiesId = new ArrayList<String>();
		try {
			entitiesId = this.getEntityDao().getAllEntityId();
		} catch (Throwable t) {
			_logger.error("Error while loading the complete list of entity IDs", t);
			//ApsSystemUtils.logThrowable(t, this, "getAllEntityId");
			throw new ApsSystemException("Error while loading the complete list of entity IDs", t);
		}
		return entitiesId;
	}

	/**
	 * Return the stutus of the desired entity. If nell it returns the general status.
	 * @param typeCode The Entity type to get the status from. It may be null.
	 * @return The status of the desidered entity. 
	 */
	@Override
	public int getStatus(String typeCode) {
		if (typeCode == null) {
			return this.getStatus();
		}
		Integer status = this._typesStatus.get(typeCode);
		if (status != null) {
			return status.intValue();
		}
		return STATUS_READY;
	}

	@Override
	public int getStatus() {
		if (this._typesStatus.containsValue(new Integer(STATUS_RELOADING_REFERENCES_IN_PROGRESS))) {
			return STATUS_RELOADING_REFERENCES_IN_PROGRESS;
		} else if (this._typesStatus.containsValue(new Integer(STATUS_NEED_TO_RELOAD_REFERENCES))) {
			return STATUS_NEED_TO_RELOAD_REFERENCES;
		}
		return STATUS_READY;
	}
	
	protected String getAttributeRolesFileName() {
		if (null == this._attributeRolesFileName) {
			return DEFAULT_ATTRIBUTE_ROLES_FILE_NAME;
		}
		return _attributeRolesFileName;
	}
	public void setAttributeRolesFileName(String attributeRolesFileName) {
		this._attributeRolesFileName = attributeRolesFileName;
	}
	
	protected String getAttributeDisablingCodesFileName() {
		if (null == this._attributeDisablingCodesFileName) {
			return DEFAULT_ATTRIBUTE_DISABLING_CODES_FILE_NAME;
		}
		return _attributeDisablingCodesFileName;
	}
	public void setAttributeDisablingCodesFileName(String disablingCodesFileName) {
		this._attributeDisablingCodesFileName = disablingCodesFileName;
	}
	
	/**
	 * Imposta lo stato del tipo di entità dato. Se null imposta lo stato generale.
	 * Set up the status of the given entity. If null it sets up the overall status.
	 * @param status The status to set up.
	 * @param typeCode The Entity Type where to apply the new status. If null it sets up the
	 * general status. 
	 */
	protected void setStatus(int status, String typeCode) {
		this._typesStatus.put(typeCode, new Integer(status));
	}

	protected List<String> getEntityTypeCodes() {
		return new ArrayList<String>(this._entityTypes.keySet());
	}
	
	/**
	 * Return the category manager used by the entities managed by the service.
	 * @return The category manager used by the entities.
	 */
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	/**
	 * Return the DAO to access the entity database with.
	 * @return The entity DAO.
	 */
	protected abstract IEntityDAO getEntityDao();

	/**
	 * Return the DAO used to search among entities.
	 * @return TheDAO used to search entities.
	 */
	protected abstract IEntitySearcherDAO getEntitySearcherDao();
	
	/**
	 * Map of the entity prototypes, indexed by the type code.
	 */
	private Map<String, IApsEntity> _entityTypes;

	private IEntityTypeFactory _entityTypeFactory;

	private Class _entityClass;

	private String _configItemName;

	private IEntityTypeDOM _entityTypeDom;

	private EntityHandler _entityHandler;

	private String _xmlAttributeRootElementName;

	private IApsEntityDOM _entityDom;
	
	private ICategoryManager _categoryManager;
	
	private Map<String, Integer> _typesStatus = new HashMap<String, Integer>();
	
	private Map<String, AttributeRole> _attributeRoles = null;
	
	private Map<String, String> _attributeDisablingCodes = null;
	
	private String _attributeRolesFileName;
	
	private String _attributeDisablingCodesFileName;
	
	/**
	 * Prefix of the thread used for references reloading.
	 */
	public static final String RELOAD_REFERENCES_THREAD_NAME_PREFIX = "RELOAD_REFERENCES_INDEX_";

}
