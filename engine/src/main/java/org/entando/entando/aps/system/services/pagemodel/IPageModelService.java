package org.entando.entando.aps.system.services.pagemodel;

import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface IPageModelService {

    String BEAN_NAME = "PageModelService";

    PagedMetadata<PageModelDto> getPageModels(RestListRequest restRequest);

    PageModelDto getPageModelDto(String groupName);

    PageModelDto updatePageModel(String groupName, String descr);

    PageModelDto addPageModel(PageModelRequest groupRequest);

    void removePageModel(String groupName);
}
