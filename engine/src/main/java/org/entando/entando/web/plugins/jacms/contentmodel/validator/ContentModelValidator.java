package org.entando.entando.web.plugins.jacms.contentmodel.validator;

import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;
import org.springframework.validation.Errors;

public class ContentModelValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_CONTENTMODEL_NOT_FOUND = "1";
    //    public static final String ERRCODE_ROLE_ALREADY_EXISTS = "2";
    //    public static final String ERRCODE_URINAME_MISMATCH = "3";
    //    public static final String ERRCODE_PERMISSON_NOT_FOUND = "4";
    //    public static final String ERRCODE_ROLE_REFERENCES = "5";



    @Override
    public boolean supports(Class<?> paramClass) {
        return ContentModelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    //    public void validateBodyName(String roleCode, RoleRequest roleRequest, Errors errors) {
    //        if (!StringUtils.equals(roleCode, roleRequest.getCode())) {
    //            errors.rejectValue("name", ERRCODE_URINAME_MISMATCH, new String[]{roleCode, roleRequest.getName()}, "role.code.mismatch");
    //        }
    //    }
}
