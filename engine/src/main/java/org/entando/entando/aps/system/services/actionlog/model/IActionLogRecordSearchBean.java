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

import java.util.Date;
import java.util.List;

/**
 * @author E.Santoboni - S.Puddu - S.Loru
 */
public interface IActionLogRecordSearchBean {
	
	public Date getStartCreation();
	
	public Date getEndCreation();
	
	public Date getStartUpdate();
	
	public Date getEndUpdate();
	
	public String getUsername();
	
	public String getNamespace();
	
	public String getActionName();
	
	public String getParams();
	
	public List<String> getUserGroupCodes();
	
}