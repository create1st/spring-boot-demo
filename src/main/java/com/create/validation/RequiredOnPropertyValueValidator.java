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

import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class RequiredOnPropertyValueValidator implements ConstraintValidator<RequiredOnPropertyValue, Object> {
    private RequiredOnPropertyValue requiredOnPropertyValue;
    private String[] values;

    @Override
    public void initialize(RequiredOnPropertyValue requiredOnPropertyValue) {
        this.requiredOnPropertyValue = requiredOnPropertyValue;
        this.values = requiredOnPropertyValue.values();
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {
        try {
            final Object propertyValue = PropertyUtils.getProperty(value, requiredOnPropertyValue.property());
            final Object requiredPropertyValue = PropertyUtils.getProperty(value, requiredOnPropertyValue
                    .requiredProperty());
            return propertyValue == null || isRequiredOnPropertyValue(propertyValue, requiredPropertyValue);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean isRequiredOnPropertyValue(Object propertyValue,
                                              Object requiredPropertyValue) {
        final boolean isRequiringPropertyToBeSet = isValueRequiringPropertyToBeSet(propertyValue);
        return !isRequiringPropertyToBeSet ||
                (isRequiringPropertyToBeSet && requiredPropertyValue != null);
    }

    private boolean isValueRequiringPropertyToBeSet(Object propertyValue) {
        final String stringPropertyValue = propertyValue.toString();
        return Arrays
                .stream(values)
                .anyMatch(value -> value.equals(stringPropertyValue));
    }
}
