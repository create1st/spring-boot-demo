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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ValidationResult {

    private List<ValidationError> validationErrors = Collections.emptyList();

    public List<ValidationError> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return Objects.equals(validationErrors, that.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationErrors);
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "validationErrors=" + validationErrors +
                '}';
    }
}
