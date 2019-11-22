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
        return validPageModelRequest(false);
    }

    public static PageModelRequest validPageModelRequest(boolean withMainFrame) {
        PageModelRequest request = new PageModelRequest();
        request.setCode(PAGE_MODEL_CODE);
        request.setDescr(DESCRIPTION);
        request.setConfiguration(createValidPageModelConfigurationRequest(withMainFrame));
        return request;
    }

    private static PageModelConfigurationRequest createValidPageModelConfigurationRequest() {
        return createValidPageModelConfigurationRequest(false);
    }

    private static PageModelConfigurationRequest createValidPageModelConfigurationRequest(boolean withMainFrame) {
        PageModelConfigurationRequest configuration = new PageModelConfigurationRequest();

        PageModelFrameReq frames;
        if (withMainFrame) {
            frames = createValidMainFrameRequest();
        } else {
            frames = createValidFrameRequest();
        }

        configuration.setFrames(singletonList(frames));
        return configuration;
    }

    private static PageModelFrameReq createValidFrameRequest() {
        return new PageModelFrameReq(0, FRAME_DESCRIPTION);
    }

    private static PageModelFrameReq createValidMainFrameRequest() {
        PageModelFrameReq req = new PageModelFrameReq(0, FRAME_DESCRIPTION);
        req.setMainFrame(true);
        return req;
    }
}
