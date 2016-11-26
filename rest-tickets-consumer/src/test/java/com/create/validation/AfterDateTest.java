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

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AfterDateTest {
    private AfterDate afterDate = new AfterDate();

    @Test
    public void shouldReturnTrueWhenBaseDateIsAfterTermDate(){
        // given
        final LocalDate baseDate = LocalDate.of(2016,8,15);
        final LocalDate termDate = LocalDate.of(2016,8,11);

        // when
        final boolean checkConstraint = afterDate.checkConstraint(baseDate, termDate);

        // then
        assertThat(checkConstraint, is(true));
    }

    @Test
    public void shouldReturnFalseWhenBaseDateIsSameAsTermDate(){
        // given
        final LocalDate baseDate = LocalDate.of(2016,8,15);
        final LocalDate termDate = baseDate;

        // when
        final boolean checkConstraint = afterDate.checkConstraint(baseDate, termDate);

        // then
        assertThat(checkConstraint, is(false));
    }

    @Test
    public void shouldReturnTrueWhenBaseDateIsBeforeAsTermDate(){
        // given
        final LocalDate baseDate = LocalDate.of(2016,8,11);
        final LocalDate termDate = LocalDate.of(2016,8,15);

        // when
        final boolean checkConstraint = afterDate.checkConstraint(baseDate, termDate);

        // then
        assertThat(checkConstraint, is(false));
    }
}