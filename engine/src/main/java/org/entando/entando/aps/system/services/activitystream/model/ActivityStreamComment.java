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
package org.entando.entando.aps.system.services.activitystream.model;

import java.util.Date;
import java.io.Serializable;

/**
 * @author S.Loru
 */
public class ActivityStreamComment implements Serializable {

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
