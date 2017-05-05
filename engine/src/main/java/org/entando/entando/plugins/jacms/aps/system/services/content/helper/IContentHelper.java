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
package org.entando.entando.plugins.jacms.aps.system.services.content.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public interface IContentHelper {

	public static String BEAN_NAME = "jacmsContentHelper";

	public Map<String, List<?>> getReferencingObjects(Content content) throws ApsSystemException;

	public Map<String, List<?>> getReferencingObjects(Content content, Collection<ContentUtilizer> contentUtilizers) throws ApsSystemException;

	public List<ContentUtilizer> getContentUtilizers();

}
