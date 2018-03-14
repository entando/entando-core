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

import com.agiletec.aps.system.common.IManager;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Base interface for the entity managers.
 *
 * @author E.Santoboni
 */
public interface IEntityManager extends IManager {

    /**
     * Return the entity class handled by the manager.
     *
     * @return The class of the managed entity.
     */
    public Class getEntityClass();

    /**
     * Search entities.
     *
     * @param filters The filters used to find an sort the entities IDs that
     * match the given criteria.
     * @return The list of the IDs found.
     * @throws ApsSystemException In case of error.
     */
    public List<String> searchId(EntitySearchFilter[] filters) throws ApsSystemException;

    /**
     * Search entities.
     *
     * @param typeCode The code of the Entity Types to look for.
     * @param filters The search filters to apply to find and sort the ID found.
     * @return The list of the ID found.
     * @throws ApsSystemException In case of error.
     */
    public List<String> searchId(String typeCode, EntitySearchFilter[] filters) throws ApsSystemException;

    /**
     * Search the entity record
     *
     * @param filters The filters applied to
     * @return a list of entity records
     * @throws ApsSystemException
     */
    public List<ApsEntityRecord> searchRecords(EntitySearchFilter[] filters) throws ApsSystemException;

    /**
     * Create an object from the prototype.
     *
     * @param typeCode The type of the prototype to return.
     * @return The object created from the prototype.
     */
    public IApsEntity getEntityPrototype(String typeCode);

    /**
     * Get the entity identified by its ID.
     *
     * @param entityId The ID of the entity.
     * @return The requested entity.
     * @throws ApsSystemException In case of error.
     */
    public IApsEntity getEntity(String entityId) throws ApsSystemException;

    /**
     * Return the map of entity prototypes.
     *
     * @return The map of entity prototypes, indexed by the Entity Type
     */
    public Map<String, IApsEntity> getEntityPrototypes();

    /**
     * Get the prototype of the Entity Attributes.
     *
     * @return The map of the attribute prototypes, indxed
     */
    public Map<String, AttributeInterface> getEntityAttributePrototypes();

    public List<SmallEntityType> getSmallEntityTypes();

    /**
     * Check if the service uses the search engine or not.
     *
     * @return true if the service uses the search engine, false otherwise.
     */
    public boolean isSearchEngineUser();

    public Thread reloadEntitiesReferences(String typeCode);

    public int getStatus(String typeCode);

    public int getStatus();

    public Map<String, String> getAttributeDisablingCodes();

    public List<AttributeRole> getAttributeRoles();

    public AttributeRole getAttributeRole(String roleName);

    public static final String ENTITY_ID_FILTER_KEY = "entityId";

    public static final String ENTITY_TYPE_CODE_FILTER_KEY = "typeCode";

    public static final int STATUS_READY = 0;
    public static final int STATUS_RELOADING_REFERENCES_IN_PROGRESS = 1;
    public static final int STATUS_NEED_TO_RELOAD_REFERENCES = 2;

    public static final String DEFAULT_ATTRIBUTE_ROLES_FILE_NAME = "attributeRoles.xml";

    public static final String DEFAULT_ATTRIBUTE_DISABLING_CODES_FILE_NAME = "attributeDisablingCodes.xml";

}
