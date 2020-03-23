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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.json.JSONUtil;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.apsadmin.portal.model.helper.IPageModelActionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.FrameSketch;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelDOM;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class PageModelAction extends AbstractPageModelAction implements ServletResponseAware {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageModelAction.class);

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}

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
				if (!this.checkModelConfiguration()) {
					this.addFieldError("xmlConfiguration", this.getText("error.pageModel.invalidConfiguration"));
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
	
	private boolean checkModelConfiguration() throws Throwable {
		try {
			String xml = this.getXmlConfiguration();
			PageModelDOM dom = new PageModelDOM(xml, this.getWidgetTypeManager());
			Frame[] configuration = dom.getConfiguration();
			return true;
		} catch (Exception e) {
			return false;
		}
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

	/**
	 * Updates the sketch info.
	 * The body format must be the same returned by /do/rs/PageModel/frames?code=code
	 * @return 200 - the page template xml representation, updated with the sketch info provided;
	 */
	@SuppressWarnings("unchecked")
	public String updateSketch() {
		try {
			PageModel pageModel = this.getPageModelManager().getPageModel(this.getCode());
			if (null != pageModel) {
				String payload = getBody(this.getRequest());
				if (StringUtils.isNotBlank(payload)) {
					List<Object> list = (List<Object>) JSONUtil.deserialize(payload);
					Iterator<Object> it = list.iterator();
					while (it.hasNext()) {
						Map<String, Object> framemap = (Map<String, Object>) it.next();
						Long posVal = (Long)framemap.get("pos");
						Map<String, Object> sketchMap = (Map<String, Object>) framemap.get("sketch");
						if (null != sketchMap) {
							Number x1 = (Number) sketchMap.get("x1");
							Number y1 = (Number) sketchMap.get("y1");
							Number x2 = (Number) sketchMap.get("x2");
							Number y2 = (Number) sketchMap.get("y2");
							FrameSketch fs = new FrameSketch();
							fs.setCoords(x1.intValue(), y1.intValue(), x2.intValue(), y2.intValue());
							if (null != pageModel.getFrameConfig(posVal.intValue())){
								pageModel.getFrameConfig(posVal.intValue()).setSketch(fs);
							}
						}
					}
					this.getPageModelManager().updatePageModel(pageModel);
					String xml = new PageModelDOM(pageModel).getXMLDocument();
					this._response.getWriter().write(xml);
					this._response.setStatus(HttpStatus.OK.value());
					return Action.NONE;					
				}
			}
		} catch (Throwable t) {
			_logger.error("error in updateSketch", t);
			this._response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Action.NONE;
		}
		this._response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
		return Action.NONE;
	}

	public static String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }
	    body = stringBuilder.toString();
	    return body;
	}

	public Frame[] getFramesSketch() {
		PageModel pageModel = super.getPageModel(this.getCode());
		if (null == pageModel) {
			this._response.setStatus(HttpStatus.NOT_FOUND.value());
			return null;
		}
		return pageModel.getFramesConfig();
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
			_logger.error("error in creating page template", t);
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
			_logger.error("Error extracting referenced objects by page template '{}'", pageModelCode, t);
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
	private HttpServletResponse _response;
	
	
}