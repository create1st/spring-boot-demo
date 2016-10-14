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

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.io.InputStream;

public class RestTestExecutor {
    private static final String JSON_EXTENSION = ".json";

    private final MockMvc mvc;

    public RestTestExecutor(MockMvc mvc) {
        this.mvc = mvc;
    }

    void executeTestRestRequest(String urlTemplate,
                                String testName) throws Exception {
        final String requestFileName = String.format("rest%s/request/%s%s", urlTemplate, testName, JSON_EXTENSION);
        final String jsonRequest = getJsonContent(requestFileName);
        final String responseFileName = String.format("rest%s/response/%s.response%s", urlTemplate, testName,
                JSON_EXTENSION);
        final String jsonResponse = getJsonContent(responseFileName);
        final ResultMatcher statusCode = getExpectedStatusCode(jsonResponse);
        final ResultMatcher responseContent = getExpectedResponseContent(jsonResponse);
        mvc
                .perform(
                        MockMvcRequestBuilders.post(urlTemplate)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(statusCode)
                .andExpect(responseContent);
    }

    ResultMatcher getExpectedResponseContent(String jsonResponse) {
        return jsonResponse.isEmpty()
                ? MockMvcResultMatchers.content().string("")
                : MockMvcResultMatchers.content().json(jsonResponse);
    }

    ResultMatcher getExpectedStatusCode(String jsonResponse) {
        return jsonResponse.isEmpty()
                ? MockMvcResultMatchers.status().isOk()
                : MockMvcResultMatchers.status().isBadRequest();
    }

    String getJsonContent(String fileName) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        try (final InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            return IOUtils.toString(inputStream);
        }
    }
}