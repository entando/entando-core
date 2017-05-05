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
package com.agiletec.aps.system.common.entity.loader;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * The Class loader of the extra attribute.
 * @author E.Santoboni
 */
public class ExtraAttributeLoader {

	private static final Logger _logger =  LoggerFactory.getLogger(ExtraAttributeLoader.class);
	
	public Map<String, AttributeInterface> extractAttributes(BeanFactory beanFactory, String entityManagerName) {
		Map<String, AttributeInterface> attributes = null;
		try {
			attributes = this.loadExtraAttributes(beanFactory, entityManagerName);
		} catch (Throwable t) {
			_logger.error("Error loading extra attributes. entityManager: {}", entityManagerName, t);
		}
		return attributes;
	}
	
	private Map<String, AttributeInterface> loadExtraAttributes(BeanFactory beanFactory, String entityManagerName) {
		Map<String, AttributeInterface> extraAttributes = new HashMap<>();
		try {
			ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
			String[] defNames = factory.getBeanNamesForType(ExtraAttributeWrapper.class);
			for (int i=0; i<defNames.length; i++) {
				try {
					Object wrapperObject = beanFactory.getBean(defNames[i]);
					if (wrapperObject != null) {
						ExtraAttributeWrapper wrapper = (ExtraAttributeWrapper) wrapperObject;
						String destEntityManagerName = wrapper.getEntityManagerNameDest();
						if (entityManagerName.equals(destEntityManagerName) && null != wrapper.getAttribute()) {
							extraAttributes.put(wrapper.getAttribute().getType(), wrapper.getAttribute());
						}
					}
				} catch (Throwable t) {
					_logger.error("Error extracting attribute : wrapper bean {}", defNames[i], t);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading extra attributes. entityManagerName: {}", entityManagerName, t);
		}
		return extraAttributes;
	}
	
}