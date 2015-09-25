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
package com.agiletec.aps.tags.util;

import com.agiletec.aps.tags.ParameterTag;

/**
 * Interface for custom tags that use the sub-tag ParameterTag.
 * @author E.Santoboni
 */
public interface IParameterParentTag {
	
	/**
	 * Add a parameter. This Method in invoked by sub-tags {@link ParameterTag}.
	 * @param name The name of the parameter.
	 * @param value The value of the parameter.
	 */
	public void addParameter(String name, String value);
	
}
