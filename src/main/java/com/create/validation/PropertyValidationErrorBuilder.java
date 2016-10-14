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

public final class PropertyValidationErrorBuilder {
    private transient String nestedPath;
    private String message;

    private PropertyValidationErrorBuilder() {
    }

    public static PropertyValidationErrorBuilder aPropertyValidationError() {
        return new PropertyValidationErrorBuilder();
    }

    public PropertyValidationErrorBuilder withNestedPath(String nestedPath) {
        this.nestedPath = nestedPath;
        return this;
    }

    public PropertyValidationErrorBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public PropertyValidationError build() {
        PropertyValidationError propertyValidationError = new PropertyValidationError();
        propertyValidationError.setNestedPath(nestedPath);
        propertyValidationError.setMessage(message);
        return propertyValidationError;
    }
}
