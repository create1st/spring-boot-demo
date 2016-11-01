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

package com.create.controller;

import com.create.validation.ValidationError;
import com.create.validation.ValidationErrorsProvider;
import com.create.validation.ValidationResult;
import com.create.validation.ValidationResultBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class BeanValidatorErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationResult<?> processValidationErrors(MethodArgumentNotValidException exception) {
        final BindingResult result = exception.getBindingResult();
        final List<ObjectError> fieldErrors = result.getAllErrors();
        final List<?> targets = getTargets(result);
        return processValidationErrors(targets, fieldErrors);
    }

    private List<?> getTargets(BindingResult result) {
        final Object target = result.getTarget();
        return target instanceof List
                ? (List<?>) target
                : Collections.singletonList(target);
    }

    @SuppressWarnings("unchecked")
    private ValidationResult<?> processValidationErrors(List<?> targets,
                                                        List<ObjectError> objectErrors) {
        final ValidationErrorsProvider validationErrorsProvider = new ValidationErrorsProvider(targets,
                objectErrors);
        final List<ValidationError<?>> validationErrors = validationErrorsProvider.getValidationErrors();
        return ValidationResultBuilder
                .aValidationResult()
                .withValidationErrors(validationErrors)
                .build();
    }
}
