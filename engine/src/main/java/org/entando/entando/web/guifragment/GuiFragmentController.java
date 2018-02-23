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
package org.entando.entando.web.guifragment;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentService;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.guifragment.validator.GuiFragmentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuiFragmentController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String ERRCODE_FRAGMENT_ALREADY_EXISTS = "1";
	public static final String ERRCODE_URINAME_MISMATCH = "2";

	@Autowired
	private IGuiFragmentService guiFragmentService;

	@Autowired
	private GuiFragmentValidator guiFragmentValidator;

	public IGuiFragmentService getGuiFragmentService() {
		return guiFragmentService;
	}

	public void setGuiFragmentService(IGuiFragmentService guiFragmentService) {
		this.guiFragmentService = guiFragmentService;
	}

	public GuiFragmentValidator getGuiFragmentValidator() {
		return guiFragmentValidator;
	}

	public void setGuiFragmentValidator(GuiFragmentValidator guiFragmentValidator) {
		this.guiFragmentValidator = guiFragmentValidator;
	}

	@RestAccessControl(permission = Permission.SUPERUSER)
	@RequestMapping(value = "/fragments/{fragmentCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGuiFragment(@PathVariable String fragmentCode) {
		GuiFragmentDto fragment = this.getGuiFragmentService().getGuiFragment(fragmentCode);
		return new ResponseEntity<>(new RestResponse(fragment), HttpStatus.OK);
	}

}
