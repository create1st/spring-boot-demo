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

import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValidationErrorsProvider {
    private final NestedPathConverter nestedPathConverter = new NestedPathConverter();
    private final List<?> targets;
    private final PropertyValidationErrorsProvider propertyValidationErrorsProvider;

    public ValidationErrorsProvider(List<?> targets,
                                    List<ObjectError> objectErrors) {
        this.targets = Collections.unmodifiableList(targets);
        this.propertyValidationErrorsProvider = new PropertyValidationErrorsProvider(objectErrors);
    }

    public List<ValidationError<?>> getValidationErrors() {
        return IntStream.range(0, targets.size())
                .boxed()
                .map(this::getValidationError)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private ValidationError<?> getValidationError(int index) {
        final Object target = targets.get(index);
        final String nestedPath = nestedPathConverter.getNestedPath(index);
        final List<String> propertyValidationErrors = propertyValidationErrorsProvider
                .getPropertyValidationErrorsByNestedPath(nestedPath);
        return propertyValidationErrors.isEmpty()
                ? null
                : getValidationError(target, propertyValidationErrors);
    }

    private ValidationError<?> getValidationError(Object target,
                                                  List<String> propertyValidationErrors) {
        return ValidationErrorBuilder
                .aValidationError()
                .withTarget(target)
                .withPropertyValidationErrors(propertyValidationErrors)
                .build();
    }
}
