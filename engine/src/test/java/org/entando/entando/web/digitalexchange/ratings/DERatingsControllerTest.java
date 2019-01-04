package org.entando.entando.web.digitalexchange.ratings;

import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.aps.system.services.digitalexchange.ratings.DERatingsService;
import org.entando.entando.web.common.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DERatingsControllerTest {

    @Mock
    private DERatingsService service;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private DERatingsController controller;


    @Test
    public void getAllRatings() {
        // Setup
        ResponseEntity<PagedRestResponse<DERatingsSummary>> expected =
                createExpectedGetRatingsResponse();

        // Record
        when(service.getAllRatingsSummaries()).thenReturn(expected.getBody());

        // Play
        RestListRequest request = new RestListRequest();
        ResponseEntity<PagedRestResponse<DERatingsSummary>> ratingsResponse =
                controller.getAllRatings(request);

        // Assert
        assertThat(ratingsResponse).isEqualTo(expected);
    }

    private ResponseEntity<PagedRestResponse<DERatingsSummary>> createExpectedGetRatingsResponse() {
        DERatingsSummary expectedRatingsInfo = createExpectedRatingsInfo();

        List<DERatingsSummary> expectedRatings =
                Collections.singletonList(expectedRatingsInfo);

        PagedRestResponse<DERatingsSummary> expectedResponse = new PagedRestResponse<>();
        expectedResponse.setPayload(expectedRatings);

        return ResponseEntity.ok(expectedResponse);
    }

    private DERatingsSummary createExpectedRatingsInfo() {
        return DERatingsSummary.builder()
                    .componentId("ABC")
                    .numberOfInstalls(10)
                    .numberOfRatings(4)
                    .rating(75)
                    .build();
    }

    @Test
    public void getComponentRatingOK() {
        // Setup
        DERatingsSummary expectedRatingsInfo = createExpectedRatingsInfo();

        // Record
        when(service.getComponentRatingsSummary(expectedRatingsInfo.getComponentId()))
                .thenReturn(Optional.of(expectedRatingsInfo));

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsSummary>> componentRatingResponse =
                controller.getComponentRatingSummary(expectedRatingsInfo.getComponentId());

        // Assert
        assertThat(componentRatingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(componentRatingResponse.getBody()).isNotNull();
        assertThat(componentRatingResponse.getBody().getPayload()).isNotNull();
        assertThat(componentRatingResponse.getBody().getPayload()).isEqualTo(expectedRatingsInfo);
    }

    @Test
    public void getComponentRatingNotFound() {
        // Setup
        String notFoundComponentId = "NOT_FOUND_COMPONENT";

        // Record
        when(service.getComponentRatingsSummary(notFoundComponentId))
                .thenReturn(Optional.empty());

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsSummary>> componentRatingResponse =
                controller.getComponentRatingSummary(notFoundComponentId);

        // Assert
        assertThat(componentRatingResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addRatingOK() {
        // Setup
        DERating ratingUpdate = createRatingUpdate();

        DERatingsSummary expected = DERatingsSummary.builder()
                .rating(ratingUpdate.getRating())
                .componentId(ratingUpdate.getComponentId())
                .build();

        // Record
        when(service.addRating(ratingUpdate)).thenReturn(Optional.of(expected));

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsSummary>> updateResponse =
                controller.addRating(ratingUpdate, bindingResult);

        // Assert
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getPayload()).isEqualTo(expected);
    }

    private DERating createRatingUpdate() {
        DERating ratingUpdate = new DERating();
        ratingUpdate.setComponentId("ABC");
        ratingUpdate.setReviewerId("mario");
        ratingUpdate.setRating(50);
        return ratingUpdate;
    }

    @Test
    public void addRatingNotFound() {
        // Setup
        DERating ratingUpdate = new DERating();
        ratingUpdate.setComponentId("DOES NOT EXIST");

        // Record
        when(service.addRating(ratingUpdate)).thenReturn(Optional.empty());

        // Play
        ResponseEntity<SimpleRestResponse<DERatingsSummary>> updateResponse =
                controller.addRating(ratingUpdate, bindingResult);

        // Assert
        assertThat(updateResponse).isNotNull();
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}