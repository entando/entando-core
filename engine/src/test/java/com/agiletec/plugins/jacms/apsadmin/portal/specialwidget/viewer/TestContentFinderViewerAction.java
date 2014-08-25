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
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.viewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestContentFinderViewerAction extends ApsAdminBaseTestCase {
	
	public void testFindContent_1() throws Throwable {
		String result = executeParametrizedSearchContents("admin", "pagina_11", "1", null);//Pagina del gruppo free
		assertEquals(Action.SUCCESS, result);
		
		ContentFinderViewerAction action = (ContentFinderViewerAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(14, contentIds.size());//Contenuti pubblici liberi o non liberi con free gruppo extra
		assertTrue(contentIds.contains("EVN25"));//Contenuto coach abilitato al gruppo free
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
	}
	
	public void testFindContent_2() throws Throwable {
		String result = executeParametrizedSearchContents("admin", "administrators_page", "1", null);//Pagina del gruppo amministratori
		assertEquals(Action.SUCCESS, result);
		
		ContentFinderViewerAction action = (ContentFinderViewerAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(23, contentIds.size());//Tutti i contenuti pubblici
	}
	
	public void testFindContent_3() throws Throwable {
		String result = executeParametrizedSearchContents("admin", "customers_page", "1", null);//Pagina del gruppo customers
		assertEquals(Action.SUCCESS, result);
		
		ContentFinderViewerAction action = (ContentFinderViewerAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(19, contentIds.size());// Contenuti pubblici liberi o non liberi con customers gruppo extra
		assertTrue(contentIds.contains("ART122"));//Contenuto del gruppo "administrators" abilitato al gruppo customers
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
		assertTrue(contentIds.contains("EVN25"));//Contenuto del gruppo "coach" abilitato al gruppo free
		assertTrue(contentIds.contains("ART111"));//Contenuto del gruppo "coach" abilitato al gruppo customers
	}
	
	public void testPerformSearch() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		this.executeParametrizedSearchContents("admin", "pagina_11", "1", null);//Pagina Free
		ContentFinderViewerAction action = (ContentFinderViewerAction) this.getAction();
		String[] order1 = {"ART121", "EVN21", "EVN20", "EVN25", 
				"EVN24", "EVN23", "EVN192", "EVN191", "RAH1", 
				"ART180", "EVN194", "EVN193", "ART1", "ART187"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("lastOrder", "DESC");
		params.put("lastGroupBy", "lastModified");
		params.put("groupBy", "lastModified");
		params.put("pageCode", "pagina_11");
		params.put("frame", "1");
		params.put("widgetTypeCode", "content_viewer");
		this.executeChangeOrder("admin", params);
		action = (ContentFinderViewerAction) this.getAction();
		contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[order1.length - i - 1], contents.get(i));
    	}
	}
	
	public void testExtendedSearch() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		this.executeParametrizedSearchContents("admin", "pagina_11", "1",  "VN1");//Pagina Free
		ContentFinderViewerAction action = (ContentFinderViewerAction) this.getAction();
		String[] rawSearchResult = {"EVN192", "EVN191", "EVN194", "EVN193"};
		String[] sortedResult = {"EVN194", "EVN193", "EVN192", "EVN191"};
		List<String> contents = action.getContents();
		assertEquals(rawSearchResult.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(rawSearchResult[i], contents.get(i));
    	}
		params.put("lastOrder", "ASC");
		params.put("lastGroupBy", "lastModified");
		params.put("groupBy", "code");
		params.put("pageCode", "pagina_11");
		params.put("contentIdToken", "VN1");
		params.put("frame", "1");
		params.put("widgetTypeCode", "content_viewer");
		this.executeChangeOrder("admin", params);
		action = (ContentFinderViewerAction) this.getAction();
		contents = action.getContents();
		assertEquals(rawSearchResult.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(sortedResult[i], contents.get(i));
    	}
	}
	
	public void testFailureJoinContent_1() throws Throwable {
		String result = this.executeJoinContent("admin", "pagina_11", "1", null);//ID Nullo
		assertEquals(Action.INPUT, result);
		
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErrors = action.getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> contentIdFieldErrors = fieldErrors.get("contentId");
		assertEquals(1, contentIdFieldErrors.size());
	}
	
	private String executeParametrizedSearchContents(String currentUserName, String pageCode, String frame, String partialId) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jacms/Page/SpecialWidget/Viewer", "searchContents");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		this.addParameter("widgetTypeCode", "content_viewer");
		// optional search value
		if (null != partialId) {
			this.addParameter("contentIdToken", partialId);
		}
		return this.executeAction();
	}
	
	private void executeChangeOrder(String currentUserName, Map<String, String> params) throws Throwable {
		this.initAction("/do/jacms/Page/SpecialWidget/Viewer", "changeContentListOrder");
		this.setUserOnSession(currentUserName);
		this.addParameters(params);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeJoinContent(String currentUserName, String pageCode, String frame, String contentId) throws Throwable {
		this.setUserOnSession(currentUserName);
		this.initAction("/do/jacms/Page/SpecialWidget/Viewer", "joinContent");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		this.addParameter("widgetTypeCode", "content_viewer");
		if (null != contentId) {
			this.addParameter("contentId", contentId);
		}
		return this.executeAction();
	}
	
}
