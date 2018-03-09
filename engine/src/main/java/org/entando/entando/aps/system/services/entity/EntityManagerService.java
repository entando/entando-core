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

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityManagerService implements IEntityManagerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private List<IEntityManager> entityManagers;

    private IDtoBuilder<IEntityManager, EntityManagerDto> dtoBuilder;

    protected List<IEntityManager> getEntityManagers() {
        return entityManagers;
    }

    public void setEntityManagers(List<IEntityManager> entityManagers) {
        this.entityManagers = entityManagers;
    }

    protected IDtoBuilder<IEntityManager, EntityManagerDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IEntityManager, EntityManagerDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public PagedMetadata<String> getEntityManagers(RestListRequest requestList) {
        PagedMetadata<String> pagedMetadata = null;
        List<String> codes = new ArrayList<>();
        List<IEntityManager> managers = this.getEntityManagers();
        managers.stream().forEach(i -> codes.add(((IManager) i).getName()));
        SearcherDaoPaginatedResult result = new SearcherDaoPaginatedResult(managers.size(), codes);
        pagedMetadata = new PagedMetadata<>(requestList, result);
        pagedMetadata.setBody(codes);
        return pagedMetadata;
    }

    @Override
    public EntityManagerDto getEntityManager(String entityManagerCode) {
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
            throw new RestRourceNotFoundException(null, "entityManagerCode", entityManagerCode);
        }
        return this.getDtoBuilder().convert(entityManager);
    }

}
