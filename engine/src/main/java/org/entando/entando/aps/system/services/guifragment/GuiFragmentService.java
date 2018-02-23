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

import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GuiFragmentService implements IGuiFragmentService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IGuiFragmentManager guiFragmentManager;

	@Autowired
	private IDtoBuilder<GuiFragment, GuiFragmentDto> dtoBuilder;

	public IGuiFragmentManager getGuiFragmentManager() {
		return guiFragmentManager;
	}

	public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
		this.guiFragmentManager = guiFragmentManager;
	}

	public IDtoBuilder<GuiFragment, GuiFragmentDto> getDtoBuilder() {
		return dtoBuilder;
	}

	public void setDtoBuilder(IDtoBuilder<GuiFragment, GuiFragmentDto> dtoBuilder) {
		this.dtoBuilder = dtoBuilder;
	}

	@Override
	public PagedMetadata<GuiFragmentDto> getGuiFragments(RestListRequest restListReq) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public GuiFragmentDto getGuiFragment(String code) {
		GuiFragment fragment = null;
		try {
			fragment = this.getGuiFragmentManager().getGuiFragment(code);
		} catch (Exception e) {
			logger.error("Error extracting fragment {}", code, e);
			throw new RestServerError("error extracting fragment", e);
		}
		if (null == fragment) {
			logger.warn("no fragment found with code {}", code);
			throw new RestRourceNotFoundException("fragment", code);
		}
		return this.getDtoBuilder().convert(fragment);
	}

}
