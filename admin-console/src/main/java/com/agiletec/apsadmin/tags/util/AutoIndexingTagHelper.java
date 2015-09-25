/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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
