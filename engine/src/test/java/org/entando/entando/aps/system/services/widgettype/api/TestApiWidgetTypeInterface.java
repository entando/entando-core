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
package org.entando.entando.aps.system.services.widgettype.api;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author E.Santoboni
 */
public class TestApiWidgetTypeInterface extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testJaxbWidgetType() throws Throwable {
        this.testJaxbWidgetType("login_form");
        this.testJaxbWidgetType("formAction");
        this.testJaxbWidgetType("logic_type");
        this.testJaxbWidgetType("entando_apis");
    }

    private void testJaxbWidgetType(String widgetTypeCode) throws Throwable {
        WidgetType widgetType = this._widgetTypeManager.getWidgetType(widgetTypeCode);
        JAXBWidgetType jaxbWidgetType = this.getJaxbWidgetType(widgetType);
        String body = this.getMarshalledObject(jaxbWidgetType);
        assertNotNull(body);
    }

    protected String getMarshalledObject(Object object) throws Throwable {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    public void testGetJaxbWidgetType() throws Throwable {
        this.testInvokeGetJaxbWidgetType("login_form");
        this.testInvokeGetJaxbWidgetType("formAction");
        this.testInvokeGetJaxbWidgetType("logic_type");
        this.testInvokeGetJaxbWidgetType("entando_apis");
    }

    private void testInvokeGetJaxbWidgetType(String widgetTypeCode) throws Throwable {
        Properties properties = new Properties();
        properties.put(SystemConstants.API_USER_PARAMETER, super.getUser("admin"));
        properties.put("code", widgetTypeCode);
        WidgetType widgetType = this._widgetTypeManager.getWidgetType(widgetTypeCode);
        try {
            JAXBWidgetType jaxbwt = this._apiWidgetTypeInterface.getWidgetType(properties);
            assertNotNull(jaxbwt);
            assertEquals(widgetTypeCode, jaxbwt.getCode());
            assertEquals(widgetType.getTitles(), jaxbwt.getTitles());
        } catch (ApiException ae) {
            if (null != widgetType) {
                fail();
            }
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testAddJaxbWidgetType() throws Throwable {
        this.testInvokeAddJaxbWidgetType("testjaxb_login_form", "login_form", null, false);
        this.testInvokeAddJaxbWidgetType("testjaxb_login_form", "login_form", "**testjaxb_login_form** gui", true);
        this.testInvokeAddJaxbWidgetType("testjaxb_formAction", "formAction", null, false);
        this.testInvokeAddJaxbWidgetType("testjaxb_formAction", "formAction", "**testjaxb_formAction** gui", true);
        this.testInvokeAddJaxbWidgetType("testjaxb_logic_type", "logic_type", null, true);
        this.testInvokeAddJaxbWidgetType("testjaxb_logic_type", "logic_type", "**testjaxb_logic_type** gui", false);
        this.testInvokeAddJaxbWidgetType("testjaxb_entando_apis", "entando_apis", null, false);
    }

    private void testInvokeAddJaxbWidgetType(String newWidgetTypeCode, String widgetToClone, String customSingleGui,
            boolean expectedSuccess) throws Throwable {
        WidgetType widgetType = this._widgetTypeManager.getWidgetType(widgetToClone);
        assertNotNull(widgetType);
        WidgetType newWidgetType = widgetType.clone();
        assertNull(this._widgetTypeManager.getWidgetType(newWidgetTypeCode));
        newWidgetType.setCode(newWidgetTypeCode);
        try {
            JAXBWidgetType jaxbWidgetType = this.getJaxbWidgetType(newWidgetType);
            if (null != customSingleGui) {
                jaxbWidgetType.setGui(customSingleGui);
            }
            this._apiWidgetTypeInterface.addWidgetType(jaxbWidgetType);
            if (!expectedSuccess) {
                fail();
            }
            WidgetType extractedWidgetType = this._widgetTypeManager.getWidgetType(newWidgetTypeCode);
            assertNotNull(extractedWidgetType);
            assertEquals(newWidgetType.getConfig(), extractedWidgetType.getConfig());
            assertEquals(newWidgetType.getMainGroup(), extractedWidgetType.getMainGroup());
            assertEquals(newWidgetType.getTitles(), extractedWidgetType.getTitles());
            assertEquals(newWidgetType.getTypeParameters(), extractedWidgetType.getTypeParameters());
            assertFalse(extractedWidgetType.isLocked());
        } catch (ApiException ae) {
            if (expectedSuccess) {
                fail();
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            List<String> codes = this._guiFragmentManager.getGuiFragmentCodesByWidgetType(newWidgetTypeCode);
            if (null != codes) {
                for (int i = 0; i < codes.size(); i++) {
                    String code = codes.get(i);
                    this._guiFragmentManager.deleteGuiFragment(code);
                }
            }
            this._widgetTypeManager.deleteWidgetType(newWidgetTypeCode);
        }
    }

    public void testUpdateJaxbWidgetType() throws Throwable {
        ApsProperties titles = new ApsProperties();
        titles.setProperty("en", "English title");
        titles.setProperty("it", "Italian title");
        this.testInvokeUpdateJaxbNoLogicWidgetType("login_form", titles, true, null, true);
        this.testInvokeUpdateJaxbNoLogicWidgetType("login_form", titles, true, "Gui of login_form", true);
    }

    private void testInvokeUpdateJaxbNoLogicWidgetType(String widgetTypeCode, ApsProperties titles, boolean removeParametersFromCall,
            String customSingleGui, boolean expectedSuccess) throws Throwable {
        WidgetType widgetType = this._widgetTypeManager.getWidgetType(widgetTypeCode);
        assertNotNull(widgetType);
        WidgetType widgetTypeToEdit = widgetType.clone();
        GuiFragment previousFragment = this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode);
        ApsProperties originalTitles = widgetType.getTitles();
        try {
            JAXBWidgetType jaxbWidgetType = this.getJaxbWidgetType(widgetTypeToEdit);
            if (StringUtils.isNotBlank(customSingleGui)) {
                jaxbWidgetType.setGui(customSingleGui);
            }
            if (null != titles) {
                jaxbWidgetType.setTitles(titles);
            }
            if (removeParametersFromCall) {
                jaxbWidgetType.setTypeParameters(null);
            }
            this._apiWidgetTypeInterface.updateWidgetType(jaxbWidgetType);
            if (!expectedSuccess) {
                fail();
            }
            WidgetType extractedWidgetType = this._widgetTypeManager.getWidgetType(widgetTypeCode);
            assertNotNull(extractedWidgetType);
            assertEquals(widgetType.getMainGroup(), extractedWidgetType.getMainGroup());
            assertEquals(titles, extractedWidgetType.getTitles());
            assertEquals(widgetType.getTypeParameters(), extractedWidgetType.getTypeParameters());
            assertEquals(widgetType.isLocked(), extractedWidgetType.isLocked());
            if (StringUtils.isNotBlank(customSingleGui) && null == previousFragment) {
                assertNotNull(this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode));
            }
        } catch (ApiException ae) {
            if (expectedSuccess) {
                fail();
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null == previousFragment) {
                List<String> codes = this._guiFragmentManager.getGuiFragmentCodesByWidgetType(widgetTypeCode);
                if (null != codes) {
                    for (int i = 0; i < codes.size(); i++) {
                        String code = codes.get(i);
                        this._guiFragmentManager.deleteGuiFragment(code);
                    }
                }
            } else if (null == this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode)) {
                this._guiFragmentManager.addGuiFragment(previousFragment);
            } else {
                this._guiFragmentManager.updateGuiFragment(previousFragment);
            }
            this._widgetTypeManager.updateWidgetType(widgetType.getCode(), originalTitles, widgetType.getConfig(), widgetType
                    .getMainGroup());
        }
    }

    private JAXBWidgetType getJaxbWidgetType(WidgetType widgetType) throws Throwable {
        assertNotNull(widgetType);
        GuiFragment singleGuiFragment = null;
        List<GuiFragment> fragments = new ArrayList<>();
        if (!widgetType.isLogic()) {
            singleGuiFragment = this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetType.getCode());
        } else {
            List<String> fragmentCodes = this._guiFragmentManager.getGuiFragmentCodesByWidgetType(widgetType.getCode());
            if (null != fragmentCodes) {
                for (int i = 0; i < fragmentCodes.size(); i++) {
                    String fragmentCode = fragmentCodes.get(i);
                    GuiFragment fragment = this._guiFragmentManager.getGuiFragment(fragmentCode);
                    if (null != fragment) {
                        fragments.add(fragment);
                    }
                }
            }
        }
        return new JAXBWidgetType(widgetType, singleGuiFragment, fragments);
    }

    public void testDeleteJaxbWidgetType_1() throws Throwable {
        this.testInvokeDeleteJaxbNoLogicWidgetType("login_form", false);
        this.testInvokeDeleteJaxbNoLogicWidgetType("formAction", false);
    }

    public void testDeleteJaxbWidgetType_2() throws Throwable {
        String code = "jaxb_test_delete_1";
        assertNull(this._widgetTypeManager.getWidgetType(code));
        try {
            this.addMockWidget(code);
            assertNotNull(this._widgetTypeManager.getWidgetType(code));
            this.testInvokeDeleteJaxbNoLogicWidgetType(code, true);
        } catch (Throwable t) {
            throw t;
        } finally {
            this._widgetTypeManager.deleteWidgetType(code);
            assertNull(this._widgetTypeManager.getWidgetType(code));
        }
    }
    
    public void testDeleteJaxbWidgetType_3() throws Throwable {
        String code = "jaxb_test_delete_2";
        String pageCode = "homepage";
        int frame = 5;
        assertNull(this._widgetTypeManager.getWidgetType(code));
        IPage homepage = this._pageManager.getDraftPage(pageCode);
        assertNull(homepage.getWidgets()[frame]);
        assertNull(homepage.getWidgets()[frame]);
        try {
            this.addMockWidget(code);
            WidgetType addedWidgetType = this._widgetTypeManager.getWidgetType(code);
            assertNotNull(addedWidgetType);
            Widget widget = new Widget();
            widget.setType(addedWidgetType);
            this.setPageWidgets(pageCode, frame, widget);
            homepage = this._pageManager.getDraftPage(pageCode);
            assertNotNull(homepage.getWidgets()[frame]);
            assertEquals(code, homepage.getWidgets()[frame].getType().getCode());
            
            this.testInvokeDeleteJaxbNoLogicWidgetType(code, false);
            this.setPageWidgets(pageCode, frame, null);
            homepage = this._pageManager.getDraftPage(pageCode);
            assertNull(homepage.getWidgets()[frame]);
            this.testInvokeDeleteJaxbNoLogicWidgetType(code, true);
        } catch (Throwable t) {
            this.setPageWidgets(pageCode, frame, null);
            throw t;
        } finally {
            this._widgetTypeManager.deleteWidgetType(code);
            assertNull(this._widgetTypeManager.getWidgetType(code));
        }
    }
    
    private void setPageWidgets(String pageCode, int frame, Widget widget) throws ApsSystemException {
        IPage page = this._pageManager.getDraftPage(pageCode);
        page.getWidgets()[frame] = widget;
        this._pageManager.updatePage(page);
    }

    private void testInvokeDeleteJaxbNoLogicWidgetType(String widgetTypeCode, boolean expectedSuccess) throws Throwable {
        Properties properties = new Properties();
        properties.put(SystemConstants.API_USER_PARAMETER, super.getUser("admin"));
        properties.put("code", widgetTypeCode);
        WidgetType widgetType = this._widgetTypeManager.getWidgetType(widgetTypeCode);
        assertNotNull(widgetType);
        WidgetType widgetTypeToDelete = widgetType.clone();
        GuiFragment previousFragment = this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode);
        try {
            this._apiWidgetTypeInterface.deleteWidgetType(properties);
            if (!expectedSuccess) {
                fail();
            }
            assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
            assertNull(this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode));
        } catch (ApiException ae) {
            if (expectedSuccess) {
                throw ae;
            }
        } catch (Throwable t) {
            if (expectedSuccess) {
                throw t;
            }
        } finally {
            if (null != previousFragment && null == this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode)) {
                this._guiFragmentManager.addGuiFragment(previousFragment);
            }
            if (null == this._widgetTypeManager.getWidgetType(widgetTypeCode)) {
                this._widgetTypeManager.addWidgetType(widgetTypeToDelete);
            }
        }
    }

    private void addMockWidget(String widgetTypeCode) throws Throwable {
        WidgetType type = new WidgetType();
        type.setCode(widgetTypeCode);
        ApsProperties titles = new ApsProperties();
        titles.setProperty("en", "English title");
        titles.setProperty("it", "Italian title");
        type.setTitles(titles);
        this._widgetTypeManager.addWidgetType(type);
    }

    private void init() throws Exception {
        try {
            this._widgetTypeManager = (IWidgetTypeManager) this.getApplicationContext().getBean(SystemConstants.WIDGET_TYPE_MANAGER);
            this._pageManager = (IPageManager) this.getApplicationContext().getBean(SystemConstants.PAGE_MANAGER);
            this._guiFragmentManager = (IGuiFragmentManager) this.getApplicationContext().getBean(SystemConstants.GUI_FRAGMENT_MANAGER);
            this._apiWidgetTypeInterface = (ApiWidgetTypeInterface) this.getApplicationContext().getBean("ApiWidgetTypeInterface");
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IWidgetTypeManager _widgetTypeManager;
    private IPageManager _pageManager;
    private IGuiFragmentManager _guiFragmentManager;
    private ApiWidgetTypeInterface _apiWidgetTypeInterface;

}
