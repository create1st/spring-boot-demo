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


import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

public class MultiDateConstraintValidator implements ConstraintValidator<MultiDateConstraint, Object> {
    private MultiDateConstraint multiDateConstraint;
    private MultiDateConstraintChecker constraintChecker;

    @Override
    public void initialize(MultiDateConstraint multiDateConstraint) {
        final Class<? extends MultiDateConstraintChecker> constraintCheckerClass = multiDateConstraint
                .constraintChecker();
        try {
            constraintChecker = ConstructorUtils.invokeConstructor(constraintCheckerClass, null);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                InstantiationException e) {
            throw new IllegalArgumentException(e);
        }
        this.multiDateConstraint = multiDateConstraint;
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {
        try {
            final LocalDate baseDate = (LocalDate) PropertyUtils.getProperty(value,
                    multiDateConstraint.baseDateProperty());
            final LocalDate termDate = (LocalDate) PropertyUtils.getProperty(value,
                    multiDateConstraint.termDateProperty());
            return baseDate == null || termDate == null || constraintChecker.checkConstraint(baseDate, termDate);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
