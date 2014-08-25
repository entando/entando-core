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
package com.agiletec.aps.tags.util;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.page.Widget;

/**
 * @author E.Santoboni
 */
public class BaseFrameDecoratorContainer implements IFrameDecoratorContainer {
	
	@Override
	public boolean needsDecoration(Widget widget, RequestContext reqCtx) {
		return true;
	}
	
	@Override
	public boolean isWidgetDecorator() {
		return false;
	}
	
	@Deprecated
	public void setHeaderPath(String headerPath) {
		this.setHeaderJspPath(headerPath);
	}
	
	@Override
	public String getHeaderJspPath() {
		return _headerJspPath;
	}
	public void setHeaderJspPath(String headerJspPath) {
		this._headerJspPath = headerJspPath;
	}
	
	@Override
	public String getHeaderFragmentCode() {
		return _headerFragmentCode;
	}
	public void setHeaderFragmentCode(String headerFragmentCode) {
		this._headerFragmentCode = headerFragmentCode;
	}
	
	@Deprecated
	public void setFooterPath(String footerPath) {
		this.setFooterJspPath(footerPath);
	}
	
	@Override
	public String getFooterJspPath() {
		return _footerJspPath;
	}
	public void setFooterJspPath(String footerJspPath) {
		this._footerJspPath = footerJspPath;
	}
	
	@Override
	public String getFooterFragmentCode() {
		return _footerFragmentCode;
	}
	public void setFooterFragmentCode(String footerFragmentCode) {
		this._footerFragmentCode = footerFragmentCode;
	}
	
	@Override
	public int getOrder() {
		return _order;
	}
	public void setOrder(int order) {
		this._order = order;
	}
	
	private String _headerJspPath;
	private String _headerFragmentCode;
	private String _footerJspPath;
	private String _footerFragmentCode;
	private int _order;
	
}
