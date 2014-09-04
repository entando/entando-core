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
package com.agiletec.apsadmin.portal;

import java.util.List;

import com.agiletec.aps.system.services.page.IPage;

/**
 * This class declares the method used to search through pages. Noticeably we don't have here a 
 * standard method whose return type is either the string 'SUCCESS' or 'INPUT' (or whatever); as of
 * the current version we have a method to invoke directly from the jsp of the view layer.
 * @author M.E. Minnai
 */
public interface IPageFinderAction {

	/**
	 * Return a list containing the given token in its code.
	 * @param pageCodeToken the piece of code to look for among the pages
	 * @return a List of the page code and containing the full page informations.
	 */
	public List<IPage> getPagesFound();
	
}
