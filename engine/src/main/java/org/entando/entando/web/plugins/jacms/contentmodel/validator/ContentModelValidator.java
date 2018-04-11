package org.entando.entando.web.plugins.jacms.contentmodel.validator;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;
import org.springframework.validation.Errors;

public class ContentModelValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_CONTENTMODEL_NOT_FOUND = "1";
    public static final String ERRCODE_CONTENTMODEL_ALREADY_EXISTS = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";
    public static final String ERRCODE_CONTENTMODEL_CANNOT_UPDATE_CONTENT_TYPE = "4";
    public static final String ERRCODE_CONTENTMODEL_REFERENCES = "5";

    public static final String ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND = "6";
    public static final String ERRCODE_CONTENTMODEL_WRONG_UTILIZER = "7";

    @Override
    public boolean supports(Class<?> paramClass) {
        return ContentModelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateBodyName(long modelId, ContentModelRequest contentModelReq, Errors errors) {
        if (modelId != contentModelReq.getId().longValue()) {
            errors.rejectValue("name", ERRCODE_URINAME_MISMATCH, new Object[]{modelId, contentModelReq.getId()}, "contentmodel.code.mismatch");
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "id";
    }
}
