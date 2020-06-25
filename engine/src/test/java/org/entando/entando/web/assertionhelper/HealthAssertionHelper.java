package org.entando.entando.web.assertionhelper;

import org.entando.entando.web.health.model.HealthErrorResponse;
import org.entando.entando.web.health.model.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthAssertionHelper {


    public static void assertHealthResponse(ResponseEntity<HealthResponse> expected, ResponseEntity<HealthResponse> actual) {

        assertEquals(expected.getStatusCodeValue(), actual.getStatusCodeValue());
        assertEquals(expected.getBody().getStatus(), actual.getBody().getStatus());
    }


    public static void assertHealthErrorResponse(HealthErrorResponse expected, HealthErrorResponse actual) {

        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.getError(), actual.getError());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertNotNull(expected.getTimestamp());
    }


    public static void assertSuccessfulRestResponse(ResultActions resultActions) throws Exception {

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(is("UP")));

    }
}
