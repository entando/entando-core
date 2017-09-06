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

import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.services.dataobject.TestDataObjectDAO;
import org.entando.entando.aps.system.services.dataobject.TestDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.TestPublicDataObjectSearcherDAO;
import org.entando.entando.aps.system.services.dataobject.TestUtilizer;
import org.entando.entando.aps.system.services.dataobject.TestValidateDataObject;
import org.entando.entando.aps.system.services.dataobject.authorization.TestDataObjectAuthorization;
import org.entando.entando.aps.system.services.dataobject.entity.TestDataObjectEntityManager;
import org.entando.entando.aps.system.services.dataobject.parse.TestDataObjectDOM;
import org.entando.entando.aps.system.services.dataobject.widget.TestDataObjectListHelper;
import org.entando.entando.aps.system.services.dataobject.widget.TestDataObjectViewerHelper;
import org.entando.entando.aps.system.services.dataobjectdispender.TestDataObjectDispenser;
import org.entando.entando.aps.system.services.dataobjectmodel.TestDataObjectModelDAO;
import org.entando.entando.aps.system.services.dataobjectmodel.TestDataObjectModelManager;
import org.entando.entando.aps.system.services.userprofile.TestUserProfileManager;

/**
 * @author W.Ambu
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for APS");

		//
		suite.addTestSuite(TestEntityManager.class);
		//
		suite.addTestSuite(TestApiCatalogManager.class);
		//
		suite.addTestSuite(TestAuthorizationManager.class);
		suite.addTestSuite(TestAuthorityManager.class);
		//
		suite.addTestSuite(TestBaseConfigService.class);
		suite.addTestSuite(TestConfigItemDAO.class);
		//
		suite.addTestSuite(TestCacheManager.class);
		//
		suite.addTestSuite(TestCategoryManager.class);
		//
		suite.addTestSuite(TestGroupManager.class);
		suite.addTestSuite(TestGroupUtilizer.class);
		//
		suite.addTestSuite(TestI18nManager.class);
		//
		suite.addTestSuite(TestKeyGeneratorDAO.class);
		suite.addTestSuite(TestKeyGeneratorManager.class);
		//
		suite.addTestSuite(TestLangManager.class);
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
		suite.addTestSuite(TestUserProfileManager.class);
		suite.addTestSuite(org.entando.entando.aps.system.services.userprofile.TestUserManager.class);
		//
		suite.addTestSuite(TestGuiFragmentManager.class);
		//
		suite.addTestSuite(TestApiWidgetTypeInterface.class);
		suite.addTestSuite(TestApiI18nLabelInterface.class);
		//
		suite.addTestSuite(TestQueryExtractor.class);

		// DATA OBJECT
		suite.addTestSuite(TestDataObjectModelDAO.class);
		suite.addTestSuite(TestDataObjectModelManager.class);

		suite.addTestSuite(TestDataObjectAuthorization.class);
		suite.addTestSuite(TestDataObjectEntityManager.class);
		suite.addTestSuite(TestDataObjectDOM.class);

		suite.addTestSuite(TestDataObjectListHelper.class);
		suite.addTestSuite(TestDataObjectViewerHelper.class);

		suite.addTestSuite(TestDataObjectDAO.class);
		suite.addTestSuite(TestDataObjectManager.class);
		suite.addTestSuite(TestPublicDataObjectSearcherDAO.class);
		suite.addTestSuite(TestValidateDataObject.class);
		suite.addTestSuite(TestUtilizer.class);
		suite.addTestSuite(TestDataObjectDispenser.class);

		return suite;
	}

}
