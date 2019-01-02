package org.entando.entando.aps.system.services.digitalexchange.ratings;

import org.entando.entando.aps.system.services.digitalexchange.DERatingsDAO;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.ratings.DERating;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DERatingsServiceImplTest {

    @Mock
    DERatingsDAO dao;

    @InjectMocks
    DERatingsServiceImpl service;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getAllRatings() {
        PagedRestResponse<DERatingsSummary> expected = new PagedRestResponse<>();

        when(dao.getAllRatingsSummaries()).thenReturn(expected);

        assertThat(service.getAllRatingsSummaries()).isEqualTo(expected);
    }

    @Test
    public void componentRatingsNotFound() {
        assertThat(service.getComponentRatingsSummary("DOES_NOT_EXIST_ID"))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void componentRatingsFound() {
        String validId = "VALID_ID";

        int perfect = 100;
        DERatingsSummary ratings = new DERatingsSummary();
        ratings.setComponentId(validId);
        ratings.setRating(perfect);

        when(dao.getComponentRatingsSummary(validId))
                .thenReturn(Optional.of(ratings));

        assertThat(service.getComponentRatingsSummary(validId))
                .isPresent()
                .hasValue(ratings);
    }

    @Test
    public void addRating() {
        int rating = 100;
        String componentId = "TestComponentID";

        DERating deRating = new DERating();
        deRating.setComponentId(componentId);
        deRating.setRating(rating);

        DERatingsSummary ratingsSummary = new DERatingsSummary();
        ratingsSummary.setRating(rating);
        ratingsSummary.setComponentId(componentId);

        when(dao.getComponentRatingsSummary(componentId))
                .thenReturn(Optional.of(ratingsSummary));

        assertThat(service.addRating(deRating))
                .isPresent()
                .hasValue(ratingsSummary);

        verify(dao, times(1))
                .saveOrUpdate(deRating);
        verify(dao, times(1))
                .getComponentRatingsSummary(componentId);
    }
}