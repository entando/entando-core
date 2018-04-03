package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionary;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary.ContentModelDictionaryProvider;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.utils.ContentModelUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.page.model.PageDtoBuilder;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;
import org.entando.entando.web.plugins.jacms.contentmodel.validator.ContentModelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class ContentModelService implements IContentModelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IContentModelManager contentModelManager;
    private IDtoBuilder<ContentModel, ContentModelDto> dtoBuilder;
    private ContentModelDictionaryProvider dictionaryProvider;
    private IContentManager contentManager;
    private PageDtoBuilder pageDtoBuilder;

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

    protected ContentModelDictionaryProvider getDictionaryProvider() {
        return dictionaryProvider;
    }

    public void setDictionaryProvider(ContentModelDictionaryProvider dictionaryProvider) {
        this.dictionaryProvider = dictionaryProvider;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected PageDtoBuilder getPageDtoBuilder() {
        return pageDtoBuilder;
    }

    public void setPageDtoBuilder(PageDtoBuilder pageDtoBuilder) {
        this.pageDtoBuilder = pageDtoBuilder;
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

        Stream<ContentModel> stream = contentModels.stream();

        //filter
        List<Predicate<ContentModel>> filters = ContentModelUtils.getPredicates(requestList);
        for (Predicate<ContentModel> predicate : filters) {
            stream = stream.filter(predicate);
        }

        //sort
        Comparator<ContentModel> comparator = ContentModelUtils.getComparator(requestList.getSort(), requestList.getDirection());
        if (null != comparator) {
            stream = stream.sorted(comparator);
        }

        contentModels = stream.collect(Collectors.toList());

        //page
        List<ContentModel> subList = requestList.getSublist(contentModels);
        List<ContentModelDto> dtoSlice = this.getDtoBuilder().convert(subList);
        SearcherDaoPaginatedResult<ContentModelDto> paginatedResult = new SearcherDaoPaginatedResult(contentModels.size(), dtoSlice);
        PagedMetadata<ContentModelDto> pagedMetadata = new PagedMetadata<>(requestList, paginatedResult);
        pagedMetadata.setBody(dtoSlice);
        return pagedMetadata;
    }

    @Override
    public ContentModelDto getContentModel(Long modelId) {
        ContentModel contentModel = this.getContentModelManager().getContentModel(modelId);
        if (null == contentModel) {
            logger.warn("no contentModel found with id {}", modelId);
            throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
        }
        ContentModelDto dto = this.getDtoBuilder().convert(contentModel);
        return dto;
    }

    @Override
    public ContentModelDto addContentModel(ContentModelRequest contentModelReq) {
        try {
            ContentModel contentModel = this.createContentModel(contentModelReq);
            BeanPropertyBindingResult validationResult = this.validateForAdd(contentModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.getContentModelManager().addContentModel(contentModel);
            ContentModelDto dto = this.getDtoBuilder().convert(contentModel);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a content model", e);
            throw new RestServerError("error in add content model", e);
        }
    }

    @Override
    public ContentModelDto updateContentModel(ContentModelRequest contentModelRequest) {
        try {
            long modelId = contentModelRequest.getId();
            ContentModel contentModel = this.getContentModelManager().getContentModel(contentModelRequest.getId());
            if (null == contentModel) {
                logger.warn("no contentModel found with id {}", modelId);
                throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
            }

            BeanPropertyBindingResult validationResult = this.validateForUpdate(contentModelRequest, contentModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.copyProperties(contentModelRequest, contentModel);

            this.getContentModelManager().updateContentModel(contentModel);
            ContentModelDto dto = this.getDtoBuilder().convert(contentModel);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error updating a content model", e);
            throw new RestServerError("error in update content model", e);
        }
    }


    @Override
    public void removeContentModel(Long modelId) {
        try {
            ContentModel contentModel = this.getContentModelManager().getContentModel(modelId);
            if (null == contentModel) {
                logger.info("contentModel {} does not exists", modelId);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(contentModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.getContentModelManager().removeContentModel(contentModel);
        } catch (ApsSystemException e) {
            logger.error("Error in delete contentModel {}", modelId, e);
            throw new RestServerError("error in delete contentModel", e);
        }
    }

    @Override
    public Map<String, List<String>> getPageReferences(Long modelId) {
        ContentModel contentModel = this.getContentModelManager().getContentModel(modelId);
        if (null == contentModel) {
            logger.info("contentModel {} does not exists", modelId);
            throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_NOT_FOUND, "contentModel", String.valueOf(modelId));
        }
        Map<String, List<String>> referencingPages = this.getReferencingPages(modelId);
        return referencingPages;
    }

    private Map<String, List<String>> getReferencingPages(Long modelId) {
        Map<String, List<IPage>> refs = this.getContentModelManager().getReferencingPages(modelId);
        Map<String, List<String>> dtoReferences = new HashMap<>();
        if (null != refs && !refs.isEmpty()) {
            refs.entrySet().stream().forEach(e -> e.getValue().stream().forEach(v -> dtoReferences.put(e.getKey(), e.getValue().stream().map(ee -> ee.getCode()).collect(Collectors.toList()))));
        }
        return dtoReferences;
    }

    @Override
    public ContentModelDictionary getContentModelDictionary(String typeCode) {
        if (StringUtils.isBlank(typeCode)) {
            return this.getDictionaryProvider().buildDictionary();
        }
        IApsEntity prototype = this.getContentManager().getEntityPrototype(typeCode);
        if (null == prototype) {
            logger.warn("no contentModel found with id {}", typeCode);
            throw new RestRourceNotFoundException(ContentModelValidator.ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND, "contentType", typeCode);
        }
        return this.getDictionaryProvider().buildDictionary((Content) prototype);
    }

    protected ContentModel createContentModel(ContentModelRequest src) {
        ContentModel contentModel = new ContentModel();
        this.copyProperties(src, contentModel);
        return contentModel;
    }

    protected void copyProperties(ContentModelRequest src, ContentModel dest) {
        dest.setContentShape(src.getContentShape());
        dest.setContentType(src.getContentType());
        dest.setDescription(src.getDescr());
        dest.setId(src.getId());
        dest.setStylesheet(src.getStylesheet());
    }

    protected BeanPropertyBindingResult validateForAdd(ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        validateIdIsUnique(contentModel, errors);
        validateContentType(contentModel, errors);
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        Map<String, List<IPage>> referencingPages = this.getContentModelManager().getReferencingPages(contentModel.getId());
        if (!referencingPages.isEmpty()) {
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_REFERENCES, null, "contetntmodel.page.references");
        }
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(ContentModelRequest request, ContentModel contentModel) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(contentModel, "contentModel");
        this.validateContentTypeIsEquals(request.getContentType(), contentModel.getContentType(), errors);
        this.validateContentType(contentModel, errors);
        return errors;
    }

    protected void validateContentTypeIsEquals(String newContentType, String existingConentType, BeanPropertyBindingResult errors) {
        if (!newContentType.equals(existingConentType)) {
            Object[] args = {existingConentType, newContentType};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_CANNOT_UPDATE_CONTENT_TYPE, args, "contetntmodel.contentType.locked");
        }
    }

    protected void validateIdIsUnique(ContentModel contentModel, BeanPropertyBindingResult errors) {
        long modelId = contentModel.getId();

        ContentModel dummyModel = this.getContentModelManager().getContentModel(modelId);
        if (dummyModel != null) {
            Object[] args = {String.valueOf(modelId)};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_ALREADY_EXISTS, args, "contetntmodel.id.already.present");
        }
        SmallEntityType utilizer = this.getContentModelManager().getDefaultUtilizer(modelId);
        if (null != utilizer && !utilizer.getCode().equals(contentModel.getContentType())) {
            Object[] args = {String.valueOf(modelId), utilizer.getDescription()};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_WRONG_UTILIZER, args, "contentModel.id.wrongUtilizer");
        }
    }

    protected void validateContentType(ContentModel contentModel, BeanPropertyBindingResult errors) {
        String contentType = contentModel.getContentType();

        if (!this.getContentManager().getSmallContentTypesMap().containsKey(contentType)) {
            Object[] args = {contentType};
            errors.reject(ContentModelValidator.ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND, args, "contentModel.contentType.notFound");
        }

    }

}
