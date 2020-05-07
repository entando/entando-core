package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import java.util.Arrays;
import java.util.List;

public class WidgetMockHelper {

    public static final String WIDGET_1_CODE = "widget1";
    public static final String WIDGET_2_CODE = "widget2";
    public static final String WIDGET_3_CODE = "widget3";
    public static final List<String> WIDGET_CODE_LIST = Arrays.asList(WIDGET_1_CODE, WIDGET_2_CODE, WIDGET_3_CODE);

    public static Widget mockWidget() {
        return mockWidget(WIDGET_1_CODE);
    }

    public static Widget mockWidget(String widgetCode) {
        Widget widget = new Widget();
        widget.setType(mockWidgetType(widgetCode));
        return widget;
    }

    public static WidgetType mockWidgetType() {
        return mockWidgetType(WIDGET_1_CODE);
    }

    public static WidgetType mockWidgetType(String widgetCode) {

        ApsProperties apsProperties = new ApsProperties();

        WidgetType widgetType = new WidgetType();
        widgetType.setCode(widgetCode);
        widgetType.setTitles(apsProperties);

        return widgetType;
    }

}