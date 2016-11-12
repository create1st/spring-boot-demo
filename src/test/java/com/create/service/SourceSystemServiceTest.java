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

package com.create.service;

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SourceSystemServiceTest {
    private static final String VALID_COUNTER_PARTY = "VALID_COUNTER_PARTY";
    private SourceSystemServiceImpl sourceSystemService = new SourceSystemServiceImpl(
            Collections.singletonList(VALID_COUNTER_PARTY));

    @Test
    public void shouldValidateValidCounterParty() {
        // given

        // when
        final boolean validCounterParty = sourceSystemService.isValidSourceSystem(VALID_COUNTER_PARTY);

        // then
        assertThat(validCounterParty, is(true));
    }

    @Test
    public void shouldNotValidateInvalidCounterParty() {
        // given

        // when
        final boolean validCounterParty = sourceSystemService.isValidSourceSystem("blah");

        // then
        assertThat(validCounterParty, is(false));
    }
}