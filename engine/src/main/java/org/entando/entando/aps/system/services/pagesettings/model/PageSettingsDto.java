/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.pagesettings.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author paddeo
 */
public class PageSettingsDto {

    List<ParamDto> params = new ArrayList<>();

    public List<ParamDto> getParams() {
        return params;
    }

    public void setParams(List<ParamDto> params) {
        this.params = params;
    }

    public void addParam(ParamDto param) {
        params.add(param);
    }

}
