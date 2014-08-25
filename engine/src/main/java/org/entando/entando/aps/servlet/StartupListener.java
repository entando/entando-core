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
package org.entando.entando.aps.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Init the system when the web application is started
 * @version 1.0
 * @author
 */
public class StartupListener extends org.springframework.web.context.ContextLoaderListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext svCtx = event.getServletContext();
		String msg = this.getClass().getName()+ ": INIT " + svCtx.getServletContextName();
		System.out.println(msg);
		super.contextInitialized(event);
		msg = this.getClass().getName() + ": INIT DONE "+ svCtx.getServletContextName();
		System.out.println(msg);
	}
	
}
