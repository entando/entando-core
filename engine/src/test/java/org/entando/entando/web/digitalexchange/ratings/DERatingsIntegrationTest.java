package org.entando.entando.web.digitalexchange.ratings;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.DERatingsDAO;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DERatingsIntegrationTest extends AbstractControllerIntegrationTest {

    private ObjectMapper jsonMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private String accessToken;

    @Autowired
    private DERatingsDAO dao;

    @Before
    public void setupTest() {
        UserDetails user = new OAuth2TestUtils
                .UserBuilder("jack_bauer", "0x24")
                .grantedToRoleAdmin().build();
        accessToken = mockOAuthInterceptor(user);
    }

    @Test
    public void findAllRatings() throws Exception {
        DERating rating1 = createDeRating(1, "component1", 100);
        dao.delete(rating1.getId());
        dao.save(rating1);

        DERating rating2 = createDeRating(2, "component2", 20);
        dao.delete(rating2.getId());
        dao.save(rating2);

        mockMvc.perform(
                get("/digitalExchange/ratings")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.metaData.pageSize").value("2"))
                .andReturn();
    }

    @Test
    public void addNewRating() throws Exception {
        DERating deRating = createDeRating(1, "component1", 60);
        dao.delete(deRating.getId());

        mockMvc.perform(
                post("/digitalExchange/ratings")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonMapper.writeValueAsString(deRating))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(dao.findById(deRating.getId()))
                .isPresent()
                .hasValue(deRating);
    }

    @Test
    public void getComponentRatingSummary() throws Exception {
        DERating rating1 = createDeRating(1, "component1", 100);
        dao.delete(rating1.getId());
        dao.save(rating1);

        DERating rating2 = createDeRating(2, "component2", 20);
        dao.delete(rating2.getId());
        dao.save(rating2);

        mockMvc.perform(
                get("/digitalExchange/ratings/{componentId}", rating2.getComponentId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.payload.componentId").value(rating2.getComponentId()))
                .andExpect(jsonPath("$.payload.rating").value(rating2.getRating()))
                .andReturn();
    }

    private DERating createDeRating(int id, String component1, int rating) {
        DERating deRating = new DERating();
        deRating.setId(id);
        deRating.setComponentId(component1);
        deRating.setReviewerId("reviewer1");
        deRating.setRating(rating);
        return deRating;
    }
}