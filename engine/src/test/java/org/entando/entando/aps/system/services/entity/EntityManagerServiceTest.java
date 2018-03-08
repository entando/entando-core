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
import junit.framework.Assert;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class EntityManagerServiceTest {

    @InjectMocks
    private EntityManagerService entityManagerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        entityManagerService.setEntityManagers(new ArrayList<>());
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void testGetNotExistingManager() throws RestRourceNotFoundException {
        this.entityManagerService.getEntityManager("customCode");
        Assert.fail();
    }

}
