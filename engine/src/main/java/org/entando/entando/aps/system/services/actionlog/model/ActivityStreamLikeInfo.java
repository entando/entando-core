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
package org.entando.entando.aps.system.services.actionlog.model;

import java.io.Serializable;

/**
 * @author E.Santoboni
 */
public class ActivityStreamLikeInfo implements Serializable {
	
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
	
	private String _username;
	private String _displayName;
	
}