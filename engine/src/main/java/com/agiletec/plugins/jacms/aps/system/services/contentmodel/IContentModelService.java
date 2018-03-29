package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface IContentModelService {

    String BEAN_NAME = "ContentModelService";

    public PagedMetadata<ContentModelDto> getContentModels(RestListRequest requestList);

}
