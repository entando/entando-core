package org.entando.entando.web.assertionhelper;

import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class PageRestResponseAssertionHelper {


    /**
     * asserts that the received ResultActions does not contains filters under the relative json path
     * @param resultActions
     * @throws Exception
     */
    public static void assertNoFilters(ResultActions resultActions) throws Exception {

        resultActions.andExpect(jsonPath("$.metaData.filters").isArray())
                .andExpect(jsonPath("$.metaData.filters").isEmpty());
    }
}
