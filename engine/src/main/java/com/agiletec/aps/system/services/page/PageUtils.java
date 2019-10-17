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
package com.agiletec.aps.system.services.page;

/**
 * Helper class for the page manager
 *
 */
public class PageUtils {

	/**
	 * Return the full path of the given page; the path is composed by the
	 * concatenation of the code of the page starting from the root to the given
	 * page.
	 *
     * @param manager
	 * @param page The page whose path must be found.
	 * @param separator The separator of the page codes
	 * @return The full path of the page
	 */
	public static StringBuffer getFullPath(IPageManager manager, IPage page, String separator) {
		if (page.isRoot()) {
			return new StringBuffer(page.getCode());
		}
		IPage temp = page;
		StringBuffer buffer = new StringBuffer();
		buffer.insert(0, temp.getCode());
		while (!temp.getCode().equals(temp.getParentCode())) {
                    boolean isOnlineInstance = temp.isOnlineInstance();
			temp = (isOnlineInstance) ? manager.getOnlinePage(temp.getParentCode()) : manager.getDraftPage(temp.getParentCode());
			if (temp.getMetadata().isShowable()) {
				buffer.insert(0, temp.getCode() + separator);
			}
		}
		return buffer;
	}
	
	public static IPage getPage(IPageManager manager, boolean onLine, String code) {
		return (onLine) ? manager.getOnlinePage(code) : manager.getDraftPage(code);
	}

}
