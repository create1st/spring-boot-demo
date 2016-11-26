/*
 * Copyright  2016 Sebastian Gil.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.create.validation;

import org.junit.Test;
import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

public class PropertyValidationErrorsProviderTest {
    private static final String ERROR_MESSAGE = "Error message";
    private static final String NESTED_PATH = "target[0].";
    private static final String OBJECT_NAME = NESTED_PATH + "objectName";

    @Test
    public void shouldReturnValidationErrorMessagesByNestedPath() {
        // given
        final List<ObjectError> objectErrors = getObjectErrors();

        // when
        final PropertyValidationErrorsProvider propertyValidationErrorsProvider = new PropertyValidationErrorsProvider(
                objectErrors);
        final List<String> propertyValidationErrorsByNestedPath = propertyValidationErrorsProvider
                .getPropertyValidationErrorsByNestedPath(NESTED_PATH);

        // then
        final List<String> expectedPropertyValidationErrors = getExpectedPropertyValidationErrors();
        assertThat(propertyValidationErrorsByNestedPath, is(expectedPropertyValidationErrors));
    }

    @Test
    public void shouldReturnEmptyMessageListWhenErrorsAreNotReportedForNestedPath() {
        // given
        final List<ObjectError> objectErrors = getObjectErrors();

        // when
        final PropertyValidationErrorsProvider propertyValidationErrorsProvider = new PropertyValidationErrorsProvider(
                objectErrors);
        final List<String> propertyValidationErrorsByNestedPath = propertyValidationErrorsProvider
                .getPropertyValidationErrorsByNestedPath("target[1].");

        // then
        assertThat(propertyValidationErrorsByNestedPath, is(empty()));
    }

    private List<ObjectError> getObjectErrors() {
        final ObjectError objectError = getObjectError();
        return Collections.singletonList(objectError);
    }

    private ObjectError getObjectError() {
        return new ObjectError(OBJECT_NAME, ERROR_MESSAGE);
    }

    private List<String> getExpectedPropertyValidationErrors() {
        return Collections.singletonList(ERROR_MESSAGE);
    }
}