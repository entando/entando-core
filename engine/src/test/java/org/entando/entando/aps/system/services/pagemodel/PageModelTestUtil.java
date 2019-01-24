package org.entando.entando.aps.system.services.pagemodel;

import org.entando.entando.web.pagemodel.model.*;

import static java.util.Collections.singletonList;

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
        configuration.setFrames(singletonList(createValidFrameRequest()));
        return configuration;
    }

    private static PageModelFrameReq createValidFrameRequest() {
        return new PageModelFrameReq(0, FRAME_DESCRIPTION);
    }
}
