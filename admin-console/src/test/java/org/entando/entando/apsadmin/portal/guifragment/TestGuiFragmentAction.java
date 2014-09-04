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
package org.entando.entando.apsadmin.portal.guifragment;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;

import com.opensymphony.xwork2.Action;

public class TestGuiFragmentAction extends ApsAdminBaseTestCase  {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testList() throws Throwable {		
		String result = this.executeList("admin");
		assertEquals(Action.SUCCESS, result);
		GuiFragmentFinderAction action = (GuiFragmentFinderAction) this.getAction();
		assertEquals(1, action.getGuiFragmentsCodes().size());
	}

	public void testDetail() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", "content_viewer");
		String result = this.executeDetail("admin", params);
		assertEquals(Action.SUCCESS, result);
		GuiFragmentAction action = (GuiFragmentAction) this.getAction();
		assertEquals(1, action.getReferences().keySet().size());
	}
	/*
	public void testDetailWithReferences() throws Throwable {
		List<GuiFragment> fragments = new ArrayList<GuiFragment>(); 
		for (int i = 0; i < 5; i++) {
			String guiContent = this.getMockTemplate("code_"+i, "mock"+i, "content_viewer_mock", null);
			fragments.add(this.createMockFragment("mock"+i, guiContent, null));
		}
		try {
			Iterator<GuiFragment> it = fragments.iterator();
			while (it.hasNext()) {
				this._guiFragmentManager.addGuiFragment(it.next());
			}
			
			Map<String, String> params = new HashMap<String, String>();
			String result = null;
			
			params.put("code", "content_viewer");
			result = this.executeDetail("admin", params);
			assertEquals(Action.SUCCESS, result);
			GuiFragmentAction action = (GuiFragmentAction) this.getAction();
			assertEquals(1, action.getReferences().keySet().size());
			assertTrue(action.getReferences().containsKey("WidgetTypeManagerUtilizers"));
			
			params.put("code", "mock0");
			result = this.executeDetail("admin", params);
			assertEquals(Action.SUCCESS, result);
			action = (GuiFragmentAction) this.getAction();
			assertEquals(1, action.getReferences().keySet().size());
			assertTrue(action.getReferences().containsKey("GuiFragmentManagerUtilizers"));

			GuiFragment mock2 = this._guiFragmentManager.getGuiFragment("mock2");
			mock2.setGui(mock2.getGui().replaceAll("content_viewer_mock", "content_viewer"));
			this._guiFragmentManager.updateGuiFragment(mock2);
			this.waitNotifyingThread();
			params.put("code", "content_viewer");
			result = this.executeDetail("admin", params);
			assertEquals(Action.SUCCESS, result);
			action = (GuiFragmentAction) this.getAction();
			assertEquals(2, action.getReferences().keySet().size());
			assertTrue(action.getReferences().containsKey("GuiFragmentManagerUtilizers"));
			assertEquals(1, action.getReferences().get("GuiFragmentManagerUtilizers").size());
			assertTrue(action.getReferences().containsKey("WidgetTypeManagerUtilizers"));
			assertEquals(1, action.getReferences().get("WidgetTypeManagerUtilizers").size());

		} catch (Exception e) {
			throw e;
		} finally {
			Iterator<GuiFragment> it = fragments.iterator();
			while (it.hasNext()) {
				GuiFragment fragment = it.next();
				String code = fragment.getCode();
				this._guiFragmentManager.deleteGuiFragment(code);
			}
		}
	}
	*/
	protected GuiFragment createMockFragment(String code, String gui, String widgetTypeCode) {
		GuiFragment mFragment = new GuiFragment();
		mFragment.setCode(code);
		mFragment.setGui(gui);
		mFragment.setWidgetTypeCode(widgetTypeCode);
		return mFragment;
	}
	
	private String getMockTemplate(String a, String b, String c, String d) throws Throwable {
		String template = IOUtils.toString(this.getClass().getResourceAsStream("mockTemplate"));
		if (null != a) {
			template = template.replaceAll("PLACECHOLDER_A", a);
		}
		if (null != b) {
			template = template.replaceAll("PLACECHOLDER_B", b);
		}
		if (null != c) {
			template = template.replaceAll("PLACECHOLDER_C", c);
		}
		if (null != d) {
			template = template.replaceAll("PLACECHOLDER_D", d);
		}
		return template;
	}
	
	private String executeList(String currentUser) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction(NS, "list");
		return this.executeAction();
	}

	private String executeDetail(String currentUser, Map<String, String> params) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction(NS, "detail");
		this.addParameters(params);
		return this.executeAction();
	}
	
	private void init() {
		this._guiFragmentManager = (IGuiFragmentManager) this.getService(SystemConstants.GUI_FRAGMENT_MANAGER);
	}

	private IGuiFragmentManager _guiFragmentManager;
	private static final String NS = "/do/Portal/GuiFragment";
}
