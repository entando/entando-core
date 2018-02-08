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
package org.entando.entando.aps.system.services.dataobjectmodel;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.notify.INotifyManager;
import org.entando.entando.aps.system.services.dataobjectmodel.cache.IDataObjectModelCacheWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class DataObjectModelManagerTest {

    @Mock
    private INotifyManager notifyManager;

    @Mock
    private IDataObjectModelCacheWrapper cacheWrapper;

    @Mock
    private IDataObjectModelDAO dataObjectModelDAO;

    @InjectMocks
    private DataObjectModelManager dataObjectModelManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetContentModel() {
        when(cacheWrapper.getModel(Mockito.anyString())).thenReturn(createModel(1, "ART"));
        DataObjectModel model = this.dataObjectModelManager.getDataObjectModel(1L);
        assertNotNull(model);
    }

    @Test
    public void testGetContentModels() {
        List<DataObjectModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createModel(i, "ART"));
        }
        when(dataObjectModelManager.getDataObjectModels()).thenReturn(fakeModels);
        List<DataObjectModel> models = this.dataObjectModelManager.getDataObjectModels();
        assertNotNull(models);
        assertEquals(4, models.size());
    }

    @Test
    public void testGetModelsForContentType() {
        String type = "ART";
        List<DataObjectModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createModel(i, type));
        }
        when(dataObjectModelManager.getDataObjectModels()).thenReturn(fakeModels);
        List<DataObjectModel> models = this.dataObjectModelManager.getModelsForDataObjectType(type);
        assertNotNull(models);
        assertEquals(4, models.size());
    }

    @Test
    public void testAddDeleteContentModel() throws Throwable {
        String type = "ART";
        List<DataObjectModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createModel(i, type));
        }
        when(dataObjectModelManager.getDataObjectModels()).thenReturn(fakeModels);

        DataObjectModel dataModel = new DataObjectModel();
        dataModel.setId(99);
        dataModel.setDataType("ART");
        dataModel.setDescription("Descr_Prova");
        dataModel.setShape("<h2></h2>");

        this.dataObjectModelManager.addDataObjectModel(dataModel);
        Mockito.verify(cacheWrapper, Mockito.times(1)).addModel(Mockito.any(DataObjectModel.class));
        Mockito.verify(dataObjectModelDAO, Mockito.times(1)).addDataModel(Mockito.any(DataObjectModel.class));

        this.dataObjectModelManager.removeDataObjectModel(dataModel);
        Mockito.verify(cacheWrapper, Mockito.times(1)).removeModel(Mockito.any(DataObjectModel.class));
        Mockito.verify(dataObjectModelDAO, Mockito.times(1)).deleteDataModel(Mockito.any(DataObjectModel.class));
    }

    @Test
    public void testUpdateContentModel() throws Throwable {
        String type = "ART";
        List<DataObjectModel> fakeModels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fakeModels.add(createModel(i, type));
        }
        when(dataObjectModelManager.getDataObjectModels()).thenReturn(fakeModels);
        List<DataObjectModel> models = dataObjectModelManager.getDataObjectModels();
        DataObjectModel contentModelNew = models.get(0);
        this.dataObjectModelManager.updateDataObjectModel(contentModelNew);
        Mockito.verify(cacheWrapper, Mockito.times(1)).updateModel(Mockito.any(DataObjectModel.class));
        Mockito.verify(dataObjectModelDAO, Mockito.times(1)).updateDataModel(Mockito.any(DataObjectModel.class));
    }

    private DataObjectModel createModel(int id) {
        DataObjectModel model = new DataObjectModel();
        model.setId(id);
        return model;
    }

    private DataObjectModel createModel(int id, String dataType) {
        DataObjectModel model = this.createModel(id);
        model.setDataType(dataType);
        return model;
    }
}
