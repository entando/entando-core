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

import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentUtilizer;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

public class GuiFragmentDtoBuilder extends DtoBuilder<GuiFragment, GuiFragmentDto> implements BeanFactoryAware {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BeanFactory beanFactory;
	private IWidgetTypeManager widgetTypeManager;

	@Override
	protected GuiFragmentDto toDto(GuiFragment src) {
		if (null == src) {
			return null;
		}
		WidgetType type = null;
		if (StringUtils.isNotEmpty(src.getWidgetTypeCode())) {
			type = this.getWidgetTypeManager().getWidgetType(src.getWidgetTypeCode());
		}
		GuiFragmentDto dest = new GuiFragmentDto(src, type);
		ListableBeanFactory factory = (ListableBeanFactory) this.beanFactory;
		String[] defNames = factory.getBeanNamesForType(GuiFragmentUtilizer.class);
		for (String defName : defNames) {
			GuiFragmentUtilizer utilizers = null;
			try {
				utilizers = this.beanFactory.getBean(defName, GuiFragmentUtilizer.class);
				List<Object> references = utilizers.getGuiFragmentUtilizers(src.getCode());
				if (null != references) {
					for (Object reference : references) {
						if (reference instanceof GuiFragment) {
							dest.addFragmentRef((GuiFragment) reference);
						} else if (reference instanceof PageModel) {
							dest.addPageModelRef((PageModel) reference);
						} else {
							logger.info("unexpected reference - type {}", reference.getClass());
						}
					}
				}
			} catch (Throwable t) {
				logger.error("Error extracting reference from bean '{}'", defName);
				utilizers = null;
			}
		}
		return dest;
	}

	protected BeanFactory getBeanFactory() {
		return beanFactory;
	}

	@Override
	public void setBeanFactory(BeanFactory bf) throws BeansException {
		this.beanFactory = bf;
	}

	protected IWidgetTypeManager getWidgetTypeManager() {
		return widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this.widgetTypeManager = widgetTypeManager;
	}

}
