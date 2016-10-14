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

import com.create.application.configuration.ValidatorConfiguration;
import com.create.service.SourceSystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ValidatorConfiguration.class)
public class SupportedSourceSystemValidatorTest {
    private static final String VALID_SOURCE_SYSTEM = "VALID_SOURCE_SYSTEM";

    @MockBean
    private SourceSystemService sourceSystemService;

    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Test
    public void shouldValidateWhenValidCounterParty() {
        // given
        setupCounterParties();
        final SupportedCounterPartyTest supportedCounterPartyTest = new SupportedCounterPartyTest(VALID_SOURCE_SYSTEM);

        // when
        final Set<ConstraintViolation<SupportedCounterPartyTest>> constraintViolations = localValidatorFactoryBean
                .validate(supportedCounterPartyTest);

        // then
        assertThat(constraintViolations, is(empty()));
    }

    private void setupCounterParties() {
        when(sourceSystemService.isValidSourceSystem(VALID_SOURCE_SYSTEM)).thenReturn(true);
    }

    @Test
    public void shouldHaveValidationErrorWhenNotSupportedCounterParty() {
        // given
        setupCounterParties();
        final SupportedCounterPartyTest supportedCounterPartyTest = new SupportedCounterPartyTest("blah");

        // when
        final Set<ConstraintViolation<SupportedCounterPartyTest>> constraintViolations = localValidatorFactoryBean
                .validate(supportedCounterPartyTest);

        // then
        assertThat(constraintViolations, hasSize(1));
    }


    public class SupportedCounterPartyTest {
        @SupportedSourceSystem
        private final String counterParty;

        public SupportedCounterPartyTest(String counterParty) {
            this.counterParty = counterParty;
        }

        public String getCounterParty() {
            return counterParty;
        }
    }
}