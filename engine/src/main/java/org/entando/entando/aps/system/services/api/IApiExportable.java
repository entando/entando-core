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
package org.entando.entando.aps.system.services.api;

import javax.ws.rs.core.MediaType;

/**
 * Interface implemented by all those services or beans 
 * which handle objects suitable to be exported through API calls.
 * @author E.Santoboni
 */
public interface IApiExportable {
	
	/**
	 * Return the uniform resource locator of the given object.
	 * @param object The object
	 * @param applicationBaseUrl The application base url.
	 * @param langCode The current lang code
	 * @param mediaType The produces media type
	 * @return The uniform resource locator of the given object.
	 */
	public String getApiResourceUrl(Object object, String applicationBaseUrl, String langCode, MediaType mediaType);
	
}