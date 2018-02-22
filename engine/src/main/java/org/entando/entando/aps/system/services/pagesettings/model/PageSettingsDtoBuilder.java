/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.pagesettings.model;

import java.util.Map;
import org.entando.entando.aps.system.services.DtoBuilder;

/**
 *
 * @author paddeo
 */
public class PageSettingsDtoBuilder extends DtoBuilder<Map<String, String>, PageSettingsDto> {

    @Override
    protected PageSettingsDto toDto(Map<String, String> sysParams) {
        PageSettingsDto dest = new PageSettingsDto();
        sysParams.keySet().stream().map((name) -> {
            ParamDto param = new ParamDto();
            param.setName(name);
            param.setValue(sysParams.get(name));
            return param;
        }).forEachOrdered((param) -> {
            dest.addParam(param);
        });
        return dest;
    }

}
