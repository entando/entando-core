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
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.cache.IContentModelManagerCacheWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class ContentModelManagerTest {

    @Mock
    private INotifyManager notifyManager;

    @Mock
    private IContentModelManagerCacheWrapper cacheWrapper;

    @Mock
    private IContentModelDAO contentModelDAO;

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
