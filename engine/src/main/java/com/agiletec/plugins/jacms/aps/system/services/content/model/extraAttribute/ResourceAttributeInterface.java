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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Interfaccia per gli attributi di entità di tipo risorsa.
 *
 * @author W.Ambu
 */
public interface ResourceAttributeInterface extends AttributeInterface {

    public static final String REFERENCED_RESOURCE_INDICATOR = "ref";

    public String getMetadata(String key);

    public String getMetadataForLang(String key, String langCode);

    public void setMetadata(String key, String langCode, String value);

    public String getResourceAlt();

    public String getResourceAltForLang(String langCode);

    public String getResourceDescription();

    public String getResourceDescriptionForLang(String langCode);

    public String getResourceLegend();

    public String getResourceLegendForLang(String langCode);

    public String getResourceTitle();

    public String getResourceTitleForLang(String langCode);

    /**
     * Restituisce la risorsa associata all'attributo.
     *
     * @return la risorsa associata all'attributo.
     */
    public ResourceInterface getResource();

    /**
     * Restituisce la risorsa associata all'attributo.
     *
     * @param langCode il codice della lingua.
     * @return la risorsa associata all'attributo.
     */
    public ResourceInterface getResource(String langCode);

    /**
     * Setta una risorsa sull'attributo.
     *
     * @param resource La risorsa da associare all'attributo.
     * @param langCode il codice della lingua.
     */
    public void setResource(ResourceInterface resource, String langCode);

}
