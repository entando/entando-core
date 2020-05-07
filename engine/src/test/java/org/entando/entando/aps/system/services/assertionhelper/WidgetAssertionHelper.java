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

        List<ComponentUsageEntity> usageEntityList = Arrays.asList(
                new ComponentUsageEntity("page", PageMockHelper.PAGE_CODE, IPageService.STATUS_ONLINE),
                new ComponentUsageEntity("page", PageMockHelper.PAGE_CODE, IPageService.STATUS_DRAFT));

        assertUsageDetails(usageDetails, usageEntityList, usageEntityList.size(), 1);
    }


    /**
     * does assertions on the received PagedMetadata
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails, List<ComponentUsageEntity> usageEntityList, int totalItems, int pageNumber) {

        assertEquals(totalItems, usageDetails.getTotalItems());
        assertEquals(usageEntityList.size(), usageDetails.getBody().size());
        assertEquals(pageNumber, usageDetails.getPage());

        IntStream.range(0, usageDetails.getBody().size())
                .forEach(i -> ComponentUsageEntityAssertionHelper.assertComponentUsageEntity(usageEntityList.get(i), usageDetails.getBody().get(i)));
    }

}
