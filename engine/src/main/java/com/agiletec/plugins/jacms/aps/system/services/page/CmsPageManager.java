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
package com.agiletec.plugins.jacms.aps.system.services.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;

/**
 * Sovrascrittura del servizio di gestione delle pagine {@link PageManager}.
 * @author E.Santoboni
 */
public class CmsPageManager extends PageManager implements ContentUtilizer {
	
	@Override
    public List getContentUtilizers(String contentId) throws ApsSystemException {
    	List<String> pageCodes = null;
    	try {
    		pageCodes = ((ICmsPageDAO) this.getPageDAO()).getContentUtilizers(contentId);
    	} catch (Throwable t) {
            throw new ApsSystemException("Errore in caricamento " +
            		"pagine referenziate con contenuto " + contentId, t);
    	}
    	List<IPage> pages = new ArrayList<IPage>(pageCodes.size());
    	for (Iterator<String> iterator = pageCodes.iterator(); iterator.hasNext();) {
			String pageCode = iterator.next();
			IPage page = this.getPage(pageCode);
			pages.add(page);
		}
		return pages;
	}
    
}