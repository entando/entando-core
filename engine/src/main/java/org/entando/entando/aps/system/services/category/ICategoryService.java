/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.category;

import java.util.List;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

/**
 * @author E.Santoboni
 */
public interface ICategoryService {

    public List<CategoryDto> getTree(String parentCode);

    public CategoryDto getCategory(String categoryCode);

    public CategoryDto addCategory(CategoryDto categoryDto);

    public CategoryDto updateCategory(CategoryDto categoryDto);

    public void deleteCategory(String categoryCode);

    public PagedMetadata<?> getCategoryReferences(String categoryCode, String managerName, RestListRequest restListRequest);

}
