/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDtoSmall;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.guifragment.model.GuiFragmentRequestBody;

public interface IGuiFragmentService {

	public PagedMetadata<GuiFragmentDtoSmall> getGuiFragments(RestListRequest restListReq);

	public GuiFragmentDto getGuiFragment(String code);

	public GuiFragmentDto addGuiFragment(GuiFragmentRequestBody guiFragmentRequest);

	public GuiFragmentDto updateGuiFragment(GuiFragmentRequestBody guiFragmentRequest);

	public void removeGuiFragment(String guiFragmentCode);

}
