package org.entando.entando.aps.system.services.widget.model;

import java.util.Hashtable;
import java.util.List;

import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

public class WidgetDtoBuilder extends DtoBuilder<WidgetType, WidgetDto> {

    @Override
    protected WidgetDto toDto(WidgetType src) {
        WidgetDto dest = new WidgetDto();

        dest.setCode(src.getCode());

        dest.setGroup(src.getMainGroup());
        dest.setTitles((Hashtable)src.getTitles());
        dest.setUsed(src.isLocked());
        dest.setPluginCode(src.getPluginCode());
        return dest;
    }
}
