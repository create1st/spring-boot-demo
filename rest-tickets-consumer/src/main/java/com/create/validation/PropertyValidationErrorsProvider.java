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

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PropertyValidationErrorsProvider {
    private static final String TARGET_PARAMETER = "\\{target\\}";
    private final NestedPathConverter nestedPathConverter = new NestedPathConverter();
    private final Map<String, List<String>> propertyValidationErrorsGroupedByNestedPath;

    PropertyValidationErrorsProvider(List<ObjectError> objectErrors) {
        propertyValidationErrorsGroupedByNestedPath = getPropertyValidationErrorsGroupedByNestedPath(objectErrors);
    }

    List<String> getPropertyValidationErrorsByNestedPath(String nestedPath) {
        final List<String> propertyValidationErrors = propertyValidationErrorsGroupedByNestedPath
                .getOrDefault(nestedPath, Collections.emptyList());
        return Collections.unmodifiableList(propertyValidationErrors);
    }

    private Map<String, List<String>> getPropertyValidationErrorsGroupedByNestedPath(
            List<ObjectError> objectErrors) {
        return objectErrors
                .stream()
                .map(this::toPropertyValidationError)
                .collect(
                        Collectors.groupingBy(
                                PropertyValidationError::getNestedPath,
                                Collectors.mapping(PropertyValidationError::getMessage, Collectors.toList())));
    }

    private PropertyValidationError toPropertyValidationError(ObjectError objectError) {
        final String targetElement = getTargetElement(objectError);
        final String message = getMessage(objectError.getDefaultMessage(), targetElement);
        final String nestedPath = nestedPathConverter.getNestedPath(targetElement);
        return PropertyValidationErrorBuilder
                .aPropertyValidationError()
                .withMessage(message)
                .withNestedPath(nestedPath)
                .build();
    }

    private String getMessage(String message,
                              String targetElement) {
        final String target = nestedPathConverter.getTargetName(targetElement);
        return message.replaceAll(TARGET_PARAMETER, target);
    }

    private String getTargetElement(ObjectError objectError) {
        return objectError instanceof FieldError
                ? ((FieldError) objectError).getField()
                : objectError.getObjectName();
    }
}
