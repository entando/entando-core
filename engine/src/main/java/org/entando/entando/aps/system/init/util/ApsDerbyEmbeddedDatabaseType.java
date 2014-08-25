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
package org.entando.entando.aps.system.init.util;

import com.j256.ormlite.db.DerbyEmbeddedDatabaseType;

/**
 * @author E.Santoboni
 */
public class ApsDerbyEmbeddedDatabaseType extends DerbyEmbeddedDatabaseType {
	
	@Override
	public void appendEscapedEntityName(StringBuilder sb, String name) {
		sb.append(" ").append(name.toUpperCase()).append(" ");
	}
	
	@Override
	protected void appendCharType(StringBuilder sb, int fieldWidth) {
		sb.append("VARCHAR(1)");
	}
	
}
