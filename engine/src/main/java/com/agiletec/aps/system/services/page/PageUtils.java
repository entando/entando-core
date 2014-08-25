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
package com.agiletec.aps.system.services.page;

/**
 * Helper class for the page manager
 * @author S.Puddu
 */
public class PageUtils {
	
	/**
	 * Return the full path of the given page; the path is composed by the concatenation of the
	 * code of the page starting from the root to the given page.
	 * @param page The page whose path must be found.
	 * @param separator The separator of the page codes
	 * @return The full path of the page
	 */
	public static StringBuffer getFullPath(IPage page, String separator) {
		if (page.isRoot()) {
			return new StringBuffer(page.getCode());
		}
		IPage temp = page;
		StringBuffer buffer = new StringBuffer();
		buffer.insert(0, temp.getCode());
		while (!temp.getCode().equals(temp.getParentCode())) {
			temp = temp.getParent();
			if (temp.isShowable()) {
				buffer.insert(0, temp.getCode() + separator);
			}
		}
		return buffer;
	}
	
}