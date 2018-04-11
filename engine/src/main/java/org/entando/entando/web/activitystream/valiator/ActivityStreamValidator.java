package org.entando.entando.web.activitystream.valiator;

import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.role.model.RoleRequest;
import org.springframework.validation.Errors;

public class ActivityStreamValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_RECORD_NOT_FOUND = "1";

    @Override
    public boolean supports(Class<?> paramClass) {
        return RoleRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

}
