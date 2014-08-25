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
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;

/**
 * @author E.Santoboni
 */
public class TestPublicContentSearcherDAO extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
	//TODO DA AGGIUNGERE TEST
	
    public void testLoadContentsId_1() throws Throwable {
    	List<String> list = null;
		try {
			list = _contentSearcherDao.loadPublicContentsId("ART", null, null, null);
		} catch (Throwable t) {
			throw t;
		}
		assertEquals(4, list.size());
		assertFalse(list.contains("ART179"));//contenuto non on-line
		assertTrue(list.contains("ART180"));
		assertTrue(list.contains("ART187"));
		assertTrue(list.contains("ART121"));//contenuto administrator abilitato ai free
		assertFalse(list.contains("ART102"));//contenuto di gruppo customers
	}
    
    public void testLoadContentsId_2() throws Throwable {
		List<String> list = null;
		try {
			List<String> groups = new ArrayList<String>();
			groups.add("customers");
			list = _contentSearcherDao.loadPublicContentsId("ART", null, null, groups);
		} catch (Throwable t) {
			throw t;
		}
		assertEquals(8, list.size());
		assertFalse(list.contains("ART179"));//contenuto non on-line
		assertTrue(list.contains("ART111"));
		assertTrue(list.contains("ART180"));
		assertTrue(list.contains("ART187"));
		assertTrue(list.contains("ART121"));//contenuto administrator abilitato ai free
		assertTrue(list.contains("ART122"));//contenuto administrator abilitato ai customers
		assertTrue(list.contains("ART102"));
		assertTrue(list.contains("ART112"));
	}
	
	private void init() throws Exception {
		try {
			_contentSearcherDao = new PublicContentSearcherDAO();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			((PublicContentSearcherDAO) _contentSearcherDao).setDataSource(dataSource);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
    
	private PublicContentSearcherDAO _contentSearcherDao;
    
}
