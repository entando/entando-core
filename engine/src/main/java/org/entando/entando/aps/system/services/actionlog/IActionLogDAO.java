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
package org.entando.entando.aps.system.services.actionlog;

import java.util.List;

import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamLikeInfo;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.IActivityStreamSearchBean;

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni - S.Puddu
 */
public interface IActionLogDAO {
	
	public List<Integer> getActionRecords(IActionLogRecordSearchBean searchBean);
	
	public List<Integer> getActivityStreamRecords(IActivityStreamSearchBean searchBean);
	
	public List<Integer> getActionRecords(FieldSearchFilter[] filters);
	
	public List<Integer> getActionRecords(FieldSearchFilter[] filters, List<String> userGroupCodes);
	
	public void addActionRecord(ActionLogRecord actionRecord);
	
	public ActionLogRecord getActionRecord(int id);
	
	public void deleteActionRecord(int id);
	
	public List<Integer> getActivityStream(List<String> userGroupCodes);
	
	public void editActionLikeRecord(int id, String username, boolean add);
	
	public void addActionLikeRecord(int id, String username);
	
	public void deleteActionLikeRecord(int id, String username);
	
	public List<ActivityStreamLikeInfo> getActionLikeRecords(int id);
	
	public List<ActivityStreamComment> getActionCommentRecords(int id);
	
	public void addActionCommentRecord(int id, int recordId, String username, String comment);
	
	public void deleteActionCommentRecord(int id, int streamId);
	
	public void cleanOldActivityStreamLogs(int maxActivitySizeByGroup);
	
}