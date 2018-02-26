package org.entando.entando.aps.system.services.widget.model;

import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

public class WidgetDtoBuilder extends DtoBuilder<WidgetType, WidgetDto> {

    @Override
    protected WidgetDto toDto(WidgetType src) {
        WidgetDto dest = new WidgetDto();

        //TODO
        return dest;
    }
}
