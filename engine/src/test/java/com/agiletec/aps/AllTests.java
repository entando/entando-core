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
package com.agiletec.aps;

import com.agiletec.aps.system.common.entity.TestEntityManager;
import com.agiletec.aps.system.services.authorization.TestAuthorityManager;
import com.agiletec.aps.system.services.authorization.TestAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.TestBaseConfigService;
import com.agiletec.aps.system.services.baseconfig.TestConfigItemDAO;
import com.agiletec.aps.system.services.category.TestCategoryManager;
import com.agiletec.aps.system.services.group.TestGroupManager;
import com.agiletec.aps.system.services.i18n.I18nManagerIntegrationTest;
import com.agiletec.aps.system.services.i18n.I18nManagerTest;
import com.agiletec.aps.system.services.i18n.cache.I18nManagerCacheWrapperTest;
import com.agiletec.aps.system.services.keygenerator.KeyGeneratorManagerIntegrationTest;
import com.agiletec.aps.system.services.keygenerator.KeyGeneratorManagerTest;
import com.agiletec.aps.system.services.keygenerator.TestKeyGeneratorDAO;
import com.agiletec.aps.system.services.lang.LangManagerIntegrationTest;
import com.agiletec.aps.system.services.lang.LangManagerTest;
import com.agiletec.aps.system.services.page.TestPageManager;
import com.agiletec.aps.system.services.page.widget.TestNavigatorExpression;
import com.agiletec.aps.system.services.page.widget.TestNavigatorParser;
import com.agiletec.aps.system.services.pagemodel.TestJaxbPageModel;
import com.agiletec.aps.system.services.pagemodel.TestPageModelDAO;
import com.agiletec.aps.system.services.pagemodel.TestPageModelDOM;
import com.agiletec.aps.system.services.pagemodel.TestPageModelManager;
import com.agiletec.aps.system.services.role.TestPermissionDAO;
import com.agiletec.aps.system.services.role.TestRoleDAO;
import com.agiletec.aps.system.services.role.TestRoleManager;
import com.agiletec.aps.system.services.url.TestURLManager;
import com.agiletec.aps.system.services.user.TestAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.TestUserDAO;
import com.agiletec.aps.system.services.widgettype.TestWidgetTypeDAO;
import com.agiletec.aps.system.services.widgettype.TestWidgetTypeDOM;
import com.agiletec.aps.system.services.widgettype.TestWidgetTypeManager;
import com.agiletec.aps.util.TestHtmlHandler;
import com.agiletec.plugins.jacms.aps.system.TestApplicationContext;
import com.agiletec.plugins.jacms.aps.system.services.content.TestGroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.TestSearchEngineManager;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.init.InitializerManagerTest;
import org.entando.entando.aps.system.init.util.TestQueryExtractor;
import org.entando.entando.aps.system.services.actionlog.TestActionLogDAO;
import org.entando.entando.aps.system.services.actionlog.TestActionLogManager;
import org.entando.entando.aps.system.services.api.ApiCatalogManagerIntegrationTest;
import org.entando.entando.aps.system.services.api.ApiCatalogManagerTest;
import org.entando.entando.aps.system.services.cache.CacheInfoManagerIntegrationTest;
import org.entando.entando.aps.system.services.cache.CacheInfoManagerTest;
import org.entando.entando.aps.system.services.dataobject.DataObjectManagerTest;
import org.entando.entando.aps.system.services.dataobject.TestDataObjectDAO;
import org.entando.entando.aps.system.services.dataobject.TestDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.TestDataObjectSearcherDAO;
import org.entando.entando.aps.system.services.dataobject.TestUtilizer;
import org.entando.entando.aps.system.services.dataobject.TestValidateDataObject;
import org.entando.entando.aps.system.services.dataobject.authorization.TestDataObjectAuthorization;
import org.entando.entando.aps.system.services.dataobject.entity.TestDataObjectEntityManager;
import org.entando.entando.aps.system.services.dataobject.parse.TestDataObjectDOM;
import org.entando.entando.aps.system.services.dataobject.widget.TestDataObjectListHelper;
import org.entando.entando.aps.system.services.dataobject.widget.TestDataObjectViewerHelper;
import org.entando.entando.aps.system.services.dataobjectdispender.TestDataObjectDispenser;
import org.entando.entando.aps.system.services.dataobjectmapper.DataObjectMapperManagerTest;
import org.entando.entando.aps.system.services.dataobjectmapper.cache.DataObjectMapperCacheWrapperTest;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModelManagerIntegrationTest;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModelManagerTest;
import org.entando.entando.aps.system.services.dataobjectmodel.TestDataObjectModelDAO;
import org.entando.entando.aps.system.services.guifragment.TestGuiFragmentManager;
import org.entando.entando.aps.system.services.i18n.TestApiI18nLabelInterface;
import org.entando.entando.aps.system.services.storage.TestLocalStorageManager;
import org.entando.entando.aps.system.services.userprofile.TestUserManager;
import org.entando.entando.aps.system.services.userprofile.UserProfileManagerIntegrationTest;
import org.entando.entando.aps.system.services.userprofile.UserProfileManagerTest;
import org.entando.entando.aps.system.services.widgettype.api.TestApiWidgetTypeInterface;

/**
 * @author W.Ambu
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for APS");

		//
		suite.addTest(new JUnit4TestAdapter(InitializerManagerTest.class));
		//
		suite.addTestSuite(TestEntityManager.class);
		//
		suite.addTestSuite(ApiCatalogManagerIntegrationTest.class);
		suite.addTest(new JUnit4TestAdapter(ApiCatalogManagerTest.class));
		//
		suite.addTestSuite(TestAuthorizationManager.class);
		suite.addTestSuite(TestAuthorityManager.class);
		//
		suite.addTestSuite(TestBaseConfigService.class);
		suite.addTestSuite(TestConfigItemDAO.class);
		//
		suite.addTestSuite(CacheInfoManagerIntegrationTest.class);
		suite.addTest(new JUnit4TestAdapter(CacheInfoManagerTest.class));
		//
		suite.addTestSuite(TestCategoryManager.class);
		//
		suite.addTestSuite(TestGroupManager.class);
		suite.addTestSuite(TestGroupUtilizer.class);
		//
		suite.addTestSuite(I18nManagerIntegrationTest.class);
		suite.addTest(new JUnit4TestAdapter(I18nManagerTest.class));
		suite.addTest(new JUnit4TestAdapter(I18nManagerCacheWrapperTest.class));
		//
		suite.addTestSuite(TestKeyGeneratorDAO.class);
		suite.addTestSuite(KeyGeneratorManagerIntegrationTest.class);
		suite.addTest(new JUnit4TestAdapter(KeyGeneratorManagerTest.class));
		//
		suite.addTestSuite(LangManagerIntegrationTest.class);
		suite.addTest(new JUnit4TestAdapter(LangManagerTest.class));
		//
		suite.addTestSuite(TestPageManager.class);
		suite.addTestSuite(TestNavigatorExpression.class);
		suite.addTestSuite(TestNavigatorParser.class);
		//
		suite.addTestSuite(TestJaxbPageModel.class);
		suite.addTestSuite(TestPageModelDAO.class);
		suite.addTestSuite(TestPageModelDOM.class);
		suite.addTestSuite(TestPageModelManager.class);
		//
		suite.addTestSuite(TestPermissionDAO.class);
		suite.addTestSuite(TestRoleDAO.class);
		suite.addTestSuite(TestRoleManager.class);

		//
		suite.addTestSuite(TestWidgetTypeDAO.class);
		suite.addTestSuite(TestWidgetTypeDOM.class);
		suite.addTestSuite(TestWidgetTypeManager.class);
		//
		suite.addTestSuite(TestURLManager.class);
		//
		suite.addTestSuite(TestAuthenticationProviderManager.class);
		suite.addTestSuite(TestUserDAO.class);
		suite.addTestSuite(TestUserManager.class);
		//
		suite.addTestSuite(TestApplicationContext.class);
		//
		suite.addTestSuite(TestHtmlHandler.class);
		//
		suite.addTestSuite(TestActionLogDAO.class);
		suite.addTestSuite(TestActionLogManager.class);
		//
		suite.addTestSuite(TestLocalStorageManager.class);
		//
		suite.addTestSuite(UserProfileManagerIntegrationTest.class);
		suite.addTestSuite(org.entando.entando.aps.system.services.userprofile.TestUserManager.class);
		suite.addTest(new JUnit4TestAdapter(UserProfileManagerTest.class));
		//
		suite.addTestSuite(TestGuiFragmentManager.class);
		//
		suite.addTestSuite(TestApiWidgetTypeInterface.class);
		suite.addTestSuite(TestApiI18nLabelInterface.class);
		//
		suite.addTestSuite(TestQueryExtractor.class);
		// DATA OBJECT
		suite.addTestSuite(TestDataObjectModelDAO.class);
		suite.addTestSuite(DataObjectModelManagerIntegrationTest.class);
		suite.addTest(new JUnit4TestAdapter(DataObjectModelManagerTest.class));
		suite.addTest(new JUnit4TestAdapter(DataObjectMapperCacheWrapperTest.class));
		suite.addTest(new JUnit4TestAdapter(DataObjectMapperManagerTest.class));
		suite.addTestSuite(TestDataObjectAuthorization.class);
		suite.addTestSuite(TestDataObjectEntityManager.class);
		suite.addTestSuite(TestDataObjectDOM.class);

		suite.addTestSuite(TestDataObjectListHelper.class);
		suite.addTestSuite(TestDataObjectViewerHelper.class);

		suite.addTestSuite(TestDataObjectDAO.class);
		suite.addTestSuite(TestDataObjectManager.class);
		suite.addTest(new JUnit4TestAdapter(DataObjectManagerTest.class));
		suite.addTestSuite(TestDataObjectSearcherDAO.class);
		suite.addTestSuite(TestValidateDataObject.class);
		suite.addTestSuite(TestUtilizer.class);
		suite.addTestSuite(TestDataObjectDispenser.class);

		suite.addTestSuite(TestSearchEngineManager.class);

		return suite;
	}

}
