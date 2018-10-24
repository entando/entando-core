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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.notify.INotifyManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.cache.IContentModelManagerCacheWrapper;
import java.util.Arrays;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ContentModelManagerTest {

    @Mock
    private INotifyManager notifyManager;

    @Mock
    private IContentModelManagerCacheWrapper cacheWrapper;

    @Mock
    private IContentModelDAO contentModelDAO;

    @Mock
    private IPageManager pageManager;

    @Mock
    private IContentManager contentManager;

    @InjectMocks
    private ContentModelManager contentModelManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetContentModel() {
        when(contentModelManager.getContentModel(1)).thenReturn(new ContentModel());
        ContentModel model = this.contentModelManager.getContentModel(1);
        assertThat(model, is(not(nullValue())));
    }

    @Test
    public void testGetContentModels() {
        List<ContentModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createContentModel(i, "ART"));
        }
        when(contentModelManager.getContentModels()).thenReturn(fakeModels);
        List<ContentModel> models = this.contentModelManager.getContentModels();
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(4));
    }

    @Test
    public void testGetModelsForContentType() {
        List<ContentModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createContentModel(i, "ART"));
        }
        when(contentModelManager.getContentModels()).thenReturn(fakeModels);
        List<ContentModel> models = this.contentModelManager.getModelsForContentType("ART");
        assertThat(models, is(not(nullValue())));
        assertThat(models.size(), is(4));
    }

    @Test
    public void testAddDeleteContentModel() throws Throwable {
        List<ContentModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createContentModel(i, "ART"));
        }
        when(contentModelManager.getContentModels()).thenReturn(fakeModels);
        when(contentModelManager.getContentModel(3)).thenReturn(createContentModel(3, "ART"));

        ContentModel contentModel = new ContentModel();
        contentModel.setId(99);
        contentModel.setContentType("ART");
        contentModel.setDescription("Descr_Prova");
        contentModel.setContentShape("<h2></h2>");

        assertNull(this.contentModelManager.getContentModel(99));
        this.contentModelManager.addContentModel(contentModel);

        Mockito.verify(cacheWrapper, Mockito.times(1)).addContentModel(Mockito.any(ContentModel.class));
        Mockito.verify(contentModelDAO, Mockito.times(1)).addContentModel(Mockito.any(ContentModel.class));

        assertNotNull(this.contentModelManager.getContentModel(3));
        this.contentModelManager.removeContentModel(contentModel);

        Mockito.verify(cacheWrapper, Mockito.times(1)).removeContentModel(Mockito.any(ContentModel.class));
        Mockito.verify(contentModelDAO, Mockito.times(1)).deleteContentModel(Mockito.any(ContentModel.class));

    }

    @Test
    public void testGetContentModelReferences() throws Exception {
        ContentModel contentModel = createContentModel(1, "ART");
        when(contentModelManager.getContentModel(1)).thenReturn(contentModel);

        IPage root = createMockPage("root");
        IPage child = createMockPage("child");
        when(root.getChildrenCodes()).thenReturn(new String[]{"child"});
        when(child.getChildrenCodes()).thenReturn(new String[]{});

        when(pageManager.getDraftRoot()).thenReturn(root);
        when(pageManager.getOnlineRoot()).thenReturn(root);
        when(pageManager.getDraftPage("root")).thenReturn(root);
        when(pageManager.getOnlinePage("root")).thenReturn(root);
        when(pageManager.getDraftPage("child")).thenReturn(child);
        when(pageManager.getOnlinePage("child")).thenReturn(child);

        Widget single = createMockWidget("content_viewer");
        single.getConfig().put("modelId", "1");
        single.getConfig().put("contentId", "content_1");

        Widget multiple = createMockWidget("row_content_viewer_list");
        multiple.getConfig().put("contents", "[{modelId=1, contentId=content_2},{modelId=1, contentId=content_3}]");

        Widget list = createMockWidget("content_viewer_list");
        list.getConfig().put("modelId", "1");
        list.getConfig().put("contentType", "ART");
        // necessary for content_viewer_list widget
        when(contentManager.searchId("ART", null)).thenReturn(Arrays.asList("art_1", "art_2"));

        when(root.getWidgets()).thenReturn(new Widget[]{});
        when(child.getWidgets()).thenReturn(new Widget[]{single, multiple, list});

        List<ContentModelReference> references = contentModelManager.getContentModelReferences(1);

        assertEquals(6, references.size());

        // single, draft
        ContentModelReference ref0 = references.get(0);
        assertEquals(0, ref0.getWidgetPosition());
        assertEquals("child", ref0.getPageCode());
        assertEquals(1, ref0.getContentsId().size());
        assertEquals("content_1", ref0.getContentsId().get(0));
        assertFalse(ref0.isOnline());

        // multiple, draft
        ContentModelReference ref1 = references.get(1);
        assertEquals(1, ref1.getWidgetPosition());
        assertEquals("child", ref1.getPageCode());
        assertEquals(2, ref1.getContentsId().size());
        assertEquals("content_2", ref1.getContentsId().get(0));
        assertEquals("content_3", ref1.getContentsId().get(1));
        assertFalse(ref1.isOnline());

        // list, draft
        ContentModelReference ref2 = references.get(2);
        assertEquals(2, ref2.getWidgetPosition());
        assertEquals("child", ref2.getPageCode());
        assertEquals(2, ref2.getContentsId().size());
        assertEquals("art_1", ref2.getContentsId().get(0));
        assertEquals("art_2", ref2.getContentsId().get(1));
        assertFalse(ref2.isOnline());

        // single, online
        ContentModelReference ref3 = references.get(3);
        assertEquals(0, ref3.getWidgetPosition());
        assertEquals("child", ref3.getPageCode());
        assertEquals(1, ref3.getContentsId().size());
        assertEquals("content_1", ref3.getContentsId().get(0));
        assertTrue(ref3.isOnline());

        // multiple, online
        ContentModelReference ref4 = references.get(4);
        assertEquals(1, ref4.getWidgetPosition());
        assertEquals("child", ref4.getPageCode());
        assertEquals(2, ref4.getContentsId().size());
        assertEquals("content_2", ref4.getContentsId().get(0));
        assertEquals("content_3", ref4.getContentsId().get(1));
        assertTrue(ref4.isOnline());

        // list, online
        ContentModelReference ref5 = references.get(5);
        assertEquals(2, ref5.getWidgetPosition());
        assertEquals("child", ref5.getPageCode());
        assertEquals(2, ref5.getContentsId().size());
        assertEquals("art_1", ref5.getContentsId().get(0));
        assertEquals("art_2", ref5.getContentsId().get(1));
        assertTrue(ref5.isOnline());
    }

    private IPage createMockPage(String code) {
        IPage page = Mockito.mock(IPage.class);
        when(page.getCode()).thenReturn(code);
        return page;
    }

    private Widget createMockWidget(String widgetCode) {
        Widget widget = Mockito.mock(Widget.class);
        WidgetType widgetType = new WidgetType();
        widgetType.setCode(widgetCode);
        when(widget.getType()).thenReturn(widgetType);
        ApsProperties widgetConfig = new ApsProperties();
        when(widget.getConfig()).thenReturn(widgetConfig);
        return widget;
    }

    @Test
    public void testUpdateContentModel() throws Throwable {
        ContentModel contentModel = new ContentModel();
        contentModel.setId(99);
        contentModel.setContentType("ART");
        contentModel.setDescription("Descr_Prova");
        contentModel.setContentShape("<h2></h2>");
        assertNull(this.contentModelManager.getContentModel(99));
        this.contentModelManager.addContentModel(contentModel);
        Mockito.verify(cacheWrapper, Mockito.times(1)).addContentModel(Mockito.any(ContentModel.class));
        Mockito.verify(contentModelDAO, Mockito.times(1)).addContentModel(Mockito.any(ContentModel.class));
        ContentModel contentModelNew = new ContentModel();
        contentModelNew.setId(contentModel.getId());
        contentModelNew.setContentType("RAH");
        contentModelNew.setDescription("Descr_Prova");
        contentModelNew.setContentShape("<h1></h1>");
        this.contentModelManager.updateContentModel(contentModelNew);
        Mockito.verify(cacheWrapper, Mockito.times(1)).updateContentModel(Mockito.any(ContentModel.class));
        Mockito.verify(contentModelDAO, Mockito.times(1)).updateContentModel(Mockito.any(ContentModel.class));
    }

    private ContentModel createContentModel(int id, String contentType) {
        ContentModel model = new ContentModel();
        model.setId(id);
        model.setContentType(contentType);
        return model;
    }
}
