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

import java.util.Collections;
import java.util.List;

public final class ValidationErrorBuilder {
    private List<String> propertyValidationErrors = Collections.emptyList();
    private Object target;

    private ValidationErrorBuilder() {
    }

    public static ValidationErrorBuilder aValidationError() {
        return new ValidationErrorBuilder();
    }

    public ValidationErrorBuilder withPropertyValidationErrors(List<String> propertyValidationErrors) {
        this.propertyValidationErrors = propertyValidationErrors;
        return this;
    }

    public ValidationErrorBuilder withTarget(Object target) {
        this.target = target;
        return this;
    }

    public ValidationError build() {
        ValidationError validationError = new ValidationError();
        validationError.setPropertyValidationErrors(propertyValidationErrors);
        validationError.setTarget(target);
        return validationError;
    }
}
