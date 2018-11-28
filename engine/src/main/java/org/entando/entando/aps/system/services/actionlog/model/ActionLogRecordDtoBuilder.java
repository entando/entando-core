package org.entando.entando.aps.system.services.actionlog.model;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.activitystream.ISocialActivityStreamManager;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamLikeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActionLogRecordDtoBuilder extends DtoBuilder<ActionLogRecord, ActionLogRecordDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Deprecated
    @Override
    public ActionLogRecordDto convert(ActionLogRecord entity) {
        return super.convert(entity);
    }

    @Deprecated
    @Override
    public List<ActionLogRecordDto> convert(List<ActionLogRecord> list) {
        return super.convert(list);
    }

    @Override
    protected ActionLogRecordDto toDto(ActionLogRecord src) {
        ActionLogRecordDto dto = new ActionLogRecordDto(src);
        return dto;
    }

    public ActionLogRecordDto toDto(ActionLogRecord src, List<ActivityStreamLikeInfo> actionLikeRecords, List<ActivityStreamComment> actionCommentRecords) {
        ActionLogRecordDto dto = new ActionLogRecordDto(src, actionLikeRecords, actionCommentRecords);
        return dto;
    }

    public List<ActionLogRecordDto> convert(List<ActionLogRecord> list, ISocialActivityStreamManager socialActivityStreamManager) {
        List<ActionLogRecordDto> out = new ArrayList<>();
        list.stream().forEach(i -> {
            try {
                out.add(toDto(i, socialActivityStreamManager.getActionLikeRecords(i.getId()), socialActivityStreamManager.getActionCommentRecords(i.getId())));
            } catch (ApsSystemException e) {
                logger.error("error converting list ",e);
            }
        });

        return out;
    }

}
