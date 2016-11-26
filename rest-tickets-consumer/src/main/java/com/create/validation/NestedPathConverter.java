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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NestedPathConverter {
    private static final String NESTED_PATH_REGEXP = "target\\[.*?\\]\\.";
    private static final int FIRST_GROUP_IN_MATCH = 0;
    private static final int NON_LIST_ELEMENT = 0;

    String getNestedPath(int index) {
        return String.format("target[%d].", index);
    }

    String getNestedPath(String targetElement) {
        final Matcher matcher = getNestedPathPatternMatcher(targetElement);
        return matcher.find()
                ? matcher.group(FIRST_GROUP_IN_MATCH)
                : getNestedPath(NON_LIST_ELEMENT);
    }

    private Matcher getNestedPathPatternMatcher(String targetElement) {
        final Pattern pattern = Pattern.compile(NESTED_PATH_REGEXP);
        return pattern.matcher(targetElement);
    }

    String getTargetName(String targetElement) {
        final Matcher matcher = getNestedPathPatternMatcher(targetElement);
        return matcher.find()
                ? targetElement.substring(matcher.end())
                : targetElement;
    }
}
