/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.*;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.exception.*;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.assembler.PagedMetadataMapper;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.pagemodel.model.*;
import org.entando.entando.web.pagemodel.validator.PageModelValidator;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.*;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;

@Service
public class PageModelService implements IPageModelService, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IPageModelManager pageModelManager;

    private final IWidgetTypeManager widgetTypeManager;

    private final IDtoBuilder<PageModel, PageModelDto> dtoBuilder;

    private ApplicationContext applicationContext;

    @Autowired
    private PagedMetadataMapper pagedMetadataMapper;

    @Autowired
    public PageModelService(IPageModelManager pageModelManager, 
            IWidgetTypeManager widgetTypeManager, IDtoBuilder<PageModel, PageModelDto> dtoBuilder) {
        this.pageModelManager = pageModelManager;
        this.widgetTypeManager = widgetTypeManager;
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
                dtoList = this.dtoBuilder.convert(pageModels.getList());
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
        PageModel pageModel = this.pageModelManager.getPageModel(code);
        if (null == pageModel) {
            logger.warn("no pageModel found with code {}", code);
            throw new ResourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", code);
        }
        PageModelDto dto = this.dtoBuilder.convert(pageModel);
        dto.setReferences(this.getReferencesInfo(pageModel));
        return dto;
    }

    @Override
    public PageModelDto addPageModel(PageModelRequest pageModelRequest) {
        try {
            BeanPropertyBindingResult validationResult = this.validateAdd(pageModelRequest);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            PageModel pageModel = this.createPageModel(pageModelRequest);
            this.pageModelManager.addPageModel(pageModel);
            return this.dtoBuilder.convert(pageModel);
        } catch (ValidationGenericException | ValidationConflictException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error in add pageModel", e);
            throw new RestServerError("error in add pageModel", e);
        }
    }

    @Override
    public PageModelDto updatePageModel(PageModelRequest pageModelRequest) {
        try {
            BeanPropertyBindingResult validationResult = this.validateEdit(pageModelRequest);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            PageModel pageModel = this.pageModelManager.getPageModel(pageModelRequest.getCode());
            this.copyProperties(pageModelRequest, pageModel);
            this.pageModelManager.updatePageModel(pageModel);
            return dtoBuilder.convert(pageModel);
        } catch (ValidationGenericException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error in update pageModel {}", pageModelRequest.getCode(), e);
            throw new RestServerError("error in update pageMdel", e);
        }
    }

    @Override
    public void removePageModel(String code) {
        try {
            PageModel pageModel = this.pageModelManager.getPageModel(code);
            if (null == pageModel) {
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateDelete(pageModel);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.pageModelManager.deletePageModel(code);
        } catch (ValidationGenericException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error in delete pagemodel {}", code, e);
            throw new RestServerError("error in delete pagemodel", e);
        }
    }

    @Override
    public PagedMetadata<?> getPageModelReferences(String pageModelCode, String managerName, RestListRequest restRequest) {
        PageModel pageModel = this.pageModelManager.getPageModel(pageModelCode);
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
    public Integer getComponentUsage(String pageModelCode) {
        RestListRequest request = new RestListRequest(1, 1);
        return getPageModelReferences(pageModelCode, "PageManager", request).getTotalItems();
    }

    @Override
    public PagedMetadata<ComponentUsageEntity> getComponentUsageDetails(String componentCode, RestListRequest restListRequest) {
        PagedMetadata<PageDto> pagedMetadata = (PagedMetadata<PageDto>) getPageModelReferences(componentCode, "PageManager", restListRequest);
        List<ComponentUsageEntity> componentUsageEntityList = pagedMetadata.getBody().stream()
                .map(pageDto -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, pageDto.getCode(), pageDto.getStatus()))
                .collect(Collectors.toList());
        return this.pagedMetadataMapper.getPagedResult(restListRequest, componentUsageEntityList);
    }

    protected PageModel createPageModel(PageModelRequest pageModelRequest) {
        PageModel pageModel = new PageModel();
        this.copyProperties(pageModelRequest, pageModel);
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
        frame.setDefaultWidget(this.createDefaultWidget(pageModelFrameReq.getDefaultWidget()));
        return frame;
    }

    private Widget createDefaultWidget(DefaultWidgetReq defaultWidgetReq) {
        if (null == defaultWidgetReq || null == defaultWidgetReq.getCode()) {
            return null;
        }
        Widget defaultWidget = new Widget();
        defaultWidget.setType(this.widgetTypeManager.getWidgetType(defaultWidgetReq.getCode()));
        if (null != defaultWidgetReq.getProperties()) {
            ApsProperties properties = new ApsProperties();
            properties.putAll(defaultWidgetReq.getProperties());
            defaultWidget.setConfig(properties);
        }
        return defaultWidget;
    }
    
    protected BeanPropertyBindingResult validateAdd(PageModelRequest pageModelRequest) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageModelRequest, "pageModel");
        PageModel pageModel = pageModelManager.getPageModel(pageModelRequest.getCode());
        if (null != pageModel) {
            bindingResult.reject(PageModelValidator.ERRCODE_CODE_EXISTS, new String[]{pageModelRequest.getCode()}, "pageModel.code.exists");
            throw new ValidationConflictException(bindingResult);
        }
        this.validateDefaultWidgets(pageModelRequest, bindingResult);
        return bindingResult;
    }

    protected BeanPropertyBindingResult validateEdit(PageModelRequest pageModelRequest) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageModelRequest, "pageModel");
        PageModel pageModel = this.pageModelManager.getPageModel(pageModelRequest.getCode());
        if (null == pageModel) {
            throw new ResourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", pageModelRequest.getCode());
        }
        this.validateDefaultWidgets(pageModelRequest, bindingResult);
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

    private void validateDefaultWidgets(PageModelRequest pageModelRequest, BeanPropertyBindingResult bindingResult) {
        if (null == pageModelRequest || null == pageModelRequest.getConfiguration().getFrames()) {
            return;
        }
        List<PageModelFrameReq> frames = pageModelRequest.getConfiguration().getFrames();
        for (int i = 0; i < frames.size(); i++) {
            PageModelFrameReq frameReq = frames.get(i);
            DefaultWidgetReq dwr = frameReq.getDefaultWidget();
            if (null == dwr || null == dwr.getCode()) {
                continue;
            }
            WidgetType type = this.widgetTypeManager.getWidgetType(dwr.getCode());
            if (null == type) {
                bindingResult.reject(PageModelValidator.ERRCODE_DEFAULT_WIDGET_NOT_EXISTS, new Object[]{dwr.getCode(), i}, "pageModel.defaultWidget.notExists");
            } else if (null != dwr.getProperties()) {
                Iterator<String> iter = dwr.getProperties().keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (!type.hasParameter(key)) {
                        bindingResult.reject(PageModelValidator.ERRCODE_DEFAULT_WIDGET_INVALID_PARAMETER, new Object[]{key, dwr.getCode(), i}, "pageModel.defaultWidget.invalidParameter");
                    }
                }
            }
        }
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
