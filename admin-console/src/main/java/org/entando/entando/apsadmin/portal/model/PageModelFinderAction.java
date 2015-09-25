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
package org.entando.entando.apsadmin.portal.model;

import com.agiletec.aps.system.services.pagemodel.PageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author E.Santoboni
 */
public class PageModelFinderAction extends AbstractPageModelAction {
	
	public List<PageModel> getPageModels() {
		List<PageModel> models = new ArrayList<PageModel>();
		models.addAll(this.getPageModelManager().getPageModels());
		BeanComparator c = new BeanComparator("description");
		Collections.sort(models, c);
		return models;
	}
	
}