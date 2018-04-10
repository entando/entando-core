package org.entando.entando.aps.system.services.activitystream;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface IActivityStreamService {

    PagedMetadata<ActionLogRecordDto> getActivityStream(RestListRequest requestList, UserDetails userDetails);

}
