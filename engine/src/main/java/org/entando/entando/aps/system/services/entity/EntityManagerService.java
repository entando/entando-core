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

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class EntityManagerService extends AbstractEntityService<IApsEntity, EntityTypeFullDto> implements IEntityManagerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected IDtoBuilder<IApsEntity, EntityTypeFullDto> getEntityTypeFullDtoBuilder(IEntityManager masterManager) {
        return new EntityTypeFullDtoBuilder(masterManager.getAttributeRoles());
    }

    @Override
    public PagedMetadata<String> getEntityManagers(RestListRequest requestList) {
        List<String> codes = new ArrayList<>();
        Filter[] filters = requestList.getFilter();
        List<IEntityManager> managers = this.getEntityManagers();
        Map<String, String> fieldMapping = new HashMap<>();
        fieldMapping.put(RestListRequest.SORT_VALUE_DEFAULT, "name");
        managers.stream().filter(i -> this.filterObjects(i, filters, fieldMapping)).forEach(i -> codes.add(i.getName()));
        Collections.sort(codes);
        if (!RestListRequest.DIRECTION_VALUE_DEFAULT.equals(requestList.getDirection())) {
            Collections.reverse(codes);
        }
        List<String> sublist = requestList.getSublist(codes);
        SearcherDaoPaginatedResult<IApsEntity> result = new SearcherDaoPaginatedResult(codes.size(), sublist);
        PagedMetadata<String> pagedMetadata = new PagedMetadata<>(requestList, result);
        pagedMetadata.setBody(sublist);
        return pagedMetadata;
    }

    @Override
    public PagedMetadata<EntityTypeShortDto> getShortEntityTypes(String entityManagerCode, RestListRequest requestList) {
        return super.getShortEntityTypes(entityManagerCode, requestList);
    }

    @Override
    public EntityManagerDto getEntityManager(String entityManagerCode) {
        IEntityManager entityManager = this.extractEntityManager(entityManagerCode);
        return this.getEntityManagerDtoBuilder().convert(entityManager);
    }

    @Override
    public EntityTypeFullDto getFullEntityType(String entityManagerCode, String entityTypeCode) {
        return super.getFullEntityType(entityManagerCode, entityTypeCode);
    }

    @Override
    public EntityTypeFullDto addEntityType(String entityManagerCode, EntityTypeDtoRequest bodyRequest, BindingResult bindingResult) {
        return super.addEntityType(entityManagerCode, bodyRequest, bindingResult);
    }

    @Override
    public EntityTypeFullDto updateEntityType(String entityManagerCode, EntityTypeDtoRequest request, BindingResult bindingResult) {
        return super.updateEntityType(entityManagerCode, request, bindingResult);
    }

    @Override
    public void deleteEntityType(String entityManagerCode, String entityTypeCode) {
        super.deleteEntityType(entityManagerCode, entityTypeCode);
    }

}
