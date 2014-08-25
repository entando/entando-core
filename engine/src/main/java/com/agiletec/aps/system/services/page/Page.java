/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system.services.page;

import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.pagemodel.PageModel;

/**
 * This is the representation of a portal page
 * @author M.Diana - E.Santoboni
 */
public class Page extends TreeNode implements IPage {
	
	/**
	 * Set the position of the page with regard to its sisters
	 * @param position the position of the page.
	 */
	@Override
	protected void setPosition(int position) {
		super.setPosition(position);
	}
	
	/**
	 * Return the related model of page
	 * @return the page model
	 */
	@Override
	public PageModel getModel() {
		return _model;
	}

	/**
	 * WARNING: This method is for the page manager service only exclusive use
	 * Assign the given page model to the current object
	 * @param pageModel the model of the page to assign
	 */
	public void setModel(PageModel pageModel) {
		this._model = pageModel;
	}
	
	@Override
	public void addExtraGroup(String groupName) {
		if (null == this.getExtraGroups()) this.setExtraGroups(new HashSet<String>());
		this.getExtraGroups().add(groupName);
	}
	
	@Override
	public void removeExtraGroup(String groupName) {
		if (null == this.getExtraGroups()) return;
		this.getExtraGroups().remove(groupName);
	}
	
	public void setExtraGroups(Set<String> extraGroups) {
		this._extraGroups = extraGroups;
		
	}
	
	@Override
	public Set<String> getExtraGroups() {
		return _extraGroups;
	}
	
	@Override
	public IPage getParent() {
		return (IPage) super.getParent();
	}

	@Override
	public IPage[] getChildren() {
		IPage[] children = new IPage[super.getChildren().length];
		for (int i=0; i<super.getChildren().length; i++) {
			children[i] = (IPage) super.getChildren()[i];
		}
		return children;
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Return the code of the father of this page. This methods exists only to
	 * simplify the loading of the pages structure, it cannot be used in any other 
	 * circumstance.
	 * @return the code of the higher level page
	 */
	@Override
	public String getParentCode() {
		return _parentCode;
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Set the code of the father of this page. This methods exists only to
	 * simplify the loading of the pages structure, it cannot be used in any other 
	 * circumstance.
	 * @param parentCode the code of the higher level page
	 */
	public void setParentCode(String parentCode) {
		this._parentCode = parentCode;
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * This returns a boolean values indicating whether the page is
	 * displayed in the menus or similar.
	 * @return true if the page must be shown in the menu, false otherwise. 
	 */
	@Override
	public boolean isShowable() {
		return _showable;
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Toggle the visibility of the current page in the menu or similar.
	 * @param showable a boolean which toggles the visibility on when true, off otherwise.
	 */
	public void setShowable(boolean showable) {
		this._showable = showable;
	}

	/**
	 * Metodo riservato al servizio di gestione pagine.
	 * Imposta un titolo alla pagina
	 * @param lang La lingua del titolo
	 * @param title Il titolo da impostare
	 * @deprecated Use setTitle(String, String)
	 */
	public void setTitle(Lang lang, String title){
		this.setTitle(lang.getCode(), title);
	}

	/**
	 * Restituisce il titolo della pagina nella lingua specificata
	 * @param lang La lingua
	 * @return il titolo della pagina
	 * @deprecated Use getTitle(String)
	 */
	public String getTitle(Lang lang) {
		return this.getTitle(lang.getCode());
	}
	
	@Override
	@Deprecated
	public Widget[] getShowlets() {
		return getWidgets();
	}

	/**
	 * Return the widgets configured in this page.
	 * @return all the widgets of the current page
	 */
	@Override
	public Widget[] getWidgets() {
		return _widgets;
	}
	
	@Deprecated
	public void setShowlets(Widget[] widgets) {
		setWidgets(widgets);
	}

	/**
	 * Assign a set of widgets to the current page.
	 * @param widgets the widgets to assign.
	 */
	public void setWidgets(Widget[] widgets) {
		this._widgets = widgets;
	}
	
	@Override
	public boolean isUseExtraTitles() {
		return _useExtraTitles;
	}
	public void setUseExtraTitles(boolean useExtraTitles) {
		this._useExtraTitles = useExtraTitles;
	}
	
	@Override
	public String getCharset() {
		return _charset;
	}
	public void setCharset(String charset) {
		this._charset = charset;
	}
	
	@Override
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	
	public boolean isVoid() {
		boolean isVoid = true;
		Widget[] widgets = this.getWidgets();
		if (null != widgets) {
			for (int i = 0; i < widgets.length; i++) {
				if (null != widgets[i]) {
					isVoid = false;
					break;
				}
			}
		}
		return isVoid;
	}
	
	@Override
	public String toString() {
		return "Page: " + this.getCode();
	}

	/**
	 * The code of the higher level page
	 */
	private String _parentCode;
	
	private Set<String> _extraGroups;
	
	/**
	 * The page model associate to the current object
	 */
	private PageModel _model;

	/**
	 * Toggle menu visibility on and off
	 */
	private boolean _showable = false;
	
	/**
	 * The widgets of the current page
	 */
	private Widget[] _widgets;
	
	private boolean _useExtraTitles = false;
	
	private String _mimeType;
	
	private String _charset;
	
}