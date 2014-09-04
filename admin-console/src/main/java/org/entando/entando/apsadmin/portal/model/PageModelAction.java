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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.apsadmin.portal.model.helper.IPageModelActionHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelDOM;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

import java.io.File;

/**
 * @author E.Santoboni
 */
public class PageModelAction extends AbstractPageModelAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageModelAction.class);
	
	@Override
	public void validate() {
		super.validate();
		List<String> codeFieldErrors = this.getFieldErrors().get("code");
		if (null == codeFieldErrors || codeFieldErrors.isEmpty()) {
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				String currectCode = this.getCode();
				if (currectCode.length() > 0 && null != this.getPageModel(currectCode)) {
					String[] args = {currectCode};
					this.addFieldError("code", this.getText("error.pageModel.duplicateCode", args));
				}
			}
			try {
				String template = this.getTemplate();
				if (StringUtils.isBlank(template)) {
					String jspTemplatePath = PageModel.getPageModelJspPath(this.getCode(), this.getPluginCode());
					boolean existsJsp = false;
					if (StringUtils.isNotBlank(jspTemplatePath)) {
						String folderPath = this.getRequest().getSession().getServletContext().getRealPath("/");
						existsJsp = (new File(folderPath + jspTemplatePath)).exists();
						if (!existsJsp) {
							existsJsp = this.checkModelResource("file:**" + jspTemplatePath);
						}
					}
					if (!existsJsp) {
						this.addFieldError("template", this.getText("error.pageModel.templateRequired"));
					}
				}
			} catch (Throwable t) {
				_logger.error("error in validate", t);
			}
		}
	}
	
	private boolean checkModelResource(String path) throws Throwable {
		boolean existsJsp = false;
		if (StringUtils.isBlank(path)) {
			return existsJsp;
		}
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(path);
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			if (resource.exists()) {
				return true;
			}
		}
		return false;
	}
	
	public String newModel() {
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}
	
	public String edit() {
		this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		return this.extractPageModelFormValues();
	}
	
	public String showDetails() {
		String result = this.extractPageModelFormValues();
		if (!result.equals(SUCCESS)) return result;
		this.extractReferencingObjects(this.getCode());
		return result;
	}
	
	protected String extractPageModelFormValues() {
		try {
			PageModel pageModel = super.getPageModel(this.getCode());
			if (null == pageModel) {
				this.addActionError(this.getText("error.pageModel.notExist"));
				return "pageModelList";
			}
			this.setDescription(pageModel.getDescription());
			this.setPluginCode(pageModel.getPluginCode());
			
			//TO DELETE - start
			if (null != pageModel.getConfiguration()) {
				PageModelDOM dom = new PageModelDOM(pageModel);
				String xml = dom.getXMLDocument();
				this.setXmlConfiguration(xml);
			}
			//TO DELETE - end
			
			this.setTemplate(pageModel.getTemplate());
		} catch (Throwable t) {
			_logger.error("error in extractPageModelFormValues", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			String check = this.checkModelForDelete();
			if (null != check) return check;
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			String check = this.checkModelForDelete();
			if (null != check) return check;
			this.getPageModelManager().deletePageModel(this.getCode());
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			PageModel model = this.createPageModel();
			if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				this.getPageModelManager().addPageModel(model);
			} else {
				this.getPageModelManager().updatePageModel(model);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected PageModel createPageModel() throws Throwable {
		PageModel model = null;
		try {
			if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				model = new PageModel();
				model.setCode(this.getCode());
			} else {
				model = this.getPageModel(this.getCode());
			}
			model.setDescription(this.getDescription());
			String template = (!StringUtils.isBlank(this.getTemplate())) ? this.getTemplate() : null;
			model.setTemplate(template);
			
			//TO DELETE - start
			String xml = this.getXmlConfiguration();
			PageModelDOM dom = new PageModelDOM(xml, this.getWidgetTypeManager());
			Frame[] configuration = dom.getConfiguration();
			int mainFrame = dom.getMainFrame();
			if (mainFrame > -1 ) {
				model.setMainFrame(mainFrame);
			}
			model.setConfiguration(configuration);
			//TO DELETE - end
		} catch (Throwable t) {
			_logger.error("error in creating page model", t);
			throw t;
		}
		return model;
	}
	
	protected String checkModelForDelete() throws ApsSystemException {
		PageModel model = super.getPageModel(this.getCode());
		if (null == model) {
			this.addActionError(this.getText("error.pageModel.notExist"));
			return "pageModelList";
		}
		this.extractReferencingObjects(this.getCode());
		if (null != this.getReferences() && this.getReferences().size() > 0) {
	        return "references";
		}
		return null;
	}
	
	protected void extractReferencingObjects(String pageModelCode) {
		try {
			PageModel model = super.getPageModel(pageModelCode);
			if (null != model) {
				Map references = this.getPageModelActionHelper().getReferencingObjects(model, this.getRequest());
				if (null != references && references.size() > 0) {
					this.setReferences(references);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting referenced objects by page model '{}'", pageModelCode, t);
		}
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	public Integer getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(Integer strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public String getPluginCode() {
		return _pluginCode;
	}
	public void setPluginCode(String pluginCode) {
		this._pluginCode = pluginCode;
	}
	
	@Deprecated //TO DELETE
	public String getXmlConfiguration() {
		return _xmlConfiguration;
	}
	@Deprecated //TO DELETE
	public void setXmlConfiguration(String xmlConfiguration) {
		this._xmlConfiguration = xmlConfiguration;
	}
	
	public String getTemplate() {
		return _template;
	}
	public void setTemplate(String template) {
		this._template = template;
	}
	
	public Map<String, List<Object>> getReferences() {
		return _references;
	}
	protected void setReferences(Map<String, List<Object>> references) {
		this._references = references;
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	protected IPageModelActionHelper getPageModelActionHelper() {
		return _pageModelActionHelper;
	}
	public void setPageModelActionHelper(IPageModelActionHelper pageModelActionHelper) {
		this._pageModelActionHelper = pageModelActionHelper;
	}
	
	private String _code;
	private Integer _strutsAction;
	private String _description;
	private String _pluginCode;
	@Deprecated
	private String _xmlConfiguration;//TO DELETE
	private String _template;
	
	private Map<String, List<Object>> _references;
	
	private IWidgetTypeManager _widgetTypeManager;
	private IPageModelActionHelper _pageModelActionHelper;
	
}