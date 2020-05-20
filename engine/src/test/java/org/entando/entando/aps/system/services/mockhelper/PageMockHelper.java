package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PageRequest;

import java.util.Arrays;

public class PageMockHelper {

    public static final String PAGE_CODE = "service";
    public static final String PAGE_MISSION_CODE = "mission";
    public static final String PAGE_MODEL_REF_CODE_1 = "page_model_1";
    public static final String PAGE_MODEL_REF_CODE_2 = "page_model_2";
    public static final String PARENT_CODE = "homepage";
    public static final String TOKEN = "tokeNNN";
    public static final String GROUP = "free";
    public static final String STATUS = IPageService.STATUS_ONLINE;
    public static final String UTILIZER_1 = "about";
    public static final String UTILIZER_2 = "contact";
    public static final String UTILIZER_3 = "homepage";
    public static final String UTILIZER_4 = "mission";
    public static final String UTILIZER_5 = "personalarea";
    public static final String[] UTILIZERS = new String[] {UTILIZER_1, UTILIZER_2, UTILIZER_3, UTILIZER_4, UTILIZER_5};



    public static Page mockTestPage(String code, String... widgetCodes) {
        Page page = new Page();
        page.setCode(code);
        page.setParentCode(PARENT_CODE);
        page.setGroup(GROUP);
        page.setModel(mockServicePageModel());

        setWidgets(page, widgetCodes);

        return page;
    }


    /**
     *
     */
    private static Page setWidgets(Page page, String... widgetCodes) {

        Widget[] widgets = Arrays.stream(widgetCodes)
                .map(WidgetMockHelper::mockWidget)
                .toArray(Widget[]::new);
        page.setWidgets(widgets);
        return page;
    }


    public static PageDto mockPageDto() {
        PageDto pageDto = new PageDto();
        pageDto.setCode(PAGE_CODE);
        pageDto.setStatus(STATUS);
        pageDto.setChildren(Arrays.asList(UTILIZERS));
        pageDto.setParentCode(PARENT_CODE);
        return pageDto;
    }

    public static PageRequest mockPageRequest(Page page) {
        PageRequest request = new PageRequest();
        request.setPageModel(page.getModel().getCode());
        request.setParentCode(page.getParentCode());
        request.setOwnerGroup(page.getGroup());
        return request;
    }



    public static PageModel mockServicePageModel() {
        return mockPageModel(PAGE_CODE);
    }

    public static PageModel mockPageModel(String pageCode) {
        PageModel pageModel = new PageModel();
        pageModel.setCode(pageCode);

        Frame frame = new Frame();
        frame.setDescription("frame desc");
        pageModel.setConfiguration(new Frame[] {frame});

        return pageModel;
    }
}
