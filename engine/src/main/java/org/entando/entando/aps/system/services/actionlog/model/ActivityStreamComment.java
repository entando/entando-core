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

package org.entando.entando.aps.system.services.actionlog.model;

import java.util.Date;

/**
 * @author S.Loru
 */
public class ActivityStreamComment {

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}
	
	public String getUsername() {
		return _username;
	}

	public void setUsername(String username) {
		this._username = username;
	}

	public String getDisplayName() {
		return _displayName;
	}

	public void setDisplayName(String displayName) {
		this._displayName = displayName;
	}

	public Date getCommentDate() {
		return _commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this._commentDate = commentDate;
	}

	public String getCommentText() {
		return _commentText;
	}

	public void setCommentText(String commentText) {
		this._commentText = commentText;
	}
	
	private int _id;
	private String _username;
	private String _displayName;
	private Date _commentDate;
	private String _commentText;

}
