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
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

public class CurrencyValidatorTest {
    private Validator validator;

    @Before
    public void before() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldValidateUSDCodeCurrency() {
        // given
        final CurrencyTest currencyTest = new CurrencyTest("USD");

        // when
        final Set<ConstraintViolation<CurrencyTest>> constraintViolations = validator.validate(currencyTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldValidateWhenIsNull() {
        // given
        final CurrencyTest currencyTest = new CurrencyTest(null);

        // when
        final Set<ConstraintViolation<CurrencyTest>> constraintViolations = validator.validate(currencyTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    @Test
    public void shouldHaveValidationErrorForDSUCodeCurrency() {
        // given
        final CurrencyTest currencyTest = new CurrencyTest("DSU");

        // when
        final Set<ConstraintViolation<CurrencyTest>> constraintViolations = validator.validate(currencyTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }

    public class CurrencyTest {
        @Currency
        private final String currency;

        public CurrencyTest(String currency) {
            this.currency = currency;
        }

        public String getCurrency() {
            return currency;
        }
    }

}