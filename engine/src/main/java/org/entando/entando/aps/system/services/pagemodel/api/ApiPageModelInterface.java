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
package org.entando.entando.aps.system.services.pagemodel.api;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelUtilizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.IApiExportable;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.LinkedListItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @author E.Santoboni
 */
public class ApiPageModelInterface implements BeanFactoryAware, IApiExportable {
	
	private static final Logger _logger = LoggerFactory.getLogger(ApiPageModelInterface.class);
	
	public List<LinkedListItem> getPageModels(Properties properties) throws Throwable {
		List<LinkedListItem> list = new ArrayList<LinkedListItem>();
		try {
			Collection<PageModel> pageModels = this.getPageModelManager().getPageModels();
			if (null != pageModels) {
				Iterator<PageModel> iter = pageModels.iterator();
				while (iter.hasNext()) {
					PageModel pageModel = iter.next();
					String url = this.getApiResourceUrl(pageModel, properties.getProperty(SystemConstants.API_APPLICATION_BASE_URL_PARAMETER), 
							properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER), (MediaType) properties.get(SystemConstants.API_PRODUCES_MEDIA_TYPE_PARAMETER));
					LinkedListItem item = new LinkedListItem();
					item.setCode(pageModel.getCode());
					item.setUrl(url);
					list.add(item);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting list of models", t);
			throw t;
		}
		return list;
	}
	
    public PageModel getPageModel(Properties properties) throws ApiException, Throwable {
        String code = properties.getProperty("code");
		PageModel pageModel = null;
		try {
			pageModel = this.getPageModelManager().getPageModel(code);
			if (null == pageModel) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "PageModel with code '" + code + "' does not exist", Response.Status.CONFLICT);
			}
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error creating page model - code '{}'", code, t);
			throw t;
		}
        return pageModel;
    }
	
    public void addPageModel(PageModel pageModel) throws ApiException, Throwable {
		try {
			if (null != this.getPageModelManager().getPageModel(pageModel.getCode())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "PageModel with code " + pageModel.getCode() + " already exists", Response.Status.CONFLICT);
			}
			this.getPageModelManager().addPageModel(pageModel);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error adding new page model", t);
			throw t;
		}
    }
	
    public void updatePageModel(PageModel pageModel) throws ApiException, Throwable {
		try {
			if (null != this.getPageModelManager().getPageModel(pageModel.getCode())) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "PageModel with code '" + pageModel.getCode() + "' does not exist", Response.Status.CONFLICT);
			}
			this.getPageModelManager().updatePageModel(pageModel);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error updating page model", t);
			throw t;
		}
    }
	
    public void deletePageModel(Properties properties) throws ApiException, Throwable {
        String code = properties.getProperty("code");
		try {
			PageModel pageModel = this.getPageModelManager().getPageModel(code);
			if (null == pageModel) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "PageModel with code '" + code + "' does not exist", Response.Status.CONFLICT);
			}
			Map<String, List<Object>> references = new HashMap<String, List<Object>>();
			ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
			String[] defNames = factory.getBeanNamesForType(PageModelUtilizer.class);
			for (int i=0; i < defNames.length; i++) {
				Object service = null;
				try {
					service = this.getBeanFactory().getBean(defNames[i]);
				} catch (Throwable t) {
					_logger.error("error extracting bean with name '{}'", defNames[i], t);
					throw new ApsSystemException("error extracting bean with name '" + defNames[i] + "'", t);
				}
				if (service != null) {
					PageModelUtilizer pageModelUtilizer = (PageModelUtilizer) service;
					List<Object> utilizers = pageModelUtilizer.getPageModelUtilizers(code);
					if (utilizers != null && !utilizers.isEmpty()) {
						references.put(pageModelUtilizer.getName(), utilizers);
					}
				}
			}
			if (!references.isEmpty()) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "PageModel with code " + code + " has references with other object", Response.Status.CONFLICT);
			}
			this.getPageModelManager().deletePageModel(code);
		} catch (ApiException ae) {
			throw ae;
		} catch (Throwable t) {
			_logger.error("Error deleting page model throw api", t);
			throw t;
		}
    }
	
	@Override
	public String getApiResourceUrl(Object object, String applicationBaseUrl, String langCode, MediaType mediaType) {
		if (!(object instanceof PageModel) || null == applicationBaseUrl || null == langCode) {
			return null;
		}
		PageModel pageModel = (PageModel) object;
		StringBuilder stringBuilder = new StringBuilder(applicationBaseUrl);
		stringBuilder.append("api/rs/").append(langCode).append("/core/pageModel");//?code=").append(pageModel.getCode());
		if (null == mediaType || mediaType.equals(MediaType.APPLICATION_XML_TYPE)) {
			stringBuilder.append(".xml");
		} else {
			stringBuilder.append(".json");
		}
		stringBuilder.append("?code=").append(pageModel.getCode());
		return stringBuilder.toString();
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}
	
	private BeanFactory _beanFactory;
	private IPageModelManager _pageModelManager;
	
}