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
package org.entando.entando.aps.system.services.widgettype.api;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.IApiExportable;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.LinkedListItem;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.guifragment.api.JAXBGuiFragment;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ApiWidgetTypeInterface implements IApiExportable {
	
	private static final Logger _logger = LoggerFactory.getLogger(ApiWidgetTypeInterface.class);
	
	public List<LinkedListItem> getWidgetTypes(Properties properties) throws Throwable {
		List<LinkedListItem> list = new ArrayList<LinkedListItem>();
		try {
			List<WidgetType> types = this.getWidgetTypeManager().getWidgetTypes();
			for (int i = 0; i < types.size(); i++) {
				WidgetType widgetType = types.get(i);
				String url = this.getApiResourceUrl(widgetType, properties.getProperty(SystemConstants.API_APPLICATION_BASE_URL_PARAMETER), 
						properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER), (MediaType) properties.get(SystemConstants.API_PRODUCES_MEDIA_TYPE_PARAMETER));
				LinkedListItem item = new LinkedListItem();
				item.setCode(widgetType.getCode());
				item.setUrl(url);
				list.add(item);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting list of widget types", t);
			throw t;
		}
		return list;
	}
	
    public JAXBWidgetType getWidgetType(Properties properties) throws ApiException, Throwable {
        String widgetTypeCode = properties.getProperty("code");
		JAXBWidgetType jaxbWidgetType = null;
		try {
			WidgetType widgetType = this.getWidgetTypeManager().getWidgetType(widgetTypeCode);
			if (null == widgetType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "WidgetType with code '" + widgetTypeCode + "' does not exist", Response.Status.CONFLICT);
			}
			GuiFragment singleGuiFragment = null;
			List<GuiFragment> fragments = new ArrayList<GuiFragment>();
			if (!widgetType.isLogic()) {
				singleGuiFragment = this.getGuiFragmentManager().getUniqueGuiFragmentByWidgetType(widgetTypeCode);
			} else {
				List<String> fragmentCodes = this.getGuiFragmentManager().getGuiFragmentCodesByWidgetType(widgetTypeCode);
				if (null != fragmentCodes) {
					for (int i = 0; i < fragmentCodes.size(); i++) {
						String fragmentCode = fragmentCodes.get(i);
						GuiFragment fragment = this.getGuiFragmentManager().getGuiFragment(fragmentCode);
						if (null != fragment) {
							fragments.add(fragment);
						}
					}
				}
			}
			jaxbWidgetType = new JAXBWidgetType(widgetType, singleGuiFragment, fragments);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error creating widget type - code '{}'", widgetTypeCode, t);
			throw t;
		}
        return jaxbWidgetType;
    }
	
    public void addWidgetType(JAXBWidgetType jaxbWidgetType) throws ApiException, Throwable {
		List<GuiFragment> addedFragments = new ArrayList<GuiFragment>();
		List<GuiFragment> updatedFragments = new ArrayList<GuiFragment>();
		try {
			WidgetType widgetType = this.getWidgetTypeManager().getWidgetType(jaxbWidgetType.getCode());
			if (null != widgetType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "WidgetType with code " + jaxbWidgetType.getCode() + " already exists", Response.Status.CONFLICT);
			}
			widgetType = jaxbWidgetType.getNewWidgetType(this.getWidgetTypeManager());
			if (!widgetType.isLogic() && StringUtils.isBlank(jaxbWidgetType.getGui())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Gui is mandatory", Response.Status.CONFLICT);
			}
			if (widgetType.isLogic() && (StringUtils.isNotBlank(jaxbWidgetType.getGui()) || (null != jaxbWidgetType.getFragments() && jaxbWidgetType.getFragments().size() > 0))) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Fragment mustn't be added on the new logic widget type", Response.Status.CONFLICT);
			}
			if (widgetType.isLogic() && this.isInternalServletWidget(widgetType.getParentType().getCode())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Logic type with parent 'Internal Servlet' widget mustn't be added", Response.Status.CONFLICT);
			}
			this.getWidgetTypeManager().addWidgetType(widgetType);
			if (!widgetType.isLogic()) {
				this.checkAndSaveFragment(widgetType, jaxbWidgetType, true, null, addedFragments, updatedFragments);
			}
		} catch (ApiException ae) {
			this.revertPreviousObject(null, addedFragments, updatedFragments);
			throw ae;
		} catch (Throwable t) {
			this.revertPreviousObject(null, addedFragments, updatedFragments);
			this.getWidgetTypeManager().deleteWidgetType(jaxbWidgetType.getCode());
			_logger.error("Error adding new widget type", t);
			throw t;
		}
    }
	
    public StringApiResponse updateWidgetType(JAXBWidgetType jaxbWidgetType) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
		WidgetType widgetTypeToUpdate = null;
		List<GuiFragment> addedFragments = new ArrayList<GuiFragment>();
		List<GuiFragment> updatedFragments = new ArrayList<GuiFragment>();
		try {
			widgetTypeToUpdate = this.getWidgetTypeManager().getWidgetType(jaxbWidgetType.getCode());
			if (null == widgetTypeToUpdate) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "WidgetType with code " + jaxbWidgetType.getCode() + " does not exists", Response.Status.CONFLICT);
			}
			WidgetType widgetType = jaxbWidgetType.getModifiedWidgetType(this.getWidgetTypeManager());
			this.checkAndSaveFragment(widgetType, jaxbWidgetType, false, response, addedFragments, updatedFragments);
			this.getWidgetTypeManager().updateWidgetType(widgetType.getCode(), 
					widgetType.getTitles(), widgetType.getConfig(), widgetType.getMainGroup());
			response.setResult(IResponseBuilder.SUCCESS, null);
		} catch (ApiException ae) {
			this.revertPreviousObject(widgetTypeToUpdate, addedFragments, updatedFragments);
			throw ae;
		} catch (Throwable t) {
			this.revertPreviousObject(widgetTypeToUpdate, addedFragments, updatedFragments);
			_logger.error("Error updating widget type", t);
			throw t;
		}
		return response;
    }
	
	private void revertPreviousObject(WidgetType widgetTypeToUpdate, List<GuiFragment> addedFragments, List<GuiFragment> updatedFragments) throws Throwable {
		if (null != widgetTypeToUpdate) {
			this.getWidgetTypeManager().updateWidgetType(widgetTypeToUpdate.getCode(), 
					widgetTypeToUpdate.getTitles(), widgetTypeToUpdate.getConfig(), widgetTypeToUpdate.getMainGroup());
		}
		for (int i = 0; i < addedFragments.size(); i++) {
			GuiFragment guiFragment = addedFragments.get(i);
			this.getGuiFragmentManager().deleteGuiFragment(guiFragment.getCode());
		}
		for (int i = 0; i < updatedFragments.size(); i++) {
			GuiFragment guiFragment = updatedFragments.get(i);
			this.getGuiFragmentManager().updateGuiFragment(guiFragment);
		}
	}
	
	protected void checkAndSaveFragment(WidgetType type, JAXBWidgetType jaxbWidgetType, 
			boolean isAdd, StringApiResponse response, List<GuiFragment> addedFragments, List<GuiFragment> updatedFragment) throws Throwable {
		try {
			if (!type.isLogic() && !this.isInternalServletWidget(type.getCode())) {
				GuiFragment guiFragment = this.getGuiFragmentManager().getUniqueGuiFragmentByWidgetType(type.getCode());
				if (StringUtils.isNotBlank(jaxbWidgetType.getGui())) {
					if (null == guiFragment) {
						guiFragment = new GuiFragment();
						String code = this.extractUniqueGuiFragmentCode(type.getCode());
						guiFragment.setCode(code);
						guiFragment.setPluginCode(type.getPluginCode());
						guiFragment.setGui(jaxbWidgetType.getGui());
						guiFragment.setWidgetTypeCode(type.getCode());
						addedFragments.add(guiFragment);
						this.getGuiFragmentManager().addGuiFragment(guiFragment);
					} else if (!isAdd) {
						GuiFragment clone = guiFragment.clone();
						updatedFragment.add(guiFragment);
						clone.setGui(jaxbWidgetType.getGui());
						this.getGuiFragmentManager().updateGuiFragment(clone);
					}
				} else {
					if (null != guiFragment && !isAdd) {
						if (StringUtils.isNotBlank(guiFragment.getDefaultGui())) {
							GuiFragment clone = guiFragment.clone();
							updatedFragment.add(guiFragment);
							clone.setGui(null);
							this.getGuiFragmentManager().updateGuiFragment(clone);
						} else {
							//nothing to do...
							//this.getGuiFragmentManager().deleteGuiFragment(guiFragment.getCode());
						}
					}
				}
			} else if (type.isLogic() && !isAdd) {
				boolean isInternalServlet = this.isInternalServletWidget(type.getParentType().getCode());
				if (!isInternalServlet && (null != jaxbWidgetType.getFragments() && jaxbWidgetType.getFragments().size() > 0)) {
					if (null != response) {
						ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, "Fragments mustn't be updated on a 'not internal servlet' logic widget type");
						response.addError(error);
					}
					//throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Fragments mustn't be updated on a 'not internal servlet' logic widget type", Response.Status.CONFLICT);
				} else {
					List<JAXBGuiFragment> fragments = jaxbWidgetType.getFragments();
					if (null != fragments && fragments.size() > 0) {
						for (int i = 0; i < fragments.size(); i++) {
							JAXBGuiFragment jaxbGuiFragment = fragments.get(i);
							GuiFragment fragment = jaxbGuiFragment.getGuiFragment();
							GuiFragment existingFragment = this.getGuiFragmentManager().getGuiFragment(fragment.getCode());
							if (null != existingFragment) {
								if (StringUtils.isBlank(existingFragment.getDefaultGui()) && StringUtils.isBlank(jaxbWidgetType.getGui())) {
									ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, "one between A and B must be valued on fragment '" + existingFragment.getCode() + "'");
									response.addError(error);
									continue;
								}
								GuiFragment clone = existingFragment.clone();
								updatedFragment.add(existingFragment);
								clone.setGui(jaxbGuiFragment.getGui());
							} else {
								ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, "Fragment '" + fragment.getCode() + "' does not exists");
								response.addError(error);
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error checking and saving fragment", t);
			throw new ApsSystemException("error checking and saving fragment", t);
		}
	}
	
	// duplicated code
	protected String extractUniqueGuiFragmentCode(String widgetTypeCode) throws ApsSystemException {
		String uniqueCode = widgetTypeCode;
		if (null != this.getGuiFragmentManager().getGuiFragment(uniqueCode)) {
			int index = 0;
			String currentCode = null;
			do {
				index++;
				currentCode = uniqueCode + "_" + index;
			} while (null != this.getGuiFragmentManager().getGuiFragment(currentCode));
			uniqueCode = currentCode;
		}
		return uniqueCode;
	}
	
	public void deleteWidgetType(Properties properties) throws ApiException, Throwable {
		String code = properties.getProperty("code");
		try {
			WidgetType widgetType = this.getWidgetTypeManager().getWidgetType(code);
			if (null == widgetType) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Widget Type with code " + code + " does not exists", Response.Status.CONFLICT);
			}
			if (widgetType.isLocked()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Widget Type '" + code + "' is locked", Response.Status.CONFLICT);
			}
			List<IPage> referencedPages = this.getPageManager().getWidgetUtilizers(code);
			if (null != referencedPages && !referencedPages.isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Widget Type '" + code + "' is published into some pages", Response.Status.CONFLICT);
			}
			this.getWidgetTypeManager().deleteWidgetType(code);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error deleting widget type throw api", t);
			throw t;
		}
	}
	
	@Override
	public String getApiResourceUrl(Object object, String applicationBaseUrl, String langCode, MediaType mediaType) {
		if (!(object instanceof WidgetType) || null == applicationBaseUrl || null == langCode) {
			return null;
		}
		WidgetType widgetType = (WidgetType) object;
		StringBuilder stringBuilder = new StringBuilder(applicationBaseUrl);
		stringBuilder.append("api/rs/").append(langCode).append("/core/widgetType");//?code=").append(widgetType.getCode());
		if (null == mediaType || mediaType.equals(MediaType.APPLICATION_XML_TYPE)) {
			stringBuilder.append(".xml");
		} else {
			stringBuilder.append(".json");
		}
		stringBuilder.append("?code=").append(widgetType.getCode());
		return stringBuilder.toString();
	}
	
	public boolean isInternalServletWidget(String widgetTypeCode) {
		return this.getInternalServletWidgetCode().equals(widgetTypeCode);
	}
	
	protected String getInternalServletWidgetCode() {
		return _internalServletWidgetCode;
	}
	public void setInternalServletWidgetCode(String internalServletWidgetCode) {
		this._internalServletWidgetCode = internalServletWidgetCode;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
	
	protected IGuiFragmentManager getGuiFragmentManager() {
		return _guiFragmentManager;
	}
	public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
		this._guiFragmentManager = guiFragmentManager;
	}
	
	private String _internalServletWidgetCode;
	
	private IPageManager _pageManager;
	private IWidgetTypeManager _widgetTypeManager;
	private IGuiFragmentManager _guiFragmentManager;
	
}