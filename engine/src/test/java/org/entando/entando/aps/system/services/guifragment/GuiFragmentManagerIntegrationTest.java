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
package org.entando.entando.aps.system.services.guifragment;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

import java.util.List;

/**
 * @author S.Puddu - E.Santoboni
 */
public class GuiFragmentManagerIntegrationTest extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testCrud() throws Exception {
        String code = "mockCrud_1";
        try {
            assertNull(this._guiFragmentManager.getGuiFragment(code));
            //add
            GuiFragment fragment = this.createMockFragment(code, "lorem ipsum", null);
            this._guiFragmentManager.addGuiFragment(fragment);

            GuiFragment fragment2 = this._guiFragmentManager.getGuiFragment(code);
            assertNotNull(fragment2);
            assertEquals(fragment.getGui(), fragment2.getGui());
            //update
            fragment2.setGui("dolor sit");
            this._guiFragmentManager.updateGuiFragment(fragment2);
            GuiFragment fragment3 = this._guiFragmentManager.getGuiFragment(code);
            assertEquals(fragment2.getGui(), fragment3.getGui());
            //delete
            this._guiFragmentManager.deleteGuiFragment(code);
            assertNull(this._guiFragmentManager.getGuiFragment(code));
        } catch (Exception e) {
            this._guiFragmentManager.deleteGuiFragment(code);
            throw e;
        }
    }

    public void testReferences() throws Exception {
        List<String> codes = this._guiFragmentManager.searchGuiFragments(null);
        assertEquals(1, codes.size());
        String codeMaster = "masterCode_1";
        String codeSlave = "mockCrud_2";
        try {
            GuiFragment fragment = this.createMockFragment(codeSlave, "lorem ipsum", null);
            this._guiFragmentManager.addGuiFragment(fragment);
            String[] utilizersNames = super.getApplicationContext().getBeanNamesForType(GuiFragmentUtilizer.class);
            for (int i = 0; i < utilizersNames.length; i++) {
                String beanNames = utilizersNames[i];
                GuiFragmentUtilizer beanUtilizer = (GuiFragmentUtilizer) this.getApplicationContext().getBean(beanNames);
                List utilizers = beanUtilizer.getGuiFragmentUtilizers(codeSlave);
                if (null != utilizers && !utilizers.isEmpty()) {
                    fail();
                }
            }
            GuiFragment guiFragment = new GuiFragment();
            guiFragment.setCode(codeMaster);
            String newGui = "<@wp.fragment code=\"" + codeSlave + "\" escapeXml=false /> " + guiFragment.getDefaultGui();
            guiFragment.setGui(newGui);
            this._guiFragmentManager.addGuiFragment(guiFragment);

            for (int i = 0; i < utilizersNames.length; i++) {
                String beanNames = utilizersNames[i];
                GuiFragmentUtilizer beanUtilizer = (GuiFragmentUtilizer) this.getApplicationContext().getBean(beanNames);
                List utilizers = beanUtilizer.getGuiFragmentUtilizers(codeSlave);
                if (beanNames.equals(SystemConstants.GUI_FRAGMENT_MANAGER)) {
                    assertEquals(1, utilizers.size());
                    GuiFragment fragmentUtilizer = (GuiFragment) utilizers.get(0);
                    assertEquals(codeMaster, fragmentUtilizer.getCode());
                } else if (null != utilizers && !utilizers.isEmpty()) {
                    fail();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            this._guiFragmentManager.deleteGuiFragment(codeSlave);
            this._guiFragmentManager.deleteGuiFragment(codeMaster);
            codes = this._guiFragmentManager.searchGuiFragments(null);
            assertEquals(1, codes.size());
        }
    }

    protected GuiFragment createMockFragment(String code, String gui, String widgetTypeCode) {
        GuiFragment fragment = new GuiFragment();
        fragment.setCode(code);
        fragment.setGui(gui);
        fragment.setWidgetTypeCode(widgetTypeCode);
        return fragment;
    }

    private void init() throws Exception {
        try {
            this._guiFragmentManager = (IGuiFragmentManager) this.getApplicationContext().getBean(SystemConstants.GUI_FRAGMENT_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IGuiFragmentManager _guiFragmentManager;

}
