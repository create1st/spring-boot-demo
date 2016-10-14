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

import com.create.application.configuration.properties.ApiInfoProperties;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.ant;


@Configuration
@EnableSwagger2
@EnableConfigurationProperties(ApiInfoProperties.class)
public class SwaggerConfiguration {

    @Value("${management.context-path:}")
    private String actuatorContextPath;
    @Value("${validator.context-path:}")
    private String validatorContextPath;

    @Autowired
    private ApiInfoProperties apiInfoProperties;

    @Bean
    public Docket validatorApi(final ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .groupName("My services")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(validatorContextPath())
                .build();
    }

    private Predicate<String> validatorContextPath() {
        return ant(getAntPattern(validatorContextPath));
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(apiInfoProperties.getTitle())
                .description(apiInfoProperties.getDescription())
                .termsOfServiceUrl(apiInfoProperties.getTermsOfServiceUrl())
                .contact(apiInfoProperties.getContact())
                .license(apiInfoProperties.getLicense())
                .licenseUrl(apiInfoProperties.getLicenseUrl())
                .version(apiInfoProperties.getVersion())
                .build();
    }

    @Bean
    public Docket actuatorApi(final ApiInfo actuatorApiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(actuatorApiInfo)
                .groupName("Actuator")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(actuatorPaths())
                .build();
    }

    @Bean
    public ApiInfo actuatorApiInfo() {
        return new ApiInfoBuilder()
                .title("Actuator API")
                .build();
    }

    private Predicate<String> actuatorPaths() {
        return ant(getAntPattern(actuatorContextPath));
    }

    private String getAntPattern(String path) {
        return String.format("%s/**", path);
    }
}
