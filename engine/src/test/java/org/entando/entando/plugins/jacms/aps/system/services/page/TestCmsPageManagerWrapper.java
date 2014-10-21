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
package org.entando.entando.plugins.jacms.aps.system.services.page;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.page.IPage;
import java.util.ArrayList;

/**
 * @author E.santoboni - M.Diana
 */
public class TestCmsPageManagerWrapper extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
    public void testGetContentUtilizers() throws Throwable {
    	List<IPage> utilizers = this._pageManagerWrapper.getContentUtilizers("ART187");
    	assertEquals(3, utilizers.size());
		List<String> codes = new ArrayList<String>();
		for (int i = 0; i < utilizers.size(); i++) {
			IPage page = utilizers.get(i);
			codes.add(page.getCode());
		}
    	assertTrue(codes.contains("coach_page"));
    	assertTrue(codes.contains("pagina_11"));
    	assertTrue(codes.contains("pagina_2"));
    	
    	utilizers = this._pageManagerWrapper.getContentUtilizers("ART111");
     	assertEquals(1, utilizers.size());
     	assertEquals("customers_page", utilizers.get(0).getCode());
    }
    
	private void init() throws Exception {
    	try {
    		this._pageManagerWrapper = (CmsPageManagerWrapper) super.getApplicationContext().getBean("jacmPageManagerWrapper");
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private CmsPageManagerWrapper _pageManagerWrapper;
	
}
