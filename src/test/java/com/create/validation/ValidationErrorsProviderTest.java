package com.create.validation;

import org.junit.Test;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationErrorsProviderTest {
    private static final String ERROR_MESSAGE = "Error message";
    private static final String NESTED_PATH = "target[1].";
    private static final String OBJECT_NAME = NESTED_PATH + "objectName";
    private static final String TARGET = "Target";
    private static final String TARGET_NO_ERRORS = "Target no errors";

    @Test
    public void shouldReturnValidationErrorsForTargetsWithErrorsOnly() {
        // given
        final List<?> targets = getTargets();
        final List<ObjectError> objectErrors = getObjectErrors();

        // when
        final ValidationErrorsProvider validationErrorsProvider = new ValidationErrorsProvider(targets, objectErrors);
        final List<ValidationError> validationErrors = validationErrorsProvider.getValidationErrors();

        // then
        final List<ValidationError> expectedValidationErrors = getExpectedValidationErrors();
        assertThat(validationErrors, is(expectedValidationErrors));
    }

    private List<?> getTargets() {
        return Arrays.asList(TARGET_NO_ERRORS, TARGET);
    }

    private List<ObjectError> getObjectErrors() {
        final ObjectError objectError = getObjectError();
        return Collections.singletonList(objectError);
    }

    private ObjectError getObjectError() {
        return new ObjectError(OBJECT_NAME, ERROR_MESSAGE);
    }

    private List<ValidationError> getExpectedValidationErrors() {
        final ValidationError expectedValidationError = getExpectedValidationError();
        return Collections.singletonList(expectedValidationError);
    }

    private ValidationError getExpectedValidationError() {
        final List<String> expectedPropertyValidationErrors = getExpectedPropertyValidationErrors();
        return ValidationErrorBuilder
                .aValidationError()
                .withTarget(TARGET)
                .withPropertyValidationErrors(expectedPropertyValidationErrors)
                .build();
    }

    private List<String> getExpectedPropertyValidationErrors() {
        return Collections.singletonList(ERROR_MESSAGE);
    }

}