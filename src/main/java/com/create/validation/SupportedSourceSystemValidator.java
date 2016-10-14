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

import com.create.service.SourceSystemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SupportedSourceSystemValidator implements ConstraintValidator<SupportedSourceSystem, String> {
    @Autowired
    private SourceSystemService sourceSystemService;

    @Override
    public void initialize(SupportedSourceSystem constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {
        return sourceSystemService.isValidSourceSystem(value);
    }
}
