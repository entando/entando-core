package org.entando.entando.aps.system.services.assertionhelper;

import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsageEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class PageAssertionHelper {


    /**
     * does assertions on the received PagedMetadata basing on the default PageMockHelper mocked data
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails) {

       assertUsageDetails(usageDetails, PageMockHelper.UTILIZERS, PageMockHelper.UTILIZERS.length, 1);
    }


    /**
     * does assertions on the received PagedMetadata basing on the default PageMockHelper mocked data
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails, String[] utilizers, int totalItems, int pageNumber) {

        assertEquals(totalItems, usageDetails.getTotalItems());
        assertEquals(utilizers.length, usageDetails.getBody().size());
        assertEquals(pageNumber, usageDetails.getPage());

        List<ComponentUsageEntity> usageEntityList = Arrays.stream(utilizers)
                .map(utilizer -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, utilizer))
                .collect(Collectors.toList());

        IntStream.range(0, usageDetails.getBody().size())
                .forEach(i -> ComponentUsageEntityAssertionHelper.assertComponentUsageEntity(usageEntityList.get(i), usageDetails.getBody().get(i)));
    }

}
