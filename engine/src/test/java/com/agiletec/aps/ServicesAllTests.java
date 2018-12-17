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
import org.entando.entando.aps.system.services.database.DatabaseServiceTest;
import org.entando.entando.aps.system.services.category.CategoryServiceTest;
import org.entando.entando.aps.system.services.dataobject.DataObjectServiceTest;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModelServiceTest;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesServiceTest;
import org.entando.entando.aps.system.services.digitalexchange.category.DigitalExchangeCategoriesServiceTest;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentsServiceTest;
import org.entando.entando.aps.system.services.entity.EntityManagerServiceIntegrationTest;
import org.entando.entando.aps.system.services.entity.EntityManagerServiceTest;
import org.entando.entando.aps.system.services.group.GroupServiceIntegrationTest;
import org.entando.entando.aps.system.services.group.GroupServiceTest;
import org.entando.entando.web.common.RestListRequestTest;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentServiceTest;
import org.entando.entando.aps.system.services.page.PageAuthorizationServiceIntegrationTest;
import org.entando.entando.aps.system.services.page.PageServiceIntegrationTest;
import org.entando.entando.aps.system.services.page.PageServiceWidgetIntegrationTest;
import org.entando.entando.aps.system.services.page.model.PageDtoBuilderTest;
import org.entando.entando.aps.system.services.pagemodel.PageModelDtoTest;
import org.entando.entando.aps.system.services.pagemodel.PageModelServiceTest;
import org.entando.entando.aps.system.services.pagesettings.PageSettingsServiceIntegrationTest;
import org.entando.entando.aps.system.services.user.UserServiceIntegrationTest;
import org.entando.entando.web.common.EntandoMessageCodesResolverTest;
import org.entando.entando.web.common.PagedMetadataTest;

public class ServicesAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ServicesAllTests.class.getName());

        suite.addTestSuite(GroupServiceIntegrationTest.class);
        suite.addTestSuite(PageSettingsServiceIntegrationTest.class);
        suite.addTestSuite(PageServiceIntegrationTest.class);
        suite.addTestSuite(PageAuthorizationServiceIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(RestListRequestTest.class));
        suite.addTest(new JUnit4TestAdapter(PagedMetadataTest.class));
        suite.addTest(new JUnit4TestAdapter(GroupServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(GuiFragmentServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DataObjectModelServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(PageModelDtoTest.class));
        suite.addTest(new JUnit4TestAdapter(PageModelServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(EntityManagerServiceTest.class));
        suite.addTestSuite(EntityManagerServiceIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(PageServiceWidgetIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DataObjectServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(CategoryServiceTest.class));
        suite.addTestSuite(UserServiceIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(DatabaseServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(EntandoMessageCodesResolverTest.class));
        suite.addTest(new JUnit4TestAdapter(PageDtoBuilderTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangesServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeComponentsServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeCategoriesServiceTest.class));
        return suite;
    }

}
