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
package com.agiletec.apsadmin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.apsadmin.TestLabelsProperties;

public class TestAdminConsoleLabelsProperties extends TestLabelsProperties {

    private static final Logger logger = LoggerFactory.getLogger(TestAdminConsoleLabelsProperties.class);
    private static String APSADMIN1_PATH = "com/agiletec/apsadmin/";
    private static String APSADMIN2_PATH = "org/entando/entando/apsadmin/";

    // TODO Fix the missing labels in the it property file
    /*    
    public void testGlobalAdminProperties1() throws Throwable {
        super.testGlobalMessagesLabelsTranslations(APSADMIN1_PATH);
    }
    */
    
    public void testGlobalAdminProperties2() throws Throwable {
        super.testGlobalMessagesLabelsTranslations(APSADMIN2_PATH);
    }

    public void testLangProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "admin/lang/");
    }

    public void testCategoryProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "category/");
    }

    public void testCommonProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "common/");
    }

    public void testSystemEntityProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "system/entity/");
    }

    public void testSystemEntityTypeProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "system/entity/type/");
    }

    public void testUserGroupProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "user/group/");
    }

    public void testUserRoleProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN1_PATH + "user/role/");
    }

    // TODO Fix the missing labels in the it property file
    /*
    public void testAdminProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "admin/");
    }
    */
    
    public void testApiProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "api/");
    }

    public void testCommonCurrebtUserProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "common/currentuser/");
    }

    public void testDataObjectModelProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "dataobject/model/");
    }

    public void testFileBrowserProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "filebrowser/");
    }

    public void testPortalGuiFragmentProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "portal/guifragment/");
    }

    // TODO Fix the missing labels in the it property file
    /*
    public void testPortalModelProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "portal/model/");
    }
     
    public void testUserProperties() throws Throwable {
        super.testPackageLabelsTranslations(APSADMIN2_PATH + "user/");
    }
    */
}
