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

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IGuiFragmentDAO {
	
	public List<String> searchGuiFragments(FieldSearchFilter[] filters);
	
	public GuiFragment loadGuiFragment(String code);
	
	public List<String> loadGuiFragments();
	
	public void removeGuiFragment(String code);
	
	public void updateGuiFragment(GuiFragment guiFragment);
	
	public void insertGuiFragment(GuiFragment guiFragment);
	
}