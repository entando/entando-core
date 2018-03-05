/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.pagesettings;

import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;

/**
 *
 * @author paddeo
 */
public interface IPageSettingsService {

    String BEAN_NAME = "PageSettingsService";

    PageSettingsDto getPageSettings();

    PageSettingsDto updatePageSettings(PageSettingsRequest request);

}
