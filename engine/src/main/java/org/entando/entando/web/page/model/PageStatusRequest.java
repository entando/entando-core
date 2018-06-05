/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.page.model;

import org.entando.entando.web.common.annotation.ValidateString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author paddeo
 */
public class PageStatusRequest {
    
    @NotEmpty(message = "page.status.invalid")
    @ValidateString(acceptedValues = {"draft", "published"}, message = "page.status.invalid")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PageStatusRequest{" + "status=" + status + '}';
    }

}
