/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.apsadmin.user;

import java.util.List;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author f.deidda
 */
public class TestUserProfileFinderAction extends ApsAdminBaseTestCase {
	
    public void testSearchSuperUser() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "list");
            String result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearchGenericUser() throws Throwable {
        try {
            this.setUserOnSession("editor");
            this.initAction("/do/User", "list");
            String result = this.executeAction();
            assertEquals("apslogin", result);
        } catch (Throwable t) {
            throw t;
        }
    }
	
    public void testInsertUsername() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("username", "admin2");
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            this.executeAction();
            assertEquals(action.getUsername(), "admin2");
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearch_1() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(8, result.size());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testSearch_2() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("withProfile", "1");
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(4, result.size());
            assertEquals("editorCoach", result.get(0));
            assertEquals("editorCustomers", result.get(1));
            assertEquals("mainEditor", result.get(2));
            assertEquals("pageManagerCoach", result.get(3));
        } catch (Throwable t) {
            throw t;
        }
    }
    
    /*
	 * Filter by attribute
	 * 
     * Field Text
     * INPUT_FIELD: "<ATTRIBUTE_NAME>_textFieldName"
     * 
     * Field DATA
     * START INPUT_FIELD: "<ATTRIBUTE_NAME>_dateStartFieldName"
     * END INPUT_FIELD: "<ATTRIBUTE_NAME>_dateEndFieldName"
     */
    public void testFindByName() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("fullname_textFieldName", "Rick");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("editorCoach", result.get(0));
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testNotName() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("fullname_textFieldName", "notadmin");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertTrue(result.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testFindSurname() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("fullname_textFieldName", "on");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("editorCoach", result.get(0));
            assertEquals("pageManagerCoach", result.get(1));
        } catch (Throwable t) {
            throw t;
        }
    }
	
    public void testNotSurname() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("fullname_textFieldName", "notadmin");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assert (result.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }
	
    public void testFindByDateRange() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("birthdate_dateStartFieldName", "02/03/1945");
            this.addParameter("birthdate_dateEndFieldName", "04/09/2000");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("editorCustomers", result.get(0));
            assertEquals("mainEditor", result.get(1));
            assertEquals("pageManagerCoach", result.get(2));
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testNotDateInRange() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("birthdate_dateStartFieldName", "25/11/1947");
            this.addParameter("birthdate_dateEndFieldName", "20/05/1952");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertTrue(result.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testFindEmail() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("email_textFieldName", "@mailinator.com");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(4, result.size());
            assertEquals("editorCoach", result.get(0));
            assertEquals("editorCustomers", result.get(1));
            assertEquals("mainEditor", result.get(2));
            assertEquals("pageManagerCoach", result.get(3));
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testNotEmailFind() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("email_textFieldName", "notmail");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertTrue(result.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testFindCrossAttribute() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("email_textFieldName", "@mailinator.com");
            this.addParameter("birthdate_dateStartFieldName", "02/03/1945");
            this.addParameter("birthdate_dateEndFieldName", "03/09/2000");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("editorCustomers", result.get(0));
            assertEquals("mainEditor", result.get(1));
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testFindNotCrossAttribute() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("fullname_textFieldName", "Smith");
            this.addParameter("birthdate_dateStartFieldName", "02/03/1945");
            this.addParameter("birthdate_dateEndFieldName", "03/09/2000");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> result = action.getSearchResult();
            assertTrue(result.isEmpty());
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testGetEntityPrototypes() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            String result = this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<IApsEntity> currentEntityPrototypes = ((UserProfileFinderAction) action).getEntityPrototypes();
            assertEquals(Action.SUCCESS, result);
            assertEquals(currentEntityPrototypes.size(), 1);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testChangeEntityType() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("entityTypeCode", "TEST");
            String result = this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            assertEquals(Action.SUCCESS, result);
            assertEquals(((UserProfileFinderAction) action).getEntityTypeCode(), "TEST");
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testGetEmail() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/User", "search");
            this.addParameter("fullname_textFieldName", "Raimond");
            this.addParameter("entityTypeCode", SystemConstants.DEFAULT_PROFILE_TYPE_CODE);
            String result = this.executeAction();
            UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
            List<String> searchResult = action.getSearchResult();
            assertEquals(Action.SUCCESS, result);
            assertNotNull(searchResult);
            String email = ((UserProfileFinderAction) action).getEmailAttributeValue(searchResult.get(0));
            assertEquals("raimond.stevenson@mailinator.com", email);
        } catch (Throwable t) {
            throw t;
        }
    }
    
	/*
	 * Filter by role
	 * 
     * Field Text
     * INPUT_FIELD: "<ROLE_NAME>_textFieldName"
     * 
     * Field DATA
     * START INPUT_FIELD: "<ROLE_NAME>_dateStartFieldName"
     * END INPUT_FIELD: "<ROLE_NAME>_dateEndFieldName"
     */
    
	public void testSearchByRole() throws Throwable {
		String fullNameRole = SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/User", "search");
			this.addParameter(fullNameRole + "_textFieldName", "se");
			String result = this.executeAction();
			UserProfileFinderAction action = (UserProfileFinderAction) this.getAction();
			List<String> searchResult = action.getSearchResult();
			assertEquals(Action.SUCCESS, result);
			assertNotNull(searchResult);
			String[] expected = {"editorCustomers", "mainEditor"};
			assertEquals(expected.length, searchResult.size());
			for (int i = 0; i < expected.length; i++) {
				String expectedUsername = expected[i];
				assertEquals(expectedUsername, searchResult.get(i));
			}
		} catch (Throwable t) {
			throw t;
		}
	}
	
}
