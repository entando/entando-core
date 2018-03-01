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
package org.entando.entando.aps.system.services.guifragment;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IGuiFragmentDAO {

	public List<String> searchGuiFragments(FieldSearchFilter[] filters);

	public int countGuiFragments(FieldSearchFilter[] filters);

	public GuiFragment loadGuiFragment(String code);

	public void removeGuiFragment(String code);

	public void updateGuiFragment(GuiFragment guiFragment);

	public void insertGuiFragment(GuiFragment guiFragment);

	public List<String> loadGuiFragmentPluginCodes();

}
