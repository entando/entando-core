/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.pagemodel;

import java.util.ArrayList;
import java.util.List;
import org.entando.entando.web.pagemodel.model.PageModelConfigurationRequest;
import org.entando.entando.web.pagemodel.model.PageModelFrameReq;
import org.entando.entando.web.pagemodel.model.PageModelRequest;

public final class PageModelTestUtil {

    private static final String PAGE_MODEL_CODE = "pageModelCode";
    private static final String DESCRIPTION = "description";
    private static final String FRAME_DESCRIPTION = "frame description";

    private PageModelTestUtil() {
        // No instance - utility class
    }

    public static PageModelRequest validPageModelRequest() {
        PageModelRequest request = new PageModelRequest();
        request.setCode(PAGE_MODEL_CODE);
        request.setDescr(DESCRIPTION);
        request.setConfiguration(createValidPageModelConfigurationRequest());
        return request;
    }

    private static PageModelConfigurationRequest createValidPageModelConfigurationRequest() {
        PageModelConfigurationRequest configuration = new PageModelConfigurationRequest();
        List<PageModelFrameReq> frames = new ArrayList<>();
        frames.add(createValidFrameRequest());
        frames.add(new PageModelFrameReq(1, "Position 1"));
        configuration.setFrames(frames);
        return configuration;
    }

    private static PageModelFrameReq createValidFrameRequest() {
        PageModelFrameReq pageReq = new PageModelFrameReq(0, FRAME_DESCRIPTION);
        pageReq.getDefaultWidget().setCode("leftmenu");
        pageReq.getDefaultWidget().getProperties().put("navSpec", "code(homepage).subtree(5)");
        return pageReq;
    }
    
}
