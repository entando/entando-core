package org.entando.entando.aps.system.services.digitalexchange;

import com.google.common.collect.ImmutableList;
import org.entando.entando.aps.config.DbTestConfig;
import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { DbTestConfig.class })
public class DERatingsDAOImplIntegrationTest {

    @Autowired
    DERatingsDAOImpl dao;

    @Before
    public void setUp() throws Exception {
        dao.delete(basicDeRating().getId());
        dao.delete(basicDeRating().getId() +1);
        dao.delete(basicDeRating().getId() +2);
    }

    @Test
    public void getAllRatingsSummaries() {
        List<DERating> ratings = createDeRatingsList();

        DERatingsSummary summary1 = DERatingsSummary.builder()
                .componentId(ratings.get(0).getComponentId())
                .rating((ratings.get(0).getRating() + ratings.get(1).getRating())/2)
                .numberOfRatings(2)
                .numberOfInstalls(2)
                .build();

        DERatingsSummary summary2 = DERatingsSummary.builder()
                .componentId(ratings.get(2).getComponentId())
                .rating(ratings.get(2).getRating())
                .numberOfRatings(1)
                .numberOfInstalls(1)
                .build();

        PagedRestResponse<DERatingsSummary> expected = new PagedRestResponse<>();
        List<DERatingsSummary> summaries = ImmutableList.of(
                summary1, summary2
        );

        expected.setPayload(summaries);

        assertThat(dao.getAllRatingsSummaries()).isEqualToComparingFieldByField(expected);
    }

    private List<DERating> createDeRatingsList() {
        List<DERating> ratings = new ArrayList<>();

        DERating rating1 = basicDeRating();
        dao.save(rating1);
        ratings.add(rating1);

        DERating rating2 = basicDeRating();
        rating2.setId(rating1.getId() +1);
        rating2.setRating(20);
        dao.save(rating2);
        ratings.add(rating2);

        DERating rating3 = basicDeRating();
        rating3.setRating(80);
        rating3.setId(rating2.getId() +1);
        rating3.setComponentId("test_component2");
        dao.save(rating3);
        ratings.add(rating3);
        return ratings;
    }

    @Test
    public void getComponentRatingsSummary() {
        List<DERating> ratings = createDeRatingsList();

        DERatingsSummary expectedSummary = DERatingsSummary.builder()
                .componentId(ratings.get(0).getComponentId())
                .rating((ratings.get(0).getRating() + ratings.get(1).getRating())/2)
                .numberOfRatings(2)
                .numberOfInstalls(2)
                .build();

        assertThat(dao.getComponentRatingsSummary(expectedSummary.getComponentId()))
                .isPresent()
                .hasValue(expectedSummary);
    }

    @Test
    public void saveOK() {
        DERating rating = basicDeRating();
        dao.save(rating);

        assertThat(dao.findById(rating.getId()))
                .isPresent()
                .hasValue(rating);
    }

    @Test
    public void saveFail() {
        DERating rating = new DERating();

        assertThatThrownBy(() -> dao.save(rating))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error executing query");
    }

    @Test
    public void updateOk() {
        DERating rating = basicDeRating();
        dao.save(rating);

        rating.setRating(20);
        dao.update(rating);

        assertThat(dao.findById(rating.getId()))
                .isPresent()
                .hasValue(rating);
    }

    @Test
    public void updateFail() {
        DERating rating = basicDeRating();
        dao.save(rating);

        rating.setComponentId(null);
        assertThatThrownBy(() -> dao.update(rating))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error executing query");
    }

    @Test
    public void saveOrUpdateWorksWithSave() {
        DERating rating = basicDeRating();
        dao.saveOrUpdate(rating);

        assertThat(dao.findById(rating.getId()))
                .isPresent()
                .hasValue(rating);
    }

    @Test
    public void saveOrUpdateWorksWithUpdate() {
        DERating rating = basicDeRating();
        dao.saveOrUpdate(rating);
        rating.setRating(25);
        dao.saveOrUpdate(rating);

        assertThat(dao.findById(rating.getId()))
                .isPresent()
                .hasValue(rating);
    }

    private DERating basicDeRating() {
        DERating rating = new DERating();
        rating.setId(1);
        rating.setRating(100);
        rating.setReviewerId("test_reviewer");
        rating.setComponentId("test_component");
        return rating;
    }
}