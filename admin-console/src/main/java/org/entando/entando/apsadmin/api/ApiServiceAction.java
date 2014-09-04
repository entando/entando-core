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
package org.entando.entando.apsadmin.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiMethodParameter;
import org.entando.entando.aps.system.services.api.model.ApiMethodRelatedWidget;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * @author E.Santoboni
 */
public class ApiServiceAction extends AbstractApiAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiServiceAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			this.checkMasterMethod(this.getNamespace(), this.getResourceName());
			this.checkCode();
			this.checkDescriptions();
			this.checkParameters();
		} catch (Throwable t) {
			_logger.error("Error validating service", t);
			throw new RuntimeException("Error validating service", t);
		}
	}

	private void checkDescriptions() {
		this.setDescriptions(new ApsProperties());
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = (Lang) langsIter.next();
			String titleKey = "lang_" + lang.getCode();
			String title = this.getRequest().getParameter(titleKey);
			if (null == title || title.trim().length() == 0) {
				String[] args = {lang.getDescr()};
				this.addFieldError(titleKey, this.getText("error.service.new.insertDescription", args));
			} else {
				this.getDescriptions().put(lang.getCode(), title.trim());
			}
		}
	}

	private void checkCode() {
		String key = this.getServiceKey();
		try {
			if ((this.getStrutsAction() == ApsAdminSystemConstants.ADD
					|| this.getStrutsAction() == ApsAdminSystemConstants.PASTE)
					&& null != key && key.trim().length() > 0) {
				if (null != this.getApiCatalogManager().getApiService(key)) {
					String[] args = {key};
					this.addFieldError("serviceKey", this.getText("error.service.new.duplicateKey", args));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking service key", t);
			throw new RuntimeException("Error checking service key", t);
		}
	}
	
	private void checkParameters() {
		try {
			this.setApiParameterValues(new ApsProperties());
			ApiMethod masterMethod = this.getMethod(this.getNamespace(), this.getResourceName());
			List<ApiMethodParameter> apiParameters = masterMethod.getParameters();
			this.extractFreeParameters(apiParameters);
			this.setApiParameters(apiParameters);
			for (int i = 0; i < apiParameters.size(); i++) {
				ApiMethodParameter apiParameter = apiParameters.get(i);
				String fieldName = apiParameter.getKey() + "_apiParam";
				String value = this.getRequest().getParameter(fieldName);
				if (null != value && value.trim().length() > 0) {
					this.getApiParameterValues().put(apiParameter.getKey(), value);
				}
				boolean isFreeParameter = (null != this.getFreeParameters()) ? this.getFreeParameters().contains(apiParameter.getKey()) : false;
				if (apiParameter.isRequired() && (null == value || value.trim().length() == 0) && !isFreeParameter) {
					this.addFieldError(fieldName, this.getText("error.service.parameter.invalidSetting", new String[]{apiParameter.getKey(), apiParameter.getDescription()}));
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking parameters", t);
			throw new RuntimeException("Error checking parameters", t);
		}
	}
	
	private void extractFreeParameters(List<ApiMethodParameter> apiParameters) {
		if (null == apiParameters) {
			return;
		}
		for (int i = 0; i < apiParameters.size(); i++) {
			ApiMethodParameter apiMethodParameter = apiParameters.get(i);
			String requestParamName = "freeParameter_" + apiMethodParameter.getKey();
			String value = this.getRequest().getParameter(requestParamName);
			if (null != value && Boolean.parseBoolean(value)) {
				this.getFreeParameters().add(apiMethodParameter.getKey());
			}
		}
	}
	
	/**
	 * Create of new api service.
	 * @return The result code.
	 */
	public String newService() {
		try {
			if (null != this.getResourceCode()) {
				String[] sections = this.getResourceCode().split(":");
				if (sections.length == 2) {
					this.setNamespace(sections[0]);
					this.setResourceName(sections[1]);
				} else {
					this.setResourceName(sections[0]);
				}
			}
			String check = this.checkMasterMethod(this.getNamespace(), this.getResourceName());
			if (null != check) {
				return check;
			}
			ApiMethod masterMethod = this.getMethod(this.getNamespace(), this.getResourceName());
			if (null != this.getWidgetTypeCode() && null != masterMethod.getRelatedWidget()) {
				WidgetType type = this.getWidgetTypeManager().getWidgetType(this.getWidgetTypeCode());
				if (null != type && type.isLogic()) {
					ApsProperties parameters =
							this.extractParametersFromWidgetProperties(masterMethod.getRelatedWidget(), type.getConfig());
					this.setApiParameterValues(parameters);
				}
			}
			this.setApiParameters(masterMethod.getParameters());
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			this.setServiceKey(this.buildTempKey(masterMethod.getResourceName()));
		} catch (Throwable t) {
			_logger.error("error in newService", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public ApiMethod getMethod(String namespace, String resourceName) {
		try {
			return this.getApiCatalogManager().getMethod(ApiMethod.HttpMethod.GET, namespace, resourceName);
		} catch (Throwable t) {
			_logger.error("Error extracting GET method of resource '{}' namespace '{}'", resourceName, namespace, t);
		}
		return null;
	}

	public List<Lang> getSystemLangs() {
		return this.getLangManager().getLangs();
	}
	
	/**
	 * @deprecated Use {@link #copyFromWidget()} instead
	 */
	public String copyFromShowlet() {
		return copyFromWidget();
	}

	/**
	 * Copy an exist widget (physic and with parameters, joined with a exist api method) 
	 * and value the form of creation of new api service.
	 * @return The result code.
	 */
	public String copyFromWidget() {
		try {
			String check = this.checkMasterMethod(this.getNamespace(), this.getResourceName());
			if (null != check) {
				return check;
			}
			ApiMethod masterMethod = this.getMethod(this.getNamespace(), this.getResourceName());
			IPage page = this.getPageManager().getPage(this.getPageCode());
			if (null == page) {
				this.addFieldError("pageCode", this.getText("error.service.paste.invalidPageCode", new String[]{this.getPageCode()}));
				return INPUT;
			}
			Widget[] widgets = page.getWidgets();
			if (null == this.getFramePos() || this.getFramePos() > widgets.length || null == widgets[this.getFramePos()]) {
				String framePosString = (null != this.getFramePos()) ? this.getFramePos().toString() : "null";
				this.addFieldError("framePos", this.getText("error.service.paste.invalidFramePos", new String[]{this.getPageCode(), framePosString}));
				return INPUT;
			}
			Widget masterWidget = widgets[this.getFramePos()];
			WidgetType type = (masterWidget.getType().isLogic()) ? masterWidget.getType().getParentType() : masterWidget.getType();
			if (null == masterMethod.getRelatedWidget()
					|| !masterMethod.getRelatedWidget().getWidgetCode().equals(type.getCode())) {
				this.addFieldError("framePos", this.getText("error.service.paste.invalidWidget",
						new String[]{masterWidget.getType().getCode(), masterMethod.getResourceName()}));
				return INPUT;
			}
			ApsProperties parameters = this.extractParametersFromWidget(masterMethod.getRelatedWidget(), masterWidget);
			this.setApiParameterValues(parameters);
			this.setApiParameters(masterMethod.getParameters());
			this.setStrutsAction(ApsAdminSystemConstants.PASTE);
			this.setServiceKey(this.buildTempKey(masterMethod.getResourceName()));
		} catch (Throwable t) {
			_logger.error("error in copyFromWidget", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private ApsProperties extractParametersFromWidget(ApiMethodRelatedWidget relatedWidget, Widget masterWidget) {
		ApsProperties showletProperties = (masterWidget.getType().isLogic())
				? masterWidget.getType().getConfig() : masterWidget.getConfig();
		return this.extractParametersFromWidgetProperties(relatedWidget, showletProperties);
	}

	private ApsProperties extractParametersFromWidgetProperties(ApiMethodRelatedWidget relatedWidget, ApsProperties widgetProperties) {
		ApsProperties parameters = new ApsProperties();
		ApsProperties mapping = relatedWidget.getMapping();
		if (null != widgetProperties && null != mapping) {
			Iterator<Object> keyIter = widgetProperties.keySet().iterator();
			while (keyIter.hasNext()) {
				Object key = keyIter.next();
				if (null != mapping.get(key)) {
					parameters.put(mapping.get(key), widgetProperties.get(key));
				}
			}
		}
		return parameters;
	}

	private String buildTempKey(String masterMethodName) throws Throwable {
		int index = 0;
		String currentCode = null;
		do {
			index++;
			currentCode = masterMethodName + "_" + index;
		} while (null != this.getApiService(currentCode));
		return currentCode;
	}

	/**
	 * Edit an exist api service.
	 * @return The result code.
	 */
	public String edit() {
		try {
			String check = this.checkService();
			if (null != check) {
				return check;
			}
			ApiService apiService = this.getApiService(this.getServiceKey());
			this.setApiParameters(apiService.getMaster().getParameters());
			this.setResourceName(apiService.getMaster().getResourceName());
			this.setNamespace(apiService.getMaster().getNamespace());
			this.setApiParameterValues(apiService.getParameters());
			this.setDescriptions(apiService.getDescription());
			this.setHiddenService(apiService.isHidden());
			this.setActiveService(apiService.isActive());
			this.setMyEntandoService(apiService.isMyEntando());
			this.setServiceKey(apiService.getKey());
			if (null != apiService.getFreeParameters()) {
				List<String> freeParams = Arrays.asList(apiService.getFreeParameters());
				this.setFreeParameters(freeParams);
			}
			this.setTag(apiService.getTag());
			this.setRequiredAuth(apiService.getRequiredAuth());
			this.setRequiredGroup(apiService.getRequiredGroup());
			this.setRequiredPermission(apiService.getRequiredPermission());
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Save an api service.
	 * @return The result code.
	 */
	public String save() {
		try {
			String key = this.getServiceKey().trim();
			ApiMethod masterMethod = this.getMethod(this.getNamespace(), this.getResourceName());
			String[] freeParams = null;
			if (null != this.getFreeParameters()) {
				freeParams = new String[this.getFreeParameters().size()];
				for (int i = 0; i < this.getFreeParameters().size(); i++) {
					freeParams[i] = this.getFreeParameters().get(i);
				}
			}
			ApiService service = new ApiService(key, this.getDescriptions(), masterMethod, this.getApiParameterValues(),
					freeParams, this.getTag(), !this.isHiddenService(), this.isActiveService(), this.isMyEntandoService());
			service.setRequiredAuth(this.getRequiredAuth());
			if (null != this.getRequiredGroup() && this.getRequiredGroup().trim().length() > 0) {
				service.setRequiredGroup(this.getRequiredGroup());
			}
			if (null != this.getRequiredPermission() && this.getRequiredPermission().trim().length() > 0) {
				service.setRequiredPermission(this.getRequiredPermission());
			}
			this.getApiCatalogManager().saveService(service);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Start the deletion operations for the given api service.
	 * @return The result code.
	 */
	public String trash() {
		try {
			String check = this.checkService();
			if (null != check) {
				return check;
			}
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Delete an api service from the system.
	 * @return The result code.
	 */
	public String delete() {
		try {
			String check = this.checkService();
			if (null != check) {
				return check;
			}
			this.getApiCatalogManager().deleteService(this.getServiceKey());
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected String checkMasterMethod(String namespace, String resourceName) throws Throwable {
		if (resourceName == null) {
			this.addActionError(this.getText("error.service.new.masterApiMethod.required"));
			return INPUT;
		}
		ApiMethod masterMethod = this.getMethod(namespace, resourceName);
		if (masterMethod == null) {
			this.addActionError(this.getText("error.service.new.masterApiMethod.invalid"));
			return INPUT;
		}
		if (!masterMethod.isCanSpawnOthers()) {
			if (null != namespace) {
				String[] args = {masterMethod.getResourceName(), masterMethod.getNamespace()};
				this.addActionError(this.getText("error.service.new.masterApiMethod.unspawnable2", args));
			} else {
				String[] args = {masterMethod.getResourceName()};
				this.addActionError(this.getText("error.service.new.masterApiMethod.unspawnable", args));
			}
			return INPUT;
		}
		return null;
	}
	
    public String generateResponseBodySchema() {
        try {
            String result = this.checkService();
			if (null != result) return result;
			ApiService apiService = this.getApiService(this.getServiceKey());
            return super.generateResponseBodySchema(apiService.getMaster());
        } catch (Throwable t) {
        	_logger.error("Error extracting response body Schema", t);
            return FAILURE;
        }
    }
	
	protected String checkService() throws Throwable {
		ApiService apiService = this.getApiService(this.getServiceKey());
		if (apiService == null) {
			this.addActionError(this.getText("error.service.invalid", new String[]{this.getServiceKey()}));
			return INPUT;
		}
		return null;
	}
	
	/**
	 * Return the list of system groups.
	 * @return The list of system groups.
	 */
	public List<Group> getGroups() {
		return this.getGroupManager().getGroups();
	}
	
	public Group getGroup(String name) {
		return this.getGroupManager().getGroup(name);
	}
	
	public String getServiceGroup() {
		return _serviceGroup;
	}
	public void setServiceGroup(String serviceGroup) {
		this._serviceGroup = serviceGroup;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public String getResourceCode() {
		return _resourceCode;
	}
	public void setResourceCode(String resourceCode) {
		this._resourceCode = resourceCode;
	}
	
	public String getNamespace() {
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	public String getResourceName() {
		return _resourceName;
	}
	public void setResourceName(String resourceName) {
		this._resourceName = resourceName;
	}
	
	public String getServiceKey() {
		return _serviceKey;
	}
	public void setServiceKey(String serviceKey) {
		this._serviceKey = serviceKey;
	}

	public ApsProperties getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}

	public boolean isActiveService() {
		return _activeService;
	}
	public void setActiveService(boolean activeService) {
		this._activeService = activeService;
	}
	
	public boolean isHiddenService() {
		return _hiddenService;
	}
	public void setHiddenService(boolean hiddenService) {
		this._hiddenService = hiddenService;
	}
	
	public boolean isMyEntandoService() {
		return _myEntandoService;
	}
	public void setMyEntandoService(boolean myEntandoService) {
		this._myEntandoService = myEntandoService;
	}
	
    public Boolean getRequiredAuth() {
		return _requiredAuth;
    }
    public void setRequiredAuth(Boolean requiredAuth) {
        this._requiredAuth = requiredAuth;
    }
    
	public String getRequiredGroup() {
		return _requiredGroup;
	}
	public void setRequiredGroup(String requiredGroup) {
		this._requiredGroup = requiredGroup;
	}
	
	public String getRequiredPermission() {
		return _requiredPermission;
	}
	public void setRequiredPermission(String requiredPermission) {
		this._requiredPermission = requiredPermission;
	}
	
	public List<ApiMethodParameter> getApiParameters() {
		return _apiParameters;
	}
	public void setApiParameters(List<ApiMethodParameter> apiParameters) {
		this._apiParameters = apiParameters;
	}

	public ApsProperties getApiParameterValues() {
		return _apiParameterValues;
	}
	public void setApiParameterValues(ApsProperties apiParameterValues) {
		this._apiParameterValues = apiParameterValues;
	}

	public List<String> getFreeParameters() {
		return _freeParameters;
	}
	public void setFreeParameters(List<String> freeParameters) {
		this._freeParameters = freeParameters;
	}

	public String getTag() {
		return _tag;
	}
	public void setTag(String tag) {
		this._tag = tag;
	}

	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}

	public Integer getFramePos() {
		return _framePos;
	}
	public void setFramePos(Integer framePos) {
		this._framePos = framePos;
	}
	
	@Deprecated
	public String getShowletTypeCode() {
		return this.getWidgetTypeCode();
	}
	@Deprecated
	public void setShowletTypeCode(String showletTypeCode) {
		this.setWidgetTypeCode(showletTypeCode);
	}
	
	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}
	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	private String _serviceGroup;
	private int _strutsAction;
	
	private String _resourceCode;
	
	private String _resourceName;
	private String _namespace;
	
	private String _serviceKey;
	private ApsProperties _descriptions;
	private boolean _activeService;
	private boolean _hiddenService;
	private boolean _myEntandoService;
	
	private Boolean _requiredAuth;
	private String _requiredPermission;
	private String _requiredGroup;
	
	private List<ApiMethodParameter> _apiParameters;
	private ApsProperties _apiParameterValues;
	private List<String> _freeParameters = new ArrayList<String>();
	private String _tag;
	private String _pageCode;
	private Integer _framePos;
	private String _widgetTypeCode;
	
	private IPageManager _pageManager;
	private IGroupManager _groupManager;
	private IWidgetTypeManager _widgetTypeManager;
	
}