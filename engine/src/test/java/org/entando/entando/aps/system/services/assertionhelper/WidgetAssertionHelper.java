package org.entando.entando.aps.system.services.assertionhelper;

import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsageEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class WidgetAssertionHelper {


    /**
     * does assertions on the received PagedMetadata basing on the default WidgetMockHelper mocked data
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails) {

        assertEquals(2, usageDetails.getTotalItems());
        assertEquals(2, usageDetails.getBody().size());

        List<ComponentUsageEntity> usageEntityList = Arrays.asList(
                new ComponentUsageEntity("page", PageMockHelper.PAGE_CODE, IPageService.STATUS_DRAFT),
                new ComponentUsageEntity("page", PageMockHelper.PAGE_CODE, IPageService.STATUS_ONLINE));

        IntStream.range(0, usageDetails.getBody().size())
                .forEach(i -> ComponentUsageEntityAssertionHelper.assertComponentUsageEntity(usageEntityList.get(i), usageDetails.getBody().get(i)));
    }

}
