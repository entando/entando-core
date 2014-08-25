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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;

/**
 * @author M. Morini - E.Santoboni
 */
public class TestContentManager extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		this.init();
    }
	
	public void testSearchContents_1_1() throws Throwable {
		List<String> contentIds = this._contentManager.searchId(null);
		assertNotNull(contentIds);
    	assertEquals(24, contentIds.size());
    	
    	EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
    	EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Cont", true);
    	EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters1);
		assertNotNull(contentIds);
    	String[] expected1 = {"RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "ART120", "ART121", "ART122"};
    	assertEquals(expected1.length, contentIds.size());
    	this.verifyOrder(contentIds, expected1);
		
    	EntitySearchFilter lastEditorFilter = new EntitySearchFilter(IContentManager.CONTENT_LAST_EDITOR_FILTER_KEY, false, "admin", true);
    	EntitySearchFilter[] filters2 = {creationOrder, descrFilter, lastEditorFilter};
    	contentIds = this._contentManager.searchId(filters2);
		assertNotNull(contentIds);
    	assertEquals(expected1.length, contentIds.size());
    	this.verifyOrder(contentIds, expected1);
		
		descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Cont", true, FieldSearchFilter.LikeOptionType.RIGHT);
    	EntitySearchFilter[] filters3 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters3);
		System.out.println(contentIds);
		assertNotNull(contentIds);
    	String[] expected3 = expected1;
    	assertEquals(expected3.length, contentIds.size());
    	this.verifyOrder(contentIds, expected3);
		
		descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Cont", true, FieldSearchFilter.LikeOptionType.LEFT);
    	EntitySearchFilter[] filters4 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters4);
		assertNotNull(contentIds);
    	assertTrue(contentIds.isEmpty());
		
		descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "1", true, FieldSearchFilter.LikeOptionType.LEFT);
    	EntitySearchFilter[] filters5 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters5);
		assertNotNull(contentIds);
		String[] expected5 = {"EVN191", "ART120"};
    	assertEquals(expected5.length, contentIds.size());
    	this.verifyOrder(contentIds, expected5);
		
	}
    
	public void testSearchContents_1_2() throws Throwable {
    	EntitySearchFilter versionFilter = new EntitySearchFilter(IContentManager.CONTENT_CURRENT_VERSION_FILTER_KEY, false, "0.", true);
    	EntitySearchFilter[] filters3 = {versionFilter};
    	List<String> contentIds = this._contentManager.searchId(filters3);
		assertNotNull(contentIds);
		String[] expected2 = {"ART179"};
    	assertEquals(expected2.length, contentIds.size());
    	this.verifyOrder(contentIds, expected2);
    	
    	versionFilter = new EntitySearchFilter(IContentManager.CONTENT_CURRENT_VERSION_FILTER_KEY, false, ".0", true);
    	EntitySearchFilter[] filters4 = {versionFilter};
    	contentIds = this._contentManager.searchId(filters4);
		assertNotNull(contentIds);
    	assertEquals(21, contentIds.size());
	}
	
	/*
	 * ATTENTION: invalid test on mysql db because the standard search with 'LIKE' clause is case insensitive
	 */
	public void testSearchContents_1_3() throws Throwable {
		EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter descrFilter_b = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "cont", true);
    	EntitySearchFilter[] filters1_b = {creationOrder, descrFilter_b};
    	List<String> contentIds = this._contentManager.searchId(filters1_b);
		assertNotNull(contentIds);
    	assertEquals(0, contentIds.size());
    	
    	EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Cont", true);
    	EntitySearchFilter lastEditorFilter_b = new EntitySearchFilter(IContentManager.CONTENT_LAST_EDITOR_FILTER_KEY, false, "AdMin", true);
    	EntitySearchFilter[] filters2_b = {creationOrder, descrFilter, lastEditorFilter_b};
    	contentIds = this._contentManager.searchId(filters2_b);
		assertNotNull(contentIds);
    	assertEquals(0, contentIds.size());
	}
	
	public void testSearchContents_1_4() throws Throwable {
		//forcing case insensitive search
    	WorkContentSearcherDAO searcherDao = (WorkContentSearcherDAO) this.getApplicationContext().getBean("jacmsWorkContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
    	
		List<String> contentIds = this._contentManager.searchId(null);
		assertNotNull(contentIds);
    	assertEquals(24, contentIds.size());
    	
    	EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
    	EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "CoNt", true);
    	EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters1);
		assertNotNull(contentIds);
    	String[] expected1 = {"RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "ART120", "ART121", "ART122"};
    	assertEquals(expected1.length, contentIds.size());
    	this.verifyOrder(contentIds, expected1);
    	
    	EntitySearchFilter lastEditorFilter = new EntitySearchFilter(IContentManager.CONTENT_LAST_EDITOR_FILTER_KEY, false, "AdMin", true);
    	EntitySearchFilter[] filters2 = {creationOrder, descrFilter, lastEditorFilter};
    	contentIds = this._contentManager.searchId(filters2);
		assertNotNull(contentIds);
    	assertEquals(expected1.length, contentIds.size());
    	this.verifyOrder(contentIds, expected1);
	}
	
	public void testSearchContents_1_5() throws Throwable {
		//forcing case insensitive search
    	WorkContentSearcherDAO searcherDao = (WorkContentSearcherDAO) this.getApplicationContext().getBean("jacmsWorkContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
    	
    	EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
    	EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "co", true, FieldSearchFilter.LikeOptionType.COMPLETE);
    	EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
    	List<String> contentIds = this._contentManager.searchId(filters1);
		assertNotNull(contentIds);
		String[] expected1 = {"ART1", "RAH1", "ART187", "RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "EVN23", "ART120", "ART121", "ART122"};
    	assertEquals(expected1.length, contentIds.size());
    	this.verifyOrder(contentIds, expected1);
    	
		descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "co", true, FieldSearchFilter.LikeOptionType.RIGHT);
    	EntitySearchFilter[] filters2 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters2);
		assertNotNull(contentIds);
    	String[] expected2 = {"RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "EVN23", "ART120", "ART121", "ART122"};
    	assertEquals(expected2.length, contentIds.size());
    	this.verifyOrder(contentIds, expected2);
		
		EntitySearchFilter idFilter = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, "1", true, FieldSearchFilter.LikeOptionType.LEFT);
    	EntitySearchFilter[] filters3 = {creationOrder, descrFilter, idFilter};
    	contentIds = this._contentManager.searchId(filters3);
		assertNotNull(contentIds);
    	String[] expected3 = {"RAH101", "ART111", "ART121"};
    	assertEquals(expected3.length, contentIds.size());
    	this.verifyOrder(contentIds, expected3);
		
		descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "co", true, FieldSearchFilter.LikeOptionType.LEFT);
    	EntitySearchFilter[] filters4 = {creationOrder, descrFilter};
    	contentIds = this._contentManager.searchId(filters4);
    	assertNotNull(contentIds);
    	String[] expected4 = {};
    	assertEquals(expected4.length, contentIds.size());
	}
	
	public void testSearchContents_1_6() throws Throwable {
		//forcing case sensitive search
    	WorkContentSearcherDAO searcherDao = (WorkContentSearcherDAO) this.getApplicationContext().getBean("jacmsWorkContentSearcherDAO");
    	searcherDao.setForceCaseSensitiveLikeSearch(true);
    	
    	EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
    	EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "CoNt", true);
    	EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
    	List<String> contentIds = this._contentManager.searchId(filters1);
    	assertEquals(0, contentIds.size());
    	
    	EntitySearchFilter lastEditorFilter = new EntitySearchFilter(IContentManager.CONTENT_LAST_EDITOR_FILTER_KEY, false, "AdMin", true);
    	EntitySearchFilter[] filters2 = {creationOrder, descrFilter, lastEditorFilter};
    	contentIds = this._contentManager.searchId(filters2);
		assertNotNull(contentIds);
    	assertEquals(0, contentIds.size());
	}
	
	public void testSearchContents_2() throws Throwable {
		EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter groupFilter = new EntitySearchFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY, false, "coach", false);
		EntitySearchFilter[] filters = {creationOrder, groupFilter};
		List<String> contentIds = this._contentManager.searchId(filters);
		assertNotNull(contentIds);
		String[] expected = {"EVN103", "ART104", "ART111", "ART112", "EVN25", "EVN41"};
    	assertEquals(expected.length, contentIds.size());
    	this.verifyOrder(contentIds, expected);
	}
	
	public void testSearchContents_3() throws Throwable {
		EntitySearchFilter modifyOrder = new EntitySearchFilter(IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, false);
		modifyOrder.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter onlineFilter = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false, "encoding=", true);
		EntitySearchFilter[] filters = {modifyOrder, onlineFilter};
		List<String> contentIds = this._contentManager.searchId(filters);
		assertNotNull(contentIds);
		String[] expected = {"ART187", "ART1","EVN193","EVN194","ART180","RAH1",
				"EVN191","EVN192","RAH101","EVN103","ART104","ART102","EVN23",
				"EVN24","EVN25","EVN41","EVN20","EVN21","ART111","ART120","ART121","ART122","ART112"};
    	assertEquals(expected.length, contentIds.size());
    	this.verifyOrder(contentIds, expected);
	}
	
	public void testSearchWorkContents() throws Throwable {
    	List<String> contents = this._contentManager.loadWorkContentsId(null, null);
    	assertNotNull(contents);
    	assertEquals(0, contents.size());
    	
    	EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);
    	EntitySearchFilter typeFilter = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
    	EntitySearchFilter[] filters1 = {creationOrder, typeFilter};
    	contents = this._contentManager.loadWorkContentsId(filters1, null);
    	assertEquals(0, contents.size());
    	
    	List<String> groupCodes = new ArrayList<String>();
    	groupCodes.add("customers");
    	contents = this._contentManager.loadWorkContentsId(filters1, groupCodes);
    	String[] order1 = {"ART102"};
    	assertEquals(order1.length, contents.size());
    	this.verifyOrder(contents, order1);
    	
    	groupCodes.add(Group.FREE_GROUP_NAME);
    	EntitySearchFilter statusFilter = new EntitySearchFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, false, Content.STATUS_DRAFT, false);
    	EntitySearchFilter[] filters2 = {creationOrder, typeFilter, statusFilter};
    	contents = this._contentManager.loadWorkContentsId(filters2, groupCodes);
    	String[] order2 = {"ART102", "ART187", "ART179", "ART1"};
    	assertEquals(order2.length, contents.size());
    	this.verifyOrder(contents, order2);
    	
    	EntitySearchFilter onlineFilter = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
    	EntitySearchFilter[] filters3 = {creationOrder, typeFilter, onlineFilter};
    	contents = this._contentManager.loadWorkContentsId(filters3, groupCodes);
    	String[] order3 = {"ART102", "ART187", "ART180", "ART1"};
    	assertEquals(order3.length, contents.size());
    	this.verifyOrder(contents, order3);
    	
    	onlineFilter.setNullOption(true);
    	contents = this._contentManager.loadWorkContentsId(filters3, groupCodes);
    	String[] order4 = {"ART179"};
    	assertEquals(order4.length, contents.size());
    	this.verifyOrder(contents, order4);
    	
    	onlineFilter.setNullOption(false);
    	EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "scr", true);
    	EntitySearchFilter[] filters5 = {creationOrder, typeFilter, onlineFilter, descrFilter};
    	contents = this._contentManager.loadWorkContentsId(filters5, groupCodes);
    	String[] order5 = {"ART187", "ART180"};
    	assertEquals(order5.length, contents.size());
    	this.verifyOrder(contents, order5);
    	
    	groupCodes.clear();
    	groupCodes.add(Group.ADMINS_GROUP_NAME);
    	contents = this._contentManager.loadWorkContentsId(null, groupCodes);
    	assertNotNull(contents);
    	assertEquals(24, contents.size());
    }
	
	/*
	 * ATTENTION: invalid test on mysql db because the standard search with 'LIKE' clause is case insensitive
	 */
	public void testSearchWorkContents_2_a() throws Throwable {
		List<String> groupCodes = new ArrayList<String>();
    	groupCodes.add("customers");
    	groupCodes.add(Group.FREE_GROUP_NAME);
		EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);
    	
    	EntitySearchFilter descrFilter_1 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "eScR", true);
    	EntitySearchFilter[] filters_1 = {creationOrder, descrFilter_1};
    	List<String> contents = this._contentManager.loadWorkContentsId(filters_1, groupCodes);
    	assertEquals(0, contents.size());
    	
    	EntitySearchFilter descrFilter_2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "escr", true);
    	EntitySearchFilter[] filters_2 = {creationOrder, descrFilter_2};
    	contents = this._contentManager.loadWorkContentsId(filters_2, groupCodes);
    	String[] order = {"ART187", "ART180", "ART179"};
    	assertEquals(order.length, contents.size());
    	this.verifyOrder(contents, order);
    }

	public void testSearchWorkContents_2_b() throws Throwable {
		//forcing case insensitive search
    	WorkContentSearcherDAO searcherDao = (WorkContentSearcherDAO) this.getApplicationContext().getBean("jacmsWorkContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
    	
		List<String> groupCodes = new ArrayList<String>();
    	groupCodes.add("customers");
    	groupCodes.add(Group.FREE_GROUP_NAME);
		EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);
    	
    	EntitySearchFilter descrFilter_1 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "eScR", true);
    	EntitySearchFilter[] filters_1 = {creationOrder, descrFilter_1};
    	List<String> contents = this._contentManager.loadWorkContentsId(filters_1, groupCodes);
    	String[] order = {"ART187", "ART180", "ART179"};
    	assertEquals(order.length, contents.size());
    	this.verifyOrder(contents, order);
    }
	
	public void testSearchWorkContents_3() throws Throwable {
		List<String> groupCodes = new ArrayList<String>();
		groupCodes.add(Group.ADMINS_GROUP_NAME);
		EntitySearchFilter creationOrder = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
		creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = { creationOrder };
		String[] categories_1 = { "general_cat2" };
		List<String> contents = this._contentManager.loadWorkContentsId(categories_1, filters, groupCodes);
		String[] order_a = {"ART120", "ART112", "ART111", "EVN193", "ART179"};
		assertEquals(order_a.length, contents.size());
		this.verifyOrder(contents, order_a);
		
		String[] categories_2 = { "general_cat1", "general_cat2" };
		contents = this._contentManager.loadWorkContentsId(categories_2, filters, groupCodes);
		String[] order_b = {"ART111", "ART179"};
		assertEquals(order_b.length, contents.size());
		assertEquals(order_b[0], contents.get(0));
		
		Content newContent = this._contentManager.loadContent("EVN193", false);
		newContent.setId(null);
		try {
			this._contentManager.saveContent(newContent);
			contents = this._contentManager.loadWorkContentsId(categories_1, filters, groupCodes);
			String[] order_c = {newContent.getId(), "ART120", "ART112", "ART111", "EVN193", "ART179"};
			assertEquals(order_c.length, contents.size());
			this.verifyOrder(contents, order_c);
			
			ICategoryManager categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
			newContent.addCategory(categoryManager.getCategory("general_cat1"));
			this._contentManager.saveContent(newContent);
			contents = this._contentManager.loadWorkContentsId(categories_2, filters, groupCodes);
			String[] order_d = {newContent.getId(), "ART111", "ART179"};
			assertEquals(order_d.length, contents.size());
			this.verifyOrder(contents, order_d);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._contentManager.deleteContent(newContent);
			assertNull(this._contentManager.loadContent(newContent.getId(), false));
		}
	}
	
    private void verifyOrder(List<String> contents, String[] order) {
    	for (int i=0; i<contents.size(); i++) {
    		assertEquals(order[i], contents.get(i));
    	}
	}
    
    public void testLoadContent() throws Throwable {
    	Content content = this._contentManager.loadContent("ART111", false);
    	assertEquals(Content.STATUS_PUBLIC, content.getStatus());
    	assertEquals("coach", content.getMainGroup());
    	assertEquals(2, content.getGroups().size());
    	assertTrue(content.getGroups().contains("customers"));
    	assertTrue(content.getGroups().contains("helpdesk"));
    	
    	Map<String, AttributeInterface> attributes = content.getAttributeMap();
    	assertEquals(7, attributes.size());
    	
    	TextAttribute title = (TextAttribute) attributes.get("Titolo");
    	assertEquals("Titolo Contenuto 3 Coach", title.getTextForLang("it"));
    	assertNull(title.getTextForLang("en"));
    	
    	MonoListAttribute authors = (MonoListAttribute) attributes.get("Autori");
    	assertEquals(4, authors.getAttributes().size());
    	
    	LinkAttribute link = (LinkAttribute) attributes.get("VediAnche");
    	assertNull(link.getSymbolicLink());
    	
    	HypertextAttribute hypertext = (HypertextAttribute) attributes.get("CorpoTesto");
		assertEquals("<p>Corpo Testo Contenuto 3 Coach</p>", hypertext.getTextForLang("it").trim());
    	assertNull(hypertext.getTextForLang("en"));
    	
    	ResourceAttributeInterface image = (ResourceAttributeInterface) attributes.get("Foto");
    	assertNull(image.getResource());
    	
    	DateAttribute date = (DateAttribute) attributes.get("Data");
    	assertEquals("13/12/2006", DateConverter.getFormattedDate(date.getDate(), "dd/MM/yyyy"));
    }
    
    public void testGetContentTypes() {
    	Map<String, SmallContentType> smallContentTypes = _contentManager.getSmallContentTypesMap();
    	assertEquals(4, smallContentTypes.size());
    }
    
    public void testCreateContent() {
    	Content contentType = _contentManager.createContentType("ART");
        assertNotNull(contentType);
    }
    
    public void testCreateContentWithViewPage() {
    	Content content = _contentManager.createContentType("ART");
    	String viewPage = content.getViewPage();
    	assertEquals(viewPage, "contentview");
    }
    
    public void testCreateContentWithDefaultModel() {
    	Content content = _contentManager.createContentType("ART");
    	String defaultModel = content.getDefaultModel();
    	assertEquals(defaultModel,"1");
    }
    
    public void testGetXML() throws Throwable {
    	Content content = this._contentManager.createContentType("ART");
    	content.setId("ART1");
    	content.setTypeCode("Articolo");
    	content.setTypeDescr("Articolo");
    	content.setDescr("descrizione");
    	content.setStatus(Content.STATUS_DRAFT);
    	content.setMainGroup("free");
    	Category cat13 = new Category();
    	cat13.setCode("13");
    	content.addCategory(cat13);
    	Category cat19 = new Category();
    	cat19.setCode("19");
    	content.addCategory(cat19);
    	String xml = content.getXML();
    	assertNotNull(xml); 
    	assertTrue(xml.indexOf("<content id=\"ART1\" typecode=\"Articolo\" typedescr=\"Articolo\">")!= -1);
    	assertTrue(xml.indexOf("<descr>descrizione</descr>")!= -1);
    	assertTrue(xml.indexOf("<status>" + Content.STATUS_DRAFT +"</status>")!= -1); 
    	assertTrue(xml.indexOf("<category id=\"13\" />")!= -1);
    	assertTrue(xml.indexOf("<category id=\"19\" />")!= -1);
    }
    
    public void testLoadPublicContents() throws ApsSystemException {
		List<String> contents = _contentManager.loadPublicContentsId(null, null, null);
		assertEquals(14, contents.size());
    }
    
    public void testLoadPublicEvents_1() throws ApsSystemException {
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, null, null);
		String[] expectedFreeContentsId = {"EVN194", "EVN193", 
				"EVN24", "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
		assertEquals(expectedFreeContentsId.length, contents.size());
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertTrue(contents.contains(expectedFreeContentsId[i]));
		}
		assertFalse(contents.contains("EVN103"));
		
		List<String> groups = new ArrayList<String>();
		groups.add("coach");
		contents = _contentManager.loadPublicContentsId("EVN", null, null, groups);
		assertEquals(expectedFreeContentsId.length+2, contents.size());
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertTrue(contents.contains(expectedFreeContentsId[i]));
		}
		assertTrue(contents.contains("EVN103"));
		assertTrue(contents.contains("EVN41"));
    }
    
    public void testLoadPublicEvents_2() throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add("coach");
		groups.add(Group.ADMINS_GROUP_NAME);
		Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
		Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Even", true);
		filter2.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter, filter2};
		
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		assertEquals(2, contents.size());
		assertEquals("EVN193", contents.get(0));
		assertEquals("EVN192", contents.get(1));
		
		filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false);
		filter2.setOrder(EntitySearchFilter.DESC_ORDER);
		
		EntitySearchFilter[] filters2 = {filter, filter2};
		contents = _contentManager.loadPublicContentsId("EVN", null, filters2, groups);
		
		String[] expectedOrderedContentsId = {"EVN25", "EVN21", "EVN20", "EVN41", "EVN193", 
				"EVN192", "EVN103", "EVN23", "EVN24"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
		
		contents = _contentManager.loadPublicContentsId("EVN", null, filters2, null);
		String[] expectedFreeOrderedContentsId = {"EVN25", "EVN21", "EVN20", "EVN193", 
				"EVN192", "EVN23", "EVN24"};
		assertEquals(expectedFreeOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedFreeOrderedContentsId.length; i++) {
			assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
		}
    }
    
    public void testLoadPublicEvents_2_1() throws ApsSystemException {
    	//forcing case insensitive search
    	PublicContentSearcherDAO searcherDao = (PublicContentSearcherDAO) this.getApplicationContext().getBean("jacmsPublicContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
    	
    	List<String> groups = new ArrayList<String>();
		groups.add("coach");
		groups.add(Group.ADMINS_GROUP_NAME);
		Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
		Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "even", true);
		filter2.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter, filter2};
		
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		assertEquals(2, contents.size());
		assertEquals("EVN193", contents.get(0));
		assertEquals("EVN192", contents.get(1));
    }
    
    public void testLoadPublicEvents_2_2() throws ApsSystemException {
    	//forcing case sensitive search
    	PublicContentSearcherDAO searcherDao = (PublicContentSearcherDAO) this.getApplicationContext().getBean("jacmsPublicContentSearcherDAO");
    	searcherDao.setForceCaseSensitiveLikeSearch(true);
    	
    	List<String> groups = new ArrayList<String>();
		groups.add("coach");
		groups.add(Group.ADMINS_GROUP_NAME);
		Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
		Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
		EntitySearchFilter filter_x1 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "even", true);
		filter_x1.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters_1 = {filter, filter_x1};
		
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters_1, groups);
		assertEquals(0, contents.size());
		
		EntitySearchFilter filter_x2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Even", true);
		filter_x2.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters_2 = {filter, filter_x2};
		
		contents = _contentManager.loadPublicContentsId("EVN", null, filters_2, groups);
		assertEquals(2, contents.size());
		assertEquals("EVN193", contents.get(0));
		assertEquals("EVN192", contents.get(1));
    }
    
    public void testLoadPublicEvents_3() throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		Date value = DateConverter.parseDate("1999-04-14", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, value, false);
		EntitySearchFilter[] filters = {filter};
		
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		assertEquals(1, contents.size());
		assertEquals("EVN192", contents.get(0));
    }
	
	public void testLoadPublicEvents_4() throws ApsSystemException {
		this.testLoadPublicEvents_4(true);
		this.testLoadPublicEvents_4(false);
	}
    
    protected void testLoadPublicEvents_4(boolean useRoleFilter) throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		EntitySearchFilter filter1 = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, "Ce", "TF") 
				: new EntitySearchFilter("Titolo", true, "Ce", "TF");
		filter1.setLangCode("it");
		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters1 = {filter1};
		List<String> contents = this._contentManager.loadPublicContentsId("EVN", null, filters1, groups);
		String[] expectedOrderedContentsId = {"EVN25", "EVN41", "EVN20", "EVN21", "EVN23"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
		filter1 = new EntitySearchFilter("Titolo", true, null, "TF");
		filter1.setLangCode("it");
		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters2 = {filter1};
		contents = this._contentManager.loadPublicContentsId("EVN", null, filters2, groups);
		String[] expectedOrderedContentsId2 = {"EVN25", "EVN41", "EVN20", "EVN21", "EVN23", "EVN24"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadPublicEvents_5() throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		List<Date> allowedDates = new ArrayList<Date>();
		allowedDates.add(DateConverter.parseDate("1999-04-14", "yyyy-MM-dd"));//EVN192
		allowedDates.add(DateConverter.parseDate("2008-02-13", "yyyy-MM-dd"));//EVN23
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, allowedDates, false);
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN23", "EVN192"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadPublicEvents_6() throws ApsSystemException {
		this.testLoadPublicEvents_6(true);
		this.testLoadPublicEvents_6(false);
	}
	
	protected void testLoadPublicEvents_6(boolean useRoleFilter) throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		List<String> allowedDescription = new ArrayList<String>();
		allowedDescription.add("Mostra");//EVN21, EVN20
		allowedDescription.add("Collezione");//EVN23
		EntitySearchFilter filter = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, allowedDescription, true) 
				: new EntitySearchFilter("Titolo", true, allowedDescription, true);
		filter.setLangCode("it");
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN20", "EVN21", "EVN23"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
    
	public void testLoadPublicEvents_7() throws ApsSystemException {
		this.testLoadPublicEvents_7(true);
		this.testLoadPublicEvents_7(false);
	}
	
	protected void testLoadPublicEvents_7(boolean useRoleFilter) throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		List<String> allowedDescription = new ArrayList<String>();
		allowedDescription.add("Mostra Zootecnica");//EVN20
		allowedDescription.add("Title B - Event 2");//EVN192
		EntitySearchFilter filter1 = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, allowedDescription, false) 
				: new EntitySearchFilter("Titolo", true, allowedDescription, false);
		filter1.setLangCode("en");
		EntitySearchFilter filter2 = new EntitySearchFilter("DataInizio", true);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter1, filter2};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN192", "EVN20"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadPublicEvents_8() throws ApsSystemException {
		this.testLoadPublicEvents_8(true);
		this.testLoadPublicEvents_8(false);
	}
	
	protected void testLoadPublicEvents_8(boolean useRoleFilter) throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		List<String> allowedDescription = new ArrayList<String>();
		allowedDescription.add("Castello");//EVN24
		allowedDescription.add("dei bambini");//EVN24
		EntitySearchFilter filter = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, allowedDescription, true) 
				: new EntitySearchFilter("Titolo", true, allowedDescription, true);
		filter.setLangCode("it");
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN24"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadPublicEvents_9_a() throws ApsSystemException {
		this.testLoadPublicEvents_9_a(true);
		this.testLoadPublicEvents_9_a(false);
	}
	
	/*
	 * ATTENTION: invalid test on mysql db because the standard search with 'LIKE' clause is case insensitive
	 */
	protected void testLoadPublicEvents_9_a(boolean useRoleFilter) throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		EntitySearchFilter filter = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, "le", true) 
				: new EntitySearchFilter("Titolo", true, "le", true);
		filter.setLangCode("it");
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN21", "EVN23"};//not EVN25
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadPublicEvents_9_b() throws ApsSystemException {
		this.testLoadPublicEvents_9_b(true);
		this.testLoadPublicEvents_9_b(false);
	}
	
	protected void testLoadPublicEvents_9_b(boolean useRoleFilter) throws ApsSystemException {
		//forcing case insensitive search
    	PublicContentSearcherDAO searcherDao = (PublicContentSearcherDAO) this.getApplicationContext().getBean("jacmsPublicContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		EntitySearchFilter filter = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, "le", true) 
				: new EntitySearchFilter("Titolo", true, "le", true);
		filter.setLangCode("it");
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = this._contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN25", "EVN21", "EVN23"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadPublicEvents_9_c() throws ApsSystemException {
		this.testLoadPublicEvents_9_c(true);
		this.testLoadPublicEvents_9_c(false);
	}
	
	protected void testLoadPublicEvents_9_c(boolean useRoleFilter) throws ApsSystemException {
		//forcing case sensitive search
    	PublicContentSearcherDAO searcherDao = (PublicContentSearcherDAO) this.getApplicationContext().getBean("jacmsPublicContentSearcherDAO");
    	searcherDao.setForceCaseSensitiveLikeSearch(true);
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		EntitySearchFilter filter = (useRoleFilter) 
				? EntitySearchFilter.createRoleFilter(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE, "LE", true) 
				: new EntitySearchFilter("Titolo", true, "LE", true);
		filter.setLangCode("it");
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = this._contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId2 = {"EVN25"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
	
	/*
	 * ATTENTION: invalid test on mysql db because the standard search with 'LIKE' clause is case insensitive
	 */
	public void testLoadWorkEvents_1_a() throws ApsSystemException {
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		List<String> allowedDescription = new ArrayList<String>();
		allowedDescription.add("descrizione");//"ART179" "ART180" "ART187"
		allowedDescription.add("on line");//"ART179"
		allowedDescription.add("customers");//"ART102" "RAH101" ...but not included because the standard search is case sensitive
		EntitySearchFilter filter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, allowedDescription, true);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter, filter2};
		List<String> contents = _contentManager.loadWorkContentsId(filters, groups);
		String[] expectedOrderedContentsId2 = {"ART179", "ART180", "ART187"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
    
	public void testLoadWorkEvents_1_b() throws ApsSystemException {
		//forcing case insensitive search
    	WorkContentSearcherDAO searcherDao = (WorkContentSearcherDAO) this.getApplicationContext().getBean("jacmsWorkContentSearcherDAO");
    	searcherDao.setForceCaseInsensitiveLikeSearch(true);
		
    	List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		List<String> allowedDescription = new ArrayList<String>();
		allowedDescription.add("descrizione");//"ART179" "ART180" "ART187"
		allowedDescription.add("on line");//"ART179"
		allowedDescription.add("customers");//"ART102" "RAH101"
		EntitySearchFilter filter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, allowedDescription, true);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter, filter2};
		List<String> contents = _contentManager.loadWorkContentsId(filters, groups);
		String[] expectedOrderedContentsId2 = {"ART102", "ART179", "ART180", "ART187", "RAH101"};
		assertEquals(expectedOrderedContentsId2.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId2.length; i++) {
			assertEquals(expectedOrderedContentsId2[i], contents.get(i));
		}
    }
    
    public void testLoadOrderedPublicEvents_1() throws ApsSystemException {
    	EntitySearchFilter filterForDescr = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false);
    	filterForDescr.setOrder(EntitySearchFilter.ASC_ORDER);
    	EntitySearchFilter[] filters = {filterForDescr};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		
		String[] expectedFreeContentsId = {"EVN24", "EVN23", "EVN191", 
				"EVN192", "EVN193", "EVN194", "EVN20", "EVN21", "EVN25"};
		assertEquals(expectedFreeContentsId.length, contents.size());
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertEquals(expectedFreeContentsId[i], contents.get(i));
		}
		
		filterForDescr.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		
		assertEquals(expectedFreeContentsId.length, contents.size());
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertEquals(expectedFreeContentsId[expectedFreeContentsId.length - i - 1], contents.get(i));
		}
    }
    
    public void testLoadOrderedPublicEvents_2() throws ApsSystemException {
    	EntitySearchFilter filterForCreation = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	filterForCreation.setOrder(EntitySearchFilter.ASC_ORDER);
    	EntitySearchFilter[] filters = {filterForCreation};
    	
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		String[] expectedFreeOrderedContentsId = {"EVN191", "EVN192", "EVN193", "EVN194", 
				"EVN20", "EVN23", "EVN24", "EVN25", "EVN21"};
		assertEquals(expectedFreeOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedFreeOrderedContentsId.length; i++) {
			assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
		}
		
		filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		assertEquals(expectedFreeOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedFreeOrderedContentsId.length; i++) {
			assertEquals(expectedFreeOrderedContentsId[expectedFreeOrderedContentsId.length - i - 1], contents.get(i));
		}
    }
    
    public void testLoadOrderedPublicEvents_3() throws ApsSystemException {
    	EntitySearchFilter filterForCreation = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
    	filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
    	EntitySearchFilter filterForDate = new EntitySearchFilter("DataInizio", true);
    	filterForDate.setOrder(EntitySearchFilter.DESC_ORDER);
    	EntitySearchFilter[] filters = {filterForCreation, filterForDate};
    	
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		String[] expectedFreeOrderedContentsId = {"EVN21", "EVN25", "EVN24", "EVN23", 
				"EVN20", "EVN194", "EVN193", "EVN192", "EVN191"};
		assertEquals(expectedFreeOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedFreeOrderedContentsId.length; i++) {
			assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
		}
    	
		EntitySearchFilter[] filters2 = {filterForDate, filterForCreation};
		
		List<String> contents2 = _contentManager.loadPublicContentsId("EVN", null, filters2, null);
		String[] expectedFreeOrderedContentsId2 = {"EVN194", "EVN193", "EVN24", 
				"EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
		assertEquals(expectedFreeOrderedContentsId2.length, contents2.size());
		for (int i=0; i<expectedFreeOrderedContentsId2.length; i++) {
			assertEquals(expectedFreeOrderedContentsId2[i], contents2.get(i));
		}
    }
    
    public void testLoadOrderedPublicEvents_4() throws Throwable {
    	Content masterContent = this._contentManager.loadContent("EVN193", true);
    	masterContent.setId(null);
    	DateAttribute dateAttribute = (DateAttribute) masterContent.getAttribute("DataInizio");
    	dateAttribute.setDate(DateConverter.parseDate("17/06/2019", "dd/MM/yyyy"));
    	try {
    		this._contentManager.saveContent(masterContent);
    		this._contentManager.insertOnLineContent(masterContent);
			this.waitNotifyingThread();
			
			EntitySearchFilter filterForDate = new EntitySearchFilter("DataInizio", true);
			filterForDate.setOrder(EntitySearchFilter.DESC_ORDER);
			EntitySearchFilter[] filters = {filterForDate};
			
			List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
			String[] expectedFreeOrderedContentsId = {"EVN194", masterContent.getId(), "EVN193", "EVN24", 
					"EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
			assertEquals(expectedFreeOrderedContentsId.length, contents.size());
			for (int i=0; i<expectedFreeOrderedContentsId.length; i++) {
				assertEquals(expectedFreeOrderedContentsId[i], contents.get(i));
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != masterContent.getId() && !"EVN193".equals(masterContent.getId())) {
				this._contentManager.removeOnLineContent(masterContent);
				this._contentManager.deleteContent(masterContent);
			}
		}
    }
    
    public void testLoadFutureEvents_1() throws ApsSystemException {
		Date today =  DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		String[] expectedOrderedContentsId = {"EVN21", "EVN20", "EVN25", "EVN23", 
				"EVN24", "EVN193", "EVN194"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
    }
    
    public void testLoadFutureEvents_2() throws ApsSystemException {
		Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		String[] expectedOrderedContentsId = {"EVN194", "EVN193", "EVN24", 
				"EVN23", "EVN25", "EVN20", "EVN21"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
    }
    
    public void testLoadFutureEvents_3() throws ApsSystemException {
		Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
		List<String> groups = new ArrayList<String>();
		groups.add("coach");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId = {"EVN194", "EVN193", "EVN24", 
				"EVN23", "EVN41", "EVN25", "EVN20", "EVN21"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
    }
    
    public void testLoadPastEvents_1() throws ApsSystemException {
    	Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");
    	
    	EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};
		
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		String[] expectedOrderedContentsId = {"EVN191", "EVN192", 
				"EVN21", "EVN20", "EVN25", "EVN23"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
    }
    
    public void testLoadPastEvents_2() throws ApsSystemException {
    	Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");
		
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, null);
		String[] expectedOrderedContentsId = {"EVN23", "EVN25", 
				"EVN20", "EVN21", "EVN192", "EVN191"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
    }
    
    public void testLoadPastEvents_3() throws ApsSystemException {
		Date today = DateConverter.parseDate("2008-02-13", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};
		
		List<String> groups = new ArrayList<String>();
		groups.add("coach");
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, filters, groups);
		String[] expectedOrderedContentsId = {"EVN191", "EVN192", "EVN103", 
				"EVN21", "EVN20", "EVN25", "EVN41", "EVN23"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
    }
    
	public void testLoadWorkContentsForCategory_1() throws ApsSystemException {
		List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		String[] categories1 = {"general_cat1"};
		List<String> contents = this._contentManager.loadWorkContentsId(categories1, null, groups);
		assertEquals(5, contents.size());
		assertTrue(contents.contains("ART179"));
		assertTrue(contents.contains("ART180"));
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("EVN192"));
		
		String[] categories2 = {"general_cat1", "general_cat2"};
		
		contents = this._contentManager.loadWorkContentsId(categories2, null, groups);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART179"));
    }
	
	public void testLoadWorkContentsForCategory_2() throws ApsSystemException {
		List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		String[] categories1 = {"general_cat1"};
		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
		EntitySearchFilter[] filters = {filter1};
		List<String> contents = this._contentManager.loadWorkContentsId(categories1, filters, groups);
		assertEquals(4, contents.size());
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART180"));
		assertTrue(contents.contains("ART179"));
		
		String[] categories2 = {"general_cat2"};
		contents = this._contentManager.loadWorkContentsId(categories2, filters, groups);
		assertEquals(4, contents.size());
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART112"));
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART179"));
		
		String[] categories12 = {"general_cat1", "general_cat2"};
		contents = this._contentManager.loadWorkContentsId(categories12, false, filters, groups);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART179"));
		contents = this._contentManager.loadWorkContentsId(categories12, true, filters, groups);
		assertEquals(6, contents.size());
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART112"));
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART180"));
		assertTrue(contents.contains("ART179"));
		
		String[] categories3 = {"general_cat3"};
		contents = this._contentManager.loadWorkContentsId(categories3, filters, groups);
		assertEquals(3, contents.size());
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART121"));
		assertTrue(contents.contains("ART122"));
		
		String[] categories23 = {"general_cat2", "general_cat3"};
		contents = this._contentManager.loadWorkContentsId(categories23, false, filters, groups);
		assertEquals(1, contents.size());
		assertTrue(contents.contains("ART120"));
		contents = this._contentManager.loadWorkContentsId(categories23, true, filters, groups);
		assertEquals(6, contents.size());
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART112"));
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART179"));
		assertTrue(contents.contains("ART121"));
		assertTrue(contents.contains("ART122"));
		
		String[] categories123 = {"general_cat1", "general_cat2", "general_cat3"};
		contents = this._contentManager.loadWorkContentsId(categories123, false, filters, groups);
		assertEquals(0, contents.size());
		contents = this._contentManager.loadWorkContentsId(categories123, true, filters, groups);
		assertEquals(8, contents.size());
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART112"));
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART121"));
		assertTrue(contents.contains("ART122"));
		assertTrue(contents.contains("ART180"));
		assertTrue(contents.contains("ART179"));
    }
	
	public void testLoadPublicContentsForCategory() throws ApsSystemException {
    	String[] categories1 = {"evento"};
		List<String> contents = _contentManager.loadPublicContentsId(categories1, null, null);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("EVN192"));
		assertTrue(contents.contains("EVN193"));
		
		String[] categories2 = {"cat1"};
		contents = _contentManager.loadPublicContentsId(categories2, null, null);
		assertEquals(1, contents.size());
		assertTrue(contents.contains("ART180"));
    }
    
    public void testLoadPublicEventsForCategory_1() throws ApsSystemException {
    	String[] categories = {"evento"};
		List<String> contents = _contentManager.loadPublicContentsId("EVN", categories, null, null);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("EVN192"));
		assertTrue(contents.contains("EVN193"));
		
		Date today = DateConverter.parseDate("2005-02-13", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};
		contents = _contentManager.loadPublicContentsId("EVN", categories, filters, null);
		assertEquals(1, contents.size());
		assertTrue(contents.contains("EVN192"));
    }
	
	public void testLoadPublicEventsForCategory_2() throws ApsSystemException {
		List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		String[] categories1 = {"general_cat1"};
		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
		EntitySearchFilter[] filters = {filter1};
		List<String> contents = this._contentManager.loadPublicContentsId(categories1, filters, groups);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		
		String[] categories2 = {"general_cat2"};
		contents = this._contentManager.loadPublicContentsId(categories2, filters, groups);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART120"));
		
		String[] categories12 = {"general_cat1", "general_cat2"};
		contents = this._contentManager.loadPublicContentsId(categories12, false, filters, groups);
		assertEquals(1, contents.size());
		assertTrue(contents.contains("ART111"));
		contents = this._contentManager.loadPublicContentsId(categories12, true, filters, groups);
		assertEquals(3, contents.size());
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART120"));
		
		String[] categories3 = {"general_cat3"};
		contents = this._contentManager.loadPublicContentsId(categories3, filters, groups);
		assertEquals(2, contents.size());
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART122"));
		
		String[] categories23 = {"general_cat2", "general_cat3"};
		contents = this._contentManager.loadPublicContentsId(categories23, false, filters, groups);
		assertEquals(1, contents.size());
		assertTrue(contents.contains("ART120"));
		contents = this._contentManager.loadPublicContentsId(categories23, true, filters, groups);
		assertEquals(3, contents.size());
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART122"));
		
		String[] categories123 = {"general_cat1", "general_cat2", "general_cat3"};
		contents = this._contentManager.loadPublicContentsId(categories123, false, filters, groups);
		assertEquals(0, contents.size());
		contents = this._contentManager.loadPublicContentsId(categories123, true, filters, groups);
		assertEquals(4, contents.size());
		assertTrue(contents.contains("ART102"));
		assertTrue(contents.contains("ART111"));
		assertTrue(contents.contains("ART120"));
		assertTrue(contents.contains("ART122"));
    }
    
    public void testLoadEventsForGroup() throws ApsSystemException {
		List<String> contents = _contentManager.loadPublicContentsId("EVN", null, null, null);
		String[] expectedFreeContentsId = {"EVN191", "EVN192", "EVN193", "EVN194", 
				"EVN20", "EVN23", "EVN21", "EVN24", "EVN25"};
		assertEquals(expectedFreeContentsId.length, contents.size());
		
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertTrue(contents.contains(expectedFreeContentsId[i]));
		}
		
		Collection<String> allowedGroup = new HashSet<String>();
		allowedGroup.add(Group.FREE_GROUP_NAME);
		allowedGroup.add("customers");
		
		contents = _contentManager.loadPublicContentsId("EVN", null, null, allowedGroup);
		assertEquals(expectedFreeContentsId.length, contents.size());
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertTrue(contents.contains(expectedFreeContentsId[i]));
		}
		assertFalse(contents.contains("EVN103"));//evento coach
		
		allowedGroup.remove("customers");
		allowedGroup.remove(Group.FREE_GROUP_NAME);
		allowedGroup.add(Group.ADMINS_GROUP_NAME);
		
		contents = _contentManager.loadPublicContentsId("EVN", null, null, allowedGroup);
		assertEquals(11, contents.size());
		for (int i=0; i<expectedFreeContentsId.length; i++) {
			assertTrue(contents.contains(expectedFreeContentsId[i]));
		}
		assertTrue(contents.contains("EVN103"));
		assertTrue(contents.contains("EVN41"));
    }
    
    public void testLoadWorkContentsByAttribute_1() throws ApsSystemException {
		List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		
		EntitySearchFilter filter0 = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false);
		filter0.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", true);
		EntitySearchFilter filter2 = new EntitySearchFilter("Numero", true);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter0, filter1, filter2};
		String[] expectedContentsId = {"ART120", "ART121"};
		
		List<String> contents = this._contentManager.loadWorkContentsId(filters, groups);
		assertEquals(expectedContentsId.length, contents.size());
		for (int i=0; i<expectedContentsId.length; i++) {
			assertEquals(expectedContentsId[i], contents.get(i));
		}
		
		filter2.setNullOption(true);
		EntitySearchFilter[] filters2 = {filter0, filter1, filter2};
		String[] expectedContentsId2 = {"ART1", "ART102", "ART104", 
				"ART111", "ART112", "ART122", "ART179", "ART180", "ART187"};
		
		contents = this._contentManager.loadWorkContentsId(filters2, groups);
		assertEquals(expectedContentsId2.length, contents.size());
		for (int i=0; i<expectedContentsId2.length; i++) {
			assertEquals(expectedContentsId2[i], contents.get(i));
		}
    }
	
	public void testLoadWorkContentsByAttribute_2() throws ApsSystemException {
		List<String> groups = new ArrayList<String>();
		groups.add(Group.ADMINS_GROUP_NAME);
		EntitySearchFilter filter0 = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false);
		filter0.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN", true);
		EntitySearchFilter filter2 = new EntitySearchFilter("Titolo", true);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter0, filter1, filter2};
		String[] expectedContentsId = {"EVN103", "EVN191", "EVN192", 
				"EVN193", "EVN194", "EVN20", "EVN21", "EVN23", "EVN24", "EVN25", "EVN41"};
		
		List<String> contents = this._contentManager.loadWorkContentsId(filters, groups);
		assertEquals(expectedContentsId.length, contents.size());
		for (int i=0; i<expectedContentsId.length; i++) {
			assertEquals(expectedContentsId[i], contents.get(i));
		}
		
		filter2.setNullOption(true);
		EntitySearchFilter[] filters2 = {filter0, filter1, filter2};
		
		contents = this._contentManager.loadWorkContentsId(filters2, groups);
		assertEquals(0, contents.size());
		
		filter2.setLangCode("it");
		EntitySearchFilter[] filters3 = {filter0, filter1, filter2};
		contents = this._contentManager.loadWorkContentsId(filters3, groups);
		assertEquals(0, contents.size());
    }
	
    public void testLoadWorkContentsByAttribute_3() throws Throwable {
		List<String> groups = new ArrayList<String>();
		String[] masterContentIds = {"EVN193", "EVN191", "EVN192", "EVN194", "EVN23", "EVN24"};
		String[] newContentIds = null;
		try {
			newContentIds = this.addDraftContentsForTest(masterContentIds, false);
			for (int i = 0; i < newContentIds.length; i++) {
				Content content = this._contentManager.loadContent(newContentIds[i], false);
				TextAttribute titleAttribute = (TextAttribute) content.getAttribute("Titolo");
				if (i%2 == 1 && i<4) {
					titleAttribute.setText(null, "en");
				}
				titleAttribute.setText(null, "it");
				this._contentManager.saveContent(content);
			}
			groups.add(Group.ADMINS_GROUP_NAME);
			EntitySearchFilter filter0 = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
			filter0.setOrder(EntitySearchFilter.ASC_ORDER);
			EntitySearchFilter filter1 = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN", false);
			EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false);
			filter2.setOrder(EntitySearchFilter.ASC_ORDER);
			EntitySearchFilter[] filters = {filter0, filter1, filter2};
			String[] expectedContentsId = {"EVN191", "EVN192", "EVN193", "EVN194", 
					"EVN103", "EVN20", "EVN23", "EVN24", "EVN25", "EVN41", "EVN21", 
					newContentIds[0], newContentIds[1], newContentIds[2], newContentIds[3], newContentIds[4], newContentIds[5]};
			
			List<String> contents = this._contentManager.loadWorkContentsId(filters, groups);
			assertEquals(expectedContentsId.length, contents.size());
			for (int i=0; i<expectedContentsId.length; i++) {
				assertEquals(expectedContentsId[i], contents.get(i));
			}

			EntitySearchFilter filter3 = new EntitySearchFilter("Titolo", true);
			filter3.setLangCode("en");
			filter3.setOrder(EntitySearchFilter.ASC_ORDER);
			EntitySearchFilter[] filters1 = {filter0, filter1, filter2, filter3};
			String[] expectedContentsId1 = {"EVN191", "EVN192", "EVN193", "EVN194", 
					"EVN103", "EVN20", "EVN23", "EVN24", "EVN25", "EVN41", "EVN21", 
					newContentIds[0], newContentIds[2]};
			
			contents = this._contentManager.loadWorkContentsId(filters1, groups);
			assertEquals(expectedContentsId1.length, contents.size());
			for (int i=0; i<expectedContentsId1.length; i++) {
				assertEquals(expectedContentsId1[i], contents.get(i));
			}

			filter3.setNullOption(true);
			filter3.setLangCode("it");
			EntitySearchFilter[] filters2 = {filter0, filter1, filter2, filter3};
			String[] expectedContentsId2 = {newContentIds[0], newContentIds[1], 
					newContentIds[2], newContentIds[3], newContentIds[4], newContentIds[5]};
			
			contents = this._contentManager.loadWorkContentsId(filters2, groups);
			assertEquals(expectedContentsId2.length, contents.size());
			for (int i=0; i<expectedContentsId2.length; i++) {
				assertEquals(expectedContentsId2[i], contents.get(i));
			}
			
			filter3.setNullOption(true);
			filter3.setLangCode("en");
			EntitySearchFilter[] filters3 = {filter0, filter1, filter2, filter3};
			String[] expectedContentsId3 = {newContentIds[1], newContentIds[3], newContentIds[4], newContentIds[5]};
			
			contents = this._contentManager.loadWorkContentsId(filters3, groups);
			assertEquals(expectedContentsId3.length, contents.size());
			for (int i=0; i<expectedContentsId3.length; i++) {
				assertEquals(expectedContentsId3[i], contents.get(i));
			}
			
			filter2.setNullOption(true);
			EntitySearchFilter[] filters4 = {filter0, filter1, filter2};
			
			contents = this._contentManager.loadWorkContentsId(filters4, groups);
			assertEquals(0, contents.size());
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteContents(newContentIds);
		}
    }
    
	protected String[] addDraftContentsForTest(String[] masterContentIds, boolean publish) throws Throwable {
		String[] newContentIds = new String[masterContentIds.length];
		for (int i=0; i<masterContentIds.length; i++) {
			Content content = this._contentManager.loadContent(masterContentIds[i], false);
			content.setId(null);
			this._contentManager.saveContent(content);
			newContentIds[i] = content.getId();
			if (publish) {
				this._contentManager.insertOnLineContent(content);
			}
		}
		for (int i=0; i<newContentIds.length; i++) {
			Content content = this._contentManager.loadContent(newContentIds[i], false);
			assertNotNull(content);
		}
		return newContentIds;
	}
	
	private void deleteContents(String[] contentIds) throws Throwable {
		for (int i=0; i<contentIds.length; i++) {
			Content content = this._contentManager.loadContent(contentIds[i], false);
			if (null != content) {
				this._contentManager.removeOnLineContent(content);
				this._contentManager.deleteContent(content);
			}
		}
	}
	
    private void init() throws Exception {
    	try {
    		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IContentManager _contentManager = null;
    
}