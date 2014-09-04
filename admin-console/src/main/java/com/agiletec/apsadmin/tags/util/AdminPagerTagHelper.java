/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.apsadmin.tags.util;

import java.util.Collection;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.aps.tags.util.PagerTagHelper;
import com.agiletec.apsadmin.util.ApsRequestParamsUtil;

/**
 * Helper class for the pager for administration interface.
 * @author E.Santoboni
 */
public class AdminPagerTagHelper extends PagerTagHelper {

	private static final Logger _logger = LoggerFactory.getLogger(AdminPagerTagHelper.class);
	
	public IPagerVO getPagerVO(Collection collection, int max, boolean isAdvanced, 
			int offset, ServletRequest request) throws ApsSystemException {
		return this.getPagerVO(collection, null, max, isAdvanced, offset, request);
	}
	
	public IPagerVO getPagerVO(Collection collection, String pagerId, int max, boolean isAdvanced, 
			int offset, ServletRequest request) throws ApsSystemException {
		IPagerVO pagerVo = null;
		try {
			int item = this.getItemNumber(pagerId, request);
			pagerVo = this.buildPageVO(collection, item, max, pagerId, isAdvanced, offset);
		} catch (Throwable t) {
			_logger.error("Error while building pagerVo", t);
			//ApsSystemUtils.logThrowable(t, this, "getPagerVO");
			throw new ApsSystemException("Error while building pagerVo", t);
		}
		return pagerVo;
	}
	
	protected int getItemNumber(ServletRequest request) {
		return this.getItemNumber(null, request);
	}
	
	protected int getItemNumber(String pagerId, ServletRequest request) {
		String stringItem = null;
		String marker = (null != pagerId && pagerId.trim().length() > 0) ? pagerId : "pagerItem"; 
		String[] params = ApsRequestParamsUtil.getApsParams(marker, "_", request);
		if (params != null && params.length == 2) {
			stringItem = params[1];
		} else {
			stringItem = request.getParameter(marker);
		}
		int item = 0;
		if (stringItem != null) {
			try {
				item = Integer.parseInt(stringItem);
			} catch (NumberFormatException e) {
				_logger.error("Errore in parsing stringItem {}", stringItem, e);
			}
		}
		return item;
	}
	
}