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

import java.util.List;
import java.util.stream.Collectors;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Arrays;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDtoSmall;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.guifragment.model.GuiFragmentRequestBody;
import org.entando.entando.web.guifragment.validator.GuiFragmentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

public class GuiFragmentService implements IGuiFragmentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IGuiFragmentManager guiFragmentManager;

    @Autowired
    private IDtoBuilder<GuiFragment, GuiFragmentDto> dtoBuilder;

    @Autowired
    private IDtoBuilder<GuiFragment, GuiFragmentDtoSmall> dtoSmallBuilder;

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

    protected IDtoBuilder<GuiFragment, GuiFragmentDtoSmall> getDtoSmallBuilder() {
        return dtoSmallBuilder;
    }

    public void setDtoSmallBuilder(IDtoBuilder<GuiFragment, GuiFragmentDtoSmall> dtoSmallBuilder) {
        this.dtoSmallBuilder = dtoSmallBuilder;
    }

    @Override
    public PagedMetadata<GuiFragmentDtoSmall> getGuiFragments(RestListRequest restListReq) {
        PagedMetadata<GuiFragmentDtoSmall> pagedMetadata = null;
        try {
            SearcherDaoPaginatedResult<GuiFragment> fragments = this.getGuiFragmentManager().getGuiFragments(restListReq.buildFieldSearchFilters());
            List<GuiFragmentDtoSmall> dtoList = this.getDtoSmallBuilder().convert(fragments.getList());
            pagedMetadata = new PagedMetadata<>(restListReq, dtoList, fragments.getCount());
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
            throw new RestRourceNotFoundException(GuiFragmentValidator.ERRCODE_FRAGMENT_DOES_NOT_EXISTS, "fragment", code);
        }
        return this.getDtoBuilder().convert(fragment);
    }

    @Override
    public GuiFragmentDto addGuiFragment(GuiFragmentRequestBody guiFragmentRequest) {
        try {
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
                throw new RestRourceNotFoundException(GuiFragmentValidator.ERRCODE_FRAGMENT_DOES_NOT_EXISTS, "fragment", code);
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
            BeanPropertyBindingResult validationResult = this.checkFragmentForDelete(fragment, dto);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getGuiFragmentManager().deleteGuiFragment(guiFragmentCode);
        } catch (ApsSystemException e) {
            logger.error("Error in delete guiFragmentCode {}", guiFragmentCode, e);
            throw new RestServerError("error in delete guiFragmentCode", e);
        }
    }

    @Override
    public List<String> getPluginCodes() {
        try {
            return this.getGuiFragmentManager().loadGuiFragmentPluginCodes();
        } catch (ApsSystemException e) {
            logger.error("Error loading plugin codes", e);
            throw new RestServerError("Error loading plugin codes", e);
        }
    }

    private GuiFragment createGuiFragment(GuiFragmentRequestBody guiFragmentRequest) {
        GuiFragment fragment = new GuiFragment();
        fragment.setCode(guiFragmentRequest.getCode());
        fragment.setGui(guiFragmentRequest.getGuiCode());
        return fragment;
    }

    protected BeanPropertyBindingResult checkFragmentForDelete(GuiFragment fragment, GuiFragmentDto dto) throws ApsSystemException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(fragment, "fragment");
        if (null == fragment) {
            return bindingResult;
        }
        if (!dto.getFragments().isEmpty() || !dto.getPageModels().isEmpty()) {
            List<String> fragments = dto.getFragments().stream().map(GuiFragmentDto.FragmentRef::getCode).collect(Collectors.toList());
            List<String> pagemodels = dto.getPageModels().stream().map(GuiFragmentDto.PageModelRef::getCode).collect(Collectors.toList());
            bindingResult.reject(GuiFragmentValidator.ERRCODE_FRAGMENT_REFERENCES,
                    new Object[]{fragment.getCode(), fragments, pagemodels}, "guifragment.cannot.delete.references");
        }
        if (fragment.isLocked()) {
            bindingResult.reject(GuiFragmentValidator.ERRCODE_FRAGMENT_LOCKED, new Object[]{fragment.getCode()}, "guifragment.cannot.delete.locked");
        }
        return bindingResult;
    }

}
