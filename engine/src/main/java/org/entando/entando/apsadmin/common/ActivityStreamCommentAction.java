/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.apsadmin.common;

import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author S.Loru
 */
public class ActivityStreamCommentAction extends BaseAction{

	private static final Logger _logger =  LoggerFactory.getLogger(ActivityStreamCommentAction.class);
	
	public String addComment() {
		try {
			String username = this.getCurrentUser().getUsername();
			this.getActionLogManager().addActionCommentRecord(username, this.getCommentText(), this.getStreamRecordId());
		} catch (Throwable t) {
			_logger.error("Error on adding comment on activity", t);
        }
		return SUCCESS;
	}
	
	public String removeComment() {
		try {
			this.getActionLogManager().deleteActionCommentRecord(this.getCommentId(), this.getStreamRecordId());
		} catch (Throwable t) {
			_logger.error("Error on removing comment on activity", t);
        }
		return SUCCESS;
	}
	
	public int getStreamRecordId() {
		return _streamRecordId;
	}

	public void setStreamRecordId(int streamRecordId) {
		this._streamRecordId = streamRecordId;
	}
	
	public String getCommentText() {
		return _commentText;
	}

	public void setCommentText(String commentText) {
		this._commentText = commentText;
	}
	
	public IActionLogManager getActionLogManager() {
		return _actionLogManager;
	}

	public void setActionLogManager(IActionLogManager actionLogManager) {
		this._actionLogManager = actionLogManager;
	}

	public int getCommentId() {
		return _commentId;
	}

	public void setCommentId(int commentId) {
		this._commentId = commentId;
	}
	
	private IActionLogManager _actionLogManager;
	private int _streamRecordId;
	private String _commentText;
	private int _commentId;
	
}
