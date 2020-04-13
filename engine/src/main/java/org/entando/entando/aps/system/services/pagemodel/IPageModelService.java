package org.entando.entando.aps.system.services.pagemodel;

import java.util.Map;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.pagemodel.model.PageModelRequest;

public interface IPageModelService {

    String BEAN_NAME = "PageModelService";

    PagedMetadata<PageModelDto> getPageModels(RestListRequest restRequest, Map<String, String> requestParams);

    PageModelDto getPageModel(String code);

    PageModelDto updatePageModel(PageModelRequest pageModelRequest);

    PageModelDto addPageModel(PageModelRequest pageModelRequest);

    void removePageModel(String code);

    PagedMetadata<?> getPageModelReferences(String pageModelCode, String managerName, RestListRequest restRequest);

    Integer getPageModelUsage(String pageModelCode);

}
