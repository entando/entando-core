/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.entity;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.entity.validator.EntityTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

public class EntityManagerService implements IEntityManagerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private List<IEntityManager> entityManagers;

    protected IDtoBuilder<IEntityManager, EntityManagerDto> getEntityManagerDtoBuilder() {
        return new DtoBuilder<IEntityManager, EntityManagerDto>() {
            @Override
            protected EntityManagerDto toDto(IEntityManager src) {
                return new EntityManagerDto(src);
            }
        };
    }

    protected IDtoBuilder<IApsEntity, EntityTypeShortDto> getEntityTypeShortDtoBuilder() {
        return new DtoBuilder<IApsEntity, EntityTypeShortDto>() {
            @Override
            protected EntityTypeShortDto toDto(IApsEntity src) {
                return new EntityTypeShortDto(src);
            }
        };
    }

    protected IDtoBuilder<IApsEntity, EntityTypeFullDto> getEntityTypeFullDtoBuilder(IEntityManager masterManager) {
        return new EntityTypeFullDtoBuilder(masterManager.getAttributeRoles());
    }

    protected List<IEntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(List<IEntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

    @Override
    public PagedMetadata<String> getEntityManagers(RestListRequest requestList) {
        List<String> codes = new ArrayList<>();
        Filter[] filters = requestList.getFilter();
        List<IEntityManager> managers = this.getEntityManagers();
        Map<String, String> fieldMapping = this.getEntityManagerFieldNameMapping();
        managers.stream().filter(i -> this.filterObjects(i, filters, fieldMapping)).forEach(i -> codes.add(i.getName()));
        Collections.sort(codes);
        if (!RestListRequest.DIRECTION_VALUE_DEFAULT.equals(requestList.getDirection())) {
            Collections.reverse(codes);
        }
        SearcherDaoPaginatedResult result = new SearcherDaoPaginatedResult(managers.size(), codes);
        PagedMetadata<String> pagedMetadata = new PagedMetadata<>(requestList, result);
        pagedMetadata.setBody(codes);
        return pagedMetadata;
    }

    protected Map<String, String> getEntityManagerFieldNameMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put(RestListRequest.SORT_VALUE_DEFAULT, "name");
        return mapping;
    }

    @Override
    public EntityManagerDto getEntityManager(String entityManagerCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        return this.getEntityManagerDtoBuilder().convert(entityManager);
    }

    @Override
    public PagedMetadata<EntityTypeShortDto> getShortEntityTypes(String entityManagerCode, RestListRequest requestList) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        List<IApsEntity> entityTypes = new ArrayList<>(entityManager.getEntityPrototypes().values());
        Map<String, String> fieldMapping = this.getEntityTypeFieldNameMapping();
        entityTypes.stream().filter(i -> this.filterObjects(i, requestList.getFilter(), fieldMapping));
        Collections.sort(entityTypes, new BeanComparator(this.getFieldName(requestList.getSort(), fieldMapping)));
        if (!RestListRequest.DIRECTION_VALUE_DEFAULT.equals(requestList.getDirection())) {
            Collections.reverse(entityTypes);
        }
        List<EntityTypeShortDto> dtoList = this.getEntityTypeShortDtoBuilder().convert(entityTypes);
        SearcherDaoPaginatedResult result = new SearcherDaoPaginatedResult(entityTypes.size(), dtoList);
        PagedMetadata<EntityTypeShortDto> pagedMetadata = new PagedMetadata<>(requestList, result);
        pagedMetadata.setBody(dtoList);
        return pagedMetadata;
    }

    protected Map<String, String> getEntityTypeFieldNameMapping() {
        Map<String, String> mapping = new HashMap<>();
        mapping.put(RestListRequest.SORT_VALUE_DEFAULT, "typeCode");
        mapping.put("name", "typeDescription");
        return mapping;
    }

    protected String getFieldName(String dtoFieldName, Map<String, String> mapping) {
        String name = mapping.get(dtoFieldName);
        return ((null != name) ? name : mapping.get(RestListRequest.SORT_VALUE_DEFAULT));
    }

    public EntityTypeFullDto getFullEntityTypes(String entityManagerCode, String entityTypeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
        if (null == entityManager) {
            logger.warn("no entity type found with code {}", entityTypeCode);
            throw new RestRourceNotFoundException("entityTypeCode", entityTypeCode);
        }
        return this.getEntityTypeFullDtoBuilder(entityManager).convert(entityType);
    }

    public EntityTypeFullDto updateEntityType(String entityManagerCode, EntityTypeFullDto request) {
        /*
        Group group = this.getGroupManager().getGroup(groupCode);
        if (null == group) {
            throw new RestRourceNotFoundException("group", groupCode);
        }
        group.setDescription(descr);
        try {
            this.getGroupManager().updateGroup(group);
            return this.getDtoBuilder().convert(group);
        } catch (ApsSystemException e) {
            logger.error("Error updating group {}", groupCode, e);
            throw new RestServerError("error in update group", e);
        }
         */
        return null;
    }

    @Override
    public void deleteEntityType(String entityManagerCode, String entityTypeCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        try {
            IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
            if (null == entityType) {
                return;
            }
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(entityType, "entityType");
            List<String> ids = entityManager.searchId(entityTypeCode, null);
            if (null != ids && ids.size() > 0) {
                bindingResult.reject(EntityTypeValidator.ERRCODE_ENTITY_TYPE_REFERENCES, new Object[]{entityTypeCode}, "entityType.cannot.delete.references");
            }
            ((IEntityTypesConfigurer) entityManager).removeEntityPrototype(entityTypeCode);
        } catch (ApsSystemException e) {
            logger.error("Error in delete entityType {}", entityTypeCode, e);
            throw new RestServerError("error in delete entityType", e);
        }
    }

    protected IEntityManager extractEntityManager(String entityManagerCode) {
        IEntityManager entityManager = null;
        List<IEntityManager> managers = this.getEntityManagers();
        for (IEntityManager manager : managers) {
            if (((IManager) manager).getName().equals(entityManagerCode)) {
                entityManager = manager;
                break;
            }
        }
        if (null == entityManager) {
            logger.warn("no entity manager found with code {}", entityManagerCode);
            throw new RestRourceNotFoundException("entityManagerCode", entityManagerCode);
        }
        return entityManager;
    }

    protected boolean filterObjects(Object bean, Filter[] filters, Map<String, String> mapping) {
        try {
            if (null == filters) {
                return true;
            }
            Map<String, Object> properties = BeanUtils.describe(bean);
            for (Filter filter : filters) {
                String fieldName = this.getFieldName(filter.getAttribute(), mapping);
                String value = (null != properties.get(fieldName))
                        ? properties.get(fieldName).toString() : null;
                if (null == value) {
                    continue;
                }
                if (!value.toLowerCase().contains(filter.getValue().toLowerCase())) {
                    return false;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("error filtering bean " + bean.getClass().getName(), e);
            throw new RestServerError("error filtering bean " + bean.getClass().getName(), e);
        }
        return true;
    }

}
