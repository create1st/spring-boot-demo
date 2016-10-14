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
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

public class MultiDateConstraintValidatorTest {
    private Validator validator;

    @Before
    public void before() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateWhenDateIsSameAsBeforeDate() {
        // given
        final LocalDate date = LocalDate.now();
        final DateValidatorTest dateValidatorTest = new DateValidatorTest(date, date);

        // when
        final Set<ConstraintViolation<DateValidatorTest>> constraintViolations = validator.validate(dateValidatorTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenDateIsAfterBeforeDate() {
        // given
        final LocalDate date = LocalDate.of(2016,8,15);
        final LocalDate beforeDate = LocalDate.of(2016,8,11);
        final DateValidatorTest dateValidatorTest = new DateValidatorTest(date, beforeDate);

        // when
        final Set<ConstraintViolation<DateValidatorTest>> constraintViolations = validator.validate(dateValidatorTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenDateIsNull() {
        // given
        final LocalDate beforeDate = LocalDate.now();
        final DateValidatorTest dateValidatorTest = new DateValidatorTest(null, beforeDate);

        // when
        final Set<ConstraintViolation<DateValidatorTest>> constraintViolations = validator.validate(dateValidatorTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldHaveValidationErrorWhenDateIsBeforeBeforeDate() {
        // given
        final LocalDate beforeDate = LocalDate.now();
        final LocalDate date = beforeDate.minus(1, ChronoUnit.DAYS);
        final DateValidatorTest dateValidatorTest = new DateValidatorTest(date, beforeDate);

        // when
        final Set<ConstraintViolation<DateValidatorTest>> constraintViolations = validator.validate(dateValidatorTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }

    @MultiDateConstraint(baseDateProperty = "date", termDateProperty = "beforeDate",
            constraintChecker = NotBeforeDate.class)
    public class DateValidatorTest {
        private final LocalDate date;
        private final LocalDate beforeDate;

        private DateValidatorTest(LocalDate date,
                                  LocalDate beforeDate) {
            this.date = date;
            this.beforeDate = beforeDate;
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalDate getBeforeDate() {
            return beforeDate;
        }
    }

}