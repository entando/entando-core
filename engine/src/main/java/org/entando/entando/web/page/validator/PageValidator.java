/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.page.validator;

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
        String groupName = request.getName();
        if (null != groupManager.getPage(groupName)) {
            errors.reject(PageController.ERRCODE_GROUP_ALREADY_EXISTS, new String[]{groupName}, "group.exists");
        }
    }

    public void validateBodyName(String groupName, PageRequest groupRequest, Errors errors) {
        if (!StringUtils.equals(groupName, groupRequest.getName())) {
            errors.rejectValue("name", PageController.ERRCODE_URINAME_MISMATCH, new String[]{groupName, groupRequest.getName()}, "group.name.mismatch");
        }
    }

}
