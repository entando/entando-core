package org.entando.entando.web.activitystream.valiator;

import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.role.model.RoleRequest;
import org.springframework.validation.Errors;

public class ActivityStreamValidator extends AbstractPaginationValidator {

    @Override
    public boolean supports(Class<?> paramClass) {
        return RoleRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

}
