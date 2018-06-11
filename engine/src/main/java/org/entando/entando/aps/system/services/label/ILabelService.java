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
package org.entando.entando.aps.system.services.label;

import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface ILabelService {

    String BEAN_NAME = "LabelService";

    PagedMetadata<LabelDto> getLabelGroups(RestListRequest restRequest);

    LabelDto getLabelGroup(String code);

    LabelDto updateLabelGroup(LabelDto labelRequest);

    LabelDto addLabelGroup(LabelDto labelRequest);

    void removeLabelGroup(String code);
}
