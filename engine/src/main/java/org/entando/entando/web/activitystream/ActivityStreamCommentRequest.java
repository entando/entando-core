package org.entando.entando.web.activitystream;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ActivityStreamCommentRequest {

    @NotNull(message = "activityStreamCommentRequest.recordId.required")
    private Integer recordId;

    @NotEmpty(message = "activityStreamCommentRequest.comment.required")
    private String comment;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
