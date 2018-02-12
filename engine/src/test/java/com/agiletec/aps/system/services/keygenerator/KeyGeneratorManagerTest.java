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
package com.agiletec.aps.system.services.keygenerator;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.cache.IKeyGeneratorManagerCacheWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class KeyGeneratorManagerTest {

    @Mock
    private IKeyGeneratorManagerCacheWrapper cacheWrapper;

    @Mock
    private IKeyGeneratorDAO keyGeneratorDAO;

    @InjectMocks
    private KeyGeneratorManager keyGeneratorManager;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_load_progressive_key() throws ApsSystemException {
        when(cacheWrapper.getUniqueKeyCurrentValue()).thenReturn(1);

        int pk = this.keyGeneratorManager.getUniqueKeyCurrentValue();
        assertThat(pk, is(2));
    }

}
