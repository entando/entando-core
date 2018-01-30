/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.cache;

/**
 * @author E.Santoboni
 */
public interface ICacheInfoManager {

	public void flushEntry(String targetCache, String key);

	public void flushGroup(String targetCache, String group);

	public void putInGroup(String targetCache, String key, String[] groups);

	public static final String DEFAULT_CACHE_NAME = "Entando_Cache";

	@Deprecated
	public static final String CACHE_NAME = DEFAULT_CACHE_NAME;

}
