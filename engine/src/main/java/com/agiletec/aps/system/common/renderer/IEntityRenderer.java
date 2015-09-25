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
package com.agiletec.aps.system.common.renderer;

import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * Basic interface which declares the services used to render entities.
 * @author M.Diana - W.Ambu - E.Santoboni
 */
public interface IEntityRenderer {
	
	/**
	 * Render an entity with a velocity template.
	 * @param entity The entity to render.
	 * @param velocityTemplate The velocity template to use to render the entity.
	 * @param langCode The rendering lang.
	 * @param convertSpecialCharacters Specifies whether to convert special characters.
	 * @return The result of the rendering.
	 */
	public String render(IApsEntity entity, String velocityTemplate, String langCode, boolean convertSpecialCharacters);
	
}
