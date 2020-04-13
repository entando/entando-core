package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.*;
import org.entando.entando.aps.system.exception.*;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.pagemodel.model.*;
import org.entando.entando.web.pagemodel.validator.PageModelValidator;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.*;

@Service
public class PageModelService implements IPageModelService, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IPageModelManager pageModelManager;

    private final IDtoBuilder<PageModel, PageModelDto> dtoBuilder;

    private ApplicationContext applicationContext;

    @Autowired
    public PageModelService(IPageModelManager pageModelManager, IDtoBuilder<PageModel, PageModelDto> dtoBuilder) {
        this.pageModelManager = pageModelManager;
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public PagedMetadata<PageModelDto> getPageModels(RestListRequest restListReq, Map<String, String> requestParams) {
        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            List<FieldSearchFilter> filters = new ArrayList<>(restListReq.buildFieldSearchFilters());
            filters.stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(PageModelDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<PageModel> pageModels = pageModelManager.searchPageModels(filters);

            List<PageModelDto> dtoList = null;
            if (null != pageModels) {
                dtoList = dtoBuilder.convert(pageModels.getList());
            }

            PagedMetadata<PageModelDto> pagedMetadata = new PagedMetadata<>(restListReq, pageModels);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search pageModels", t);
            throw new RestServerError("error in search pageModels", t);
        }
    }

    @Override
    public PageModelDto getPageModel(String code) {
        PageModel pageModel = pageModelManager.getPageModel(code);
        if (null == pageModel) {
            logger.warn("no pageModel found with code {}", code);
            throw new ResourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", code);
        }
        PageModelDto dto = dtoBuilder.convert(pageModel);
        dto.setReferences(this.getReferencesInfo(pageModel));
        return dto;
    }

    @Override
    public PageModelDto addPageModel(PageModelRequest pageModelRequest) {
        try {
            BeanPropertyBindingResult validationResult = this.validateAdd(pageModelRequest);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            PageModel pageModel = this.createPageModel(pageModelRequest);
            pageModelManager.addPageModel(pageModel);
            return dtoBuilder.convert(pageModel);
        } catch (ApsSystemException e) {
            logger.error("Error in add pageModel", e);
            throw new RestServerError("error in add pageModel", e);
        }
    }

    @Override
    public PageModelDto updatePageModel(PageModelRequest pageModelRequest) {
        try {
            PageModel pageModel = pageModelManager.getPageModel(pageModelRequest.getCode());
            if (null == pageModel) {
                throw new ResourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", pageModelRequest.getCode());
            }
            this.copyProperties(pageModelRequest, pageModel);
            pageModelManager.updatePageModel(pageModel);
            return dtoBuilder.convert(pageModel);
        } catch (ApsSystemException e) {
            logger.error("Error in update pageModel {}", pageModelRequest.getCode(), e);
            throw new RestServerError("error in update pageMdel", e);
        }
    }

    @Override
    public void removePageModel(String code) {
        try {
            PageModel pageModel = pageModelManager.getPageModel(code);
            if (null == pageModel) {
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateDelete(pageModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            pageModelManager.deletePageModel(code);

        } catch (ApsSystemException e) {
            logger.error("Error in delete pagemodel {}", code, e);
            throw new RestServerError("error in delete pagemodel", e);
        }
    }

    @Override
    public PagedMetadata<?> getPageModelReferences(String pageModelCode, String managerName, RestListRequest restRequest) {
        PageModel pageModel = pageModelManager.getPageModel(pageModelCode);
        if (null == pageModel) {
            logger.warn("no pageModel found with code {}", pageModelCode);
            throw new ResourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", pageModelCode);
        }
        PageModelServiceUtilizer<?> utilizer = this.getPageModelServiceUtilizer(managerName);
        if (null == utilizer) {
            logger.warn("no references found for {}", managerName);

            throw new ResourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_REFERENCES, "reference", managerName);
        }
        List<?> dtoList = utilizer.getPageModelUtilizer(pageModelCode);
        List<?> subList = restRequest.getSublist(dtoList);
        SearcherDaoPaginatedResult<?> pagedResult = new SearcherDaoPaginatedResult<>(dtoList.size(), subList);
        PagedMetadata<Object> pagedMetadata = new PagedMetadata<>(restRequest, pagedResult);
        pagedMetadata.setBody((List<Object>) subList);
        return pagedMetadata;
    }

    @Override
    public Integer getPageModelUsage(String pageModelCode) {
        RestListRequest request = new RestListRequest(1, 1);
        return getPageModelReferences(pageModelCode, "PageManager", request).getTotalItems();
    }

    protected PageModel createPageModel(PageModelRequest pageModelRequest) {
        PageModel pageModel = new PageModel();
        copyProperties(pageModelRequest, pageModel);
        return pageModel;
    }

    protected void copyProperties(PageModelRequest srcPpageModelRequest, PageModel descPageModel) {
        descPageModel.setCode(srcPpageModelRequest.getCode());
        descPageModel.setDescription(srcPpageModelRequest.getDescr());
        descPageModel.setTemplate(srcPpageModelRequest.getTemplate());
        descPageModel.setPluginCode(srcPpageModelRequest.getPluginCode());
        descPageModel.setConfiguration(this.createPageModelConfiguration(srcPpageModelRequest));
    }


    protected Frame[] createPageModelConfiguration(PageModelRequest pageModelRequest) {
        Frame[] destConfiguration = null;
        List<PageModelFrameReq> frameRequestList = pageModelRequest.getConfiguration().getFrames();
        if (null == frameRequestList || frameRequestList.isEmpty()) {
            return destConfiguration;
        }

        return frameRequestList.stream()
                               .map(this::createFrame)
                               .toArray(Frame[]::new);
    }

    protected Frame createFrame(PageModelFrameReq pageModelFrameReq) {
        Frame frame = new Frame();
        frame.setPos(pageModelFrameReq.getPos());
        frame.setDescription(pageModelFrameReq.getDescr());
        frame.setMainFrame(pageModelFrameReq.isMainFrame());
        frame.setSketch(pageModelFrameReq.getSketch());
        return frame;
    }

    protected BeanPropertyBindingResult validateAdd(PageModelRequest pageModelRequest) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageModelRequest, "pageModel");
        PageModel pageModel = pageModelManager.getPageModel(pageModelRequest.getCode());
        if (null == pageModel) {
            return bindingResult;
        }
        bindingResult.reject(PageModelValidator.ERRCODE_CODE_EXISTS, new String[]{pageModelRequest.getCode()}, "pageModel.code.exists");
        return bindingResult;
    }

    protected BeanPropertyBindingResult validateDelete(PageModel pageModel) throws ApsSystemException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageModel, "pageModel");

        Map<String, List<Object>> references = this.getReferencingObjects(pageModel);
        if (references.size() > 0) {
            bindingResult.reject(PageModelValidator.ERRCODE_PAGEMODEL_REFERENCES, new Object[]{pageModel.getCode(), references}, "pageModel.cannot.delete.references");
        }
        return bindingResult;

    }

    @SuppressWarnings("unchecked")
    public Map<String, List<Object>> getReferencingObjects(PageModel pageModel) throws ApsSystemException {
        Map<String, List<Object>> references = new HashMap<>();
        try {
            String[] defNames = applicationContext.getBeanNamesForType(PageModelUtilizer.class);

            for (String beanName : defNames) {
                Object service = null;
                try {
                    service = applicationContext.getBean(beanName);
                } catch (Throwable t) {
                    logger.error("error in hasReferencingObjects", t);
                    service = null;
                }
                if (service != null) {
                    PageModelUtilizer pageModelUtilizer = (PageModelUtilizer) service;
                    List<Object> utilizers = pageModelUtilizer.getPageModelUtilizers(pageModel.getCode());
                    if (utilizers != null && !utilizers.isEmpty()) {
                        references.put(pageModelUtilizer.getName() + "Utilizers", utilizers);
                    }
                }
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error on getReferencingObjects methods", t);
        }
        return references;
    }

    public Map<String, Boolean> getReferencesInfo(PageModel pageModel) {
        Map<String, Boolean> references = new HashMap<>();
        try {
            String[] defNames = applicationContext.getBeanNamesForType(PageModelUtilizer.class);
            for (String defName : defNames) {
                Object service = null;
                try {
                    service = applicationContext.getBean(defName);
                } catch (Throwable t) {
                    logger.error("error in hasReferencingObjects", t);
                    service = null;
                }
                if (service != null) {
                    PageModelUtilizer pageModelUtilizer = (PageModelUtilizer) service;
                    List<?> utilizers = pageModelUtilizer.getPageModelUtilizers(pageModel.getCode());
                    if (utilizers != null && !utilizers.isEmpty()) {
                        references.put(pageModelUtilizer.getName(), true);
                    } else {
                        references.put(pageModelUtilizer.getName(), false);
                    }
                }
            }
        } catch (ApsSystemException ex) {
            logger.error("error loading references for pageModel {}", pageModel.getCode(), ex);
            throw new RestServerError("error in getReferencingObjects ", ex);
        }
        return references;
    }

    @SuppressWarnings("rawtypes")
    private PageModelServiceUtilizer<?> getPageModelServiceUtilizer(String managerName) {
        Map<String, PageModelServiceUtilizer> beans = applicationContext.getBeansOfType(PageModelServiceUtilizer.class);
        Optional<PageModelServiceUtilizer> defName = beans.values().stream()
                                                          .filter(service -> service.getManagerName().equals(managerName))
                                                          .findFirst();
        return defName.orElse(null);
    }
}
