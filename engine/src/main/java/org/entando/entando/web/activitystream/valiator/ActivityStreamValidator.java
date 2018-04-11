package org.entando.entando.web.activitystream.valiator;

import org.entando.entando.web.activitystream.ActivityStreamCommentRequest;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.validation.Errors;

public class ActivityStreamValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_RECORD_NOT_FOUND = "1";
    public static final String ERRCODE_URINAME_MISMATCH = "2";

    @Override
    public boolean supports(Class<?> paramClass) {
        return ActivityStreamCommentRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateBodyName(int recordId, ActivityStreamCommentRequest commentRequest, Errors errors) {
        if (recordId != commentRequest.getRecordId()) {
            errors.rejectValue("recordId", ERRCODE_URINAME_MISMATCH, new Object[]{recordId, commentRequest.getRecordId()}, "comment.recordId.mismatch");
        }
    }

}
