package org.entando.entando.aps.system.services.assertionhelper;

import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsage;
import org.entando.entando.web.component.ComponentUsageEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class PageAssertionHelper {


    /**
     * does assertions on the received PagedMetadata basing on the default PageMockHelper mocked data
     * @param pageUsageDetails
     */
    public static void assertPageUsageDetails(PagedMetadata<ComponentUsageEntity> pageUsageDetails) {

        assertEquals(PageMockHelper.UTILIZERS.length, pageUsageDetails.getTotalItems());
        assertEquals(1, pageUsageDetails.getPage());

        String[] utilizs = {PageMockHelper.UTILIZER_2, PageMockHelper.UTILIZER_1};  // does not utilize PageMockHelper.UTILIZERS in order to manually simulate sorting
        List<ComponentUsageEntity> usageEntityList = Arrays.stream(utilizs)
                .map(utilizer -> new ComponentUsageEntity("page", utilizer))
                .collect(Collectors.toList());

        IntStream.range(0, pageUsageDetails.getBody().size())
                .forEach(i -> assertComponentUsageEntity(usageEntityList.get(i), pageUsageDetails.getBody().get(i)));
    }


    public static void assertComponentUsageEntity(ComponentUsageEntity expected, ComponentUsageEntity actual) {

        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getCode(), actual.getCode());
    }
}
