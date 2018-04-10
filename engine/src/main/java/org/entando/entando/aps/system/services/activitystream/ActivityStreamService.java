package org.entando.entando.aps.system.services.activitystream;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDto;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;
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

    private IDtoBuilder<ActionLogRecord, ActionLogRecordDto> dtoBuilder;

    @Autowired
    private IActionLogManager actionLogManager;

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

    protected IDtoBuilder<ActionLogRecord, ActionLogRecordDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<ActionLogRecord, ActionLogRecordDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void setUp() {
        this.setDtoBuilder(new DtoBuilder<ActionLogRecord, ActionLogRecordDto>() {

            @Override
            protected ActionLogRecordDto toDto(ActionLogRecord src) {
                ActionLogRecordDto dto = new ActionLogRecordDto(src);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<ActionLogRecordDto> getActivityStream(RestListRequest requestList, UserDetails userDetails) {
        try {
            ActionLogRecordSearchBean searchBean = buildSearchBean(requestList, userDetails);
            SearcherDaoPaginatedResult<ActionLogRecord> pager = getActionLogManager().getPaginatedActionRecords(searchBean);

            List<ActionLogRecordDto> dtoList = getDtoBuilder().convert(pager.getList());

            PagedMetadata<ActionLogRecordDto> pagedMetadata = new PagedMetadata<>(requestList, pager);
            pagedMetadata.setBody(dtoList);
            return pagedMetadata;

        } catch (Throwable t) {
            logger.error("error searching actionLog ", t);
            throw new RestServerError("error searching actionLog ", t);
        }
    }

    private ActionLogRecordSearchBean buildSearchBean(RestListRequest requestList, UserDetails userDetails) {
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
