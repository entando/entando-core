package org.entando.entando.aps.system.services.widget;

import java.util.HashMap;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.aps.system.services.widget.model.WidgetDto;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WidgetService implements IWidgetService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IWidgetTypeManager widgetManager;

    @Autowired
    private IDtoBuilder<WidgetType, WidgetDto> dtoBuilder;

    @Override
    public WidgetDto getWidget(String widgetCode) {
        WidgetType widgetType = widgetManager.getWidgetType(widgetCode);
        WidgetDto widgetDto = dtoBuilder.convert(widgetType);
        return widgetDto;
    }

    @Override
    public WidgetDto addWidget(WidgetRequest widgetRequest) {

        WidgetType widgetType = this.createWidget(widgetRequest);

        try {
           widgetManager.addWidgetType(widgetType);
        }catch(ApsSystemException e) {
            logger.error("Failed to add widget type for request {} ",widgetRequest);
            throw new RestServerError("error in update group", e);
        }

        WidgetDto widgetDto = this.dtoBuilder.convert(widgetType);
        return widgetDto;
    }

    @Override
    public void removeWidget(String widgetCode) {

        try {
            this.widgetManager.deleteWidgetType(widgetCode);
        }catch(ApsSystemException e) {
            logger.error("Failed to remove widget type for request {} ",widgetCode);
            throw new RestServerError("failed to update widget type by code ", e);
        }
    }

    @Override
    public PagedMetadata<WidgetDto> getWidgets(RestListRequest restRequest) {
        //TODO
        return null;
    }

    @Override
    public WidgetDto updateWidget(String widgetCode, WidgetRequest widgetRequest) {

        //TODO
//        this.widgetManager.updateWidgetType(widgetCode, );
        return null;
    }

    private WidgetType createWidget(WidgetRequest widgetRequest) {

        //TODO finish this
        WidgetType type = new WidgetType();
        type.setCode(widgetRequest.getCode());

        //TODO validate this
        type.setMainGroup(widgetRequest.getGroup());

        ApsProperties titles = new ApsProperties();

        widgetRequest.getTitles().forEach((k,v) -> titles.put(k, v));


        return type;
    }
}
