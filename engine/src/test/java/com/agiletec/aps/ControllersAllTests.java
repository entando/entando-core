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
package com.agiletec.aps;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.web.dashboard.DashboardControllerIntegrationTest;
import org.entando.entando.web.page.PageControllerIntegrationTest;

public class ControllersAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ControllersAllTests.class.getName());

//        suite.addTest(new JUnit4TestAdapter(GroupControllerUnitTest.class));
//        suite.addTest(new JUnit4TestAdapter(GroupControllerIntegrationTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(GuiFragmentControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(GuiFragmentControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(GuiFragmentSettingsControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(GuiFragmentSettingsControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(GuiFragmentValidatorTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(DataObjectModelControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(DataObjectModelControllerIntegrationTest.class));

//        suite.addTest(new JUnit4TestAdapter(PageModelControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(PageModelControllerIntegrationTest.class));

//        suite.addTest(new JUnit4TestAdapter(PageSettingsControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(PageControllerTest.class));
        suite.addTest(new JUnit4TestAdapter(PageControllerIntegrationTest.class));

//        suite.addTest(new JUnit4TestAdapter(WidgetControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(WidgetControllerIntegrationTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(EntityManagerControllerTest.class));
//        suite.addTest(new JUnit4TestAdapter(ReloadConfigurationControllerTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(LanguageControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(LanguageControllerUnitTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(LabelControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(LabelControllerUnitTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(DataTypeControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(DataTypeControllerTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(ProfileTypeControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(UserProfileControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(ProfileTypeControllerTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(UserControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(UserControllerDeleteAuthoritiesIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(UserControllerUnitTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(UserSettingsControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(UserSettingsControllerUnitTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(DatabaseControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(DatabaseControllerTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(CategoryControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(CategoryControllerTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(RoleControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(RoleControllerUnitTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(PermissionControllerIntegrationTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(FileBrowserControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(FileBrowserControllerTest.class));
//
//        suite.addTest(new JUnit4TestAdapter(ActivityStreamControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(StreamInterceptorIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(DashboardControllerIntegrationTest.class));

//        suite.addTest(new JUnit4TestAdapter(ApiConsumerControllerIntegrationTest.class));
//        suite.addTest(new JUnit4TestAdapter(ApiConsumerControllerTest.class));

        return suite;
    }

}
