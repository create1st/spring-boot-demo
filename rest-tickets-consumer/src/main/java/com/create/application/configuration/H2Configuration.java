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

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2Configuration {

    @Bean
    public ServletRegistrationBean h2servletRegistration(
            @Value("${spring.h2.console.path}") String h2ConsoleContextPath) {
        WebServlet webServlet = new WebServlet();
        ServletRegistrationBean bean = new ServletRegistrationBean(webServlet);
        String h2ConsoleMappings = getH2ConsoleMappings(h2ConsoleContextPath);
        bean.addUrlMappings(h2ConsoleMappings);
        return bean;
    }
    private String getH2ConsoleMappings(String h2ConsoleContextPath) {
        return String.format("%s/*", h2ConsoleContextPath);
    }
}
