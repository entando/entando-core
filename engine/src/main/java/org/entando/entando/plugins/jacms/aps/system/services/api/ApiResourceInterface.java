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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.plugins.jacms.aps.system.services.api.model.JAXBResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.ResourceUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * @author E.Santoboni
 */
public class ApiResourceInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiResourceInterface.class);
	
	public List<String> getImages(Properties properties) throws Throwable {
		properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_IMAGE_CODE);
        return this.getResources(properties);
    }

	public List<String> getAttachments(Properties properties) throws Throwable {
		properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_ATTACH_CODE);
        return this.getResources(properties);
    }

	public List<String> getResources(Properties properties) throws Throwable {
		List<String> resources = null;
		try {
			String resourceTypeCode = properties.getProperty(RESOURCE_TYPE_CODE_PARAM);
			String description = properties.getProperty("description");
			String filename = properties.getProperty("filename");
			String category = properties.getProperty("category");
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
			List<String> groupsCodes = this.getAllowedGroupCodes(user);
            resources = this.getResourceManager().searchResourcesId(resourceTypeCode,
					description, filename, category, groupsCodes);
        } catch (Throwable t) {
        	_logger.error("error in getResources", t);
            //ApsSystemUtils.logThrowable(t, this, "getResources");
            throw t;
        }
        return resources;
    }

	private List<String> getAllowedGroupCodes(UserDetails user) {
		List<Group> groups = new ArrayList<Group>();
		List<Group> groupsOfUser = this.getAuthorizationManager().getUserGroups(user);
		if (this.getAuthorizationManager().isAuthOnGroup(user, Group.ADMINS_GROUP_NAME)) {
			groups.addAll(this.getGroupManager().getGroups());
		} else {
			groups.addAll(groupsOfUser);
		}
		List<String> codes = new ArrayList<String>();
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			if (null != group) {
				codes.add(group.getName());
			}
		}
		if (!codes.contains(Group.FREE_GROUP_NAME)) {
			codes.add(Group.FREE_GROUP_NAME);
		}
    	return codes;
    }

	public JAXBResource getImage(Properties properties) throws Throwable {
        properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_IMAGE_CODE);
        return this.getResource(properties);
    }

	public JAXBResource getAttachment(Properties properties) throws Throwable {
        properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_ATTACH_CODE);
        return this.getResource(properties);
    }

	public JAXBResource getResource(Properties properties) throws Throwable {
		JAXBResource jaxbResource = null;
        String id = properties.getProperty("id");
        String resourceTypeCode = properties.getProperty(RESOURCE_TYPE_CODE_PARAM);
        try {
			ResourceInterface resource = this.getResourceManager().loadResource(id);
			if (null == resource || !resource.getType().equalsIgnoreCase(resourceTypeCode)) {
				throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
						"Null resource by id '" + id + "'", Response.Status.CONFLICT);
			}
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
			String groupName = resource.getMainGroup();
            if (!Group.FREE_GROUP_NAME.equals(groupName) && !this.getAuthorizationManager().isAuthOnGroup(user, groupName)) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Required resource '" + id + "' does not allowed", Response.Status.FORBIDDEN);
            }
			jaxbResource = new JAXBResource(resource);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in getResource", t);
            //ApsSystemUtils.logThrowable(t, this, "getResource");
            throw new ApsSystemException("Error into API method", t);
        }
        return jaxbResource;
    }

	public StringApiResponse addImage(JAXBResource jaxbResource, Properties properties) throws Throwable {
		this.checkType(jaxbResource, JacmsSystemConstants.RESOURE_IMAGE_CODE);
        properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_IMAGE_CODE);
        return this.addResource(jaxbResource, properties);
    }

	public StringApiResponse addAttachment(JAXBResource jaxbResource, Properties properties) throws Throwable {
		this.checkType(jaxbResource, JacmsSystemConstants.RESOURE_ATTACH_CODE);
        properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_ATTACH_CODE);
        return this.addResource(jaxbResource, properties);
    }

	public StringApiResponse addResource(JAXBResource jaxbResource, Properties properties) throws ApiException, Throwable {
        StringApiResponse response = new StringApiResponse();
		BaseResourceDataBean bean = null;
		try {
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            this.check(jaxbResource, user, response, true);
			if (null != response.getErrors() && !response.getErrors().isEmpty()) {
				return response;
			}
			bean = jaxbResource.createBataBean(this.getCategoryManager());
			String id = bean.getResourceId();
			if (null != id && id.trim().length() > 0) {
				Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
				Matcher matcher = pattern.matcher(id);
				if (!matcher.matches()) {
					throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR,
						"The resourceId can contain only alphabetic characters", Response.Status.CONFLICT);
				}
			}
			this.getResourceManager().addResource(bean);
			response.setResult(IResponseBuilder.SUCCESS);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("error in addResource", t);
            //ApsSystemUtils.logThrowable(t, this, "addResource");
            throw new ApsSystemException("Error into API method", t);
        } finally {
			if (null != bean && null != bean.getFile()) {
				bean.getFile().delete();
			}
		}
		return response;
    }

	public StringApiResponse updateImage(JAXBResource jaxbResource, Properties properties) throws Throwable {
        this.checkType(jaxbResource, JacmsSystemConstants.RESOURE_IMAGE_CODE);
        properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_IMAGE_CODE);
		return this.updateResource(jaxbResource, properties);
    }

	public StringApiResponse updateAttachment(JAXBResource jaxbResource, Properties properties) throws Throwable {
		this.checkType(jaxbResource, JacmsSystemConstants.RESOURE_ATTACH_CODE);
        properties.setProperty(RESOURCE_TYPE_CODE_PARAM, JacmsSystemConstants.RESOURE_ATTACH_CODE);
        return this.updateResource(jaxbResource, properties);
    }

	private void checkType(JAXBResource jaxbResource, String expectedTypeCode) throws Throwable {
		if (!jaxbResource.getTypeCode().equals(expectedTypeCode)) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
					"Invalid resource type - '" + jaxbResource.getTypeCode() + "'", Response.Status.CONFLICT);
		}
	}

	public StringApiResponse updateResource(JAXBResource jaxbResource, Properties properties) throws Throwable {
        StringApiResponse response = new StringApiResponse();
		BaseResourceDataBean bean = null;
		try {
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            this.check(jaxbResource, user, response, false);
			if (null != response.getErrors() && !response.getErrors().isEmpty()) {
				return response;
			}
			bean = jaxbResource.createBataBean(this.getCategoryManager());
			this.getResourceManager().updateResource(bean);
			response.setResult(IResponseBuilder.SUCCESS);
        } catch (Throwable t) {
        	_logger.error("error in updateResource", t);
            //ApsSystemUtils.logThrowable(t, this, "updateResource");
            throw new ApsSystemException("Error into API method", t);
        } finally {
			if (null != bean && null != bean.getFile()) {
				bean.getFile().delete();
			}
		}
		return response;
    }

	private void check(JAXBResource jaxbResource, UserDetails user,
			StringApiResponse response, boolean add) throws Throwable {
		ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(jaxbResource.getTypeCode());
		if (null == resourcePrototype) {
			this.addValidationError("Invalid resource type - '" + jaxbResource.getTypeCode() + "'", response);
		}
		if (null == user) {
			user = this.getUserManager().getGuestUser();
		}
		String groupName = jaxbResource.getMainGroup();
		if (null == groupName || groupName.trim().length() == 0) {
			this.addValidationError("Group required", response);
		} else {
			if (null == this.getGroupManager().getGroup(groupName)) {
				//this.addValidationError("Group '" + groupName + "' does not exist", response);
			} else if (!this.getAuthorizationManager().isAuthOnGroup(user, groupName)) {
				//this.addValidationError("Group '" + groupName + "' not allowed", response);
			}
		}
		if (add) {
			if (null == jaxbResource.getBase64()) {
				this.addValidationError("Binary Image required", response);
			}
			if (null == jaxbResource.getFileName() || jaxbResource.getFileName().trim().length() == 0) {
				this.addValidationError("FileName required", response);
			}
		} else {
			ResourceInterface oldResource = this.getResourceManager().loadResource(jaxbResource.getId());
			if (null == oldResource) {
				this.addValidationError("Resource with id '" + jaxbResource.getId() + "' does not exist", response);
			} else if (!oldResource.getMainGroup().equals(jaxbResource.getMainGroup())) {
				this.addValidationError("Resource group has to be '" + oldResource.getMainGroup() + "'", response);
			}
		}
		if (this.isDuplicateFile(jaxbResource, resourcePrototype, add)) {
			this.addValidationError("File '" + jaxbResource.getFileName() + "' is already present on Archive", response);
		}
		if (null == jaxbResource.getDescription() || jaxbResource.getDescription().trim().length() == 0) {
			this.addValidationError("Description required", response);
		}

		if (null == jaxbResource.getTypeCode() || jaxbResource.getTypeCode().trim().length() == 0) {
			this.addValidationError("TypeCode required", response);
		}
	}

	private boolean isDuplicateFile(JAXBResource jaxbResource, ResourceInterface resourcePrototype, boolean add) {
		if (null == jaxbResource.getBase64()) return false;
		boolean addError = true;
		String formFileName = jaxbResource.getFileName();
		try {
			resourcePrototype.setMainGroup(jaxbResource.getMainGroup());
			if (resourcePrototype.exists(formFileName)) {
				if (!add) {
					ResourceInterface masterResource = this.getResourceManager().loadResource(jaxbResource.getId());
					String masterFileName = (null != masterResource) ? masterResource.getMasterFileName() : null;
					if (null != masterFileName && masterFileName.equalsIgnoreCase(formFileName)) {
						addError = false;
					}
				}
			} else {
				addError = false;
			}
		} catch (Throwable t) {
			_logger.error("Error while check duplicate file - master file name '{}'", formFileName, t);
			//ApsSystemUtils.logThrowable(t, this, "isDuplicateFile", "Error while check duplicate file - master file name '" + formFileName + "'");
		}
		return addError;
	}

	private void addValidationError(String message, StringApiResponse response) {
		ApiError error = new ApiError(IApiErrorCodes.API_VALIDATION_ERROR, message, Response.Status.FORBIDDEN);
		response.addError(error);
	}

	/**
	 * Delete an Image Resource. The method can be called by Entando API Engine.
	 * @param properties The properties of the DELETE method call.
	 * @throws Throwable Il case of error.
	 */
	public StringApiResponse deleteImage(Properties properties) throws Throwable {
		return this.deleteResource(properties, JacmsSystemConstants.RESOURE_IMAGE_CODE);
	}

	/**
	 * Delete an Attach Resource. The method can be called by Entando API Engine.
	 * @param properties The properties of the DELETE method call.
	 * @throws Throwable Il case of error.
	 */
	public StringApiResponse deleteAttachment(Properties properties) throws Throwable {
		return this.deleteResource(properties, JacmsSystemConstants.RESOURE_ATTACH_CODE);
	}

	private StringApiResponse deleteResource(Properties properties, String expectedTypeCode) throws ApiException, Throwable {
		StringApiResponse response = null;
		try {
            String id = properties.getProperty("id");
            ResourceInterface resource = this.getResourceManager().loadResource(id);
            if (null != resource && !resource.getType().equals(expectedTypeCode)) {
				throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Invalid resource type - '" + resource.getType() + "'", Response.Status.CONFLICT);
			}
			properties.setProperty(RESOURCE_TYPE_CODE_PARAM, expectedTypeCode);
			response = this.deleteResource(properties, resource);
        } catch (ApiException ae) {
			throw ae;
        } catch (Throwable t) {
        	_logger.error("Error deleting resource", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteResource");
            throw new ApsSystemException("Error deleting resource", t);
        }
		return response;
	}

	/**
	 * Delete a Resource. The method can be called by Entando API Engine.
	 * @param properties The properties of the DELETE method call.
	 * @throws Throwable Il case of error.
	 */
	public StringApiResponse deleteResource(Properties properties) throws Throwable {
		StringApiResponse response = null;
        try {
            String id = properties.getProperty("id");
            ResourceInterface resource = this.getResourceManager().loadResource(id);
            response = this.deleteResource(properties, resource);
        } catch (Throwable t) {
        	_logger.error("Error deleting resource", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteResource");
            throw new ApsSystemException("Error deleting resource", t);
        }
        return response;
	}

	private StringApiResponse deleteResource(Properties properties, ResourceInterface resource) throws ApiException, Throwable {
		StringApiResponse response = new StringApiResponse();
        try {
			String id = properties.getProperty("id");
			if (null == resource) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
						"Resource with code '" + id + "' does not exist", Response.Status.CONFLICT);
            }
			UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == user) {
                user = this.getUserManager().getGuestUser();
            }
			if (!this.getAuthorizationManager().isAuthOnGroup(user, resource.getMainGroup())) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Resource not allowed for user '" + user.getUsername() + "' - resource group '" + resource.getMainGroup() + "'", Response.Status.FORBIDDEN);
            }
            List<String> references = ((ResourceUtilizer) this.getContentManager()).getResourceUtilizers(id);
			if (references != null && references.size() > 0) {
                boolean found = false;
                for (int i = 0; i < references.size(); i++) {
                    String reference = references.get(i);
                    ContentRecordVO record = this.getContentManager().loadContentVO(reference);
                    if (null != record) {
                        found = true;
                        response.addError(new ApiError(IApiErrorCodes.API_VALIDATION_ERROR,
                                "Resource " + id + " referenced to content " + record.getId() + " - '" + record.getDescr() + "'", Response.Status.CONFLICT));
                    }
                }
                if (found) {
                    response.setResult(IResponseBuilder.FAILURE, null);
                    return response;
                }
            }
            this.getResourceManager().deleteResource(resource);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
        	_logger.error("Error deleting resource", t);
            //ApsSystemUtils.logThrowable(t, this, "deleteResource");
            throw new ApsSystemException("Error deleting resource", t);
        }
        return response;
	}

	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}

	protected IContentManager getContentManager() {
        return _contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }

    protected IUserManager getUserManager() {
        return _userManager;
    }
    public void setUserManager(IUserManager userManager) {
        this._userManager = userManager;
    }

	protected IGroupManager getGroupManager() {
		return groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this.groupManager = groupManager;
	}

	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	private IResourceManager _resourceManager;
	private IContentManager _contentManager;
	private IUserManager _userManager;
	private IGroupManager groupManager;
	private IAuthorizationManager _authorizationManager;
	private ICategoryManager _categoryManager;

	private static final String RESOURCE_TYPE_CODE_PARAM = "resourceTypeCode";

}
