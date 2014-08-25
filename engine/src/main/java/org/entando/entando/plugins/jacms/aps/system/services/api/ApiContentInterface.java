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
package org.entando.entando.plugins.jacms.aps.system.services.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.plugins.jacms.aps.system.services.api.model.ApiContentListBean;
import org.entando.entando.plugins.jacms.aps.system.services.api.model.JAXBContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentRenderizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;

/**
 * @author E.Santoboni
 */
public class ApiContentInterface extends AbstractCmsApiInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiContentInterface.class);
	
    public List<String> getContents(Properties properties) throws Throwable {
        return this.extractContents(properties);
    }

    protected List<String> extractContents(Properties properties) throws Throwable {
        List<String> contentsId = null;
        try {
            ApiContentListBean bean = this.buildSearchBean(properties);
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            contentsId = this.getContentListHelper().getContentsId(bean, user);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in extractContents", t);
            //ApsSystemUtils.logThrowable(t, this, "getContents");
            throw new ApsSystemException("Error into API method", t);
        }
        return contentsId;
    }

    protected ApiContentListBean buildSearchBean(Properties properties) throws ApiException, Throwable {
        ApiContentListBean bean = null;
        try {
            String contentType = properties.getProperty("contentType");
            if (null == this.getContentManager().getSmallContentTypesMap().get(contentType)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Content Type '" + contentType + "' does not exist", Response.Status.CONFLICT);
            }
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            String filtersParam = properties.getProperty("filters");
            EntitySearchFilter[] filters = this.getContentListHelper().getFilters(contentType, filtersParam, langCode);
            String[] categoryCodes = null;
            String categoriesParam = properties.getProperty("categories");
            if (null != categoriesParam && categoriesParam.trim().length() > 0) {
                categoryCodes = categoriesParam.split(IContentListHelper.CATEGORIES_SEPARATOR);
            }
            bean = new ApiContentListBean(contentType, filters, categoryCodes);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in buildSearchBean", t);
            //ApsSystemUtils.logThrowable(t, this, "buildSearchBean");
            throw new ApsSystemException("Error into API method", t);
        }
        return bean;
    }

    public String getContentsToHtml(Properties properties) throws Throwable {
        StringBuilder render = new StringBuilder();
        try {
            String modelId = properties.getProperty("modelId");
            if (null == modelId || modelId.trim().length() == 0) {
                return null;
            }
            String contentType = properties.getProperty("contentType");
            Content prototype = (Content) this.getContentManager().getEntityPrototype(contentType);
            Integer modelIdInteger = this.checkModel(modelId, prototype);
            if (null == modelIdInteger) {
                return null;
            }
            List<String> contentsId = this.extractContents(properties);
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            render.append(this.getItemsStartElement());
            for (int i = 0; i < contentsId.size(); i++) {
                render.append(this.getItemStartElement());
				ContentRenderizationInfo renderizationInfo = this.getContentDispenser().getRenderizationInfo(contentsId.get(i), modelIdInteger, langCode, null);
	            if (null != renderizationInfo) {
					render.append(renderizationInfo.getCachedRenderedContent());
	            }
                render.append(this.getItemEndElement());
            }
            render.append(this.getItemsEndElement());
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in getContentsToHtml", t);
            //ApsSystemUtils.logThrowable(t, this, "getContentsToHtml");
            throw new ApsSystemException("Error into API method", t);
        }
        return render.toString();
    }

    public JAXBContent getContent(Properties properties) throws ApiException, Throwable {
        JAXBContent jaxbContent = null;
        String id = properties.getProperty("id");
        try {
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            Content mainContent = this.getPublicContent(id);
            jaxbContent = this.getJAXBContentInstance(mainContent, langCode);
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
            if (!this.getContentAuthorizationHelper().isAuth(user, mainContent)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Required content '" + id + "' does not allowed", Response.Status.FORBIDDEN);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in getContent", t);
            //ApsSystemUtils.logThrowable(t, this, "getContent");
            throw new ApsSystemException("Error into API method", t);
        }
        return jaxbContent;
    }

	protected JAXBContent getJAXBContentInstance(Content mainContent, String langCode) {
		return new JAXBContent(mainContent, langCode);
	}

    public String getContentToHtml(Properties properties) throws ApiException, Throwable {
        String render = null;
        String id = properties.getProperty("id");
        String modelId = properties.getProperty("modelId");
        try {
            if (null == modelId || modelId.trim().length() == 0) {
                return null;
            }
            Content mainContent = this.getPublicContent(id);
            Integer modelIdInteger = this.checkModel(modelId, mainContent);
            if (null == modelIdInteger) {
                return null;
            }
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            //render = this.getContentDispenser().getRenderedContent(id, modelIdInteger, langCode, null);
			ContentRenderizationInfo renderizationInfo = this.getContentDispenser().getRenderizationInfo(id, modelIdInteger, langCode, null);
			if (null != renderizationInfo) {
				return renderizationInfo.getCachedRenderedContent();
			}
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in getContentToHtml", t);
            //ApsSystemUtils.logThrowable(t, this, "getContentToHtml");
            throw new ApsSystemException("Error into API method", t);
        }
        return render;
    }
	
    protected Content getPublicContent(String id) throws ApiException, Throwable {
        Content content = null;
        try {
			content = this.getContentManager().loadContent(id, true);
            if (null == content) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Null content by id '" + id + "'", Response.Status.CONFLICT);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error extracting content by id '{}'",id, t);
            //ApsSystemUtils.logThrowable(t, this, "getPublicContent");
            throw new ApsSystemException("Error extracting content by id '" + id + "'", t);
        }
        return content;
    }

    protected Integer checkModel(String modelId, Content content) throws ApiException, Throwable {
        Integer modelIdInteger = null;
        try {
            if (null == modelId || modelId.trim().length() == 0) {
                return null;
            }
            if (modelId.equals("default")) {
                if (null == content.getDefaultModel()) {
                    throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
                            "Invalid 'default' system model for content type '" + content.getTypeCode() + "' - Contact the administrators", Response.Status.ACCEPTED);
                }
                modelIdInteger = Integer.parseInt(content.getDefaultModel());
            } else if (modelId.equals("list")) {
                if (null == content.getListModel()) {
                    throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
                            "Invalid 'list' system model for content type '" + content.getTypeCode() + "' - Contact the administrators", Response.Status.ACCEPTED);
                }
                modelIdInteger = Integer.parseInt(content.getListModel());
            } else {
                try {
                    modelIdInteger = Integer.parseInt(modelId);
                } catch (Throwable t) {
                    throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
							"The model id must be an integer or 'default' or 'list' - '" + modelId + "'", Response.Status.ACCEPTED);
                }
            }
            ContentModel model = this.getContentModelManager().getContentModel(modelIdInteger);
            if (model == null) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
						"The content model with id '" + modelId + "' does not exist", Response.Status.ACCEPTED);
            } else if (!content.getTypeCode().equals(model.getContentType())) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
						"The content model with id '" + modelId + "' does not match with content of type '" + content.getTypeDescr() + "' ", Response.Status.ACCEPTED);
            }
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error checking model id '{}'", modelId, t);
            //ApsSystemUtils.logThrowable(t, this, "checkModel");
            throw new ApsSystemException("Error checking model id '" + modelId + "'", t);
        }
        return modelIdInteger;
    }

    public StringApiResponse addContent(JAXBContent jaxbContent, Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String typeCode = jaxbContent.getTypeCode();
            Content prototype = (Content) this.getContentManager().getEntityPrototype(typeCode);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Content type with code '" + typeCode + "' does not exist", Response.Status.CONFLICT);
            }
            Content content = (Content) jaxbContent.buildEntity(prototype, this.getCategoryManager());
            if (null != content.getId()) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"You cannot specify Content Id", Response.Status.CONFLICT);
            }
			response = this.validateAndSaveContent(content, properties);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error adding content", t);
            //ApsSystemUtils.logThrowable(t, this, "addContent");
            throw new ApsSystemException("Error adding content", t);
        }
        return response;
    }

    public StringApiResponse updateContent(JAXBContent jaxbContent, Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String typeCode = jaxbContent.getTypeCode();
            Content prototype = (Content) this.getContentManager().getEntityPrototype(typeCode);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Content type with code '" + typeCode + "' does not exist", Response.Status.CONFLICT);
            }
            Content content = (Content) jaxbContent.buildEntity(prototype, this.getCategoryManager());
            Content masterContent = this.getContentManager().loadContent(content.getId(), false);
            if (null == masterContent) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Content with code '" + content.getId() + "' does not exist", Response.Status.CONFLICT);
            } else if (!masterContent.getMainGroup().equals(content.getMainGroup())) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Invalid main group " + content.getMainGroup() + " not equals then master " + masterContent.getMainGroup(), Response.Status.CONFLICT);
            }
            response = this.validateAndSaveContent(content, properties);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error updating content", t);
            //ApsSystemUtils.logThrowable(t, this, "updateContent");
            throw new ApsSystemException("Error updating content", t);
        }
        return response;
    }

    private StringApiResponse validateAndSaveContent(Content content, Properties properties) throws ApiException, Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
            if (!this.getContentAuthorizationHelper().isAuth(user, content)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Content groups makes the new content not allowed for user " + user.getUsername(), Response.Status.FORBIDDEN);
            }
            List<ApiError> errors = this.validate(content);
            if (errors.size() > 0) {
                response.addErrors(errors);
                response.setResult(IResponseBuilder.FAILURE, null);
                return response;
            }
            String insertOnLineString = properties.getProperty("insertOnLine");
            boolean insertOnLine = (null != insertOnLineString) ? Boolean.parseBoolean(insertOnLineString) : false;
            if (!insertOnLine) {
                this.getContentManager().saveContent(content);
            } else {
                this.getContentManager().insertOnLineContent(content);
            }
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("error in validateAndSaveContent", t);
            //ApsSystemUtils.logThrowable(t, this, "addContent");
            throw new ApsSystemException("Error adding content", t);
        }
        return response;
    }

    private List<ApiError> validate(Content content) throws ApsSystemException {
        List<ApiError> errors = new ArrayList<ApiError>();
        try {
            if (null == content.getMainGroup()) {
                errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, "Main group null", Response.Status.CONFLICT));
            }
            List<FieldError> fieldErrors = content.validate(this.getGroupManager());
            if (null != fieldErrors) {
                for (int i = 0; i < fieldErrors.size(); i++) {
                    FieldError fieldError = fieldErrors.get(i);
                    if (fieldError instanceof AttributeFieldError) {
                        AttributeFieldError attributeError = (AttributeFieldError) fieldError;
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, attributeError.getFullMessage(), Response.Status.CONFLICT));
                    } else {
                        errors.add(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, fieldError.getMessage(), Response.Status.CONFLICT));
                    }
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error validating content", t);
            //ApsSystemUtils.logThrowable(t, this, "validate");
            throw new ApsSystemException("Error validating content", t);
        }
        return errors;
    }

    public StringApiResponse deleteContent(Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
        try {
            String id = properties.getProperty("id");
            Content masterContent = this.getContentManager().loadContent(id, false);
            if (null == masterContent) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Content with code '" + id + "' does not exist", Response.Status.CONFLICT);
            }
            UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
            if (!this.getContentAuthorizationHelper().isAuth(user, masterContent)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Content groups makes the new content not allowed for user " + user.getUsername(), Response.Status.FORBIDDEN);
            }
            List<String> references = ((ContentUtilizer) this.getContentManager()).getContentUtilizers(id);
            if (references != null && references.size() > 0) {
                boolean found = false;
                for (int i = 0; i < references.size(); i++) {
                    String reference = references.get(i);
                    ContentRecordVO record = this.getContentManager().loadContentVO(reference);
                    if (null != record) {
                        found = true;
                        response.addError(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR,
                                "Content " + id + " referenced to content " + record.getId() + " - '" + record.getDescr() + "'", Response.Status.CONFLICT));
                    }
                }
                if (found) {
                    response.setResult(IResponseBuilder.FAILURE, null);
                    return response;
                }
            }
            if (masterContent.isOnLine()) {
                this.getContentManager().removeOnLineContent(masterContent);
            }
            String removeWorkVersionString = properties.getProperty("removeWorkVersion");
            boolean removeWorkVersion = (null != removeWorkVersionString) ? Boolean.parseBoolean(removeWorkVersionString) : false;
            if (removeWorkVersion) {
                this.getContentManager().deleteContent(masterContent);
            }
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error deleting content", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteContent");
            throw new ApsSystemException("Error deleting content", t);
        }
        return response;
    }

    protected IContentListHelper getContentListHelper() {
        return _contentListHelper;
    }
    public void setContentListHelper(IContentListHelper contentListHelper) {
        this._contentListHelper = contentListHelper;
    }

    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }

    protected ICategoryManager getCategoryManager() {
        return _categoryManager;
    }
    public void setCategoryManager(ICategoryManager categoryManager) {
        this._categoryManager = categoryManager;
    }

    protected IGroupManager getGroupManager() {
        return _groupManager;
    }
    public void setGroupManager(IGroupManager groupManager) {
        this._groupManager = groupManager;
    }

    protected IPageManager getPageManager() {
        return _pageManager;
    }
    public void setPageManager(IPageManager pageManager) {
        this._pageManager = pageManager;
    }

    protected IResourceManager getResourceManager() {
        return _resourceManager;
    }
    public void setResourceManager(IResourceManager resourceManager) {
        this._resourceManager = resourceManager;
    }

    protected IContentAuthorizationHelper getContentAuthorizationHelper() {
        return _contentAuthorizationHelper;
    }
    public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
        this._contentAuthorizationHelper = contentAuthorizationHelper;
    }
	/*
    protected ICmsCacheWrapperManager getCmsCacheWrapperManager() {
        return _cmsCacheWrapperManager;
    }
    public void setCmsCacheWrapperManager(ICmsCacheWrapperManager cmsCacheWrapperManager) {
        this._cmsCacheWrapperManager = cmsCacheWrapperManager;
    }
	*/
    protected IContentDispenser getContentDispenser() {
        return _contentDispenser;
    }
    public void setContentDispenser(IContentDispenser contentDispenser) {
        this._contentDispenser = contentDispenser;
    }

    public String getItemsStartElement() {
        return _itemsStartElement;
    }
    public void setItemsStartElement(String itemsStartElement) {
        this._itemsStartElement = itemsStartElement;
    }

    public String getItemStartElement() {
        return _itemStartElement;
    }
    public void setItemStartElement(String itemStartElement) {
        this._itemStartElement = itemStartElement;
    }

    public String getItemEndElement() {
        return _itemEndElement;
    }
    public void setItemEndElement(String itemEndElement) {
        this._itemEndElement = itemEndElement;
    }

    public String getItemsEndElement() {
        return _itemsEndElement;
    }
    public void setItemsEndElement(String itemsEndElement) {
        this._itemsEndElement = itemsEndElement;
    }

    private IContentListHelper _contentListHelper;
    private IUserManager _userManager;
    private ICategoryManager _categoryManager;
    private IGroupManager _groupManager;
    private IPageManager _pageManager;
    private IResourceManager _resourceManager;
    private IContentAuthorizationHelper _contentAuthorizationHelper;
    //private ICmsCacheWrapperManager _cmsCacheWrapperManager;
    private IContentDispenser _contentDispenser;
    private String _itemsStartElement = "<ul>";
    private String _itemStartElement = "<li>";
    private String _itemEndElement = "</li>";
    private String _itemsEndElement = "</ul>";

}