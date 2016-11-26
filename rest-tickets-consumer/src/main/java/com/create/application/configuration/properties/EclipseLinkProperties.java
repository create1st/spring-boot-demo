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

package com.create.application.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.create.application.configuration.properties.EclipseLinkProperties.SPRING_JPA_PREFIX;

@ConfigurationProperties(SPRING_JPA_PREFIX)
public class EclipseLinkProperties {
    static final String SPRING_JPA_PREFIX = "spring.jpa";
    private static final String ECLIPSE_LINK_PREFIX = "eclipselink";

    private Map<String, String> eclipselink = new HashMap<>();

    public Map<String, String> getEclipselink() {
        return eclipselink;
    }

    public void setEclipselink(Map<String, String> eclipselink) {
        this.eclipselink = eclipselink;
    }

    public Map<String, Object> getVendorProperties() {
        return eclipselink
                .entrySet()
                .stream()
                .collect(Collectors.toMap(toPrefixedProperty(), Entry::getValue));
    }

    private Function<Entry<String, String>, String> toPrefixedProperty() {
        return entry -> String.format("%s.%s", ECLIPSE_LINK_PREFIX, entry.getKey());
    }
}