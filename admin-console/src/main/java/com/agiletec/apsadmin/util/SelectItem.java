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
package com.agiletec.apsadmin.util;

/**
 * @author E.Santoboni
 * @deprecated Use {@link com.agiletec.aps.util.SelectItem}
 */
public class SelectItem extends com.agiletec.aps.util.SelectItem {
	
	public SelectItem(String key, String value) {
		super(key, value);
	}
	
	public SelectItem(String key, String value, String optgroup) {
		super(key, value, optgroup);
	}
	
}