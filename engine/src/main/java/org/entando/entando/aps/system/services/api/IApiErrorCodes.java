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
package org.entando.entando.aps.system.services.api;

/**
 * @author E.Santoboni
 */
public final class IApiErrorCodes {

    private IApiErrorCodes() {
        //
    }

    /**
     * Generic Error returned by server.
     */
    public static final String SERVER_ERROR = "SERVER_ERROR";
    
    /**
     * Generic Error - Authentication Required. Error returned by server.
     */
    public static final String API_AUTHENTICATION_REQUIRED = "API_AUTHENTICATION_REQUIRED";
    
    /**
     * Generic Error - Authorization Required. Error returned by server.
     */
    public static final String API_AUTHORIZATION_REQUIRED = "API_AUTHORIZATION_REQUIRED";

    //------------------------------ Response Builder errors

    /**
     * Validation Error - Invalid method. Error returned by API Response Builder.
     */
    public static final String API_INVALID = "API_INVALID";

    /**
     * Validation Error - Unactive method. Error returned by API Response Builder.
     */
    public static final String API_ACTIVE_FALSE = "API_ACTIVE_FALSE";

    /**
     * Validation Error - Invalid method response. Error returned by API Response Builder.
     */
    public static final String API_INVALID_RESPONSE = "API_INVALID_RESPONSE";

    /**
     * Validation error - Required parameter. Error returned by API Response Builder.
     */
    public static final String API_PARAMETER_REQUIRED = "API_PARAMETER_REQUIRED";

    /**
     * Method error - Unexpected method error. Error returned by API Response Builder.
     */
    public static final String API_METHOD_ERROR = "API_METHOD_ERROR";

    //------------------------------ Service errors

    /**
     * Service Error - Invalid Service. Error returned by API Service Interface.
     */
    public static final String API_SERVICE_INVALID = "API_SERVICE_INVALID";

    /**
     * Service Error - Service not active. Error returned by API Service Interface.
     */
    public static final String API_SERVICE_ACTIVE_FALSE = "API_SERVICE_ACTIVE_FALSE";

    //------------------------------ Custom java method errors

    /**
     * Validation Error. Error returned by custom method implementation.
     */
    public static final String API_VALIDATION_ERROR = "API_VALIDATION_ERROR";

    /**
     * Validation Error - Invalid parameter value (ex: Wrong patterm). Error returned by custom method implementation.
     */
    public static final String API_PARAMETER_VALIDATION_ERROR = "API_PARAMETER_VALIDATION_ERROR";
    
}