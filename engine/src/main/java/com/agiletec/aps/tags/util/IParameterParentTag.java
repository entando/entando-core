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
package com.agiletec.aps.tags.util;

import com.agiletec.aps.tags.ParameterTag;

/**
 * Interface for custom tags that use the sub-tag ParameterTag.
 * @author E.Santoboni
 */
public interface IParameterParentTag {
	
	/**
	 * Add a parameter. This Method in invoked by sub-tags {@link ParameterTag}.
	 * @param name The name of the parameter.
	 * @param value The value of the parameter.
	 */
	public void addParameter(String name, String value);
	
}
