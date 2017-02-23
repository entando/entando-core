package com.agiletec.aps.system.services.page;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

public class PageTestUtil {
	
	public static void comparePagesFull(IPage expected, IPage actual, boolean changed) {
		comparePages(expected, actual, changed);
		comparePageMetadata(expected.getDraftMetadata(), actual.getDraftMetadata(), 0);
		comparePageMetadata(expected.getOnlineMetadata(), actual.getOnlineMetadata(), 0);
		compareWidgets(expected.getDraftWidgets(), actual.getDraftWidgets());
		compareWidgets(expected.getOnlineWidgets(), actual.getOnlineWidgets());
	}
	
	public static void comparePages(IPage expected, IPage actual, boolean changed) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected.getCode(), actual.getCode());
//			assertEquals(expected.isRoot(), actual.isRoot());
//			assertEquals(expected.getParentCode(), actual.getParentCode());
//			assertEquals(expected.getPosition(), actual.getPosition());
			if (expected.getChildren() == null) {
				assertNull(actual.getChildren());
			} else {
				assertEquals(expected.getChildren().length, actual.getChildren().length);
			}
			if (expected.getAllChildren() == null) {
				assertNull(actual.getAllChildren());
			} else {
				assertEquals(expected.getAllChildren().length, actual.getAllChildren().length);
			}
			assertEquals(expected.getGroup(), actual.getGroup());
			assertEquals(expected.getTitles(), actual.getTitles());
//			assertEquals(expected.getPath(), actual.getPath());
//			assertEquals(expected.getPath("|", true), actual.getPath("|", true));
//			assertEquals(expected.getPathArray(), actual.getPathArray());
			assertEquals(changed, actual.isChanged());
			assertEquals(expected.isOnline(), actual.isOnline());
			assertEquals(expected.isShowable(), actual.isShowable());
			assertEquals(expected.isUseExtraTitles(), actual.isUseExtraTitles());
			assertEquals(expected.getMimeType(), actual.getMimeType());
			assertEquals(expected.getCharset(), actual.getCharset());
		}
	}
	
	/**
	 * @param expected The expected PageMetadata to check
	 * @param actual The actual PageMetadata to check
	 * @param dateExpectation The result of the comparison between the 2 updatedAt dates. 1 if expected is greater, 0 if equals, -1 if lower
	 */
	public static void comparePageMetadata(PageMetadata expected, PageMetadata actual, Integer dateExpectation) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected.getTitles(), actual.getTitles());
			assertEquals(expected.getExtraGroups(), actual.getExtraGroups());
			if (expected.getModel() == null) {
				assertNull(actual.getModel());
			} else {
				assertEquals(expected.getModel().getCode(), actual.getModel().getCode());
			}
			assertEquals(expected.isShowable(), actual.isShowable());
			assertEquals(expected.isUseExtraTitles(), actual.isUseExtraTitles());
			assertEquals(expected.getMimeType(), actual.getMimeType());
			assertEquals(expected.getCharset(), actual.getCharset());
			
			if (dateExpectation != null) {
				if (expected.getUpdatedAt() == null) {
					if (dateExpectation < 0) {
						assertNotNull(actual.getUpdatedAt());
					} else {
						assertNull(actual.getUpdatedAt());
					}
				} else {
					assertEquals(dateExpectation.intValue(), DateUtils.truncate(expected.getUpdatedAt(), Calendar.MILLISECOND)
							.compareTo(DateUtils.truncate(actual.getUpdatedAt(), Calendar.MILLISECOND)));
				}
			}
		}
	}
	
	public static void compareWidgets(Widget[] expected, Widget[] actual) {
		if (expected == null) {
			assertNull(actual);
		} else {
			assertEquals(expected.length, actual.length);
			for (int i = 0; i < expected.length; i++) {
				assertEquals(expected[i], actual[i]);
			}
		}
	}
	
	public static IPage getPageByCode(List<IPage> pages, String code) {
		IPage extractedPage = null;
	    for (int i=0; i<pages.size(); i++) {
			IPage page = pages.get(i);
			if (page.getCode().equals(code)) {
				extractedPage = page;
				break;
			}
		}
		return extractedPage;
	}
	
	public static Page createPage(String code, IPage parentPage, String groupName, PageMetadata onlineMetadata, 
			PageMetadata draftMetadata, Widget[] onlineWidgets, Widget[] draftWidgets) {
		Page page = new Page();
    	page.setCode(code);
    	page.setParent(parentPage);
    	page.setParentCode(parentPage.getCode());
    	page.setOnlineMetadata(onlineMetadata);
    	page.setDraftMetadata(draftMetadata);
    	page.setGroup(groupName);
    	page.setOnlineWidgets(onlineWidgets);
    	page.setDraftWidgets(draftWidgets);
		return page;
	}
	
	public static PageMetadata createPageMetadata(String pageModelCode, boolean showable, String defaultTitle, 
			String mimeType, String charset, boolean useExtraTitles, Set<String> extraGroups, Date updatedAt) {
		PageMetadata metadata = new PageMetadata();
		PageModel pageModel = new PageModel();
		pageModel.setCode(pageModelCode);
		metadata.setModel(pageModel);
		
		metadata.setShowable(showable);
		metadata.setTitle("it", defaultTitle);
		if (extraGroups != null) {
			metadata.setExtraGroups(extraGroups);
		}
		metadata.setMimeType(mimeType);
		metadata.setCharset(charset);
		metadata.setUseExtraTitles(useExtraTitles);
		metadata.setExtraGroups(extraGroups);
		metadata.setUpdatedAt(updatedAt);
		return metadata;
	}
	
	public static <T> T[] copyArray(T[] arrayToCopy) {
		T[] copiedArray = null;
		if (arrayToCopy != null) {
			copiedArray = Arrays.copyOf(arrayToCopy, arrayToCopy.length);
		}
		return copiedArray;
	}
	
	public static ApsProperties createProperties(String p1Key, String p1Value, String p2Key, String p2Value) {
		ApsProperties properties = new ApsProperties();
		properties.setProperty(p1Key, p1Value);
		properties.setProperty(p2Key, p2Value);
		return properties;
	}
	
	public static Widget createWidget(String widgetCode, ApsProperties config, IWidgetTypeManager widgetTypeManager) {
		Widget widget = new Widget();
		widget.setConfig(config);
		WidgetType widgetType = widgetTypeManager.getWidgetType(widgetCode);
		widget.setType(widgetType);
		return widget;
	}
	
	public static Widget[] getValuedWidgets(Widget[] widgets) {
		int inUse = 0;
		Widget[] widgetsInUse = {};
		if (widgets != null) {
			for (Widget current : widgets) {
				if (current != null) {
					inUse++;
				}
			}
			widgetsInUse = new Widget[inUse];
			int index = 0;
			for (int i = 0; i < widgets.length; i++) {
				Widget current = widgets[i];
				if (current != null) {
					widgetsInUse[index++] = current;
				}
			}
		}
		return widgetsInUse;
	}
	
	public static void deletePage(IPage page, PageDAO pageDAO) throws Throwable {
		try {
        	pageDAO.deletePage(page);
        } catch (Throwable e) {
        	throw e;
        }
        IPage currentPage = PageTestUtil.getPageByCode(pageDAO.loadPages(), "temp");
		assertNull(currentPage);        
	}
	
}
