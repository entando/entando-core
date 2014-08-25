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

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IGuiFragmentManager {
	
	public GuiFragment getGuiFragment(String code) throws ApsSystemException;
	
	public List<String> getGuiFragments() throws ApsSystemException;
	
	public List<String> searchGuiFragments(FieldSearchFilter filters[]) throws ApsSystemException;
	
	public void addGuiFragment(GuiFragment guiFragment) throws ApsSystemException;
	
	public void updateGuiFragment(GuiFragment guiFragment) throws ApsSystemException;
	
	public void deleteGuiFragment(String code) throws ApsSystemException;
	
	public GuiFragment getUniqueGuiFragmentByWidgetType(String widgetTypeCode) throws ApsSystemException;
	
	public List<String> getGuiFragmentCodesByWidgetType(String widgetTypeCode) throws ApsSystemException;
	
}