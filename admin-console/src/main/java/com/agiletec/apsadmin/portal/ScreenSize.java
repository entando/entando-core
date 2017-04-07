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
package com.agiletec.apsadmin.portal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenSize {

	private static final Logger _logger = LoggerFactory.getLogger(ScreenSize.class);
	
	private static final String DEFAULT_SEPARATOR = "x";
	private static final int WIDTH_DEFAULT = 600;
	private static final int HEIGHT_DEFAULT = 800;
	private static final String CODE_DEFAULT = "-";
	
	private String code;
	private int width;
	private int height;

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ScreenSize() {

	}

	public ScreenSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.code= CODE_DEFAULT;
	}

	public ScreenSize(String param) {
		this.code= CODE_DEFAULT;
		if (StringUtils.isNotBlank(param)) {
			String[] size = param.toLowerCase().trim().split(DEFAULT_SEPARATOR);
			if (null != size && size.length == 2 && StringUtils.isNumeric(size[0]) && StringUtils.isNumeric(size[1])) {
				this.width = Integer.valueOf(size[0]);
				this.height = Integer.valueOf(size[1]);
			}
		}
		if (this.height == 0 || this.width == 0) {
			this.setWidth(WIDTH_DEFAULT);
			this.setHeight(HEIGHT_DEFAULT);
			_logger.warn("Invalid or null size detected. Defaulting to {}x{}", this.getWidth(), this.getHeight());
		}
	}
	
}




