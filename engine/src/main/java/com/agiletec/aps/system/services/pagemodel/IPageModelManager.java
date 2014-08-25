/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Collection;

/**
 * Interface of the page models manager.
 * @author E.Santoboni
 */
public interface IPageModelManager {

	/**
	 * Return a Page Model by the code.
	 * @param code The code of the Page Model
	 * @return The required Page Model
	 */
	public PageModel getPageModel(String code);
	
	/**
	 * Return the collection of defined Page Models
	 * @return The collection of defined Page Models
	 */
	public Collection<PageModel> getPageModels();
	
	public void addPageModel(PageModel pageModel) throws ApsSystemException;
	
	public void updatePageModel(PageModel pageModel) throws ApsSystemException;
	
	public void deletePageModel(String code) throws ApsSystemException;
	
}
