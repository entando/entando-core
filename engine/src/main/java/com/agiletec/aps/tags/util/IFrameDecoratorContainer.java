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
public interface IFrameDecoratorContainer {
	
	public boolean needsDecoration(Widget widget, RequestContext reqCtx);
	
	public boolean isWidgetDecorator();
	
	public String getHeaderJspPath();
	
	public String getHeaderFragmentCode();
	
	public String getFooterJspPath();
	
	public String getFooterFragmentCode();
	
	public int getOrder();
	
}
