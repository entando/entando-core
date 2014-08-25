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
package org.entando.entando.aps.system.services.api.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 */
public class ApiRestStatusServer {

	private static final Logger _logger = LoggerFactory.getLogger(ApiRestStatusServer.class);
	
    @GET
    @Produces({"application/json", "application/xml", "application/javascript"})
    @Path("/{resourceName}/{httpMethod}")
    public Object getApiStatus(@PathParam("httpMethod") String httpMethodString,
            @PathParam("resourceName") String resourceName, @Context HttpServletRequest request) {
        return this.getApiStatus(httpMethodString, null, resourceName, request);
    }

    @GET
    @Produces({"application/json", "application/xml", "application/javascript"})
    @Path("/{namespace}/{resourceName}/{httpMethod}")
    public Object getApiStatus(@PathParam("httpMethod") String httpMethodString,
            @PathParam("namespace") String namespace, @PathParam("resourceName") String resourceName, @Context HttpServletRequest request) {
        StringApiResponse response = new StringApiResponse();
        ApiMethod.HttpMethod httpMethod = Enum.valueOf(ApiMethod.HttpMethod.class, httpMethodString.toUpperCase());
        try {
            IResponseBuilder responseBuilder = (IResponseBuilder) ApsWebApplicationUtils.getBean(SystemConstants.API_RESPONSE_BUILDER, request);
            ApiMethod apiMethod = responseBuilder.extractApiMethod(httpMethod, namespace, resourceName);
            if (null != apiMethod.getRequiredPermission()) {
                response.setResult(ApiStatus.AUTHORIZATION_REQUIRED.toString(), null);
            } else if (apiMethod.getRequiredAuth()) {
                response.setResult(ApiStatus.AUTHENTICATION_REQUIRED.toString(), null);
            } else {
                response.setResult(ApiStatus.FREE.toString(), null);
            }
        } catch (ApiException ae) {
            response.addErrors(((ApiException) ae).getErrors());
            response.setResult(ApiStatus.INACTIVE.toString(), null);
        } catch (Throwable t) {
            return this.buildErrorResponse(httpMethod, namespace, resourceName, t);
        }
        return response;
    }

    private StringApiResponse buildErrorResponse(ApiMethod.HttpMethod httpMethod,
			String namespace, String resourceName, Throwable t) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Method '").append(httpMethod).
				append("' Namespace '").append(namespace).append("' Resource '").append(resourceName).append("'");
		_logger.error("Error building api response  - {}", buffer.toString(), t);
		//ApsSystemUtils.logThrowable(t, this, "buildErrorResponse", "Error building api response  - " + buffer.toString());
        StringApiResponse response = new StringApiResponse();
        ApiError error = new ApiError(IApiErrorCodes.SERVER_ERROR, "Error building response - " + buffer.toString(), Response.Status.INTERNAL_SERVER_ERROR);
        response.addError(error);
        response.setResult(IResponseBuilder.FAILURE, null);
        return response;
    }

    public static enum ApiStatus {
        FREE, INACTIVE, AUTHENTICATION_REQUIRED, AUTHORIZATION_REQUIRED
    }

}
