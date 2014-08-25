/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.guifragment;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

import java.util.List;
import static junit.framework.Assert.assertEquals;

/**
 * @author S.Puddu - E.Santoboni
 */
public class TestGuiFragmentManager extends BaseTestCase {
	
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
		String codeMaster = codes.get(0);
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
			GuiFragment guiFragment = this._guiFragmentManager.getGuiFragment(codeMaster);
			assertNull(guiFragment.getGui());
			assertNotNull(guiFragment.getDefaultGui());
			String newGui = "<@wp.fragment code=\"" + codeSlave + "\" escapeXml=false /> " + guiFragment.getDefaultGui();
			guiFragment.setGui(newGui);
			this._guiFragmentManager.updateGuiFragment(guiFragment);
			
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
			GuiFragment guiFragment = this._guiFragmentManager.getGuiFragment(codeMaster);
			guiFragment.setGui(null);
			this._guiFragmentManager.updateGuiFragment(guiFragment);
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
