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

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

public class NotWeekendValidatorTest {
    private Validator validator;

    @Before
    public void before() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateOnMonday() {
        // given
        final LocalDate date = LocalDate.of(2016, 1, 4);
        final WeekendTest weekendTest = new WeekendTest(date);

        // when
        final Set<ConstraintViolation<WeekendTest>> constraintViolations = validator.validate(weekendTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenIsNull() {
        // given
        final WeekendTest weekendTest = new WeekendTest(null);

        // when
        final Set<ConstraintViolation<WeekendTest>> constraintViolations = validator.validate(weekendTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldHaveValidationErrorOnSaturday() {
        // given
        final LocalDate date = LocalDate.of(2016, 1, 2);
        final WeekendTest weekendTest = new WeekendTest(date);

        // when
        final Set<ConstraintViolation<WeekendTest>> constraintViolations = validator.validate(weekendTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }

    @Test
    public void shouldHaveValidationErrorOnSunday() {
        // given
        final LocalDate date = LocalDate.of(2016, 1, 3);
        final WeekendTest weekendTest = new WeekendTest(date);

        // when
        final Set<ConstraintViolation<WeekendTest>> constraintViolations = validator.validate(weekendTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }

    public class WeekendTest {
        @NotWeekend
        private final LocalDate date;

        public WeekendTest(LocalDate date) {
            this.date = date;
        }

        public LocalDate getDate() {
            return date;
        }
    }

}