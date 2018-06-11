/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.label;

import java.util.Map;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LabelRequest extends LabelDto {
    
    @NotEmpty(message = "labelRequest.key.required")
    @Size(max = 50, message = "labelRequest.key.invalidSize")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "labelRequest.key.invalidCharacters")
    @Override
    public String getKey() {
        return super.getKey();
    }

    @NotEmpty(message = "labelRequest.titles.required")
    @Override
    public Map<String, String> getTitles() {
        return super.getTitles();
    }

}
