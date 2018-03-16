package org.entando.entando.web.page.model;

import org.entando.entando.web.common.annotation.ValidateString;

/**
 *
 * @author paddeo
 */
public class PageStatusRequest {

    @ValidateString(acceptedValues = {"draft", "published"}, message = "{page.status.invalid}")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
