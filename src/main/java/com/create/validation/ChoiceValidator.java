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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ChoiceValidator implements ConstraintValidator<Choice, String> {
    private Enum[] enumValues;

    @Override
    public void initialize(Choice choice) {
        final Class<? extends Enum<?>> valueSource = choice.valueSource();
        enumValues = valueSource.getEnumConstants();
    }

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {
        return value == null || isValidEnumName(value);
    }

    private boolean isValidEnumName(String value) {
        return Arrays
                .stream(enumValues)
                .anyMatch(anEnum -> anEnum.name().equals(value));
    }
}
