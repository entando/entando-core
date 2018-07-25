/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jacms.apsadmin.portal.helper;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.helper.IExternalPageValidator;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eu
 */
public class ContentPageValidator implements IExternalPageValidator {

    private static final Logger logger = LoggerFactory.getLogger(ContentPageValidator.class);

    private IContentManager contentManager;

    @Override
    public void checkPageGroup(IPage page, boolean draftPageHepler, BaseAction action) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkForSetOnline(IPage page, BaseAction action) {
        try {
            for (Widget widget : page.getWidgets()) {
                if (null != widget) {
                    ApsProperties config = widget.getConfig();
                    String contentId = (null != config) ? config.getProperty("contentId") : null;
                    this.checkContent(action, contentId);
                }
            }
        } catch (ApsSystemException e) {
            logger.error("error checking draft page - content references", e);
            return false;
        }
        return true;
    }

    protected void checkContent(ActionSupport action, String contentId) throws ApsSystemException {
        if (StringUtils.isNotBlank(contentId)) {
            Content content = this.getContentManager().loadContent(contentId, true);
            if (null == content || !content.isOnLine()) {
                List<String> args = new ArrayList<>();
                args.add(null == content ? contentId : content.getDescription());
                action.addActionError(action.getText("error.page.set.online.content.ref.offline", args));
            }
        }
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

}
