/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.util.List;

import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.activitystream.model.ActivityStreamLikeInfo;

/**
 * @author E.Santoboni - S.Puddu
 */
public interface ISocialActivityStreamDAO {
	
	public void editActionLikeRecord(int id, String username, boolean add);
	
	public void addActionLikeRecord(int id, String username);
	
	public void deleteActionLikeRecord(int id, String username);
	
	public List<ActivityStreamLikeInfo> getActionLikeRecords(int id);
	
	public List<ActivityStreamComment> getActionCommentRecords(int id);
	
	public void addActionCommentRecord(int id, int recordId, String username, String comment);
	
	public void deleteActionCommentRecord(int id);
	
	public void deleteSocialRecordsRecord(int streamId);
	
}