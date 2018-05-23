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
package org.entando.entando.aps.system.services.activitystream;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDto;
import org.entando.entando.web.activitystream.ActivityStreamCommentRequest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface IActivityStreamService {

    PagedMetadata<ActionLogRecordDto> getActivityStream(RestListRequest requestList, UserDetails userDetails);

    ActionLogRecordDto addLike(int recordId, UserDetails userDetails);

    ActionLogRecordDto removeLike(int recordId, UserDetails userDetails);

    ActionLogRecordDto addComment(ActivityStreamCommentRequest commentRequest, UserDetails attribute);

    ActionLogRecordDto removeComment(int recordId, int commentId, UserDetails attribute);

}
