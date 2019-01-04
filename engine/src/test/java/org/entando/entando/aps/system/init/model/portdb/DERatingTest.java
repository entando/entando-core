package org.entando.entando.aps.system.init.model.portdb;

import org.junit.*;

import javax.validation.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DERatingTest {

    private static final String COMPONENT_PATH = "componentId";
    private static final String REVIEWER_PATH = "reviewerId";
    private static final String RATING_PATH = "rating";

    private static final String NOT_EMPTY_MESSAGE = "{org.hibernate.validator.constraints.NotEmpty.message}";
    private static final String SIZE_MESSAGE = "{javax.validation.constraints.Size.message}";
    private static final String MIN_MESSAGE = "{javax.validation.constraints.Min.message}";
    private static final String MAX_MESSAGE = "{javax.validation.constraints.Max.message}";

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validSucceeds() {
        DERating deRating = createValidDeRating();

        Set<ConstraintViolation<DERating>> violations = validator.validate(deRating);

        assertThat(violations).isEmpty();
    }

    @Test
    public void validationWithAllNullFails() {
        DERating deRating = createValidDeRating();
        deRating.setComponentId(null);
        deRating.setReviewerId(null);

        Set<ConstraintViolation<DERating>> violations = validator.validate(deRating);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        propertyPathIs(violation, COMPONENT_PATH) &&
                        messageTemplateIs(violation, NOT_EMPTY_MESSAGE))
                .anyMatch(violation ->
                        propertyPathIs(violation, REVIEWER_PATH) &&
                        messageTemplateIs(violation, NOT_EMPTY_MESSAGE));
    }

    @Test
    public void validationWithLongStringsFails() {
        DERating deRating = createValidDeRating();
        deRating.setComponentId("123456789_123456789_123456789_1");
        deRating.setReviewerId("123456789_123456789_123456789_1");

        Set<ConstraintViolation<DERating>> violations = validator.validate(deRating);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        propertyPathIs(violation, COMPONENT_PATH) &&
                        messageTemplateIs(violation, SIZE_MESSAGE))
                .anyMatch(violation ->
                        propertyPathIs(violation, REVIEWER_PATH) &&
                        messageTemplateIs(violation, SIZE_MESSAGE));
    }

    @Test
    public void validationWithNegativeRatingFails() {
        DERating deRating = createValidDeRating();
        deRating.setRating(-1);

        Set<ConstraintViolation<DERating>> violations = validator.validate(deRating);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        propertyPathIs(violation, RATING_PATH) &&
                        messageTemplateIs(violation, MIN_MESSAGE));
    }
    
    @Test
    public void validationWithRatingOver100Fails() {
        DERating deRating = createValidDeRating();
        deRating.setRating(101);

        Set<ConstraintViolation<DERating>> violations = validator.validate(deRating);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        propertyPathIs(violation, RATING_PATH) &&
                        messageTemplateIs(violation, MAX_MESSAGE));
    }

    private DERating createValidDeRating() {
        DERating deRating = new DERating();
        deRating.setId(1);
        deRating.setComponentId("abc");
        deRating.setReviewerId("abc");
        deRating.setRating(40);
        return deRating;
    }

    private boolean messageTemplateIs(ConstraintViolation<DERating> violation, String messageTemplate) {
        return violation.getMessageTemplate().equals(messageTemplate);
    }

    private boolean propertyPathIs(ConstraintViolation<DERating> violation, String path) {
        return violation.getPropertyPath().toString().equals(path);
    }

}