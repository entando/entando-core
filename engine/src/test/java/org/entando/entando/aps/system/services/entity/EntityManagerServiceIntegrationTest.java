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

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import java.util.List;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntityManagerServiceIntegrationTest extends BaseTestCase {

    private IEntityManagerService entityManagerService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            entityManagerService = (IEntityManagerService) this.getApplicationContext().getBean("EntityManagerService");
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testGetManagers() {
        RestListRequest restListRequest = new RestListRequest();
        restListRequest.setPageSize(5);
        PagedMetadata<String> res = this.entityManagerService.getEntityManagers(restListRequest);
        assertEquals(1, res.getPage());
        assertEquals(5, res.getPageSize());
        assertEquals(1, res.getLastPage());
        assertEquals(2, res.getTotalItems());
        List<String> result = res.getBody();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetManager_1() {
        EntityManagerDto dto = this.entityManagerService.getEntityManager(SystemConstants.USER_PROFILE_MANAGER);
        assertNotNull(dto);
        assertEquals(SystemConstants.USER_PROFILE_MANAGER, dto.getCode());
        assertEquals(1, dto.getEntityTypes().size());
        assertEquals("PFL", dto.getEntityTypes().get(0).getCode());
    }

    @Test
    public void testGetNotExistingManager() throws ResourceNotFoundException {
        try {
            this.entityManagerService.getEntityManager("customManagerName");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof ResourceNotFoundException);
        }
    }

    @Test
    public void testGetEntityTypes_1() {
        RestListRequest restListRequest = new RestListRequest();
        PagedMetadata<EntityTypeShortDto> dtos = this.entityManagerService.getShortEntityTypes(SystemConstants.USER_PROFILE_MANAGER, restListRequest);
        assertNotNull(dtos);
        assertEquals(1, dtos.getBody().size());
    }

}
