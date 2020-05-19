package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.model.WidgetDetails;
import org.entando.entando.aps.system.services.widgettype.model.WidgetInfoDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WidgetMockHelper {

    public static final String WIDGET_1_CODE = "widget1";
    public static final String WIDGET_2_CODE = "widget2";
    public static final String WIDGET_3_CODE = "widget3";
    public static final List<String> WIDGET_CODE_LIST = Arrays.asList(WIDGET_1_CODE, WIDGET_2_CODE, WIDGET_3_CODE);
    public static final String WIDGET_CUSTOM_UI = "Hi dear";
    public static final List<String> LANGS = Arrays.asList("IT", "EN");
    public static final List<String> TITLES = Arrays.asList("Titolone", "Big Title");
    public static final Map<String, String> TITLES_MAP = IntStream.range(0, LANGS.size())
            .mapToObj(i -> i)
            .collect(Collectors.toMap(LANGS::get, TITLES::get));

    /**
     *
     * @return
     */
    public static Widget mockWidget() {
        return mockWidget(WIDGET_1_CODE);
    }

    /**
     *
     * @param widgetCode
     * @return
     */
    public static Widget mockWidget(String widgetCode) {
        Widget widget = new Widget();
        widget.setType(mockWidgetType(widgetCode));
        return widget;
    }

    /**
     *
     * @return
     */
    public static WidgetType mockWidgetType() {
        return mockWidgetType(WIDGET_1_CODE);
    }

    /**
     *
     * @param widgetCode
     * @return
     */
    public static WidgetType mockWidgetType(String widgetCode) {

        ApsProperties apsProperties = new ApsProperties();

        WidgetType widgetType = new WidgetType();
        widgetType.setCode(widgetCode);
        widgetType.setTitles(apsProperties);

        return widgetType;
    }

    /**
     *
     */
    public static WidgetInfoDto mockWidgetInfoDto() {

        WidgetInfoDto widgetInfoDto = new WidgetInfoDto();
        widgetInfoDto.setCode(WIDGET_1_CODE);
        widgetInfoDto.setPublishedUtilizers(mockPublishedUtilizers());
        widgetInfoDto.setDraftUtilizers(mockDraftUtilizers());
        return widgetInfoDto;
    }

    /**
     *
     * @param pageCode
     * @return
     */
    public static WidgetDetails mockWidgetDetails(String pageCode) {

        WidgetDetails widgetDetails = new WidgetDetails();
        widgetDetails.setPageCode(pageCode);
        return widgetDetails;
    }

    /**
     *
     * @return
     */
    public static List<WidgetDetails> mockPublishedUtilizers() {
        return Arrays.asList(mockWidgetDetails(PageMockHelper.PAGE_CODE), mockWidgetDetails(PageMockHelper.PAGE_MISSION_CODE));
    }

    /**
     *
     * @return
     */
    public static List<WidgetDetails> mockDraftUtilizers() {
        return Arrays.asList(mockWidgetDetails(PageMockHelper.PAGE_MODEL_REF_CODE_1), mockWidgetDetails(PageMockHelper.PAGE_MODEL_REF_CODE_1));
    }
}