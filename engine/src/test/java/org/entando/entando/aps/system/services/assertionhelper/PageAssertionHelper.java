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

        assertEquals(PageMockHelper.UTILIZERS.length, usageDetails.getTotalItems());
        assertEquals(PageMockHelper.UTILIZERS.length, usageDetails.getBody().size());
        assertEquals(1, usageDetails.getPage());

        String[] utilizs = {PageMockHelper.UTILIZER_2, PageMockHelper.UTILIZER_1};  // does not utilize PageMockHelper.UTILIZERS in order to manually simulate sorting
        List<ComponentUsageEntity> usageEntityList = Arrays.stream(utilizs)
                .map(utilizer -> new ComponentUsageEntity("page", utilizer, PageMockHelper.STATUS))
                .collect(Collectors.toList());

        IntStream.range(0, usageDetails.getBody().size())
                .forEach(i -> ComponentUsageEntityAssertionHelper.assertComponentUsageEntity(usageEntityList.get(i), usageDetails.getBody().get(i)));
    }

}
