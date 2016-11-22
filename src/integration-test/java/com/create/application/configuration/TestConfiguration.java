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

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import java.net.URI;

import static com.create.application.configuration.Bootstrap.TICKET_SERVICE_USER;
import static com.create.application.configuration.Bootstrap.USER_PASSWORD;
import static org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption.SSL;

@Configuration
@Import({
        AopConfiguration.class,
        JpaConfiguration.class,
        OAuth2Configuration.class,
        Bootstrap.class
})
@EnableAutoConfiguration
public class TestConfiguration {
    private static final HttpClientOption[] DEFAULT_OPTIONS = {};
    private static final HttpClientOption[] SSL_OPTIONS = {SSL};

    @Bean
    public LocalHostUriTemplateHandler localHostUriTemplateHandler(
            Environment environment,
            AbstractConfigurableEmbeddedServletContainer container) {
        return new LocalHostUriTemplateHandler(
                environment, getSchema(container));
    }

    private String getSchema(AbstractConfigurableEmbeddedServletContainer container) {
        return isSslEnabled(container)
                ? "https"
                : "http";
    }

    private boolean isSslEnabled(AbstractConfigurableEmbeddedServletContainer container) {
        try {
            return container.getSsl() != null && container.getSsl()
                    .isEnabled();
        } catch (NoSuchBeanDefinitionException ex) {
            return false;
        }
    }

    @Bean(name = "authenticatedUserResourceDetails")
    @Lazy
    public OAuth2ProtectedResourceDetails authenticatedUserResourceDetails(
            @Value("${security.oauth2.client.accessTokenUri:/oauth/token}") String accessTokenUri,
            @Value("${security.oauth2.client.client-id}") String clientId,
            @Value("${security.oauth2.client.client-secret}") String clientSecret,
            LocalHostUriTemplateHandler localHostUriTemplateHandler) {
        final URI expandedAccessTokenUri = localHostUriTemplateHandler.expand(accessTokenUri);
        return ResourceOwnerPasswordResourceDetailsBuilder
                .aResourceOwnerPasswordResourceDetails()
                .withAccessTokenUri(expandedAccessTokenUri.toString())
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withUsername(TICKET_SERVICE_USER)
                .withPassword(USER_PASSWORD)
                .withGrantTypePassword()
                .build();
    }

    @Bean
    @Lazy
    public OAuth2RestTemplate authenticatedUserRestTemplate(
            TestRestTemplate testRestTemplate,
            @Qualifier("authenticatedUserResourceDetails") OAuth2ProtectedResourceDetails resourceDetails) {
        final OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
        return restTemplate;
    }

    @Bean
    @Lazy
    public TestRestTemplate authenticatedUserTestRestTemplate(OAuth2RestTemplate authenticatedUserRestTemplate,
                                                              LocalHostUriTemplateHandler localHostUriTemplateHandler,
                                                              HttpClientOption[] httpClientOptions) {

        final TestRestTemplate restTemplate =
                new TestRestTemplate(authenticatedUserRestTemplate, null, null, httpClientOptions);
        restTemplate.setUriTemplateHandler(localHostUriTemplateHandler);
        return restTemplate;
    }

    @Bean
    public HttpClientOption[] getHttpClientOptions(AbstractConfigurableEmbeddedServletContainer container) {
        return isSslEnabled(container)
                ? SSL_OPTIONS
                : DEFAULT_OPTIONS;
    }
}
