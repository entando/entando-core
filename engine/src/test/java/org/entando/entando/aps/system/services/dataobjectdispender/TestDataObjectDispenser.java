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
package org.entando.entando.aps.system.services.dataobjectdispender;

import org.entando.entando.aps.system.services.dataobjectdispenser.DataObjectRenderizationInfo;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.dataobjectdispenser.IDataObjectDispenser;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * @author E.Santoboni
 */
public class TestDataObjectDispenser extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testGetRenderedContent_1() throws Throwable {
        RequestContext reqCtx = this.getRequestContext();

        DataObjectRenderizationInfo outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART1", 2, "en", reqCtx);
        assertEquals(this.replaceNewLine(_attendedEnART1_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        this.setUserOnSession("admin");
        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART1", 2, "en", reqCtx);
        assertEquals(this.replaceNewLine(_attendedEnART1_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
        assertEquals(this.replaceNewLine(_attendedItART104_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        this.setUserOnSession("editorCoach");
        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
        assertEquals(this.replaceNewLine(_attendedItART104_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        this.setUserOnSession("pageManagerCoach");
        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
        assertEquals(this.replaceNewLine(_attendedItART104_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));
    }

    public void testGetRenderedContent_2() throws Throwable {
        RequestContext reqCtx = this.getRequestContext();
        this.setUserOnSession("admin");

        DataObjectRenderizationInfo outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART120", 2, "it", reqCtx);
        assertEquals(this.replaceNewLine(_attendedItART120_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART120", 2, "en", reqCtx);
        assertEquals(this.replaceNewLine(_attendedEnART120_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART121", 2, "it", reqCtx);
        assertEquals(this.replaceNewLine(_attendedItART121_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART121", 2, "en", reqCtx);
        assertEquals(this.replaceNewLine(_attendedEnART121_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));

        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART122", 2, "en", reqCtx);
        assertEquals(this.replaceNewLine(_attendedEnART122_cached.trim()), this.replaceNewLine(outputInfo.getRenderedDataobject().trim()));
    }

    public void testGetRenderedContent_3() throws Throwable {
        DataObject dataobject = this._dataObjectManager.loadDataObject("ART120", true);
        dataobject.setId(null);
        try {
            RequestContext reqCtx = this.getRequestContext();
            this.setUserOnSession("admin");
            this._dataObjectManager.insertDataObject(dataobject);
            DataObjectRenderizationInfo outputInfo = this._dataObjectDispenser.getRenderizationInfo(dataobject.getId(), 2, "it", reqCtx);
            assertNotNull(outputInfo);

            //assertNotNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_CACHE_PREFIX+dataobject.getId()));
            //assertNotNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX+dataobject.getId()));
            this._dataObjectManager.insertDataObject(dataobject);
            this.waitNotifyingThread();

            //assertNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_CACHE_PREFIX+dataobject.getId()));
            //assertNull(this._cacheInfoManager.getFromCache(JacmsSystemConstants.CONTENT_AUTH_INFO_CACHE_PREFIX+dataobject.getId()));
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null != dataobject.getId()) {
                this._dataObjectManager.deleteDataObject(dataobject);
            }
        }
    }

    public void testGetRenderedContent_4() throws Throwable {
        String dataobjectId = "ART120";
        String dataobjectShapeModel = "title (Text): testo=$data.Titolo.getText()";
        int modelId = 1972;
        try {
            this.addNewDataObjectModel(modelId, dataobjectShapeModel, "ART");
            RequestContext reqCtx = this.getRequestContext();
            this.setUserOnSession("admin");
            DataObjectRenderizationInfo outputInfo = this._dataObjectDispenser.getRenderizationInfo(dataobjectId, modelId, "en", reqCtx);
            assertEquals("title (Text): testo=Title of Administrator's Content", outputInfo.getRenderedDataobject());

            DataObjectModel model = this._dataObjectModelManager.getDataObjectModel(modelId);
            String newContentShapeModel = "title: testo=$data.Titolo.getText()";
            model.setShape(newContentShapeModel);
            this._dataObjectModelManager.updateDataObjectModel(model);
            this.waitNotifyingThread();

            outputInfo = this._dataObjectDispenser.getRenderizationInfo(dataobjectId, modelId, "en", reqCtx);
            assertEquals("title: testo=Title of Administrator's Content", outputInfo.getRenderedDataobject());
        } catch (Throwable t) {
            throw t;
        } finally {
            DataObjectModel model = this._dataObjectModelManager.getDataObjectModel(modelId);
            if (null != model) {
                this._dataObjectModelManager.removeDataObjectModel(model);
            }
        }
    }

    public void addNewDataObjectModel(int id, String shape, String dataTypeCode) throws Throwable {
        DataObjectModel model = new DataObjectModel();
        model.setDataType(dataTypeCode);
        model.setDescription("test");
        model.setId(id);
        model.setShape(shape);
        this._dataObjectModelManager.addDataObjectModel(model);
    }

    public void testGetUnauthorizedContent() throws Throwable {
        RequestContext reqCtx = this.getRequestContext();

        DataObjectRenderizationInfo outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
        assertEquals("Current user 'guest' can't view this DataObject", outputInfo.getRenderedDataobject().trim());

        this.setUserOnSession("editorCustomers");
        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
        assertEquals("Current user 'editorCustomers' can't view this DataObject", outputInfo.getRenderedDataobject().trim());

        this.setUserOnSession("supervisorCustomers");
        outputInfo = this._dataObjectDispenser.getRenderizationInfo("ART104", 2, "it", reqCtx);
        assertEquals("Current user 'supervisorCustomers' can't view this DataObject", outputInfo.getRenderedDataobject().trim());
    }

    private String replaceNewLine(String input) {
        input = input.replaceAll("\\n", "");
        input = input.replaceAll("\\r", "");
        return input;
    }

    private void init() throws Exception {
        try {
            this._dataObjectDispenser = (IDataObjectDispenser) this.getService("DataObjectDispenserManager");
            this._dataObjectManager = (IDataObjectManager) this.getService("DataObjectManager");
            this._dataObjectModelManager = (IDataObjectModelManager) this.getService("DataObjectModelManager");
            //this._cacheInfoManager = (CacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IDataObjectDispenser _dataObjectDispenser = null;
    private IDataObjectManager _dataObjectManager = null;
    private IDataObjectModelManager _dataObjectModelManager = null;
    //private CacheInfoManager _cacheInfoManager;

    private String _attendedEnART1_cached
            = "ART1;\n"
            + "Pippo;\n"
            + "Paperino;\n"
            + "Pluto;\n"
            + "The title;\n"
            + "Mar 10, 2004;";

    private String _attendedEnART1
            = "ART1;\n"
            + "Pippo;\n"
            + "Paperino;\n"
            + "Pluto;\n"
            + "The title;\n"
            + "Mar 10, 2004;";

    private String _attendedItART104_cached
            = "ART104;\n"
            + "Walter;\n"
            + "Marco;\n"
            + "Eugenio;\n"
            + "William;\n"
            + "Titolo Contenuto 2 Coach;\n"
            + "4-gen-2007;";

    private String _attendedItART104
            = "ART104;\n"
            + "Walter;\n"
            + "Marco;\n"
            + "Eugenio;\n"
            + "William;\n"
            + "Titolo Contenuto 2 Coach;\n"
            + ",;\n"
            + "4-gen-2007;";

    private String _attendedItART120_cached
            = "ART120;\n"
            + "Titolo Contenuto degli &quot;Amministratori&quot;;\n"
            + "28-mar-2009;";

    private String _attendedItART120
            = "ART120;\n"
            + "Titolo Contenuto degli &quot;Amministratori&quot;;\n"
            + "28-mar-2009;";

    private String _attendedEnART120_cached
            = "ART120;\n"
            + "Title of Administrator's Content;\n"
            + "Mar 28, 2009;";

    private String _attendedEnART120
            = "ART120;\n"
            + "Title of Administrator's Content;\n"
            + "Mar 28, 2009;";

    private String _attendedItART121_cached
            = "ART121;\n"
            + "Titolo Contenuto degli &quot;Amministratori&quot; 2;\n"
            + "30-mar-2009;";

    private String _attendedEnART121_cached
            = "ART121;\n"
            + "Title of Administrator's Content &lt;2&gt;;\n"
            + "Mar 30, 2009;";

    private String _attendedEnART122_cached
            = "ART122;\n"
            + "Titolo Contenuto degli &quot;Amministratori&quot; 3;;";

}
