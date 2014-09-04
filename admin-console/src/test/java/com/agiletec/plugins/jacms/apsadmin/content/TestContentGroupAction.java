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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.Collection;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestContentGroupAction extends AbstractBaseTestContentAction {
	
	public void testInsertOnLineContents_1() throws Throwable {
		this.setUserOnSession("admin");
		String[] masterContentIds = {"ART111", "EVN20"};//CONTENUTI FREE
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, false);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertFalse(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "approveContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertTrue(content.isOnLine());
			}
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(1, messages.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testInsertOnLineContents_2() throws Throwable {
		this.setUserOnSession("supervisorCoach");
		String[] masterContentIds = {"ART180", "EVN20", "ART104", "ART102"};//2 CONTENUTI FREE, 1 Customers e 1 Coach
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, false);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertFalse(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "approveContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			int unpublishedContents = 0;
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				if (Group.FREE_GROUP_NAME.equals(content.getMainGroup())) {
					assertFalse(content.isOnLine());
					++unpublishedContents;
				} else {
					assertTrue(content.isOnLine());
				}
			}
			assertEquals(2, unpublishedContents);
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(1, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(2, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testInsertOnLineContents_3() throws Throwable {
		this.setUserOnSession("admin");
		String[] masterContentIds = {"ART179"};//CONTENUTO FREE con errori di validazione
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, false);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertFalse(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "approveContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(0, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(1, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testSuspendContents_1() throws Throwable {
		this.setUserOnSession("admin");
		String[] masterContentIds = {"ART111", "EVN20"};//CONTENUTO FREE
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, true);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertTrue(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "suspendContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(1, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(0, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testSuspendContents_2() throws Throwable {
		this.setUserOnSession("supervisorCoach");
		String[] masterContentIds = {"ART180", "EVN20", "ART104", "ART102"};//2 CONTENUTI FREE, 1 Customers e 1 Coach
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, true);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertTrue(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "suspendContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			int suspendedContents = 0;
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				if (!Group.FREE_GROUP_NAME.equals(content.getMainGroup())) {
					assertFalse(content.isOnLine());
					++suspendedContents;
				} else {
					assertTrue(content.isOnLine());
				}
			}
			assertEquals(2, suspendedContents);
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(1, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(2, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testDeleteContents_1() throws Throwable {
		this.setUserOnSession("admin");
		String[] masterContentIds = {"ART111", "EVN20"};//CONTENUTO FREE
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, false);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertFalse(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "deleteContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(1, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(0, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testDeleteContents_2() throws Throwable {
		this.setUserOnSession("supervisorCoach");
		String[] masterContentIds = {"ART180", "EVN20", "ART104", "ART102"};//2 CONTENUTI FREE, 1 Customers e 1 Coach
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, false);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertFalse(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "deleteContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			int deletedContents = 0;
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				if (content == null) {
					++deletedContents;
				} else {
					//Verifica che non si sono cancellati contenuti free
					String mainGroup = content.getMainGroup();
					assertTrue(Group.FREE_GROUP_NAME.equals(mainGroup));
				}
			}
			assertEquals(2, deletedContents);
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(1, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(2, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	public void testDeleteContents_3() throws Throwable {
		this.setUserOnSession("admin");
		String[] masterContentIds = {"ART111", "EVN20"};//CONTENUTO FREE
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, true);//CRERAZIONE CONTENUTI PUBBLICI
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertTrue(content.isOnLine());
			}
			this.initAction("/do/jacms/Content", "deleteContentGroup");
			this.addParameter("contentIds", newContentIds);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			for (int i=0; i<newContentIds.length; i++) {
				Content content = this.getContentManager().loadContent(newContentIds[i], false);
				assertNotNull(content);
				assertTrue(content.isOnLine());
			}
			ActionSupport action = this.getAction();
			Collection<String> messages = action.getActionMessages();
			assertEquals(0, messages.size());
			Collection<String> errors = action.getActionErrors();
			assertEquals(2, errors.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
	}
	
	/**
	 * Test trashing published contents
	 * @throws Throwable
	 */
	public void testTrashContents_1() throws Throwable {
		String result = null;
		String[] testContentsIds = null;
		String[] contentIds_1 = {"ART111", "EVN20"};
		try {
			this.setUserOnSession("admin");
			testContentsIds = this.addDraftContentsForTest(contentIds_1, true);// PUBLISHED CONTENTS			
			this.initAction("/do/jacms/Content", "trashContentGroup");
			this.addParameter("contentIds", testContentsIds);
			result = this.executeAction();
			assertEquals("cannotProceed", result);
			assertEquals(2, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(testContentsIds);
		}
	}
	
	/**
	 * Test trashing invalid (non existent) content
	 * @throws Throwable
	 */
	public void testTrashContents_2() throws Throwable {
		String result = null;
		String[] tempContents = null;
		String[] sourceContents = {"ART111", "EVN41", "RAH101"};
		try {
			this.setUserOnSession("admin");
			tempContents = this.addDraftContentsForTest(sourceContents, false);
			String[] testContentsIds = new String[(tempContents.length + 1)]; 
			for (int i = 0; i < tempContents.length; i++) {
				testContentsIds[i] = tempContents[i];
			}
			testContentsIds[tempContents.length] = "BAD1"; // ADD INVALID CONTENT
			this.initAction("/do/jacms/Content", "trashContentGroup");
			this.addParameter("contentIds", testContentsIds);
			result = this.executeAction();
			assertEquals("cannotProceed", result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(tempContents);
		}
	}
	
	/**
	 * Test trashing non published contents; one belongs to the free group
	 * @throws Throwable
	 */
	public void testTrashContents_3() throws Throwable {
		String result = null;
		String[] testContentsIds = null;
		String[] contentIds_1 = {"ART111", "EVN20"};
		try {
			this.setUserOnSession("admin");
			testContentsIds = this.addDraftContentsForTest(contentIds_1, false);// NON PUBLISHED CONTENTS
			this.initAction("/do/jacms/Content", "trashContentGroup");
			this.addParameter("contentIds", testContentsIds);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			assertTrue(this.getAction().getActionErrors().isEmpty());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(testContentsIds);
		}
	}
	
	/**
	 * Test trashing non published contents belonging to group which current user cannot handle 
	 * @throws Throwable
	 */
	public void testTrashContents_4() throws Throwable {
		String result = null;
		String[] testContentsIds = null;
		String[] contentIds_1 = {"ART120", "ART179"};		
		try {
			this.setUserOnSession("supervisorCoach");
			testContentsIds = this.addDraftContentsForTest(contentIds_1, false);// NON PUBLISHED CONTENTS
			this.initAction("/do/jacms/Content", "trashContentGroup");
			this.addParameter("contentIds", testContentsIds);
			result = this.executeAction();
			assertEquals("cannotProceed", result);
			assertEquals(2, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(testContentsIds);
		}
	}
	
	private void deleteContents(String[] contentIds) throws Throwable {
		for (int i=0; i<contentIds.length; i++) {
			Content content = this.getContentManager().loadContent(contentIds[i], false);
			if (null != content) {
				this.getContentManager().removeOnLineContent(content);
				this.getContentManager().deleteContent(content);
			}
		}
	}
	
}