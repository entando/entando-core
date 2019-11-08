package org.entando.entando.aps.system.services.pagemodel.model;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.*;
import org.entando.entando.aps.system.services.DtoBuilder;

import java.util.*;

public class PageModelDtoBuilder extends DtoBuilder<PageModel, PageModelDto> {

    @Override
    protected PageModelDto toDto(PageModel src) {
        if (null == src) {
            return null;
        }
        PageModelDto dest = new PageModelDto();

        dest.setCode(src.getCode());
        dest.setDescr(src.getDescription());
        dest.getConfiguration().setFrames(this.convertFrames(src.getFramesConfig()));

        dest.setMainFrame(src.getMainFrame());
        dest.setPluginCode(src.getPluginCode());
        dest.setTemplate(src.getTemplate());

        return dest;
    }

    private List<FrameDto> convertFrames(Frame[] framesConfig) {
        if (null == framesConfig) {
            return null;
        }
        final List<FrameDto> framesDto = new ArrayList<>();
        Arrays.stream(framesConfig).forEach(i -> framesDto.add(convertFrame(i)));

        return framesDto;
    }

    private FrameDto convertFrame(Frame frame) {
        if (null == frame) {
            return null;
        }
        FrameDto frameDto = new FrameDto();
        frameDto.setDefaultWidget(this.convertDefaultWidget(frame.getDefaultWidget()));
        frameDto.setDescr(frame.getDescription());
        frameDto.setMainFrame(frame.isMainFrame());
        frameDto.setPos(frame.getPos());
        frameDto.setSketch(frame.getSketch());
        return frameDto;
    }

    private DefaultWidgetDto convertDefaultWidget(Widget defaultWidget) {
        if (defaultWidget == null) {
            return null;
        }
        DefaultWidgetDto defaultWidgetDto = new DefaultWidgetDto();
        defaultWidgetDto.setCode(defaultWidget.getType().getCode());
        defaultWidgetDto.setProperties(defaultWidget.getConfig()); 
        return defaultWidgetDto;
    }

}
