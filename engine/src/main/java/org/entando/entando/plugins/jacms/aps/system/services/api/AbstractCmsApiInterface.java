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
package org.entando.entando.plugins.jacms.aps.system.services.api;

import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

/**
 * @author E.Santoboni
 */
public abstract class AbstractCmsApiInterface {
    
    protected IContentManager getContentManager() {
        return _contentManager;
    }
    public void setContentManager(IContentManager contentManager) {
        this._contentManager = contentManager;
    }
    
    protected IContentModelManager getContentModelManager() {
        return _contentModelManager;
    }
    public void setContentModelManager(IContentModelManager contentModelManager) {
        this._contentModelManager = contentModelManager;
    }
    
    private IContentManager _contentManager;
    private IContentModelManager _contentModelManager;
    
}