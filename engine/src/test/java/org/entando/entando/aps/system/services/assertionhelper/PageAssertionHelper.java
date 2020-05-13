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

public class PageAssertionHelper {


    /**
     * does assertions on the received PagedMetadata basing on the default PageMockHelper mocked data
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails, String pageStatus) {

       assertUsageDetails(usageDetails, PageMockHelper.UTILIZERS, PageMockHelper.UTILIZERS.length, 1, pageStatus);
    }


    /**
     * does assertions on the received PagedMetadata basing on the default PageMockHelper mocked data
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails, String[] utilizers, int totalItems, int pageNumber, String pageStatus) {

        int pagePublished = pageStatus.equals(IPageService.STATUS_ONLINE) ? 1 : 0;
        boolean lastPage = usageDetails.getLastPage() == usageDetails.getPage();

        assertEquals(totalItems + pagePublished, usageDetails.getTotalItems());
        assertEquals(utilizers.length + (lastPage ? pagePublished : 0), usageDetails.getBody().size());
        assertEquals(pageNumber, usageDetails.getPage());

        List<ComponentUsageEntity> usageEntityList = Arrays.stream(utilizers)
                .map(utilizer -> new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE, utilizer))
                .collect(Collectors.toList());

        int maxRange = usageDetails.getBody().size() - pagePublished;

        IntStream.range(0, maxRange)
                .forEach(i -> ComponentUsageEntityAssertionHelper.assertComponentUsageEntity(usageEntityList.get(i), usageDetails.getBody().get(i)));
    }

}
