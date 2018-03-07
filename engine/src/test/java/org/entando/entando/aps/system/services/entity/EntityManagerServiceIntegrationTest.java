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
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
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
    public void testGetManagers() throws JsonProcessingException {
        RestListRequest restListRequest = new RestListRequest();
        restListRequest.setPageSize(5);
        PagedMetadata<String> res = this.entityManagerService.getEntityManagers(restListRequest);
        assertThat(res.getPage(), is(0));
        assertThat(res.getPageSize(), is(3));
        assertThat(res.getLastPage(), is(0));
        assertThat(res.getCount(), is(3));
        List<String> result = res.getBody();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

}
