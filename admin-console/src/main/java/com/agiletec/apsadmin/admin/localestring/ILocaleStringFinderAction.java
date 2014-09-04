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
package com.agiletec.apsadmin.admin.localestring;

import java.util.List;

/**
 * This base interface declares the searcher method upon the localization strings.
 * @author E.Santoboni
 */
public interface ILocaleStringFinderAction {
	
	/**
	 * Return the list of the label keys, given the proper search parameters. This list is alphabetically sorted. 
	 * @return The list indexed by the keys of the various labels.
	 */
	public List<String> getLocaleStrings();
	
}