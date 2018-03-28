/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetProcessorFactory;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetValidatorFactory;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 *
 * @author paddeo
 */
public class PageService implements IPageService, GroupServiceUtilizer<PageDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ERRCODE_PAGE_NOT_FOUND = "1";
    private static final String ERRCODE_RESOURCE_NOT_FOUND = "2";
    private static final String ERRCODE_PAGE_ONLY_DRAFT = "2";
    private static final String ERRCODE_FRAME_INVALID = "3";
    private static final String ERRCODE_WIDGET_INVALID = "4";

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IGroupManager groupManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private WidgetValidatorFactory widgetValidatorFactory;

    @Autowired
    private WidgetProcessorFactory widgetProcessorFactory;

    @Autowired
    private IDtoBuilder<IPage, PageDto> dtoBuilder;

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IPageModelManager getPageModelManager() {
        return pageModelManager;
    }

    public void setPageModelManager(IPageModelManager pageModelManager) {
        this.pageModelManager = pageModelManager;
    }

    public IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    protected IDtoBuilder<IPage, PageDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IPage, PageDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected WidgetValidatorFactory getWidgetValidatorFactory() {
        return widgetValidatorFactory;
    }

    public void setWidgetValidatorFactory(WidgetValidatorFactory widgetValidatorFactory) {
        this.widgetValidatorFactory = widgetValidatorFactory;
    }

    protected WidgetProcessorFactory getWidgetProcessorFactory() {
        return widgetProcessorFactory;
    }

    public void setWidgetProcessorFactory(WidgetProcessorFactory widgetProcessorFactory) {
        this.widgetProcessorFactory = widgetProcessorFactory;
    }

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    @Override
    public List<PageDto> getPages(String parentCode) {
        List<PageDto> res = new ArrayList<>();
        IPage parent = this.getPageManager().getDraftPage(parentCode);
        Optional.ofNullable(parent).ifPresent(root -> Optional.ofNullable(parent.getChildrenCodes()).ifPresent(children -> Arrays.asList(children).forEach(childCode -> {
            IPage childO = this.getPageManager().getOnlinePage(childCode),
                    childD = this.getPageManager().getDraftPage(childCode);
            PageDto child = null;
            if (childO != null) {
                child = dtoBuilder.convert(childO);
                if (childO.isChanged()) {
                    child.setStatus(STATUS_DRAFT);
                }
            } else {
                child = dtoBuilder.convert(childD);
                child.setStatus(STATUS_UNPUBLISHED);
            }
            child.setChildren(Arrays.asList(childD.getChildrenCodes()));
            res.add(child);
        })));
        return res;
    }

    @Override
    public PageDto getPage(String pageCode, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            logger.warn("no page found with code {}", pageCode);
            throw new RestRourceNotFoundException(null, "page", pageCode);
        }
        return this.getDtoBuilder().convert(page);
    }

    @Override
    public PageDto addPage(PageRequest pageRequest) {
        this.validateRequest(pageRequest);
        try {
            IPage page = this.createPage(pageRequest);
            this.getPageManager().addPage(page);
            return this.getDtoBuilder().convert(page);
        } catch (ApsSystemException e) {
            logger.error("Error adding page", e);
            throw new RestServerError("error add page", e);
        }
    }

    @Override
    public void removePage(String pageCode) {
        try {
            IPage page = this.getPageManager().getDraftPage(pageCode);
            if (null != page) {
                this.getPageManager().deletePage(pageCode);
            }
        } catch (ApsSystemException e) {
            logger.error("Error in delete page {}", pageCode, e);
            throw new RestServerError("error in delete page", e);
        }
    }

    @Override
    public PageDto updatePage(String pageCode, PageRequest pageRequest) {
        IPage oldPage = this.getPageManager().getDraftPage(pageCode);
        if (null == oldPage) {
            throw new RestRourceNotFoundException(null, "page", pageCode);
        }
        this.validateRequest(pageRequest);
        try {
            IPage newPage = this.updatePage(oldPage, pageRequest);
            this.getPageManager().updatePage(newPage);
            return this.getDtoBuilder().convert(newPage);
        } catch (ApsSystemException e) {
            logger.error("Error updating page {}", pageCode, e);
            throw new RestServerError("error in update page", e);
        }
    }

    @Override
    public PageDto updatePageStatus(String pageCode, String status) {
        IPage newPage = null;
        try {
            if (status != null && status.equals(STATUS_ONLINE)) {
                this.getPageManager().setPageOnline(pageCode);
                newPage = this.getPageManager().getOnlinePage(pageCode);
            } else if (status != null && status.equals(STATUS_DRAFT)) {
                this.getPageManager().setPageOffline(pageCode);
                newPage = this.getPageManager().getDraftPage(pageCode);
            }
            return this.getDtoBuilder().convert(newPage);
        } catch (ApsSystemException e) {
            logger.error("Error updating page {} status", pageCode, e);
            throw new RestServerError("error in update page status", e);
        }
    }

    @Override
    public PageDto movePage(String pageCode, PagePositionRequest pageRequest) {
        IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode()),
                page = this.getPageManager().getDraftPage(pageCode);
        boolean moved = true;
        int iterations = Math.abs(page.getPosition() - pageRequest.getPosition());
        boolean moveUp = page.getPosition() > pageRequest.getPosition();
        try {
            if (page.getParentCode().equals(parent.getCode())) {
                while (iterations-- > 0 && (moved = this.getPageManager().movePage(pageCode, moveUp)));
            } else {
                moved = this.getPageManager().movePage(page, parent);
            }
            page = this.getPageManager().getDraftPage(pageCode);
        } catch (ApsSystemException e) {
            logger.error("Error moving page {}", pageCode, e);
            throw new RestServerError("error in moving page", e);
        }
        return this.getDtoBuilder().convert(page);
    }

    @Override
    public PageConfigurationDto getPageConfiguration(String pageCode, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        PageConfigurationDto pageConfigurationDto = new PageConfigurationDto(page, status);
        return pageConfigurationDto;
    }

    @Override
    public WidgetConfigurationDto getWidgetConfiguration(String pageCode, int frameId, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        if (frameId > page.getWidgets().length) {
            throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
        }
        Widget widget = page.getWidgets()[frameId];
        if (null == widget) {
            return null;
        }
        return new WidgetConfigurationDto(widget);
    }

    @Override
    public WidgetConfigurationDto updateWidgetConfiguration(String pageCode, int frameId, WidgetConfigurationRequest widgetReq) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            if (frameId > page.getWidgets().length) {
                throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
            }
            if (null == this.getWidgetType(widgetReq.getCode())) {
                throw new RestRourceNotFoundException(ERRCODE_WIDGET_INVALID, "widget", String.valueOf(widgetReq.getCode()));
            }

            BeanPropertyBindingResult validation = this.getWidgetValidatorFactory().get(widgetReq.getCode()).validate(widgetReq, page);
            if (null != validation && validation.hasErrors()) {
                throw new ValidationConflictException(validation);
            }

            ApsProperties properties = (ApsProperties) this.getWidgetProcessorFactory().get(widgetReq.getCode()).buildConfiguration(widgetReq);

            WidgetType widgetType = this.getWidgetType(widgetReq.getCode());
            Widget widget = new Widget();
            widget.setType(widgetType);
            widget.setConfig(properties);
            this.getPageManager().joinWidget(pageCode, widget, frameId);

            ApsProperties outProperties = this.getWidgetProcessorFactory().get(widgetReq.getCode()).extractConfiguration(widget.getConfig());
            return new WidgetConfigurationDto(widget.getType().getCode(), outProperties);
        } catch (ApsSystemException e) {
            logger.error("Error in update widget configuration {}", pageCode, e);
            throw new RestServerError("error in update widget configuration", e);
        }
    }

    @Override
    public void deleteWidgetConfiguration(String pageCode, int frameId) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            if (frameId > page.getWidgets().length) {
                throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
            }
            this.pageManager.removeWidget(pageCode, frameId);
        } catch (ApsSystemException e) {
            logger.error("Error in delete widget configuration for page {} and frame {}", pageCode, frameId, e);
            throw new RestServerError("error in delete widget configuration", e);
        }
    }

    public WidgetType getWidgetType(String typeCode) {
        return this.getWidgetTypeManager().getWidgetType(typeCode);
    }

    private IPage createPage(PageRequest pageRequest) {
        Page page = new Page();
        page.setCode(pageRequest.getCode());
        page.setShowable(pageRequest.isDisplayedInMenu());
        PageModel model = this.getPageModelManager().getPageModel(pageRequest.getPageModel());
        page.setModel(model);
        page.setCharset(pageRequest.getCharset());
        page.setMimeType(pageRequest.getContentType());
        page.setParentCode(pageRequest.getParentCode());
        page.setUseExtraTitles(pageRequest.isSeo());
        Optional<Map<String, String>> titles = Optional.ofNullable(pageRequest.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.keySet().forEach((lang) -> {
            apsTitles.put(lang, values.get(lang));
        }));
        page.setTitles(apsTitles);
        page.setGroup(pageRequest.getOwnerGroup());
        Optional<List<String>> groups = Optional.ofNullable(pageRequest.getJoinGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            page.addExtraGroup(group);
        }));
        page.setParentCode(pageRequest.getParentCode());
        if (pageRequest.getParentCode() != null) {
            IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode());
            page.setParent(parent);
        }
        PageMetadata metadata = new PageMetadata();
        this.valueMetadataFromRequest(metadata, pageRequest);
        page.setMetadata(metadata);
        return page;
    }

    private IPage updatePage(IPage oldPage, PageRequest pageRequest) {
        Page page = new Page();
        page.setCode(pageRequest.getCode());
        page.setShowable(pageRequest.isDisplayedInMenu());
        if (oldPage.getModel().getCode().equals(pageRequest.getPageModel())) {
            PageModel model = this.getPageModelManager().getPageModel(pageRequest.getPageModel());
            page.setModel(model);
        }
        page.setCharset(pageRequest.getCharset());
        page.setMimeType(pageRequest.getContentType());
        page.setParentCode(pageRequest.getParentCode());
        page.setUseExtraTitles(pageRequest.isSeo());
        Optional<Map<String, String>> titles = Optional.ofNullable(pageRequest.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.keySet().forEach((lang) -> {
            apsTitles.put(lang, values.get(lang));
        }));
        page.setTitles(apsTitles);
        page.setGroup(pageRequest.getOwnerGroup());
        Optional<List<String>> groups = Optional.ofNullable(pageRequest.getJoinGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            page.addExtraGroup(group);
        }));
        page.setParentCode(pageRequest.getParentCode());
        PageMetadata metadata = oldPage.getMetadata();
        if (metadata == null) {
            metadata = new PageMetadata();
        }
        this.valueMetadataFromRequest(metadata, pageRequest);
        page.setMetadata(metadata);
        return page;
    }

    private void valueMetadataFromRequest(PageMetadata metadata, PageRequest request) {
        if (metadata.getModel() == null || !metadata.getModel().getCode().equals(request.getPageModel())) {
            // Ho cambiato modello e allora cancello tutte le showlets
            // Precedenti
            PageModel model = this.getPageModelManager().getPageModel(request.getPageModel());
            metadata.setModel(model);
        }
        metadata.setShowable(request.isDisplayedInMenu());
        metadata.setUseExtraTitles(request.isSeo());
        Optional<Map<String, String>> titles = Optional.ofNullable(request.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.keySet().forEach((lang) -> {
            apsTitles.put(lang, values.get(lang));
        }));
        metadata.setTitles(apsTitles);
        Optional<List<String>> groups = Optional.ofNullable(request.getJoinGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            metadata.addExtraGroup(group);
        }));
        String charset = request.getCharset();
        metadata.setCharset(StringUtils.isNotBlank(charset) ? charset : null);

        String mimetype = request.getContentType();
        metadata.setMimeType(StringUtils.isNotBlank(mimetype) ? mimetype : null);
    }

    private IPage loadPage(String pageCode, String status) {
        IPage page = null;
        switch (status) {
            case STATUS_DRAFT:
                page = this.getPageManager().getDraftPage(pageCode);
                break;
            case STATUS_ONLINE:
                page = this.getPageManager().getOnlinePage(pageCode);
                break;
            default:
                break;
        }
        if (status.equals(STATUS_ONLINE) && null == page && null != this.getPageManager().getDraftPage(pageCode)) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(page, "page");
            bindingResult.reject(ERRCODE_PAGE_ONLY_DRAFT, new Object[]{pageCode}, "page.status.draftOnly");
            throw new ValidationGenericException(bindingResult);
        }
        return page;
    }

    private void validateRequest(PageRequest request) {
        if (this.getPageModelManager().getPageModel(request.getPageModel()) == null) {
            throw new RestRourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "pageModel", request.getPageModel());
        }
        if (this.getPageManager().getDraftPage(request.getParentCode()) == null) {
            throw new RestRourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "parent", request.getParentCode());
        }
        if (this.getGroupManager().getGroup(request.getOwnerGroup()) == null) {
            throw new RestRourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "group", request.getOwnerGroup());
        }
        Optional.ofNullable(request.getJoinGroups()).ifPresent(groups -> groups.forEach(group -> {
            if (this.getGroupManager().getGroup(group) == null) {
                throw new RestRourceNotFoundException(ERRCODE_RESOURCE_NOT_FOUND, "joingroup", group);
            }
        }));
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getPageManager()).getName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PageDto> getGroupUtilizer(String groupName) {
        try {
            List<IPage> pages = ((GroupUtilizer<IPage>) this.getPageManager()).getGroupUtilizers(groupName);
            return this.getDtoBuilder().convert(pages);
        } catch (ApsSystemException ex) {
            logger.error("Error loading page references for group {}", groupName, ex);
            throw new RestServerError("Error loading page references for group", ex);
        }
    }
}
