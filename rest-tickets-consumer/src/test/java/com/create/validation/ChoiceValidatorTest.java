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
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

public class ChoiceValidatorTest {
    private Validator validator;

    @Before
    public void before() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateWhenTicketTypeIsINTERNAL() {
        // given
        final ChoiceTest ChoiceTest = new ChoiceTest(TicketType.INTERNAL.name());

        // when
        final Set<ConstraintViolation<ChoiceTest>> constraintViolations = validator.validate(ChoiceTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenTicketTypeIsNull() {
        // given
        final ChoiceTest ChoiceTest = new ChoiceTest(null);

        // when
        final Set<ConstraintViolation<ChoiceTest>> constraintViolations = validator.validate(ChoiceTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldHaveValidationErrorWhenTicketTypeIsUnknown() {
        // given
        final ChoiceTest ChoiceTest = new ChoiceTest("Unknown");

        // when
        final Set<ConstraintViolation<ChoiceTest>> constraintViolations = validator.validate(ChoiceTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }

    public class ChoiceTest {
        @Choice(valueSource = TicketType.class)
        private final String ticketType;

        public ChoiceTest(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getticketType() {
            return ticketType;
        }
    }

}