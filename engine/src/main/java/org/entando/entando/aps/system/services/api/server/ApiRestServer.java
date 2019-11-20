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
package org.entando.entando.aps.system.services.api.server;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.UnmarshalUtils;
import org.entando.entando.aps.system.services.api.model.*;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AccessTokenImpl;
import org.entando.entando.web.common.interceptor.EntandoBearerTokenExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author E.Santoboni
 */
public class ApiRestServer {

    private static final Logger _logger = LoggerFactory.getLogger(ApiRestServer.class);

    @GET
    @Produces({"application/xml", "text/plain", "application/json", "application/javascript"})
    @Path("/{langCode}/{resourceName}")
    public Object doGet(@PathParam("langCode") String langCode, @PathParam("resourceName") String resourceName,
            @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildGetDeleteResponse(langCode, ApiMethod.HttpMethod.GET, null, resourceName, request, ui);
    }

    @GET
    @Produces({"application/xml", "text/plain", "application/json", "application/javascript"})
    @Path("/{langCode}/{namespace}/{resourceName}")
    public Object doGet(@PathParam("langCode") String langCode, @PathParam("namespace") String namespace,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildGetDeleteResponse(langCode, ApiMethod.HttpMethod.GET, namespace, resourceName, request, ui);
    }

    @POST
    @Consumes({"application/xml"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{resourceName}")
    public Object doPostFromXmlBody(@PathParam("langCode") String langCode, @PathParam("resourceName") String resourceName,
            @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.POST, null, resourceName, request, ui, MediaType.APPLICATION_XML_TYPE);
    }

    @POST
    @Consumes({"application/xml"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{namespace}/{resourceName}")
    public Object doPostFromXmlBody(@PathParam("langCode") String langCode, @PathParam("namespace") String namespace,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.POST, namespace, resourceName, request, ui, MediaType.APPLICATION_XML_TYPE);
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{resourceName}")
    public Object doPostFromJsonBody(@PathParam("langCode") String langCode, @PathParam("resourceName") String resourceName,
            @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.POST, null, resourceName, request, ui, MediaType.APPLICATION_JSON_TYPE);
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{namespace}/{resourceName}")
    public Object doPostFromJsonBody(@PathParam("langCode") String langCode, @PathParam("namespace") String namespace,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.POST, namespace, resourceName, request, ui, MediaType.APPLICATION_JSON_TYPE);
    }

    @PUT
    @Consumes({"application/xml"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{resourceName}")
    public Object doPutFromXmlBody(@PathParam("langCode") String langCode, @PathParam("resourceName") String resourceName,
            @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.PUT, null, resourceName, request, ui, MediaType.APPLICATION_XML_TYPE);
    }

    @PUT
    @Consumes({"application/xml"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{namespace}/{resourceName}")
    public Object doPutFromXmlBody(@PathParam("langCode") String langCode, @PathParam("namespace") String namespace,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.PUT, namespace, resourceName, request, ui, MediaType.APPLICATION_XML_TYPE);
    }

    @PUT
    @Consumes({"application/json"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{resourceName}")
    public Object doPutFromJsonBody(@PathParam("langCode") String langCode, @PathParam("resourceName") String resourceName,
            @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.PUT, null, resourceName, request, ui, MediaType.APPLICATION_JSON_TYPE);
    }

    @PUT
    @Consumes({"application/json"})
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{namespace}/{resourceName}")
    public Object doPutFromJsonBody(@PathParam("langCode") String langCode, @PathParam("namespace") String namespace,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildPostPutResponse(langCode, ApiMethod.HttpMethod.PUT, namespace, resourceName, request, ui, MediaType.APPLICATION_JSON_TYPE);
    }

    @DELETE
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{resourceName}")
    public Object doDelete(@PathParam("langCode") String langCode, @PathParam("resourceName") String resourceName,
            @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildGetDeleteResponse(langCode, ApiMethod.HttpMethod.DELETE, null, resourceName, request, ui);
    }

    @DELETE
    @Produces({"application/json", "application/xml"})
    @Path("/{langCode}/{namespace}/{resourceName}")
    public Object doDelete(@PathParam("langCode") String langCode, @PathParam("namespace") String namespace,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request, @Context UriInfo ui) {
        return this.buildGetDeleteResponse(langCode, ApiMethod.HttpMethod.DELETE, namespace, resourceName, request, ui);
    }

    protected Object buildGetDeleteResponse(String langCode, ApiMethod.HttpMethod httpMethod,
            String namespace, String resourceName, HttpServletRequest request, UriInfo ui) {
        Object responseObject = null;
        try {
            IResponseBuilder responseBuilder = (IResponseBuilder) ApsWebApplicationUtils.getBean(SystemConstants.API_RESPONSE_BUILDER, request);
            Properties properties = this.extractProperties(langCode, ui, request);
            ApiMethod apiMethod = responseBuilder.extractApiMethod(httpMethod, namespace, resourceName);
            this.extractOAuthParameters(request, apiMethod, properties);
            responseObject = responseBuilder.createResponse(apiMethod, properties);
        } catch (ApiException ae) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, ae);
        } catch (Throwable t) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, t);
        }
        return this.createResponse(responseObject);
    }

    protected Object buildPostPutResponse(String langCode, ApiMethod.HttpMethod httpMethod,
            String namespace, String resourceName, HttpServletRequest request, UriInfo ui, MediaType mediaType) {
        Object responseObject = null;
        try {
            IResponseBuilder responseBuilder = (IResponseBuilder) ApsWebApplicationUtils.getBean(SystemConstants.API_RESPONSE_BUILDER, request);
            Properties properties = this.extractProperties(langCode, ui, request);
            ApiMethod apiMethod = responseBuilder.extractApiMethod(httpMethod, namespace, resourceName);
            this.extractOAuthParameters(request, apiMethod, properties);
            Object bodyObject = UnmarshalUtils.unmarshal(apiMethod.getExpectedType(), request, mediaType);
            responseObject = responseBuilder.createResponse(apiMethod, bodyObject, properties);
        } catch (ApiException ae) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, ae);
        } catch (Throwable t) {
            responseObject = this.buildErrorResponse(httpMethod, namespace, resourceName, t);
        }
        return this.createResponse(responseObject);
    }

    protected Properties extractProperties(String langCode, UriInfo ui, HttpServletRequest request) throws Throwable {
        ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, request);
        Properties properties = this.extractRequestParameters(ui);
        if (null == langManager.getLang(langCode)) {
            langCode = langManager.getDefaultLang().getCode();
        }
        String applicationBaseUrl = this.extractApplicationBaseUrl(request);
        if (null != applicationBaseUrl) {
            properties.put(SystemConstants.API_APPLICATION_BASE_URL_PARAMETER, applicationBaseUrl);
        }
        properties.put(SystemConstants.API_LANG_CODE_PARAMETER, langCode);
        properties.put(SystemConstants.API_PRODUCES_MEDIA_TYPE_PARAMETER, this.extractProducesMediaType(request));
        return properties;
    }

    protected Properties extractRequestParameters(UriInfo ui) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters(false);
        Properties properties = new Properties();
        if (null != queryParams) {
            List<String> reservedParameters = Arrays.asList(SystemConstants.API_RESERVED_PARAMETERS);
            Set<Entry<String, List<String>>> entries = queryParams.entrySet();
            Iterator<Entry<String, List<String>>> iter = entries.iterator();
            while (iter.hasNext()) {
                Entry<String, List<String>> entry = iter.next();
                String key = entry.getKey();
                if (!reservedParameters.contains(key)) {
                    //extract only the first value
                    properties.put(key, entry.getValue().get(0));
                }
            }
        }
        return properties;
    }

    protected String extractApplicationBaseUrl(HttpServletRequest request) throws Exception {
        String applicationBaseUrl = null;
        try {
            IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, request);
            applicationBaseUrl = urlManager.getApplicationBaseURL(request);
        } catch (Exception t) {
            _logger.error("Error extracting application base url", t);
        }
        return applicationBaseUrl;
    }

    protected MediaType extractProducesMediaType(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        int index = pathInfo.indexOf('.');
        if (index < 0) {
            return MediaType.APPLICATION_XML_TYPE;
        }
        String extension = pathInfo.substring(index + 1);
        if (extension.equalsIgnoreCase("json")) {
            return MediaType.APPLICATION_JSON_TYPE;
        } else {
            return MediaType.APPLICATION_XML_TYPE;
        }
    }

    protected StringApiResponse buildErrorResponse(ApiMethod.HttpMethod httpMethod, String namespace, String resourceName, Throwable t) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Method '").append(httpMethod).append("' Resource '").append(resourceName).append("'");
        if (null != namespace) {
            buffer.append(" Namespace '").append(namespace).append("'");
        }
        final String message = buffer.toString();
        _logger.error("Error building api response  - {}", message, t);
        StringApiResponse response = new StringApiResponse();
        if (t instanceof ApiException) {
            response.addErrors(((ApiException) t).getErrors());
        } else {
            ApiError error = new ApiError(IApiErrorCodes.SERVER_ERROR, "Error building response - " + message, Response.Status.INTERNAL_SERVER_ERROR);
            response.addError(error);
        }
        response.setResult(IResponseBuilder.FAILURE, null);
        return response;
    }

    protected void extractOAuthParameters(HttpServletRequest request, ApiMethod apiMethod, Properties properties) throws ApiException {
        IUserManager userManager = (IUserManager) ApsWebApplicationUtils.getBean(SystemConstants.USER_MANAGER, request);
        IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, request);
        try {
            UserDetails user = null;
            String permission = apiMethod.getRequiredPermission();
            _logger.debug("Permission required: {}", permission);
            String accessToken = new EntandoBearerTokenExtractor().extractToken(request);
            IApiOAuth2TokenManager tokenManager = (IApiOAuth2TokenManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_TOKEN_MANAGER, request);
            final OAuth2AccessTokenImpl token = (OAuth2AccessTokenImpl) tokenManager.readAccessToken(accessToken);
            if (token != null) {
                // Validate the access token
                if (!token.getValue().equals(accessToken)) {
                    throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Token does not match", Response.Status.UNAUTHORIZED);
                } // check if access token is expired
                else if (token.isExpired()) {
                    throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Token expired", Response.Status.UNAUTHORIZED);
                }
                String username = token.getLocalUser();
                user = userManager.getUser(username);
                if (user != null) {
                    user.addAuthorizations(authManager.getUserAuthorizations(username));
                    properties.put(SystemConstants.API_USER_PARAMETER, user);
                    _logger.info("User {} requesting resource that requires {} permission ", username, permission);
                    request.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
                }
            } else if (accessToken != null) {
                _logger.warn("Token not found from access token");
            }
            if (null != user) {
                String username = user.getUsername();
                if (permission != null) {
                    if (!authManager.isAuthOnPermission(user, permission)) {
                        List<Role> roles = authManager.getUserRoles(user);
                        for (Role role : roles) {
                            _logger.debug("User {} requesting resource has {} permission ", username, (null != role.getPermissions()) ? role.getPermissions().toString() : "");
                        }
                        throw new ApiException(IApiErrorCodes.API_AUTHORIZATION_REQUIRED, "Authorization Required", Response.Status.UNAUTHORIZED);
                    }
                }
            } else if (apiMethod.getRequiredAuth()) {
                throw new ApiException(IApiErrorCodes.API_AUTHENTICATION_REQUIRED, "Authentication Required", Response.Status.UNAUTHORIZED);
            }
        } catch (ApsSystemException ex) {
            _logger.error("System exception {}", ex);
            throw new ApiException(IApiErrorCodes.SERVER_ERROR, ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    protected Response createResponse(Object responseObject) {
        ResponseBuilderImpl responsex = new ResponseBuilderImpl();
        responsex.entity(responseObject);
        if (responseObject instanceof AbstractApiResponse) {
            Response.Status status = Response.Status.OK;
            AbstractApiResponse mainResponse = (AbstractApiResponse) responseObject;
            if (null != mainResponse.getErrors()) {
                for (int i = 0; i < mainResponse.getErrors().size(); i++) {
                    ApiError error = mainResponse.getErrors().get(i);
                    Response.Status errorStatus = error.getStatus();
                    if (null != errorStatus && status.getStatusCode() < errorStatus.getStatusCode()) {
                        status = errorStatus;
                    }
                }
            }
            responsex.status(status);
        } else {
            responsex.status(Response.Status.OK);
        }
        return responsex.build();
    }

}
