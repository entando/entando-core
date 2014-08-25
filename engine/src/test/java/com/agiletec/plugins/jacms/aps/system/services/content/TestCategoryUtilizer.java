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

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * Test del servizio gestore categorie.
 * @author E.Santoboni
 */
public class TestCategoryUtilizer extends BaseTestCase {
	
	public void testGetCategoryUtilizers_1() throws Throwable {
    	String[] names = this.getApplicationContext().getBeanNamesForType(CategoryUtilizer.class);
    	assertTrue(names.length>=2);
    	for (int i=0; i<names.length; i++) {
    		CategoryUtilizer service = (CategoryUtilizer) this.getApplicationContext().getBean(names[i]);
    		List utilizers = service.getCategoryUtilizers("evento");
			if (names[i].equals(JacmsSystemConstants.CONTENT_MANAGER)) {
				assertTrue(utilizers.size()==2);
			} else {
				assertTrue(utilizers.size()==0);
			}
		}
    }
	
	public void testGetCategoryUtilizers_2() throws Throwable {
    	String[] names = this.getApplicationContext().getBeanNamesForType(CategoryUtilizer.class);
    	assertTrue(names.length>=2);
    	for (int i=0; i<names.length; i++) {
    		CategoryUtilizer service = (CategoryUtilizer) this.getApplicationContext().getBean(names[i]);
    		List utilizers = service.getCategoryUtilizers("resCat1");
			if (names[i].equals(JacmsSystemConstants.RESOURCE_MANAGER)) {
				assertTrue(utilizers.size()==1);
			} else {
				assertTrue(utilizers.size()==0);
			}
		}
    }
    
}