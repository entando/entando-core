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
package org.entando.entando.web.page.validator;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.page.PageController;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author paddeo
 */
@Component
public class PageValidator extends AbstractPaginationValidator {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IPageManager pageManager;

    @Autowired
    PageUtilizer pageUtilizer;

    @Autowired
    IContentManager contentManager;

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    public PageUtilizer getPageUtilizer() {
        return pageUtilizer;
    }

    public void setPageUtilizer(PageUtilizer pageUtilizer) {
        this.pageUtilizer = pageUtilizer;
    }

    public IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    @Override
    public boolean supports(Class<?> paramClass) {

        return PageRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PageRequest request = (PageRequest) target;
        String pageCode = request.getCode();
        if (null != this.getPageManager().getDraftPage(pageCode)) {
            errors.reject(PageController.ERRCODE_PAGE_ALREADY_EXISTS, new String[]{pageCode}, "page.exists");
        }
    }

    public void validateBodyCode(String pageCode, PageRequest pageRequest, Errors errors) {
        if (!StringUtils.equals(pageCode, pageRequest.getCode())) {
            errors.rejectValue("code", PageController.ERRCODE_URINAME_MISMATCH, new String[]{pageCode, pageRequest.getCode()}, "page.code.mismatch");
        }
    }

    public void validateOnlinePage(String pageCode, Errors errors) {
        if (null != this.getPageManager().getOnlinePage(pageCode)) {
            errors.reject(PageController.ERRCODE_ONLINE_PAGE, new String[]{pageCode}, "page.delete.online");
        }
    }

    public void validateChildren(String pageCode, Errors errors) {
        IPage page = this.getPageManager().getDraftPage(pageCode);
        if (page != null && page.getChildrenCodes() != null && page.getChildrenCodes().length > 0) {
            errors.reject(PageController.ERRCODE_PAGE_HAS_CHILDREN, new String[]{pageCode}, "page.delete.children");
        }
    }

    public void validateChangePositionRequest(String pageCode, PagePositionRequest pageRequest, Errors errors) {
        if (!StringUtils.equals(pageCode, pageRequest.getCode())) {
            errors.rejectValue("code", PageController.ERRCODE_URINAME_MISMATCH, new String[]{pageCode, pageRequest.getCode()}, "page.code.mismatch");
        }
        IPage parent = null;
        if (pageRequest.getParentCode() == null || pageRequest.getPosition() <= 0
                || (parent = this.getPageManager().getDraftPage(pageRequest.getParentCode())) == null) {
            errors.reject(PageController.ERRCODE_CHANGE_POSITION_INVALID_REQUEST, new String[]{pageCode}, "page.move.position.invalid");
        }
    }

    public void validateGroups(String pageCode, PagePositionRequest pageRequest, Errors errors) {
        IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode()),
                page = this.getPageManager().getDraftPage(pageCode);
        if (!page.getGroup().equals(Group.FREE_GROUP_NAME)
                && !page.getGroup().equals(parent.getGroup())) {
            errors.reject(PageController.ERRCODE_GROUP_MISMATCH, new String[]{pageCode}, "page.move.group.mismatch");
        }
    }

    public void validatePagesStatus(String pageCode, PagePositionRequest pageRequest, Errors errors) {
        IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode()),
                page = this.getPageManager().getDraftPage(pageCode);
        if (page.isOnline() && !parent.isOnline()) {
            errors.reject(PageController.ERRCODE_STATUS_PAGE_MISMATCH, new String[]{pageCode}, "page.move.status.mismatch");
        }
    }

    public void validateReferences(String pageCode, PageStatusRequest pageStatusRequest, Errors errors) {
        List<Content> contents = new ArrayList<>();
        List<String> invalidRefs = new ArrayList<>();
        IPage page = this.getPageManager().getDraftPage(pageCode);

        try {
            List<String> contentIds = this.getPageUtilizer().getPageUtilizers(pageCode);
            Optional.ofNullable(contentIds).ifPresent(ids -> ids.forEach(id -> {
                try {
                    contents.add(this.getContentManager().loadContent(id, true));
                } catch (ApsSystemException e) {
                    logger.error("Error loadingreferences for page {}", pageCode, e);
                    throw new RestServerError("Error loadingreferences for page", e);
                }
            }));
        } catch (ApsSystemException e) {
            logger.error("Error loadingreferences for page {}", pageCode, e);
            throw new RestServerError("Error loadingreferences for page", e);
        }

        if (pageStatusRequest.getStatus().equals(IPageService.STATUS_ONLINE)) {
            contents.stream().forEach(content -> {
                if (!content.isOnLine()) {
                    invalidRefs.add(content.getId());
                }
            });
            IPage parent = null;
            if (invalidRefs.isEmpty()) {
                parent = this.getPageManager().getDraftPage(page.getParentCode());
            }
            if (!invalidRefs.isEmpty() || !parent.isOnline()) {
                errors.reject(PageController.ERRCODE_REFERENCED_DRAFT_PAGE, new String[]{pageCode}, "page.status.invalid.draft.ref");
            }
        } else {
            boolean isRoot = this.getPageManager().getOnlineRoot().getCode().equals(pageCode);
            if (contents.isEmpty() && !isRoot) {
                Optional.ofNullable(page.getChildrenCodes()).ifPresent(children -> Arrays.asList(children).forEach(child -> {
                    IPage childPage = this.getPageManager().getDraftPage(child);
                    if (childPage.isOnline()) {
                        invalidRefs.add(child);
                    }
                }));
            }
            if (isRoot || !contents.isEmpty() || !invalidRefs.isEmpty()) {
                errors.reject(PageController.ERRCODE_REFERENCED_ONLINE_PAGE, new String[]{pageCode}, "page.status.invalid.online.ref");
            }
        }
    }

}
