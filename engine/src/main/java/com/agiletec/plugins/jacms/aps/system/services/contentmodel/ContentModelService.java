package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.utils.ContentModelUtils;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentModelService implements IContentModelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IContentModelManager contentModelManager;
    private IDtoBuilder<ContentModel, ContentModelDto> dtoBuilder;

    protected IContentModelManager getContentModelManager() {
        return contentModelManager;
    }

    public void setContentModelManager(IContentModelManager contentModelManager) {
        this.contentModelManager = contentModelManager;
    }

    protected IDtoBuilder<ContentModel, ContentModelDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<ContentModel, ContentModelDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void setUp() {
        this.setDtoBuilder(new DtoBuilder<ContentModel, ContentModelDto>() {

            @Override
            protected ContentModelDto toDto(ContentModel src) {
                ContentModelDto dto = new ContentModelDto();
                dto.setContentShape(src.getContentShape());
                dto.setContentType(src.getContentType());
                dto.setDescr(src.getDescription());
                dto.setId(src.getId());
                dto.setStylesheet(src.getStylesheet());
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<ContentModelDto> getContentModels(RestListRequest requestList) {

        List<ContentModel> contentModels = this.getContentModelManager().getContentModels();

        //filter
        List<Predicate<ContentModel>> filters = ContentModelUtils.getPredicates(requestList);
        Stream<ContentModel> stream = contentModels.stream();
        for (Predicate<ContentModel> predicate : filters) {
            stream = stream.filter(predicate);
        }

        //sort
        Comparator<ContentModel> comparator = ContentModelUtils.getComparator(requestList.getSort(), requestList.getDirection());
        stream = stream.sorted(comparator);
        contentModels = stream.collect(Collectors.toList());

        //page
        List<ContentModel> subList = requestList.getSublist(contentModels);
        List<ContentModelDto> dtoSlice = this.getDtoBuilder().convert(subList);
        SearcherDaoPaginatedResult<ContentModelDto> paginatedResult = new SearcherDaoPaginatedResult(contentModels.size(), dtoSlice);
        PagedMetadata<ContentModelDto> pagedMetadata = new PagedMetadata<>(requestList, paginatedResult);
        pagedMetadata.setBody(dtoSlice);
        return pagedMetadata;
    }




}
