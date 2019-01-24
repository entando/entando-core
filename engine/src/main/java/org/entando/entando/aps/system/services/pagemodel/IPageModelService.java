package org.entando.entando.aps.system.services.pagemodel;

import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.pagemodel.model.PageModelRequest;

public interface IPageModelService {

    String BEAN_NAME = "PageModelService";

    /**
     * Retrieves only local page models, excludes Digital Exchange page models
     */
    PagedMetadata<PageModelDto> getLocalPageModels(RestListRequest restRequest);

    /**
     * Retrieves all page models including Digital Exchange page models
     */
    PagedMetadata<PageModelDto> getAllPageModels(RestListRequest restRequest);

    PageModelDto getPageModel(String code);

    PageModelDto updatePageModel(PageModelRequest pageModelRequest);

    PageModelDto addPageModel(PageModelRequest pageModelRequest);

    void removePageModel(String code);

    PagedMetadata<?> getPageModelReferences(String pageModelCode, String managerName, RestListRequest restRequest);

}
