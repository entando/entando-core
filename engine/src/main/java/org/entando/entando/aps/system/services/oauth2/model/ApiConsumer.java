/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.oauth2.model;

import com.agiletec.aps.system.SystemConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public class ApiConsumer {

    @NotBlank(message = "string.notBlank")
    @Size(min = 4, max = 100, message = "string.size.invalid")
    @Pattern(regexp = "([a-zA-Z0-9_\\.])+", message = "string.notAlphanumeric")
    private String key;

    @Size(max = 100, message = "string.size.invalid")
    @Pattern(regexp = "([a-zA-Z0-9_\\.])+", message = "string.notAlphanumeric")
    private String secret;

    @NotBlank(message = "string.notBlank")
    @Size(max = 100, message = "string.size.invalid")
    private String name;

    @NotBlank(message = "string.notBlank")
    @Size(max = 500, message = "string.size.invalid")
    private String description;

    private String callbackUrl;
    private String scope;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SystemConstants.API_DATE_FORMAT)
    private Date issuedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SystemConstants.API_DATE_FORMAT)
    private Date expirationDate;

    private List<String> authorizedGrantTypes;

    public ApiConsumer() {
        this.authorizedGrantTypes = new ArrayList<>();
    }

    public ApiConsumer(ConsumerRecordVO vo) {
        this();

        // secret is not read from vo
        key = vo.getKey();
        name = vo.getName();
        description = vo.getDescription();
        callbackUrl = vo.getCallbackUrl();
        scope = vo.getScope();
        expirationDate = vo.getExpirationDate();
        issuedDate = vo.getIssuedDate();

        if (!StringUtils.isEmpty(vo.getAuthorizedGrantTypes())) {
            authorizedGrantTypes = Arrays.asList(vo.getAuthorizedGrantTypes().split(","));
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public List<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public ConsumerRecordVO toConsumerRecordVO() {

        ConsumerRecordVO vo = new ConsumerRecordVO();

        vo.setKey(key);
        vo.setSecret(secret);
        vo.setName(name);
        vo.setDescription(description);
        vo.setCallbackUrl(callbackUrl);
        vo.setScope(scope);
        vo.setExpirationDate(expirationDate);
        vo.setIssuedDate(issuedDate);

        if (authorizedGrantTypes != null) {
            vo.setAuthorizedGrantTypes(String.join(",", authorizedGrantTypes));
        }

        return vo;
    }
}
