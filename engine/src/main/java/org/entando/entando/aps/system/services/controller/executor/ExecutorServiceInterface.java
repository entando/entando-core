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
package org.entando.entando.aps.system.services.controller.executor;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;

import com.agiletec.aps.system.RequestContext;

/**
 * @author M.Diana - E.Santoboni
 */
public interface ExecutorServiceInterface extends InitializingBean, Serializable {
	
	/**
	 * Esegue le operazioni specifiche del sottoservizio.
	 * @param reqCtx Il contesto di richiesta.
	 */
	public void service(RequestContext reqCtx);

}
