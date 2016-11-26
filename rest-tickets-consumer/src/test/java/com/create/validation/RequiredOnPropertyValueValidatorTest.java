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

import com.create.model.TicketType;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class RequiredOnPropertyValueValidatorTest {
    private Validator validator;

    @Before
    public void before() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateWhenTicketTypeIsEXTERNALAndexpectedDateIsSet() {
        // given
        final LocalDate expectedDate = LocalDate.now();
        final PropertyValueTest propertyValueTest = new PropertyValueTest(TicketType.EXTERNAL, expectedDate);

        // when
        final Set<ConstraintViolation<PropertyValueTest>> constraintViolations = validator.validate(propertyValueTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenTicketTypeIsVanillaOptionAndExpectedDateIsNotSet() {
        // given
        final LocalDate expectedDate = LocalDate.now();
        final PropertyValueTest propertyValueTest = new PropertyValueTest(TicketType.INTERNAL, null);

        // when
        final Set<ConstraintViolation<PropertyValueTest>> constraintViolations = validator.validate(propertyValueTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenTicketTypeIsNotSetAndExpectedDateIsNotSet() {
        // given
        final LocalDate expectedDate = LocalDate.now();
        final PropertyValueTest propertyValueTest = new PropertyValueTest(null, null);

        // when
        final Set<ConstraintViolation<PropertyValueTest>> constraintViolations = validator.validate(propertyValueTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldHaveValidationErrorWhenTicketTypeIsEXTERNALAndExpectedDateIsNotSet() {
        // given
        final PropertyValueTest propertyValueTest = new PropertyValueTest(TicketType.EXTERNAL, null);

        // when
        final Set<ConstraintViolation<PropertyValueTest>> constraintViolations = validator.validate(propertyValueTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }

    @RequiredOnPropertyValue(property="ticketType", values={"EXTERNAL"}, requiredProperty="expectedDate")
    public class PropertyValueTest {
        private final TicketType ticketType;
        private final LocalDate expectedDate;

        public PropertyValueTest(TicketType TicketType, LocalDate expectedDate) {
            this.ticketType = TicketType;
            this.expectedDate = expectedDate;
        }

        public TicketType getTicketType() {
            return ticketType;
        }

        public LocalDate getexpectedDate() {
            return expectedDate;
        }
    }
}