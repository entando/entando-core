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
package com.agiletec.aps.system.services.baseconfig;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author M.Casari
 */
public class TestBaseConfigService extends BaseTestCase {

    private ConfigInterface configInterface;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.configInterface = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
    }

    public void testGetParam() throws ApsSystemException {
        String param = this.configInterface.getParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE);
        assertEquals(param, "notfound");
    }

    public void testUpdateParam_1() throws ApsSystemException {
        String value = this.configInterface.getParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE);
        assertEquals(value, "notfound");
        this.configInterface.updateParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE, "newValue");
        value = this.configInterface.getParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE);
        assertEquals(value, "newValue");
        this.configInterface.updateParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE, "notfound");
        value = this.configInterface.getParam(SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE);
        assertEquals(value, "notfound");
    }

    public void testUpdateParam_2() throws ApsSystemException {
        String paramName = "wrongParamName";
        String value = this.configInterface.getParam(paramName);
        assertNull(value);
        this.configInterface.updateParam(paramName, "newValue");
        value = this.configInterface.getParam(paramName);
        assertNull(value);
    }

    public void testUpdateParams() throws ApsSystemException {
        String paramName1 = SystemConstants.CONFIG_PARAM_NOT_FOUND_PAGE_CODE;
        String paramName2 = "wrongParamName";
        try {
            String value1 = this.configInterface.getParam(paramName1);
            String value2 = this.configInterface.getParam(paramName2);
            assertEquals(value1, "notfound");
            assertNull(value2);
            Map<String, String> paramsToUpdate = new HashMap<>();
            paramsToUpdate.put(paramName1, "newValue1");
            paramsToUpdate.put(paramName2, "newValue2");
            this.configInterface.updateParams(paramsToUpdate);
            value1 = this.configInterface.getParam(paramName1);
            value2 = this.configInterface.getParam(paramName2);
            assertEquals(value1, "newValue1");
            assertNull(value2);
        } catch (Exception e) {
            throw e;
        } finally {
            this.configInterface.updateParam(paramName1, "notfound");
            String value = this.configInterface.getParam(paramName1);
            assertEquals(value, "notfound");
        }
    }

}
