package org.entando.entando.aps.system.services.assertionhelper;

import org.entando.entando.web.component.ComponentUsageEntity;

import static org.junit.Assert.assertEquals;

public class ComponentUsageEntityAssertionHelper {


    public static void assertComponentUsageEntity(ComponentUsageEntity expected, ComponentUsageEntity actual) {

        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}
