package org.entando.entando.aps.system.services.activitystream;

import java.util.List;
import java.util.stream.Collectors;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDto;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDtoBuilder;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;
import org.entando.entando.web.activitystream.ActivityStreamCommentRequest;
import org.entando.entando.web.activitystream.valiator.ActivityStreamValidator;
import org.entando.entando.web.common.model.DateRange;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityStreamService implements IActivityStreamService {

    private static final String KEY_FILTER_CREATION = "createdAt";
    private static final String KEY_FILTER_UPDATE = "updatedAt";
    private static final String KEY_FILTER_USERNAME = "username";
    private static final String KEY_FILTER_NS = "namespace";
    private static final String KEY_FILTER_ACTIONNAME = "actionname";
    private static final String KEY_FILTER_PARAMS = "params";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ActionLogRecordDtoBuilder dtoBuilder = new ActionLogRecordDtoBuilder();

    @Autowired
    private IActionLogManager actionLogManager;

    @Autowired
    private ISocialActivityStreamManager socialActivityStreamManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    protected IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    protected IActionLogManager getActionLogManager() {
        return actionLogManager;
    }

    public void setActionLogManager(IActionLogManager actionLogManager) {
        this.actionLogManager = actionLogManager;
    }

    protected ISocialActivityStreamManager getSocialActivityStreamManager() {
        return socialActivityStreamManager;
    }

    public void setSocialActivityStreamManager(ISocialActivityStreamManager socialActivityStreamManager) {
        this.socialActivityStreamManager = socialActivityStreamManager;
    }

    protected ActionLogRecordDtoBuilder getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(ActionLogRecordDtoBuilder dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public PagedMetadata<ActionLogRecordDto> getActivityStream(RestListRequest requestList, UserDetails userDetails) {
        try {
            ActionLogRecordSearchBean searchBean = buildSearchBean(requestList, userDetails);
            SearcherDaoPaginatedResult<ActionLogRecord> pagedResult = getActionLogManager().getPaginatedActionRecords(searchBean);

            List<ActionLogRecordDto> dtoList = this.getDtoBuilder().convert(pagedResult.getList(), this.getSocialActivityStreamManager());

            PagedMetadata<ActionLogRecordDto> pagedMetadata = new PagedMetadata<>(requestList, pagedResult);
            pagedMetadata.setBody(dtoList);
            return pagedMetadata;

        } catch (Throwable t) {
            logger.error("error searching actionLog ", t);
            throw new RestServerError("error searching actionLog ", t);
        }
    }

    @Override
    public ActionLogRecordDto addLike(int recordId, UserDetails userDetails) {
        try {
            if (null == this.getActionLogManager().getActionRecord(recordId)) {
                logger.warn("no record found with id {}", recordId);
                throw new RestRourceNotFoundException(ActivityStreamValidator.ERRCODE_RECORD_NOT_FOUND, "actionLogRecord", String.valueOf(recordId));
            }
            this.getSocialActivityStreamManager().editActionLikeRecord(recordId, userDetails.getUsername(), true);
            ActionLogRecordDto dto = getFullDto(recordId);
            return dto;
        } catch (ApsSystemException t) {
            logger.error("error add add like for id {}", recordId, t);
            throw new RestServerError("error in add like ", t);
        }
    }

    @Override
    public ActionLogRecordDto removeLike(int recordId, UserDetails userDetails) {
        try {
            this.getSocialActivityStreamManager().editActionLikeRecord(recordId, userDetails.getUsername(), false);
            ActionLogRecordDto dto = getFullDto(recordId);
            return dto;
        } catch (Throwable t) {
            logger.error("error in remove like for id {}", recordId, t);
            throw new RestServerError("error in remove like ", t);
        }
    }

    @Override
    public ActionLogRecordDto addComment(ActivityStreamCommentRequest commentRequest, UserDetails attribute) {
        try {
            int recordId = commentRequest.getRecordId();
            if (null == this.getActionLogManager().getActionRecord(recordId)) {
                logger.warn("no record found with id {}", recordId);
                throw new RestRourceNotFoundException(ActivityStreamValidator.ERRCODE_RECORD_NOT_FOUND, "actionLogRecord", String.valueOf(recordId));
            }
            this.getSocialActivityStreamManager().addActionCommentRecord(attribute.getUsername(), commentRequest.getComment(), recordId);

            ActionLogRecordDto dto = this.getDtoBuilder().toDto(this.getActionLogManager().getActionRecord(recordId),
                    this.getSocialActivityStreamManager().getActionLikeRecords(recordId),
                    this.getSocialActivityStreamManager().getActionCommentRecords(recordId));
            return dto;
        } catch (ApsSystemException t) {
            logger.error("error in add comment for id {}", commentRequest.getRecordId(), t);
            throw new RestServerError("error in add comment", t);
        }
    }

    @Override
    public ActionLogRecordDto removeComment(int recordId, int commentId, UserDetails attribute) {
        try {
            if (null == this.getActionLogManager().getActionRecord(recordId)) {
                logger.warn("no record found with id {}", recordId);
                throw new RestRourceNotFoundException(ActivityStreamValidator.ERRCODE_RECORD_NOT_FOUND, "actionLogRecord", String.valueOf(recordId));
            }

            this.getSocialActivityStreamManager().deleteActionCommentRecord(commentId, recordId);

            ActionLogRecordDto dto = this.getDtoBuilder().toDto(this.getActionLogManager().getActionRecord(recordId),
                    this.getSocialActivityStreamManager().getActionLikeRecords(recordId),
                    this.getSocialActivityStreamManager().getActionCommentRecords(recordId));
            return dto;
        } catch (Throwable t) {
            logger.error("error in delete comment for id {}", recordId, t);
            throw new RestServerError("error in remove comment", t);
        }
    }

    protected ActionLogRecordDto getFullDto(int recordId) throws ApsSystemException {
        ActionLogRecordDto dto = this.getDtoBuilder().toDto(this.getActionLogManager().getActionRecord(recordId),
                this.getSocialActivityStreamManager().getActionLikeRecords(recordId),
                this.getSocialActivityStreamManager().getActionCommentRecords(recordId));
        return dto;
    }

    protected ActionLogRecordSearchBean buildSearchBean(RestListRequest requestList, UserDetails userDetails) {
        ActionLogRecordSearchBean searchBean = new ActionLogRecordSearchBean();

        //groups
        List<Group> userGroups = this.getAuthorizationManager().getUserGroups(userDetails);
        searchBean.setUserGroupCodes(userGroups.stream().map(i -> i.getAuthority()).collect(Collectors.toList()));

        if (null == requestList.getFilters() || requestList.getFilters().length == 0) {
            return searchBean;
        }
        for (Filter f : requestList.getFilters()) {

            //creation date range
            if (f.getAttributeName().equals(KEY_FILTER_CREATION)) {
                DateRange range = new DateRange(f.getValue());
                searchBean.setStartCreation(range.getStart());
                searchBean.setEndCreation(range.getEnd());
            }

            //update date range
            if (f.getAttributeName().equals(KEY_FILTER_UPDATE)) {
                DateRange range = new DateRange(f.getValue());
                searchBean.setStartCreation(range.getStart());
                searchBean.setEndCreation(range.getEnd());
            }

            if (f.getAttributeName().equals(KEY_FILTER_USERNAME)) {
                searchBean.setUsername(f.getValue());
            }
            if (f.getAttributeName().equals(KEY_FILTER_NS)) {
                searchBean.setNamespace(f.getValue());
            }
            if (f.getAttributeName().equals(KEY_FILTER_ACTIONNAME)) {
                searchBean.setActionName(f.getValue());
            }
            if (f.getAttributeName().equals(KEY_FILTER_PARAMS)) {
                searchBean.setParams(f.getValue());
            }
        }
        return searchBean;
    }

}
