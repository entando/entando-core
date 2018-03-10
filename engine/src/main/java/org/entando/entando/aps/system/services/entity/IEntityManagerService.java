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

import java.util.List;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.dataobject.model.DataTypesBodyRequest;

/**
 * @author E.Santoboni
 */
public interface IEntityManagerService {

    public PagedMetadata<String> getEntityManagers(RestListRequest requestList);

    public EntityManagerDto getEntityManager(String entityManagerCode);

    public PagedMetadata<EntityTypeShortDto> getShortEntityTypes(String entityManagerCode, RestListRequest requestList);

    public EntityTypeFullDto getFullEntityTypes(String entityManagerCode, String entityTypeCode);

    public List<EntityTypeFullDto> addEntityTypes(String entityManagerCode, DataTypesBodyRequest bodyRequest);

    public EntityTypeFullDto updateEntityType(String entityManagerCode, EntityTypeFullDto request);

    public void deleteEntityType(String entityManagerCode, String entityTypeCode);

}
