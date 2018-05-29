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

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.page.PageController;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 *
 * @author paddeo
 */
@Component
public class PageValidator extends AbstractPaginationValidator {

    private static final String ERRCODE_FRAMEID_INVALID = null;

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPageManager pageManager;

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
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
        if (pageRequest.getParentCode() == null || pageRequest.getPosition() <= 0
                || this.getPageManager().getDraftPage(pageRequest.getParentCode()) == null) {
            errors.reject(PageController.ERRCODE_CHANGE_POSITION_INVALID_REQUEST, new String[]{pageCode}, "page.move.position.invalid");
        }
    }

    public void validateGroups(String pageCode, PagePositionRequest pageRequest, Errors errors) {
        IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode());
        IPage page = this.getPageManager().getDraftPage(pageCode);
        if (!parent.getGroup().equals(Group.FREE_GROUP_NAME) 
                && (!page.getGroup().equals(Group.FREE_GROUP_NAME) && !page.getGroup().equals(parent.getGroup()))) {
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

}
