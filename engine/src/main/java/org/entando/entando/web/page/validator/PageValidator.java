/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.page.validator;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.page.PageController;
import org.entando.entando.web.page.model.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author paddeo
 */
@Component
public class PageValidator implements Validator {

    @Autowired
    IPageManager pageManager;

    @Override
    public boolean supports(Class<?> paramClass) {

        return PageRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PageRequest request = (PageRequest) target;
        String pageCode = request.getCode();
        if (null != pageManager.getDraftPage(pageCode)) {
            errors.reject(PageController.ERRCODE_PAGE_ALREADY_EXISTS, new String[]{pageCode}, "page.exists");
        }
    }

    public void validateBodyCode(String pageCode, PageRequest pageRequest, Errors errors) {
        if (!StringUtils.equals(pageCode, pageRequest.getCode())) {
            errors.rejectValue("code", PageController.ERRCODE_URINAME_MISMATCH, new String[]{pageCode, pageRequest.getCode()}, "page.code.mismatch");
        }
    }

    public void validateOnlinePage(String pageCode, Errors errors) {
        if (null != pageManager.getOnlinePage(pageCode)) {
            errors.reject(PageController.ERRCODE_ONLINE_PAGE, new String[]{pageCode}, "page.delete.online");
        }
    }

    public void validateChildren(String pageCode, Errors errors) {
        IPage page = pageManager.getDraftPage(pageCode);
        if (page != null && page.getChildrenCodes() != null && page.getChildrenCodes().length > 0) {
            errors.reject(PageController.ERRCODE_PAGE_HAS_CHILDREN, new String[]{pageCode}, "page.delete.children");
        }
    }

}
