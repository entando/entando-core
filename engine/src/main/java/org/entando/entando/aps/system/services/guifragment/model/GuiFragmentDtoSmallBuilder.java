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
package org.entando.entando.aps.system.services.guifragment.model;

import com.agiletec.aps.system.services.lang.ILangManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

public class GuiFragmentDtoSmallBuilder extends DtoBuilder<GuiFragment, GuiFragmentDtoSmall> {

	private IWidgetTypeManager widgetTypeManager;
	private ILangManager langManager;

	@Override
	protected GuiFragmentDtoSmall toDto(GuiFragment src) {
		if (null == src) {
			return null;
		}
		WidgetType type = null;
		if (StringUtils.isNotEmpty(src.getWidgetTypeCode())) {
			type = this.getWidgetTypeManager().getWidgetType(src.getWidgetTypeCode());
		}
		GuiFragmentDtoSmall dest = new GuiFragmentDtoSmall(src, type, langManager);
		return dest;
	}

	protected IWidgetTypeManager getWidgetTypeManager() {
		return widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this.widgetTypeManager = widgetTypeManager;
	}

	public ILangManager getLangManager() {
		return langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this.langManager = langManager;
	}
}
