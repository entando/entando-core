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
package com.agiletec.plugins.jacms.apsadmin.content.attribute;

import javax.servlet.http.HttpSession;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.ContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ExtendedResourceFinderAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ResourceAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceFinderAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestExtendedResourceFinderAction extends AbstractBaseTestContentAction {

    public void testSearchImageResource_1() throws Throwable {
        this.executeEdit("ART1", "admin");//Contenuto FREE
        String contentOnSessionMarker = super.extractSessionMarker("ART1", ApsAdminSystemConstants.EDIT);

        //iniziazione parametri sessione
        HttpSession session = this.getRequest().getSession();
        session.setAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "Foto");
        session.setAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM, "Image");
        session.setAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM, "it");

        this.initContentAction("/do/jacms/Content/Resource", "search", contentOnSessionMarker);
        this.addParameter("resourceTypeCode", "Image");//per replicare il chain in occasione dei chooseResource da edit Contenuto.
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);

        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(2, action.getResources().size());
        assertEquals("22", action.getResources().get(0));
        assertEquals("44", action.getResources().get(1));
    }

    public void testSearchImageResource_2() throws Throwable {
        this.executeEdit("ART102", "admin");//Contenuto customers
        String contentOnSessionMarker = super.extractSessionMarker("ART102", ApsAdminSystemConstants.EDIT);

        //iniziazione parametri sessione
        HttpSession session = this.getRequest().getSession();
        session.setAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "Foto");
        session.setAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM, "Image");
        session.setAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM, "it");

        this.initContentAction("/do/jacms/Content/Resource", "search", contentOnSessionMarker);
        this.addParameter("resourceTypeCode", "Image");//per replicare il chain in occasione dei chooseResource da edit Contenuto.
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);

        ResourceFinderAction action = (ResourceFinderAction) this.getAction();
        assertEquals(3, action.getResources().size());
        assertTrue(action.getResources().contains("82"));
    }

    public void testJoinImageResource() throws Throwable {
        this.executeEdit("ART102", "admin");
        String contentOnSessionMarker = super.extractSessionMarker("ART102", ApsAdminSystemConstants.EDIT);

        ContentAction action = (ContentAction) this.getAction();
        ImageAttribute imageAttribute = (ImageAttribute) action.getContent().getAttribute("Foto");
        assertNull(imageAttribute.getResource("it"));
        assertNull(imageAttribute.getResource("en"));

        //iniziazione parametri sessione
        HttpSession session = this.getRequest().getSession();
        session.setAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, "Foto");
        session.setAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM, "Image");
        session.setAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM, "it");

        this.initContentAction("/do/jacms/Content/Resource", "joinResource", contentOnSessionMarker);
        this.addParameter("resourceTypeCode", "Image");//per replicare il chain in occasione dei chooseResource da edit Contenuto.
        this.addParameter("resourceId", "44");
        String result = this.executeAction();

        assertEquals(Action.SUCCESS, result);
        ExtendedResourceFinderAction attributeAction = (ExtendedResourceFinderAction) this.getAction();
        imageAttribute = (ImageAttribute) attributeAction.getContent().getAttribute("Foto");
        assertNotNull(imageAttribute.getResource("it"));
        assertEquals("44", imageAttribute.getResource("it").getId());
        assertEquals("", imageAttribute.getResourceAltForLang("it"));
        assertNull(imageAttribute.getResourceAltMap().get("it"));
        assertEquals("", imageAttribute.getResourceTitleForLang("it"));
        assertNull(imageAttribute.getResourceTitleMap().get("it"));
        assertEquals("Joint Photographic Experts Group", imageAttribute.getResourceLegendForLang("it"));
        assertEquals("CREATOR: gd-jpeg v1.0 (using IJG JPEG v62), default quality", imageAttribute.getResourceDescriptionForLang("it"));
    }

}
