package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PageDtoBuilderTest {

    @Test
    public void testDefaultContentTypeAndCharSet() {
        PageDtoBuilder builder = new PageDtoBuilder();

        Page page = new Page();
        page.setModel(new PageModel());
        PageDto dto = builder.convert(page);

        assertEquals(dto.getCharset(), "utf8");
        assertEquals(dto.getContentType(), "text/html");
    }

}
