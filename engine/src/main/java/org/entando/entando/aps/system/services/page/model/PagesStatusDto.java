/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.PagesStatus;
import java.text.SimpleDateFormat;

/**
 *
 * @author paddeo
 */
public class PagesStatusDto {

    private int published;
    private int unpublished;
    private int draft;
    private String lastUpdate;

    public PagesStatusDto() {
    }

    public PagesStatusDto(PagesStatus pagesStatus) {
        this.published = pagesStatus.getOnline();
        this.unpublished = pagesStatus.getUnpublished();
        this.draft = pagesStatus.getOnlineWithChanges();
        if (null == pagesStatus.getLastUpdate()) {
            this.lastUpdate = "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.API_DATE_FORMAT);
            this.lastUpdate = sdf.format(pagesStatus.getLastUpdate());
        }
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public int getUnpublished() {
        return unpublished;
    }

    public void setUnpublished(int unpublished) {
        this.unpublished = unpublished;
    }

    public int getDraft() {
        return draft;
    }

    public void setDraft(int draft) {
        this.draft = draft;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
