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

package com.create.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class EndpointsConfiguration {

    @Bean
    public List<String> actuatorEndpoints(@Value("${management.context-path}") String actuatorEndpoints) {
        return Collections.singletonList(getWildcardMappings(actuatorEndpoints));
    }

    private String getWildcardMappings(String contexPath) {
        return String.format("%s/**", contexPath);
    }

    @Bean
    public List<String> h2Endpoints(@Value("${spring.h2.console.path}") String h2Endpoint) {
        return Collections.singletonList(getWildcardMappings(h2Endpoint));
    }

    @Bean
    public List<String> swaggerEndpoints() {
        return Arrays.asList(
                getWildcardMappings("/v2/api-docs"),
                getWildcardMappings("/swagger"),
                getWildcardMappings("/swagger-resources"),
                getWildcardMappings("/configuration"),
                "/swagger-ui.html"
        );
    }

    @Bean
    public List<String> allowedEndpoints(List<String> actuatorEndpoints,
                                         List<String> h2Endpoints,
                                         List<String> swaggerEndpoints) {
        return Stream.of(
                actuatorEndpoints,
                h2Endpoints,
                swaggerEndpoints
        )
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}