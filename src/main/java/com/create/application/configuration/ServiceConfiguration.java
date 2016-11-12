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

import com.create.service.SourceSystemService;
import com.create.service.SourceSystemServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Configuration
public class ServiceConfiguration {
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Bean
    public SourceSystemService counterPartyProvider(
            @Value("classpath:source-systems.json")Resource sourceSystemsResource) throws IOException {
        final List<String> sourceSystems = getAsStrings(sourceSystemsResource);
        return new SourceSystemServiceImpl(sourceSystems);
    }

    private List<String> getAsStrings(Resource resource) throws IOException {
        try (final Reader reader = new InputStreamReader(resource.getInputStream())) {
            return jacksonObjectMapper.readValue(reader, List.class);
        }
    }
}
