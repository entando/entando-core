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
 * @author S.Puddu
 */
public class PageUtils {
	
	/**
	 * Return the full path of the given page; the path is composed by the concatenation of the
	 * code of the page starting from the root to the given page.
	 * @param page The page whose path must be found.
	 * @param separator The separator of the page codes
	 * @return The full path of the page
	 * @deprecated Use {@link #getOnlineFullPath(IPage, String)} or {@link #getDraftFullPath(IPage, String)} instead
	 */
	@Deprecated
	public static StringBuffer getFullPath(IPage page, String separator) {
		return getOnlineFullPath(page, separator);
	}
	
	/**
	 * Return the full path of the given, online, page; the path is composed by the concatenation of the
	 * code of the page starting from the root to the given page.
	 * @param page The page whose path must be found.
	 * @param separator The separator of the page codes
	 * @return The full path of the page
	 */
	public static StringBuffer getOnlineFullPath(IPage page, String separator) {
		if (page.isRoot()) {
			return new StringBuffer(page.getCode());
		}
		IPage temp = page;
		StringBuffer buffer = new StringBuffer();
		buffer.insert(0, temp.getCode());
		while (!temp.getCode().equals(temp.getParentCode())) {
			temp = temp.getParent();
			if (temp.getOnlineMetadata().isShowable()) {
				buffer.insert(0, temp.getCode() + separator);
			}
		}
		return buffer;
	}
	
	/**
	 * Return the full path of the given, draft, page; the path is composed by the concatenation of the
	 * code of the page starting from the root to the given page.
	 * @param page The page whose path must be found.
	 * @param separator The separator of the page codes
	 * @return The full path of the page
	 */
	public static StringBuffer getDraftFullPath(IPage page, String separator) {
		if (page.isRoot()) {
			return new StringBuffer(page.getCode());
		}
		IPage temp = page;
		StringBuffer buffer = new StringBuffer();
		buffer.insert(0, temp.getCode());
		while (!temp.getCode().equals(temp.getParentCode())) {
			temp = temp.getParent();
			if (temp.getDraftMetadata().isShowable()) {
				buffer.insert(0, temp.getCode() + separator);
			}
		}
		return buffer;
	}
	
}