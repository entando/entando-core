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
