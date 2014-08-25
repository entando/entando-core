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
package com.agiletec.apsadmin.tags.util;

import javax.servlet.ServletRequest;

/**
 * This helper class supports all the tags which implement a self-incrementing Tabindex property. 
 * @author E.Santoboni
 */
public class AutoIndexingTagHelper {
	
	/**
	 * Return the current tabindex.
	 * @param tag The tag containing the tabindex property. 
	 * @param request The request.
	 * @return The current request.
	 */
	public static String getCurrentIndex(IAutoIndexingTag tag, ServletRequest request) {
		if (null == tag.getUseTabindexAutoIncrement() || !tag.getUseTabindexAutoIncrement().booleanValue()) {
			return null;
		}
		String currentCounter = (String) request.getAttribute(COUNTER_KEY);
		int currentCounterValue = 1;
		if (null != currentCounter) {
			currentCounterValue = Integer.parseInt(currentCounter);
		}
		int step = (null != tag.getStep()) ? tag.getStep().intValue() : 0;
		int nextValue = currentCounterValue + step;
		request.setAttribute(COUNTER_KEY, String.valueOf(nextValue));
		return String.valueOf(currentCounterValue);
	}
	
	/**
	 * The name of the variable holding the Tabindex value in the request.
	 */
	public static final String COUNTER_KEY = "counterTabIndexKey";
	
}
