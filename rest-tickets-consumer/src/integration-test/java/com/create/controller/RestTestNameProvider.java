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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class RestTestNameProvider {
    private static final String JSON_EXTENSION = ".json";

    String[] getTestNames(String urlTemplate) throws IOException {
        final String resourcePath = String.format("rest%s/request", urlTemplate);
        return getResourceFiles(resourcePath)
                .toArray(new String[0]);
    }

    private List<String> getResourceFiles(String path) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        try (final InputStream inputStream = classLoader.getResourceAsStream(path);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader
                    .lines()
                    .filter(isJsonFile())
                    .map(getTestName())
                    .collect(Collectors.toList());
        }
    }

    private Predicate<String> isJsonFile() {
        return name -> name.endsWith(JSON_EXTENSION);
    }

    private Function<String, String> getTestName() {
        return name -> name.substring(0, name.length() - JSON_EXTENSION.length());
    }
}