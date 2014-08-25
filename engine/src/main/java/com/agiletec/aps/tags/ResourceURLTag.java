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
package com.agiletec.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Return the URl of the resources.
 * There two attribute that can be used (but not mandatory):<br/>
 * - "root" if not otherwise specified, the value of SystemConstants.PAR_RESOURCES_ROOT_URL is used.<br/>
 * - "folder" declares a specific directory for the desired resources. Unless specified, the value "" 
 * (empty string) is used in the generation of the URL.
 * @author E.Santoboni
 */
@SuppressWarnings("serial")
public class ResourceURLTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceURLTag.class);
	
	public int doEndTag() throws JspException {
		try {
			if (null == _root) {
				ConfigInterface configService = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, this.pageContext);
				_root = configService.getParam(SystemConstants.PAR_RESOURCES_ROOT_URL);
			}
			if (null == _folder) {
				_folder = "";
			}
			pageContext.getOut().print(_root + _folder);
		} catch (Throwable t) {
			_logger.error("Error closing the tag", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag");
			throw new JspException("Error closing the tag", t);
		}
		return EVAL_PAGE;
	}
	
	/**
	 * Return the root folder
	 * @return The root.
	 */
	public String getRoot() {
		return _root;
	}

	/**
	 * Set the root folder.
	 * @param root La root.
	 */
	public void setRoot(String root) {
		this._root = root;
	}
	
	/**
	 * Get the resource folder.
	 * @return Il folder.
	 */
	public String getFolder() {
		return _folder;
	}

	/**
	 * Set the resource folder.
	 * @param folder The folder
	 */
	public void setFolder(String folder) {
		this._folder = folder;
	}
	
	private String _root;
	private String _folder;

}
