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

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import org.entando.entando.aps.system.exception.RestReferencedResourceException;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.guifragment.model.GuiFragmentRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GuiFragmentService implements IGuiFragmentService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IGuiFragmentManager guiFragmentManager;

	@Autowired
	private IDtoBuilder<GuiFragment, GuiFragmentDto> dtoBuilder;

	protected IGuiFragmentManager getGuiFragmentManager() {
		return guiFragmentManager;
	}

	public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
		this.guiFragmentManager = guiFragmentManager;
	}

	protected IDtoBuilder<GuiFragment, GuiFragmentDto> getDtoBuilder() {
		return dtoBuilder;
	}

	public void setDtoBuilder(IDtoBuilder<GuiFragment, GuiFragmentDto> dtoBuilder) {
		this.dtoBuilder = dtoBuilder;
	}

	@Override
	public PagedMetadata<GuiFragmentDto> getGuiFragments(RestListRequest restListReq) {
		PagedMetadata<GuiFragmentDto> pagedMetadata = null;
		try {
			/*
			//transforms the filters by overriding the key specified in the request with the correct one known by the dto
			restListReq.getFieldSearchFilters().stream()
					.filter(searchFilter -> searchFilter.getKey() != null)
					.forEach(searchFilter -> searchFilter.setKey(GuiFragmentDto.getEntityFieldName(searchFilter.getKey())));
			 */
			SearcherDaoPaginatedResult<GuiFragment> fragments = this.getGuiFragmentManager().getGuiFragments(restListReq.getFieldSearchFilters());
			List<GuiFragmentDto> dtoList = this.getDtoBuilder().convert(fragments.getList());
			pagedMetadata = new PagedMetadata<>(restListReq, fragments);
			pagedMetadata.setBody(dtoList);
		} catch (Throwable t) {
			logger.error("error in search fragments", t);
			throw new RestServerError("error in search fragments", t);
		}
		return pagedMetadata;
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

	@Override
	public GuiFragmentDto addGuiFragment(GuiFragmentRequestBody guiFragmentRequest) {
		//String code = guiFragmentRequest.getCode();
		try {
			//if (null != this.getGuiFragment(code)) {
			//	throw new RestRourceNotFoundException("fragment", code);
			//}
			GuiFragment fragment = this.createGuiFragment(guiFragmentRequest);
			this.getGuiFragmentManager().addGuiFragment(fragment);
			return this.getDtoBuilder().convert(fragment);
		} catch (ApsSystemException e) {
			logger.error("Error adding fragment", e);
			throw new RestServerError("error add fragment", e);
		}
	}

	@Override
	public GuiFragmentDto updateGuiFragment(GuiFragmentRequestBody guiFragmentRequest) {
		String code = guiFragmentRequest.getCode();
		try {
			GuiFragment fragment = this.getGuiFragmentManager().getGuiFragment(code);
			if (null == fragment) {
				throw new RestRourceNotFoundException("fragment", code);
			}
			fragment.setGui(guiFragmentRequest.getGuiCode());
			this.getGuiFragmentManager().updateGuiFragment(fragment);
			return this.getDtoBuilder().convert(fragment);
		} catch (RestRourceNotFoundException e) {
			throw e;
		} catch (ApsSystemException e) {
			logger.error("Error updating fragment {}", code, e);
			throw new RestServerError("error in update fragment", e);
		}
	}

	@Override
	public void removeGuiFragment(String guiFragmentCode) {
		try {
			GuiFragment fragment = this.getGuiFragmentManager().getGuiFragment(guiFragmentCode);
			if (null == fragment) {
				return;
			}
			GuiFragmentDto dto = this.getDtoBuilder().convert(fragment);
			if (!dto.getFragments().isEmpty() || !dto.getPageModels().isEmpty()) {
				throw new RestReferencedResourceException(guiFragmentCode);
			}
			this.getGuiFragmentManager().deleteGuiFragment(guiFragmentCode);
		} catch (RestReferencedResourceException e) {
			throw e;
		} catch (ApsSystemException e) {
			logger.error("Error in delete guiFragmentCode {}", guiFragmentCode, e);
			throw new RestServerError("error in delete guiFragmentCode", e);
		}
	}

	private GuiFragment createGuiFragment(GuiFragmentRequestBody guiFragmentRequest) {
		GuiFragment fragment = new GuiFragment();
		fragment.setCode(guiFragmentRequest.getCode());
		fragment.setGui(guiFragmentRequest.getGuiCode());
		return fragment;
	}

}
