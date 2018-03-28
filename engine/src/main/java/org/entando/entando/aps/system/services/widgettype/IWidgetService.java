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
package org.entando.entando.aps.system.services.widgettype;

import org.entando.entando.aps.system.services.widgettype.model.WidgetDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.widget.model.WidgetRequest;

public interface IWidgetService {

    public String BEAN_NAME = "WidgetService";

    public WidgetDto getWidget(String widgetCode);

    public WidgetDto addWidget(WidgetRequest widgetRequest);

    public void removeWidget(String widgetCode);

    public PagedMetadata<WidgetDto> getWidgets(RestListRequest restRequest);

    public WidgetDto updateWidget(String widgetCode, WidgetRequest widgetRequest);
}
