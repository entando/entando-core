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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel.cache;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelDAO;

public interface IContentModelManagerCacheWrapper {

    public static final String CACHE_NAME = "Entando_ContentModelManager";

    public static final String CACHE_NAME_PREFIX = "ContentModelManager_model_";
    public static final String CODES_CACHE_NAME = "ContentModelManager_codes";

    public void initCache(IContentModelDAO contentModelDao) throws ApsSystemException;

    public List<ContentModel> getContentModels();

    public ContentModel getContentModel(String code);

    public void addContentModel(ContentModel contentModel);

    public void updateContentModel(ContentModel contentModel);

    public void removeContentModel(ContentModel contentModel);
}
