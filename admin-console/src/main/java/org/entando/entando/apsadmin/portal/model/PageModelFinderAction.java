/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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