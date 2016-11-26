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

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ValidationError<T> {
    private List<String> propertyValidationErrors = Collections.emptyList();
    @ApiModelProperty(example = "Input data type")
    private T target;

    public void setPropertyValidationErrors(List<String> propertyValidationErrors) {
        this.propertyValidationErrors = new ArrayList<>(propertyValidationErrors);
    }

    public List<String> getPropertyValidationErrors() {
        return Collections.unmodifiableList(propertyValidationErrors);
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return Objects.equals(propertyValidationErrors, that.propertyValidationErrors) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyValidationErrors, target);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "propertyValidationErrors=" + propertyValidationErrors +
                ", target=" + target +
                '}';
    }
}
