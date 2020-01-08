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
package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import org.entando.entando.aps.system.services.DtoBuilder;

/**
 *
 * @author paddeo
 */
public class PageDtoBuilder extends DtoBuilder<IPage, PageDto> {
    
    private IPageManager pageManager;

    @Override
    protected PageDto toDto(IPage src) {
        return new PageDto(src, this.getPageManager());
    }
    
    public static PageDto converToDto(IPage src, IPageManager pageManager) {
        PageDtoBuilder dto = new PageDtoBuilder();
        dto.setPageManager(pageManager);
        return dto.toDto(src);
    }

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

}
