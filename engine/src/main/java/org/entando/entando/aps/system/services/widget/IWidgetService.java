package org.entando.entando.aps.system.services.widget;

import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.aps.system.services.widget.model.WidgetDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.group.model.GroupRequest;
import org.entando.entando.web.widget.model.WidgetRequest;

public interface IWidgetService {

    String BEAN_NAME = "WidgetService";

    WidgetDto getWidget(String widgetCode);

    WidgetDto addWidget(WidgetRequest widgetRequest);

    void removeWidget(String widgetCode);

    PagedMetadata<WidgetDto> getWidgets(RestListRequest restRequest);

    WidgetDto updateWidget(String widgetCode, WidgetRequest widgetRequest);
}
