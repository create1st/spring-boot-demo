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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NestedPathConverterTest {
    private NestedPathConverter nestedPathConverter = new NestedPathConverter();

    @Test
    public void shouldGetNestedPathForIndex() throws Exception {
        // given

        // when
        final String nestedPath = nestedPathConverter.getNestedPath(10);

        // then
        assertThat(nestedPath, is("target[10]."));
    }

    @Test
    public void shouldGetNestedPathForFieldTargetName() throws Exception {
        // given

        // when
        final String nestedPath = nestedPathConverter.getNestedPath("target[10].id");

        // then
        assertThat(nestedPath, is("target[10]."));
    }

    @Test
    public void shouldGetNestedPathForObjectTargetName() throws Exception {
        // given

        // when
        final String nestedPath = nestedPathConverter.getNestedPath("target[10].");

        // then
        assertThat(nestedPath, is("target[10]."));
    }

    @Test
    public void shouldGetNestedPathForNonListElementTargetName() throws Exception {
        // given

        // when
        final String nestedPath = nestedPathConverter.getNestedPath("id");

        // then
        assertThat(nestedPath, is("target[0]."));
    }

    @Test
    public void shouldGetTargetFromNestedPath() throws Exception {
        // given

        // when
        final String targetName = nestedPathConverter.getTargetName("target[10].id");

        // then
        assertThat(targetName, is("id"));
    }
}