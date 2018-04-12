package org.entando.entando.aps.system.services.pagemodel;

import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.pagemodel.model.PageModelRequest;

public interface IPageModelService {

    String BEAN_NAME = "PageModelService";

    PagedMetadata<PageModelDto> getPageModels(RestListRequest restRequest);

    PageModelDto getPageModel(String code);

    PageModelDto updatePageModel(PageModelRequest pageModelRequest);

    PageModelDto addPageModel(PageModelRequest pageModelRequest);

    void removePageModel(String code);

    PagedMetadata<?> getPageModelReferences(String pageModelCode, String managerName, RestListRequest restRequest);

}
