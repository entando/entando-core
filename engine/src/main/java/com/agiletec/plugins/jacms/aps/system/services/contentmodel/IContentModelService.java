package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionary;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;

public interface IContentModelService {

    String BEAN_NAME = "ContentModelService";

    public PagedMetadata<ContentModelDto> getContentModels(RestListRequest requestList);

    public ContentModelDto getContentModel(Long modelId);

    public ContentModelDictionary getContentModelDictionary(String typeCode);

    public ContentModelDto addContentModel(ContentModelRequest contentModel);

    public void removeContentModel(Long modelId);

    public Map<String, List<String>> getPageReferences(Long modelId);

    public ContentModelDto updateContentModel(ContentModelRequest contentModelRequest);

}
