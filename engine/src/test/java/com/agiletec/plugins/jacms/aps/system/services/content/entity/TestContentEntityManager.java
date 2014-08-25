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
package com.agiletec.plugins.jacms.aps.system.services.content.entity;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;

/**
 * @author E.Santoboni
 */
public class TestContentEntityManager extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testSearchRecords() throws Throwable {
		List<ApsEntityRecord> contents = this._contentManager.searchRecords(null);
		assertNotNull(contents);
		assertEquals(24, contents.size());

		EntitySearchFilter typeFilter = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
		EntitySearchFilter[] filters1 = {typeFilter};
		contents = this._contentManager.searchRecords(filters1);
		assertEquals(11, contents.size());

		EntitySearchFilter creationOrderFilter = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
		creationOrderFilter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters2 = {typeFilter, creationOrderFilter};
		String[] order2 = {"ART122","ART121","ART120","ART112","ART111","ART104","ART102","ART187","ART180","ART179","ART1"};
		
		contents = this._contentManager.searchRecords(filters2);
		assertEquals(order2.length, contents.size());
		this.verifyOrder(contents, order2);

		EntitySearchFilter descrFilter = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "descriz", true);
		EntitySearchFilter[] filters3 = {typeFilter, creationOrderFilter, descrFilter};
		String[] order3 = {"ART187","ART180","ART179"};
		contents = this._contentManager.searchRecords(filters3);
		assertEquals(order3.length, contents.size());
		this.verifyOrder(contents, order3);

		EntitySearchFilter statusFilter = new EntitySearchFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, false, "AF", true);
		EntitySearchFilter[] filters4 = {typeFilter, creationOrderFilter, descrFilter, statusFilter};
		String[] order4 = {"ART187","ART179"};
		contents = this._contentManager.searchRecords(filters4);
		assertEquals(order4.length, contents.size());
		this.verifyOrder(contents, order4);

		EntitySearchFilter onLineFilter = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
		EntitySearchFilter[] filters5 = {typeFilter, creationOrderFilter, descrFilter, statusFilter, onLineFilter};
		String[] order5 = {"ART187"};
		contents = this._contentManager.searchRecords(filters5);
		assertEquals(order5.length, contents.size());
		this.verifyOrder(contents, order5);

		onLineFilter.setNullOption(true);
		EntitySearchFilter[] filters6 = {typeFilter, creationOrderFilter, descrFilter, statusFilter, onLineFilter};
		String[] order6 = {"ART179"};
		contents = this._contentManager.searchRecords(filters6);
		assertEquals(order6.length, contents.size());
		this.verifyOrder(contents, order6);
	}

	private void verifyOrder(List<ApsEntityRecord> contents, String[] order) {
		for (int i=0; i<contents.size(); i++) {
			ContentRecordVO vo = (ContentRecordVO) contents.get(i);
			assertEquals(order[i], vo.getId());
		}
	}

	public void testSearchEvents() throws ApsSystemException {
		EntitySearchFilter filterIt = new EntitySearchFilter("Titolo", true, "it", false);
		filterIt.setLangCode("it");
		EntitySearchFilter[] filters = {filterIt};
		List<String> contents = this._contentManager.searchId("EVN", filters);
		assertTrue(contents.isEmpty());

		filterIt = new EntitySearchFilter("Titolo", true, "it", true);
		filterIt.setLangCode("it");
		EntitySearchFilter[] filters1 = {filterIt};
		contents = this._contentManager.searchId("EVN", filters1);
		assertFalse(contents.isEmpty());
		String[] expectedItalianContentsId = {"EVN194", "EVN193", "EVN192", "EVN191", "EVN103"};
		assertEquals(expectedItalianContentsId.length, contents.size());
		for (int i=0; i<expectedItalianContentsId.length; i++) {
			assertTrue(contents.contains(expectedItalianContentsId[i]));
		}

		EntitySearchFilter filterEn = new EntitySearchFilter("Titolo", true, "it", true);
		filterEn.setLangCode("en");
		EntitySearchFilter[] filters2 = {filterEn};
		contents = this._contentManager.searchId("EVN", filters2);
		assertFalse(contents.isEmpty());
		String[] expectedEnglishContentsId = {"EVN103", "EVN193", "EVN191", "EVN192", "EVN194"};
		assertEquals(expectedEnglishContentsId.length, contents.size());
		for (int i=0; i<expectedEnglishContentsId.length; i++) {
			assertTrue(contents.contains(expectedEnglishContentsId[i]));
		}

		filterEn.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters3 = {filterEn};
		contents = this._contentManager.searchId("EVN", filters3);
		assertEquals(expectedEnglishContentsId.length, contents.size());
		for (int i=0; i<expectedEnglishContentsId.length; i++) {
			assertEquals(expectedEnglishContentsId[i], contents.get(i));
		}

		filterEn.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters4 = {filterEn};
		contents = this._contentManager.searchId("EVN", filters4);
		assertEquals(expectedEnglishContentsId.length, contents.size());
		for (int i=0; i<expectedEnglishContentsId.length; i++) {
			assertEquals(expectedEnglishContentsId[expectedEnglishContentsId.length - i - 1], contents.get(i));
		}
	}
	
	public void testSearchEntities() throws ApsSystemException {
		EntitySearchFilter filter = new EntitySearchFilter("Data", true);
		EntitySearchFilter[] filters = {filter};
		List<String> contents = this._contentManager.searchId("ART", filters);
		String[] expectedContentsId1 = {"ART1", "ART104", 
				"ART112", "ART111", "ART120", "ART121", "ART179"};
		assertEquals(expectedContentsId1.length, contents.size());
		for (int i=0; i<expectedContentsId1.length; i++) {
			assertTrue(contents.contains(expectedContentsId1[i]));
		}
		
		contents = this._contentManager.searchId("EVN", null);
		String[] expectedContentsId2 = {"EVN194", "EVN193", "EVN192", "EVN191", "EVN103", 
				"EVN20", "EVN21", "EVN23", "EVN24", "EVN25", "EVN41"};
		assertEquals(expectedContentsId2.length, contents.size());
		for (int i=0; i<expectedContentsId2.length; i++) {
			assertTrue(contents.contains(expectedContentsId2[i]));
		}
	}
	
	public void testLoadOrderedEvents_1() throws ApsSystemException {
		EntitySearchFilter filterForDescr = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false);
		filterForDescr.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filterForDescr};
		List<String> contents = _contentManager.searchId("EVN", filters);

		String[] expectedContentsId = {"EVN24", "EVN23", "EVN103", "EVN191", 
				"EVN192", "EVN193", "EVN194", "EVN41", "EVN20", "EVN21", "EVN25"};
		assertEquals(expectedContentsId.length, contents.size());
		for (int i=0; i<expectedContentsId.length; i++) {
			assertEquals(expectedContentsId[i], contents.get(i));
		}

		filterForDescr.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = _contentManager.searchId("EVN", filters);

		assertEquals(expectedContentsId.length, contents.size());
		for (int i=0; i<expectedContentsId.length; i++) {
			assertEquals(expectedContentsId[expectedContentsId.length - i - 1], contents.get(i));
		}
	}

	public void testLoadOrderedEvents_2() throws ApsSystemException {
		EntitySearchFilter filterForCreation = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false);
		filterForCreation.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filterForCreation};

		List<String> contents = _contentManager.searchId("EVN", filters);
		String[] expectedOrderedContentsId = {"EVN191", "EVN192", "EVN193", "EVN194", 
				"EVN103", "EVN20", "EVN23", "EVN24", "EVN25", "EVN41", "EVN21"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}

		filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = _contentManager.searchId("EVN", filters);
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[expectedOrderedContentsId.length - i - 1], contents.get(i));
		}
	}

	public void testLoadEvents2() throws ApsSystemException {
		Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
		Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
		EntitySearchFilter filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, "Even", true);
		filter2.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter, filter2};

		List<String> contents = _contentManager.searchId("EVN", filters);
		assertEquals(2, contents.size());
		assertEquals("EVN193", contents.get(0));
		assertEquals("EVN192", contents.get(1));

		EntitySearchFilter filter3 = new EntitySearchFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, false, "pronto", true);
		EntitySearchFilter[] filters2 = {filter, filter3, filter2};
		contents = _contentManager.searchId("EVN", filters2);
		assertEquals(0, contents.size());

		EntitySearchFilter[] filters2_bis = {filter, filter2, filter3};
		contents = _contentManager.searchId("EVN", filters2_bis);
		assertEquals(0, contents.size());

		filter2 = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false);
		filter2.setOrder(EntitySearchFilter.DESC_ORDER);

		EntitySearchFilter[] filters3 = {filter, filter2};
		contents = _contentManager.searchId("EVN", filters3);

		String[] expectedOrderedContentsId = {"EVN25", "EVN21", "EVN20", "EVN41", "EVN193", 
				"EVN192", "EVN103", "EVN23", "EVN24"};
		assertEquals(expectedOrderedContentsId.length, contents.size());
		for (int i=0; i<expectedOrderedContentsId.length; i++) {
			assertEquals(expectedOrderedContentsId[i], contents.get(i));
		}
	}


	public void testLoadFutureEvents1() throws ApsSystemException {
		Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};

		List<String> contents = this._contentManager.searchId("EVN", filters);
		String[] expectedOrderedEntitiesId = {"EVN21", "EVN20", "EVN25", "EVN41", "EVN23", 
				"EVN24", "EVN193", "EVN194"};
		assertEquals(expectedOrderedEntitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents.get(i));
		}
	}

	public void testLoadFutureEvents2() throws ApsSystemException {
		Date date = DateConverter.parseDate("2008-01-01", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, date, null);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};

		List<String> contents = this._contentManager.searchId("EVN", filters);
		String[] expectedOrderedEntitiesId = {"EVN41", "EVN23", "EVN24", "EVN193", "EVN194"};
		assertEquals(expectedOrderedEntitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents.get(i));
		}
	}

	public void testLoadFutureEvents3() throws ApsSystemException {
		Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};

		List<String> contents = this._contentManager.searchId("EVN", filters);
		String[] expectedOrderedEntitiesId = {"EVN194", "EVN193", "EVN24", 
				"EVN23", "EVN41", "EVN25", "EVN20", "EVN21"};
		assertEquals(expectedOrderedEntitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents.get(i));
		}
	}

	public void testLoadPastEvents1() throws ApsSystemException {
		Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");

		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
		filter.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter};

		List<String> contents2 = this._contentManager.searchId("EVN", filters);
		String[] expectedOrderedEntitiesId = {"EVN191", "EVN192", "EVN103", 
				"EVN21", "EVN20", "EVN25", "EVN41", "EVN23"};
		assertEquals(expectedOrderedEntitiesId.length, contents2.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents2.get(i));
		}
	}

	public void testLoadPastEvents2() throws ApsSystemException {
		Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");

		EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
		filter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = {filter};
		List<String> contents2 = this._contentManager.searchId("EVN", filters);
		String[] expectedOrderedEntitiesId = {"EVN23", "EVN41", "EVN25", 
				"EVN20", "EVN21", "EVN103", "EVN192", "EVN191"};
		assertEquals(expectedOrderedEntitiesId.length, contents2.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents2.get(i));
		}
	}


	public void testLoadFutureEntityEvents1() throws Throwable {
		Date dateForTest = DateConverter.parseDate("1999-03-14", "yyyy-MM-dd");

		EntitySearchFilter filter1 = new EntitySearchFilter("DataInizio", true, dateForTest, null);
		filter1.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters = {filter1};

		List<String> contents = this._contentManager.searchId("EVN", filters);
		String[] expectedOrderedEnitiesId = {"EVN192", "EVN103", 
				"EVN21", "EVN20", "EVN25", "EVN41", "EVN23", "EVN24", "EVN193", "EVN194"};

		assertEquals(expectedOrderedEnitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEnitiesId.length; i++) {
			assertEquals(expectedOrderedEnitiesId[i], contents.get(i));
		}

		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters2 = {filter1};
		contents = this._contentManager.searchId("EVN", filters2);

		assertEquals(expectedOrderedEnitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEnitiesId.length; i++) {
			assertEquals(expectedOrderedEnitiesId[expectedOrderedEnitiesId.length - i - 1], contents.get(i));
		}
	}

	public void testLoadFutureEntityEvents2() throws Throwable {
		Date startDateForTest = DateConverter.parseDate("1999-03-14", "yyyy-MM-dd");
		EntitySearchFilter filter1 = new EntitySearchFilter("DataInizio", true, startDateForTest, null);
		filter1.setOrder(EntitySearchFilter.ASC_ORDER);
		Date endDateForTest = DateConverter.parseDate("2017-09-12", "yyyy-MM-dd");
		EntitySearchFilter filter2 = new EntitySearchFilter("DataFine", true, null, endDateForTest);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);

		EntitySearchFilter[] filters3 = {filter1, filter2};
		List<String> contents = this._contentManager.searchId("EVN", filters3);
		String[] expectedOrderedEntitiesId = {"EVN192", "EVN103", 
				"EVN21", "EVN20", "EVN25", "EVN41", "EVN23", "EVN24", "EVN193"};
		assertEquals(expectedOrderedEntitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents.get(i));
		}

		filter2.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = this._contentManager.searchId("EVN", filters3);
		//l'ordinamento è lo stesso il quanto il primo ordinamento viene fatto con il filter1
		assertEquals(expectedOrderedEntitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[i], contents.get(i));
		}

		filter1.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = this._contentManager.searchId("EVN", filters3);
		assertEquals(expectedOrderedEntitiesId.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId.length; i++) {
			assertEquals(expectedOrderedEntitiesId[expectedOrderedEntitiesId.length - i - 1], contents.get(i));
		}

		filter1.setOrder(EntitySearchFilter.ASC_ORDER);
		filter2.setOrder(EntitySearchFilter.ASC_ORDER);
		EntitySearchFilter[] filters4 = {filter2, filter1};
		contents = this._contentManager.searchId("EVN", filters4);
		String[] expectedOrderedEntitiesId2 = {"EVN192", "EVN103", 
				"EVN20", "EVN21", "EVN25", "EVN41", "EVN23", "EVN24", "EVN193"};
		assertEquals(expectedOrderedEntitiesId2.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId2.length; i++) {
			assertEquals(expectedOrderedEntitiesId2[i], contents.get(i));
		}

		filter2.setOrder(EntitySearchFilter.DESC_ORDER);
		contents = this._contentManager.searchId("EVN", filters4);
		assertEquals(expectedOrderedEntitiesId2.length, contents.size());
		for (int i=0; i<expectedOrderedEntitiesId2.length; i++) {
			assertEquals(expectedOrderedEntitiesId2[expectedOrderedEntitiesId2.length - i - 1], contents.get(i));
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
