package org.entando.entando.aps.system.services.assertionhelper;

import org.entando.entando.aps.system.services.mockhelper.FragmentMockHelper;
import org.entando.entando.aps.system.services.mockhelper.PageMockHelper;
import org.entando.entando.aps.system.services.mockhelper.WidgetMockHelper;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsageEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class GuiFragmentAssertionHelper {

    /**
     * does assertions on the received PagedMetadata basing on the default FragmentMockHelper mocked data
     * @param usageDetails
     */
    public static void assertUsageDetails(PagedMetadata<ComponentUsageEntity> usageDetails) {

        int expectedTotalItems = 5;

        assertEquals(expectedTotalItems, usageDetails.getTotalItems());
        assertEquals(expectedTotalItems, usageDetails.getBody().size());
        assertEquals(1, usageDetails.getPage());

        List<ComponentUsageEntity> usageEntityList = Arrays.asList(
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_WIDGET, WidgetMockHelper.WIDGET_1_CODE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_FRAGMENT, FragmentMockHelper.FRAGMENT_REF_1_CODE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_FRAGMENT, FragmentMockHelper.FRAGMENT_REF_2_CODE),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE_TEMPLATE, PageMockHelper.PAGE_MODEL_REF_CODE_1),
                new ComponentUsageEntity(ComponentUsageEntity.TYPE_PAGE_TEMPLATE, PageMockHelper.PAGE_MODEL_REF_CODE_2));

        IntStream.range(0, usageDetails.getBody().size())
                .forEach(i -> ComponentUsageEntityAssertionHelper.assertComponentUsageEntity(usageEntityList.get(i), usageDetails.getBody().get(i)));
    }

}