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

import com.create.validation.ValidationResult;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ValidationResultRestTestExecutor {
    private static final String JSON_EXTENSION = "json";

    private final TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ValidationResultRestTestExecutor(TestRestTemplate restTemplate,
                                            ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> void executeListTestRestRequest(String urlTemplate,
                                               String testName,
                                               Class<T> type) throws Exception {
        executeTestRestRequest(urlTemplate, testName, List.class, type);
    }

    public <T> void executeTestRestRequest(String urlTemplate,
                                           String testName,
                                           Class<T> type) throws Exception {
        executeTestRestRequest(urlTemplate, testName, type, type);
    }

    private <R, T> void executeTestRestRequest(String urlTemplate,
                                               String testName,
                                               Class<R> requestObjectType,
                                               Class<T> validatedObjectType) throws Exception {
        final R requestObject = getRequestObject(urlTemplate, testName, requestObjectType);
        final HttpEntity<R> entity = new HttpEntity<>(requestObject);
        final ParameterizedTypeReference<ValidationResult<T>> genericResponseType = getGenericResponseType(
                validatedObjectType);

        final ResponseEntity<ValidationResult<T>> responseEntity = restTemplate.exchange(urlTemplate, HttpMethod.POST,
                entity, genericResponseType);

        final ValidationResult<T> responseObject = getResponseObject(urlTemplate, testName, validatedObjectType);
        assertThat(responseEntity.getBody(), is(responseObject));
        final HttpStatus statusCode = getExpectedStatusCode(responseObject);
        assertThat(responseEntity.getStatusCode(), is(statusCode));
    }

    private <T> ParameterizedTypeReference<ValidationResult<T>> getGenericResponseType(Class<T> type) {
        return new ParameterizedTypeReference<ValidationResult<T>>() {
            @Override
            public Type getType() {
                return getValidationResultGenericType(type);
            }
        };
    }

    private String getRequestFileName(String urlTemplate, String testName) {
        return String.format("rest%s/request/%s.%s", urlTemplate, testName, JSON_EXTENSION);
    }

    private String getResponseFileName(String urlTemplate, String testName) {
        return String.format("rest%s/response/%s.response.%s", urlTemplate, testName,
                JSON_EXTENSION);
    }

    private <T> T getRequestObject(String urlTemplate,
                                   String testName,
                                   Class<T> type) throws IOException {
        final String requestFileName = getRequestFileName(urlTemplate, testName);
        return getRequestObject(requestFileName, type);
    }

    private <T> T getRequestObject(String fileName,
                                   Class<T> type) throws IOException {
        final String jsonContent = getJsonContent(fileName);
        return jsonContent.isEmpty()
                ? null
                : objectMapper.readValue(jsonContent, type);
    }

    private String getJsonContent(String fileName) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        try (final InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            return IOUtils.toString(inputStream);
        }
    }

    private <T> ValidationResult<T> getResponseObject(String urlTemplate,
                                                      String testName,
                                                      Class<T> type) throws IOException {
        final String responseFileName = getResponseFileName(urlTemplate, testName);
        return getResponseObject(responseFileName, type);
    }

    private <T> ValidationResult<T> getResponseObject(String fileName,
                                                      Class<T> type) throws IOException {
        final String jsonContent = getJsonContent(fileName);
        return jsonContent.isEmpty()
                ? null
                : objectMapper.readValue(jsonContent, getValidationResultGenericType(type));
    }

    private <T> JavaType getValidationResultGenericType(Class<T> type) {
        return objectMapper.getTypeFactory().constructParametricType(ValidationResult.class, type);
    }

    private <T> HttpStatus getExpectedStatusCode(T jsonResponse) {
        return jsonResponse == null
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
    }
}