/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.DtoBuilder;

/**
 *
 * @author paddeo
 */
public class PageDtoBuilder extends DtoBuilder<IPage, PageDto> {

    @Override
    protected PageDto toDto(IPage src) {
        return new PageDto(src);
    }

}
