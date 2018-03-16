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
package org.entando.entando.aps.system.services.pagesettings.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author paddeo
 */
public class PageSettingsDto {

    List<ParamDto> params = new ArrayList<>();

    public List<ParamDto> getParams() {
        return params;
    }

    public void setParams(List<ParamDto> params) {
        this.params = params;
    }

    public void addParam(ParamDto param) {
        params.add(param);
    }

}
