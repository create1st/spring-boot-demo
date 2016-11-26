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


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.validation.ValidationUtils.invokeValidator;

public class ListValidator implements Validator {
    private final NestedPathConverter nestedPathConverter = new NestedPathConverter();
    private final Validator validator;

    public ListValidator(LocalValidatorFactoryBean validatorFactory) {
        this.validator = validatorFactory;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target,
                         Errors errors) {
        final List<?> targets = (List) target;
        IntStream
                .range(0, targets.size())
                .forEach(index -> validate(targets, index, errors));
    }

    private void validate(List<?> targets,
                          int index,
                          Errors errors) {
        final String nestedPath = nestedPathConverter.getNestedPath(index);
        errors.pushNestedPath(nestedPath);
        final Object target = targets.get(index);
        invokeValidator(validator, target, errors);
        errors.popNestedPath();
    }
}
