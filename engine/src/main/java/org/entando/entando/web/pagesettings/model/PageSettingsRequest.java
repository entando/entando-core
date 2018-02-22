/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.pagesettings.model;

import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author paddeo
 */
public class PageSettingsRequest {

    @NotEmpty(message = "NotEmpty.pagesettings.params")
    private List<Param> params;

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

}
