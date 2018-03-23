package org.entando.entando.web.label;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LabelValidator implements Validator {

    public static final String ERRCODE_LABELGROUP_EXISTS = "1";

    public static final String ERRCODE_URINAME_MISMATCH = "2";

    public static final String ERRCODE_LABELGROUP_LANGS_DEFAULT_LANG_REQUIRED = "3";

    public static final String ERRCODE_LABELGROUP_LANGS_INVALID_LANG = "4";

    public static final String ERRCODE_LABELGROUP_LANGS_TEXT_REQURED = "4";

    @Override
    public boolean supports(Class<?> paramClass) {
        return LabelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateBodyName(String labelCode, LabelRequest labelRequest, Errors errors) {
        if (!StringUtils.equals(labelCode, labelRequest.getKey())) {
            errors.rejectValue("key", ERRCODE_URINAME_MISMATCH, new String[]{labelCode, labelRequest.getKey()}, "labelRequest.key.mismatch");
        }
    }
}
