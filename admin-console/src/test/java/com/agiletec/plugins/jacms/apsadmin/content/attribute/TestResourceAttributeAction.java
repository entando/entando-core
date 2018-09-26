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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AttachAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource.ResourceAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestResourceAttributeAction extends AbstractBaseTestContentAction {

    public void testChooseImageResource() throws Throwable {
        String contentId = "ART1";
        Content content = this.getContentManager().loadContent(contentId, false);
        this.executeEdit(contentId, "admin");
        String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);

        this.initContentAction("/do/jacms/Content", "chooseResource", contentOnSessionMarker);
        this.addParameter("attributeName", "Foto");
        this.addParameter("resourceTypeCode", "Image");
        this.addParameter("resourceLangCode", "it");
        String result = this.executeAction();
        assertEquals(Action.SUCCESS, result);

        HttpSession session = this.getRequest().getSession();
        assertEquals("Foto", session.getAttribute(ResourceAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM));
        assertEquals("Image", session.getAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM));
        assertEquals("it", session.getAttribute(ResourceAttributeActionHelper.RESOURCE_LANG_CODE_SESSION_PARAM));
        assertNull(session.getAttribute(ResourceAttributeActionHelper.LIST_ELEMENT_INDEX_SESSION_PARAM));
    }

    public void testRemoveImageResource_1() throws Throwable {
        String contentOnSessionMarker = this.initForImageRemovingTest();

        this.initContentAction("/do/jacms/Content", "removeResource", contentOnSessionMarker);
        this.addParameter("attributeName", "Foto");
        this.addParameter("resourceTypeCode", "Image");
        this.addParameter("resourceLangCode", "it");
        assertEquals(Action.SUCCESS, this.executeAction());

        Content contentOnEdit = this.getContentOnEdit(contentOnSessionMarker);
        ImageAttribute imageAttribute = (ImageAttribute) contentOnEdit.getAttribute("Foto");
        assertNull(imageAttribute.getResource("it"));
        assertNull(imageAttribute.getResource("en"));
    }

    public void testRemoveImageResource_2() throws Throwable {
        String contentOnSessionMarker = this.initForImageRemovingTest();

        this.initContentAction("/do/jacms/Content", "removeResource", contentOnSessionMarker);
        this.addParameter("attributeName", "Foto");
        this.addParameter("resourceTypeCode", "Image");
        this.addParameter("resourceLangCode", "en");
        assertEquals(Action.SUCCESS, this.executeAction());

        Content contentOnEdit = this.getContentOnEdit(contentOnSessionMarker);
        ImageAttribute imageAttribute = (ImageAttribute) contentOnEdit.getAttribute("Foto");
        assertNotNull(imageAttribute.getResource("it"));
        assertEquals("44", imageAttribute.getResource("it").getId());
        assertNull(imageAttribute.getResource("en"));
    }

    private String initForImageRemovingTest() throws Throwable, ApsSystemException {
        String contentId = "ART180";
        Content content = this.getContentManager().loadContent(contentId, false);
        this.executeEdit(contentId, "admin");
        String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
        Content contentOnEdit = this.getContentOnEdit(contentOnSessionMarker);
        ImageAttribute imageAttribute = (ImageAttribute) contentOnEdit.getAttribute("Foto");
        assertEquals("44", imageAttribute.getResource("it").getId());
        assertNull(imageAttribute.getResource("en"));

        IResourceManager resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
        ResourceInterface res = resourceManager.loadResource("22");
        assertNotNull(res);
        assertEquals("jAPS Team", res.getDescription());
        imageAttribute.getResources().put("en", res);
        return contentOnSessionMarker;
    }

    public void testRemoveAttachResource() throws Throwable {
        String contentId = "RAH1";
        Content content = this.getContentManager().loadContent(contentId, false);
        this.executeEdit(contentId, "admin");
        String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
        Content contentOnEdit = this.getContentOnEdit(contentOnSessionMarker);

        AttachAttribute attachAttribute = (AttachAttribute) contentOnEdit.getAttribute("Allegati");
        assertEquals("7", attachAttribute.getResource("it").getId());
        assertNull(attachAttribute.getResource("en"));

        this.initContentAction("/do/jacms/Content", "removeResource", contentOnSessionMarker);
        this.addParameter("attributeName", "Allegati");
        this.addParameter("resourceTypeCode", "Attach");
        this.addParameter("resourceLangCode", "it");
        assertEquals(Action.SUCCESS, this.executeAction());

        contentOnEdit = this.getContentOnEdit(contentOnSessionMarker);
        attachAttribute = (AttachAttribute) contentOnEdit.getAttribute("Allegati");
        assertNull(attachAttribute.getResource("it"));
        assertNull(attachAttribute.getResource("en"));
    }

    public void testUpdateAttachResource() throws Throwable {
        String[] addedContents = null;
        try {
            addedContents = super.addDraftContentsForTest(new String[]{"ALL4"}, true);
            Content content = this.getContentManager().loadContent(addedContents[0], false);
            assertEquals(0, content.getCategories().size());
            this.executeEdit(addedContents[0], "admin");
            String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);

            //execute an action to force field upgrade
            this.initContentAction("/do/jacms/Content", "joinCategory", contentOnSessionMarker);
            this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
            this.addParameter("categoryCode", "general");
            this.addParameter("Image:en_Image_metadata_alt", "Image Alt - en");
            this.addParameter("Image:en_Image_metadata_description", "Image Description - en");
            this.addParameter("Image:en_Image_metadata_legend", "Image Legend - en");
            this.addParameter("Image:en_Image_metadata_title", "Image Title - en");
            this.addParameter("Image:it_Image_metadata_alt", "Image Alt - it");
            this.addParameter("Image:it_Image_metadata_description", "Image Description - it");
            this.addParameter("Image:it_Image_metadata_legend", "Image Legend - it");
            this.addParameter("Image:it_Image_metadata_title", "Image Title - it");
            String result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
            Content contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
            assertEquals(1, contentOnSession.getCategories().size());

            ImageAttribute imageAttribute = (ImageAttribute) contentOnSession.getAttribute("Image");
            assertEquals("Image Alt - en", imageAttribute.getResourceAltForLang("en"));
            assertEquals("Image Description - en", imageAttribute.getResourceDescriptionForLang("en"));
            assertEquals("Image Legend - en", imageAttribute.getResourceLegendForLang("en"));
            assertEquals("Image Title - en", imageAttribute.getResourceTitleForLang("en"));
            assertEquals("Image Alt - it", imageAttribute.getResourceAltForLang("it"));
            assertEquals("Image Description - it", imageAttribute.getResourceDescriptionForLang("it"));
            assertEquals("Image Legend - it", imageAttribute.getResourceLegendForLang("it"));
            assertEquals("Image Title - it", imageAttribute.getResourceTitleForLang("it"));

            this.initContentAction("/do/jacms/Content", "removeCategory", contentOnSessionMarker);
            this.addParameter("contentOnSessionMarker", contentOnSessionMarker);
            this.addParameter("categoryCode", "general");
            this.addParameter("Image:en_Image_metadata_alt", "Image Alt - en");
            this.addParameter("Image:en_Image_metadata_description", "");
            this.addParameter("Image:en_Image_metadata_legend", "Image Legend modified - en");
            this.addParameter("Image:en_Image_metadata_title", "Image Title modified - en");
            this.addParameter("Image:it_Image_metadata_alt", "");
            this.addParameter("Image:it_Image_metadata_description", "Image Description - it");
            this.addParameter("Image:it_Image_metadata_legend", "Image Legend modified - it");
            this.addParameter("Image:it_Image_metadata_title", "Image Title - it");
            result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
            contentOnSession = this.getContentOnEdit(contentOnSessionMarker);
            assertEquals(0, contentOnSession.getCategories().size());
            imageAttribute = (ImageAttribute) contentOnSession.getAttribute("Image");
            assertEquals("Image Alt - en", imageAttribute.getResourceAltForLang("en"));
            assertEquals("", imageAttribute.getResourceDescriptionForLang("en"));
            assertEquals("Image Legend modified - en", imageAttribute.getResourceLegendForLang("en"));
            assertEquals("Image Title modified - en", imageAttribute.getResourceTitleForLang("en"));
            assertEquals("", imageAttribute.getResourceAltForLang("it"));
            assertEquals("Image Description - it", imageAttribute.getResourceDescriptionForLang("it"));
            assertEquals("Image Legend modified - it", imageAttribute.getResourceLegendForLang("it"));
            assertEquals("Image Title - it", imageAttribute.getResourceTitleForLang("it"));
        } catch (Throwable e) {
            throw e;
        } finally {
            Content content = this.getContentManager().loadContent(addedContents[0], false);
            if (null != content) {
                this.getContentManager().removeOnLineContent(content);
                this.getContentManager().deleteContent(content);
            }
        }
    }

}
