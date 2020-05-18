package org.entando.entando.web.mockhelper;

import com.agiletec.aps.system.services.group.Group;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.web.page.model.PageRequest;

import java.util.HashMap;
import java.util.Map;

public class PageRequestMockHelper {

    public static final String ADD_PAGE_CODE = "newPage";
    public static final String ADD_FIRST_CHILD_PAGE_CODE = "child1";
    public static final String ADD_PAGE_MODEL = "home";
    public static final String ADD_PAGE_PARENT_CODE = "service";
    public static final String ADD_PAGE_STATUS = IPageService.STATUS_ONLINE;


    /**
     *
     * @return
     */
    public static PageRequest mockPageRequest() {

        return mockPageRequest(ADD_PAGE_CODE, ADD_PAGE_PARENT_CODE, ADD_PAGE_MODEL, Group.FREE_GROUP_NAME, ADD_PAGE_STATUS);
    }


    /**
     *
     * @param code
     * @param parentCode
     * @param pageModel
     * @param ownerGroup
     * @return
     */
    public static PageRequest mockPageRequest(String code, String parentCode, String pageModel, String ownerGroup, String status) {

        PageRequest pageRequest = new PageRequest();
        pageRequest.setCode(code);
        pageRequest.setPageModel(pageModel);
        pageRequest.setOwnerGroup(ownerGroup);
        pageRequest.setStatus(status);

        Map<String, String> titles = new HashMap<>();
        titles.put("it", code);
        titles.put("en", code);
        pageRequest.setTitles(titles);

        pageRequest.setParentCode(parentCode);

        return pageRequest;
    }
}
