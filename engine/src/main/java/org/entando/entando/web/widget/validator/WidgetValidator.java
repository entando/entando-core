package org.entando.entando.web.widget.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.web.group.GroupController;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class WidgetValidator implements Validator {


    public static final String ERRCODE_CANNOT_DELETE_USED_WIDGET = "1";
    public static final String ERRCODE_URINAME_MISMATCH = "2";


    @Override
    public boolean supports(Class<?> paramClass) {
        return WidgetRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validateWidgetCode(String widgetCode, WidgetRequest widgetRequest, Errors errors) {
        if (!StringUtils.equals(widgetCode, widgetRequest.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{widgetCode, widgetRequest.getCode()}, "widget.code.mismatch");
        }
    }
}
