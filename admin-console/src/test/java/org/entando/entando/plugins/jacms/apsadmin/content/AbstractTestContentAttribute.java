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
package org.entando.entando.plugins.jacms.apsadmin.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public abstract class AbstractTestContentAttribute extends AbstractBaseTestContentAction {

	protected ILangManager getLangManager() {
		return (ILangManager) super.getService(SystemConstants.LANGUAGE_MANAGER);
	}

	protected AttributeTracer getTracer() {
		AttributeTracer tracer = new AttributeTracer();
		tracer.setLang(this.getLangManager().getDefaultLang());
		return tracer;
	}

	protected String executeCreateNewContent() throws Throwable {
		String result = this.executeCreateNewVoid(TEST_CONTENT_TYPE_CODE,
				TEST_CONTENT_DESCRIPTION, Content.STATUS_DRAFT, Group.FREE_GROUP_NAME, "admin");
		assertEquals(Action.SUCCESS, result);
		String contentOnSessionMarker = this.extractSessionMarker(TEST_CONTENT_TYPE_CODE, ApsAdminSystemConstants.ADD);
		Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
		assertNotNull(contentOnSession);
		return contentOnSessionMarker;
	}

	protected void initSaveContentAction(String contentOnSessionMarker) throws Throwable {
		this.initContentAction("/do/jacms/Content", "save", contentOnSessionMarker);
	}

	protected void executeAction(String expectedResult) throws Throwable {
		String result = super.executeAction();
		assertEquals(expectedResult, result);
	}

	protected void checkFieldErrors(int singleFieldErrors, String formFieldName) {
		Map<String, List<String>> fieldsErrors = this.getAction().getFieldErrors();
		//System.out.println(fieldsErrors);
		if (0 == singleFieldErrors) {
			assertEquals(1, fieldsErrors.size());
		} else {
			assertEquals(2, fieldsErrors.size());
		}
		assertTrue(fieldsErrors.containsKey("Monotext:MARKER"));
		List<String> fieldErrors = fieldsErrors.get(formFieldName);
		if (0 == singleFieldErrors) {
			assertNull(fieldErrors);
		} else {
			assertNotNull(fieldErrors);
			//System.out.println(titleFieldErrors);
			assertEquals(singleFieldErrors, fieldErrors.size());
		}
	}

	protected void deleteTestContent() throws Throwable {
		EntitySearchFilter filter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, TEST_CONTENT_DESCRIPTION, false);
		EntitySearchFilter[] filters = {filter};
		List<String> groupCodes = new ArrayList<String>();
		groupCodes.add(Group.ADMINS_GROUP_NAME);
		List<String> contentIds = this.getContentManager().loadWorkContentsId(filters, groupCodes);
		for (int i=0; i<contentIds.size(); i++) {
			String contentId = (String) contentIds.get(i);
			Content content = this.getContentManager().loadContent(contentId, false);
			this.getContentManager().deleteContent(content);
		}
	}

	protected static final String TEST_CONTENT_TYPE_CODE = "ALL";
	protected static final String TEST_CONTENT_DESCRIPTION = "CONTENT TEST DESCRIPTION";

}
