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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.WorkContentSearcherDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentFinderAction extends AbstractBaseTestContentAction {
	
	public void testGetList() throws Throwable {
		String result = this.executeGetList("admin");
		assertEquals(Action.SUCCESS, result);
		List<String> contents = (List<String>) ((IContentFinderAction)this.getAction()).getContents();
		assertEquals(24, contents.size());
		
		result = this.executeGetList("editorCoach");
		assertEquals(Action.SUCCESS, result);
		contents = (List<String>) ((IContentFinderAction)this.getAction()).getContents();
		assertEquals(8, contents.size());
		
		result = this.executeGetList("editorCustomers");
		assertEquals(Action.SUCCESS, result);
		contents = (List<String>) ((IContentFinderAction)this.getAction()).getContents();
		assertEquals(2, contents.size());
		
		result = this.executeGetList("pageConfigCustomers");
		assertEquals("apslogin", result);
	}
	
	private String executeGetList(String currentUserName) throws Throwable {
		this.initAction("/do/jacms/Content", "list");
		this.setUserOnSession(currentUserName);
		return this.executeAction();
	}
	
	public void testPerformSearch_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		this.executeSearch("admin", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART112","ART122","ART121","ART120","ART111","ART179","EVN21",
				"EVN20","EVN41","EVN25","EVN24","EVN23","ART102","ART104","EVN103",
				"RAH101","EVN192","EVN191","RAH1","ART180","EVN194","EVN193","ART1","ART187"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("lastOrder", "DESC");
		params.put("lastGroupBy", "lastModified");
		params.put("groupBy", "lastModified");
		this.executeChangeOrder("admin", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[order1.length - i - 1], contents.get(i));
    	}
	}
	
	public void testPerformSearch_2() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		this.executeSearch("supervisorCoach", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART112", "ART111", "EVN41", "EVN25", "ART102", "ART104", "EVN103", "RAH101"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		params.put("lastOrder", "DESC");
		params.put("lastGroupBy", "lastModified");
		params.put("groupBy", "lastModified");
		this.executeChangeOrder("supervisorCoach", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[order1.length - i - 1], contents.get(i));
    	}
	}
	
	public void testPerformSearch_3() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lastOrder", "ASC");
		params.put("lastGroupBy", "created");
		params.put("text", "desc");
		params.put("state", Content.STATUS_DRAFT);
		this.executeSearch("admin", params);
		
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order = {"ART179", "ART187"};
		List<String> contents = action.getContents();
		assertEquals(order.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order[i], contents.get(i));
    	}
	}
	
	/**
	 * Test the newly added search criteria contentId, #1
	 */
	public void testPerformSearch_4() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lastOrder", "ASC");
		params.put("lastGroupBy", "created");
		params.put("state", Content.STATUS_DRAFT);
		params.put("contentIdToken", "RA");
		this.executeSearch("admin", params);
		
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		
		List<String> contents = action.getContents();
		String[] order = {"RAH1", "RAH101"};
		assertEquals(order.length, contents.size());
		for (int index=0; index < contents.size(); index++) {
    		assertEquals(order[index], contents.get(index));
    	}
	}
	
	/**
	 * Thest the newly added search criteria contentId, #2
	 */
	public void testPerformSearch_5() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lastOrder", "DESC");
		params.put("lastGroupBy", "created");
		params.put("state", Content.STATUS_READY);
		params.put("contentIdToken", "r");
		this.executeSearch("admin", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		List<String> contents = action.getContents();
		assertEquals(0, contents.size());
		
		WorkContentSearcherDAO searcherDao = (WorkContentSearcherDAO) this.getApplicationContext().getBean("jacmsWorkContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
		this.executeSearch("admin", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		String[] order = {"ART180"};
		assertEquals(order.length, contents.size());
		for (int index=0; index < contents.size(); index++) {
    		assertEquals(order[index], contents.get(index));
    	}
	}

	public void testPerformSearch_6() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentType", "ART");
		this.executeSearch("admin", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART112","ART122","ART121","ART120",
				"ART111","ART179","ART102","ART104","ART180","ART1","ART187"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
	}

	public void testPerformSearch_7() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentType", "ART");
		params.put("Data_dateStartFieldName", "12/02/2009");
		this.executeSearch("admin", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART121", "ART120", "ART179"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("categoryCode", "general_cat1");
		this.executeSearch("admin", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		assertEquals(1, contents.size());
		assertTrue(contents.contains("ART179"));
	}
	
	public void testPerformSearch_8() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentType", "ART");
		params.put("Data_dateStartFieldName", "12/02/2009");
		params.put("Data_dateEndFieldName", "02/06/2009");
		this.executeSearch("admin", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART121", "ART120"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
	}
	
	public void testPerformSearch_9() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentType", "EVN");
		this.executeSearch("editorCoach", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"EVN41", "EVN25", "EVN103"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("DataInizio_dateStartFieldName", "06/09/2007");
		params.put("DataInizio_dateEndFieldName", "02/05/2008");
		this.executeSearch("editorCoach", params);
		action = (IContentFinderAction) this.getAction();
		String[] order2 = {"EVN41", "EVN25"};
		contents = action.getContents();
		assertEquals(order2.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order2[i], contents.get(i));
    	}
		
		params.put("Titolo_textFieldName", "ci");
		this.executeSearch("editorCoach", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		assertEquals(1, contents.size());
		assertTrue(contents.contains("EVN41"));
	}
	
	public void testPerformSearch_10() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("categoryCode", "home");
		params.put("contentType", "EVN");
		this.executeSearch("admin", params);
		String[] order1 = {"EVN21", "EVN20", "EVN41", "EVN25", "EVN24", 
				"EVN23", "EVN103", "EVN192", "EVN191", "EVN194", "EVN193"};
		
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("categoryCode", "general");
		this.executeSearch("admin", params);
		action = (IContentFinderAction) this.getAction();
		String[] order2 = {"EVN192", "EVN193"};
		contents = action.getContents();
		assertEquals(order2.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order2[i], contents.get(i));
    	}
	}

	public void testPerformSearch_11() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ownerGroupName", "coach");
		this.executeSearch("admin", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART112", "ART111", "EVN41", "EVN25", "ART104", "EVN103"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("ownerGroupName", "customers");
		this.executeSearch("admin", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		String[] order2 = {"ART102", "RAH101"};
		assertEquals(order2.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order2[i], contents.get(i));
    	}
		
		params.put("ownerGroupName", "administrators");
		this.executeSearch("admin", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		String[] order3 = {"ART122", "ART121", "ART120"};
		assertEquals(order3.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order3[i], contents.get(i));
    	}
	}

	public void testPerformSearch_12() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ownerGroupName", "coach");
		this.executeSearch("editorCoach", params);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		String[] order1 = {"ART112", "ART111", "EVN41", "EVN25", "ART104", "EVN103"};
		List<String> contents = action.getContents();
		assertEquals(order1.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order1[i], contents.get(i));
    	}
		
		params.put("ownerGroupName", "customers");
		this.executeSearch("editorCoach", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		String[] order2 = {"ART102", "RAH101"};
		assertEquals(order2.length, contents.size());
		for (int i=0; i<contents.size(); i++) {
    		assertEquals(order2[i], contents.get(i));
    	}
		
		params.put("ownerGroupName", Group.ADMINS_GROUP_NAME);//Invalid group for coach
		this.executeSearch("editorCoach", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		assertEquals(0, contents.size());
		
		params.put("ownerGroupName", Group.FREE_GROUP_NAME);//Invalid group for coach
		this.executeSearch("editorCoach", params);
		action = (IContentFinderAction) this.getAction();
		contents = action.getContents();
		assertEquals(0, contents.size());
	}
	
	private void executeSearch(String currentUserName, Map<String, String> params) throws Throwable {
		this.initAction("/do/jacms/Content", "search");
		this.setUserOnSession(currentUserName);
		this.addParameters(params);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	private void executeChangeOrder(String currentUserName, Map<String, String> params) throws Throwable {
		this.initAction("/do/jacms/Content", "changeOrder");
		this.setUserOnSession(currentUserName);
		this.addParameters(params);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testSearchWithWrongStatus() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lastOrder", "ASC");
		params.put("lastGroupBy", "created");
		params.put("text", "desc");
		params.put("state", "wrongStatus");
		this.executeSearch("admin", params);
		
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		List<String> contents = action.getContents();
		assertEquals(0, contents.size());
	}
	
}