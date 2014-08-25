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
package org.entando.entando.aps.system.services.guifragment;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for those services whose handled elements are based on fragment.
 * @author E.Santoboni
 */
public interface GuiFragmentUtilizer {
	
	/**
	 * Return the id of the utilizing service.
	 * @return The id of the utilizer.
	 */
	public String getName();
	
	/**
	 * Return the list of the objects that use the fragment with the given name.
	 * @param guiFragmentCode The name of the fragment
	 * @return The list of the objects that use the fragment with the given code.
	 * @throws ApsSystemException In case of error
	 */
	public List getGuiFragmentUtilizers(String guiFragmentCode) throws ApsSystemException;
	
}
