package org.entando.entando.web.language.validator;

import org.entando.entando.web.language.model.LanguageRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LanguageValidator implements Validator {

    public static final String ERRCODE_LANGUAGE_DOES_NOT_EXISTS = "1";
    public static final String ERRCODE_LANGUAGE_CANNOT_DISABLE_DEFAULT = "2";

    @Override
    public boolean supports(Class<?> paramClass) {
        return LanguageRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub

    }

}
