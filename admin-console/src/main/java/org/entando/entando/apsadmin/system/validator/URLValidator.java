/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.apsadmin.system.validator;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * URLValidator checks that a given field is a String and a valid URL
 *
 * <pre>
 * &lt;validators&gt;
 *      &lt;!-- Plain Validator Syntax --&gt;
 *      &lt;validator type="url"&gt;
 *          &lt;param name="fieldName"&gt;myHomePage&lt;/param&gt;
 *          &lt;message&gt;Invalid homepage url&lt;/message&gt;
 *      &lt;/validator&gt;
 *
 *      &lt;!-- Field Validator Syntax --&gt;
 *      &lt;field name="myHomepage"&gt;
 *          &lt;field-validator type="url"&gt;
 *              &lt;message&gt;Invalid homepage url&lt;/message&gt;
 *          &lt;/field-validator&gt;
 *      &lt;/field&gt;
 * &lt;/validators&gt;
 * </pre>
 */
public class URLValidator extends FieldValidatorSupport {

    private static final Logger LOG = LogManager.getLogger(URLValidator.class);

    @Override
    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        Object value = this.getFieldValue(fieldName, object);
        // if there is no value - don't do comparison
        // if a value is required, a required validator should be added to the field
        if (value == null || value.toString().length() == 0) {
            return;
        }
        if (!(value.getClass().equals(String.class)) || !this.verifyUrl((String) value)) {
            this.addFieldError(fieldName, object);
        }
    }

    private boolean verifyUrl(String url) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking if url [#0] is valid", url);
        }
        if (url == null) {
            return false;
        }
        if (url.startsWith("https://")) {
            // URL doesn't understand the https protocol, hack it
            url = "http://" + url.substring(8);
        }
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Url [#0] is invalid: #1", e, url, e.getMessage());
            }
            return false;
        }
    }

}
