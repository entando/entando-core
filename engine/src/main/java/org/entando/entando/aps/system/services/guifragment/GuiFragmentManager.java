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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import org.entando.entando.aps.system.services.cache.CacheInfoEvict;
import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.guifragment.event.GuiFragmentChangedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class GuiFragmentManager extends AbstractService implements IGuiFragmentManager, GuiFragmentUtilizer {
	
	private static final Logger _logger =  LoggerFactory.getLogger(GuiFragmentManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}
	
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#code)")
	public GuiFragment getGuiFragment(String code) throws ApsSystemException {
		GuiFragment guiFragment = null;
		try {
			guiFragment = this.getGuiFragmentDAO().loadGuiFragment(code);
		} catch (Throwable t) {
			_logger.error("Error loading guiFragment with code '{}'", code,  t);
			throw new ApsSystemException("Error loading guiFragment with code: " + code, t);
		}
		return guiFragment;
	}
	
	@Override
	public List<String> getGuiFragments() throws ApsSystemException {
		List<String> guiFragments = null;
		try {
			guiFragments = this.getGuiFragmentDAO().loadGuiFragments();
		} catch (Throwable t) {
			_logger.error("Error loading GuiFragment list",  t);
			throw new ApsSystemException("Error loading GuiFragment ", t);
		}
		return guiFragments;
	}
	
	@Override
	public List<String> searchGuiFragments(FieldSearchFilter filters[]) throws ApsSystemException {
		List<String> guiFragments = null;
		try {
			guiFragments = this.getGuiFragmentDAO().searchGuiFragments(filters);
		} catch (Throwable t) {
			_logger.error("Error searching GuiFragments", t);
			throw new ApsSystemException("Error searching GuiFragments", t);
		}
		return guiFragments;
	}
	
	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#guiFragment.code)")
	@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "'GuiFragment_uniqueByWidgetTypeGroup,GuiFragment_codesByWidgetTypeGroup'")//TODO improve group handling
	public void addGuiFragment(GuiFragment guiFragment) throws ApsSystemException {
		try {
			this.getGuiFragmentDAO().insertGuiFragment(guiFragment);
			this.notifyGuiFragmentChangedEvent(guiFragment, GuiFragmentChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding GuiFragment", t);
			throw new ApsSystemException("Error adding GuiFragment", t);
		}
	}
	
	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#guiFragment.code)")
	@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "'GuiFragment_uniqueByWidgetTypeGroup,GuiFragment_codesByWidgetTypeGroup'")//TODO improve group handling
	public void updateGuiFragment(GuiFragment guiFragment) throws ApsSystemException {
		try {
			this.getGuiFragmentDAO().updateGuiFragment(guiFragment);
			this.notifyGuiFragmentChangedEvent(guiFragment, GuiFragmentChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating GuiFragment", t);
			throw new ApsSystemException("Error updating GuiFragment " + guiFragment, t);
		}
	}
	
	@Override
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_'.concat(#code)")
	@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "'GuiFragment_uniqueByWidgetTypeGroup,GuiFragment_codesByWidgetTypeGroup'")//TODO improve group handling
	public void deleteGuiFragment(String code) throws ApsSystemException {
		try {
			GuiFragment guiFragment = this.getGuiFragment(code);
			this.getGuiFragmentDAO().removeGuiFragment(code);
			this.notifyGuiFragmentChangedEvent(guiFragment, GuiFragmentChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting GuiFragment with code {}", code, t);
			throw new ApsSystemException("Error deleting GuiFragment with code:" + code, t);
		}
	}
	
	private void notifyGuiFragmentChangedEvent(GuiFragment guiFragment, int operationCode) {
		GuiFragmentChangedEvent event = new GuiFragmentChangedEvent();
		event.setGuiFragment(guiFragment);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
	
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_uniqueByWidgetType_'.concat(#widgetTypeCode)")
	@CacheableInfo(groups = "'GuiFragment_uniqueByWidgetTypeGroup'")//TODO improve group handling
	public GuiFragment getUniqueGuiFragmentByWidgetType(String widgetTypeCode) throws ApsSystemException {
		GuiFragment guiFragment = null;
		try {
			List<String> fragmentCodes = this.getGuiFragmentCodesByWidgetType(widgetTypeCode);
			if (null != fragmentCodes && !fragmentCodes.isEmpty()) {
				if (fragmentCodes.size() > 1) {
					_logger.warn("There are more then one fragment joined with widget '{}'", widgetTypeCode);
				}
				guiFragment = this.getGuiFragment(fragmentCodes.get(0));
			}
		} catch (Throwable t) {
			_logger.error("Error loading guiFragment by widget '{}'", widgetTypeCode,  t);
			throw new ApsSystemException("Error loading guiFragment by widget " + widgetTypeCode, t);
		}
		return guiFragment;
	}
	
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, key = "'GuiFragment_codesByWidgetType_'.concat(#widgetTypeCode)")
	@CacheableInfo(groups = "'GuiFragment_codesByWidgetTypeGroup'")//TODO improve group handling
	public List<String> getGuiFragmentCodesByWidgetType(String widgetTypeCode) throws ApsSystemException {
		List<String> codes = null;
		try {
			FieldSearchFilter filter = new FieldSearchFilter("widgettypecode", widgetTypeCode, false);
			filter.setOrder(FieldSearchFilter.Order.ASC);
			FieldSearchFilter[] filters = {filter};
			codes = this.searchGuiFragments(filters);
		} catch (Throwable t) {
			_logger.error("Error loading fragments code by widget '{}'", widgetTypeCode,  t);
			throw new ApsSystemException("Error loading fragment codes by widget " + widgetTypeCode, t);
		}
		return codes;
	}
	
	@Override
	public List getGuiFragmentUtilizers(String guiFragmentCode)	throws ApsSystemException {
		List<GuiFragment> utilizers = new ArrayList<GuiFragment>();
		try {
			String strToSearch = "code=\""+guiFragmentCode + "\"";
			Set<String> results = new HashSet<String>();
			results.addAll(this.searchFragments(strToSearch, "gui"));
			results.addAll(this.searchFragments(strToSearch, "defaultgui"));
			if (!results.isEmpty()) {
				Pattern pattern = Pattern.compile("<@wp\\.fragment.*code=\""+ guiFragmentCode + "\".*/>", Pattern.MULTILINE);
				Iterator<String> it = results.iterator();
				while (it.hasNext()) {
					String fcode = it.next();
					GuiFragment fragment = this.getGuiFragment(fcode);
					if (this.scanTemplate(pattern, fragment.getGui()) || this.scanTemplate(pattern, fragment.getDefaultGui())) {
						utilizers.add(fragment);						
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting utilizers", t);
			throw new ApsSystemException("Error extracting utilizers", t);
		}
		return utilizers;
	}

	protected boolean scanTemplate(Pattern pattern, String template) {
		boolean check = false;
		if (StringUtils.isNotBlank(template)) {
			Matcher matcher = pattern.matcher(template);
			if (matcher.find()) {
				check = true;
			}	
		}
		return check;
	}
	
	protected Set<String> searchFragments(String strToSearch, String column) throws ApsSystemException {
		FieldSearchFilter filterTag = new FieldSearchFilter(column, "<@wp.fragment", true);
		FieldSearchFilter[] filters1 = new FieldSearchFilter[]{filterTag};
		List<String> result1 = this.searchGuiFragments(filters1);
		FieldSearchFilter filterCode = new FieldSearchFilter(column, strToSearch, true);
		FieldSearchFilter[] filters2 = new FieldSearchFilter[]{filterCode};
		List<String> result2 = this.searchGuiFragments(filters2);
		Set<String> result = new HashSet<String>();
		result.addAll(result1);
		result.addAll(result2);
		return result;
	}
	
	public void setGuiFragmentDAO(IGuiFragmentDAO guiFragmentDAO) {
		 this._guiFragmentDAO = guiFragmentDAO;
	}
	protected IGuiFragmentDAO getGuiFragmentDAO() {
		return _guiFragmentDAO;
	}
	
	private IGuiFragmentDAO _guiFragmentDAO;
	
}
