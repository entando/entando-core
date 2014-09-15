/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.apsadmin.system.services.activitystream;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.List;

import org.entando.entando.apsadmin.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.apsadmin.system.services.activitystream.model.ActivityStreamLikeInfo;

/**
 * @author E.Santoboni - S.Puddu
 */
public interface ISocialActivityStreamManager {
	
	public void editActionLikeRecord(int id, String username, boolean add) throws ApsSystemException;
	
	public List<ActivityStreamLikeInfo> getActionLikeRecords(int id) throws ApsSystemException;
	
	public void addActionCommentRecord(String username, String commentText, int streamId) throws ApsSystemException;
	
	public void deleteActionCommentRecord(int id, int streamId) throws ApsSystemException;
	
	public List<ActivityStreamComment> getActionCommentRecords(int id) throws ApsSystemException;
	
}