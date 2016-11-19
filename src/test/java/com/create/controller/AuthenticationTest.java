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

import com.create.application.configuration.ResourceOwnerPasswordResourceDetailsBuilder;
import com.create.application.configuration.TestConfiguration;
import com.create.application.configuration.TestControllerConfiguration;
import com.create.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static com.create.application.configuration.Bootstrap.TICKET_SERVICE_USER;
import static com.create.application.configuration.Bootstrap.USER_PASSWORD;
import static com.create.application.configuration.TestControllerConfiguration.TEST_GET_PATH;
import static com.create.application.configuration.TestControllerConfiguration.TEST_POST_PATH;
import static com.create.application.configuration.TestControllerConfiguration.TEST_POST_VALIDATOR_PATH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {
        TestConfiguration.class,
        TestControllerConfiguration.class
})
@RunWith(SpringRunner.class)
public class AuthenticationTest {
    @Value("${security.oauth2.client.client-id}")
    private String clientId;
    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;
    @Value("${security.oauth2.client.accessTokenUri:/oauth/token}")
    private String accessTokenUri;
    @Autowired
    private LocalHostUriTemplateHandler localHostUriTemplateHandler;
    @Autowired
    private OAuth2RestTemplate restTemplate;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private TestRestTemplate authenticatedUserTestRestTemplate;

    @Test
    public void shouldGetUserAuthenticationToken() {
        final OAuth2AccessToken accessToken = restTemplate.getAccessToken();
        assertThat(accessToken, notNullValue());
    }

    @Test(expected = OAuth2AccessDeniedException.class)
    public void shouldThrowAnExceptionForInvalidCredentials() {
        final OAuth2RestTemplate restTemplate = getRestTemplateWithInvalidUserPassword();
        restTemplate.getAccessToken();
    }

    private OAuth2RestTemplate getRestTemplateWithInvalidUserPassword() {
        final OAuth2ProtectedResourceDetails resourceDetails = getLocalOAuth2RemoteResourceWithInvalidPassword();
        return new OAuth2RestTemplate(resourceDetails);
    }

    public OAuth2ProtectedResourceDetails getLocalOAuth2RemoteResourceWithInvalidPassword() {
        final URI expandedAccessTokenUri = localHostUriTemplateHandler.expand(accessTokenUri);
        return ResourceOwnerPasswordResourceDetailsBuilder
                .aResourceOwnerPasswordResourceDetails()
                .withAccessTokenUri(expandedAccessTokenUri.toString())
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withUsername(TICKET_SERVICE_USER)
                .withPassword("invalid_password")
                .withGrantTypePassword()
                .build();
    }

    @Test(expected = OAuth2AccessDeniedException.class)
    public void shouldThrowAnExceptionForInvalidClientCredentials() {
        final OAuth2RestTemplate restTemplate = getRestTemplateWithInvalidClientPassword();
        restTemplate.getAccessToken();
    }

    private OAuth2RestTemplate getRestTemplateWithInvalidClientPassword() {
        final OAuth2ProtectedResourceDetails resourceDetails = getLocalOAuth2RemoteResourceWithInvalidClientPassword();
        return new OAuth2RestTemplate(resourceDetails);
    }

    public OAuth2ProtectedResourceDetails getLocalOAuth2RemoteResourceWithInvalidClientPassword() {
        final URI expandedAccessTokenUri = localHostUriTemplateHandler.expand(accessTokenUri);
        return ResourceOwnerPasswordResourceDetailsBuilder
                .aResourceOwnerPasswordResourceDetails()
                .withAccessTokenUri(expandedAccessTokenUri.toString())
                .withClientId(clientId)
                .withClientSecret("invalid_password")
                .withUsername(TICKET_SERVICE_USER)
                .withPassword(USER_PASSWORD)
                .withGrantTypePassword()
                .build();
    }

    @Test
    public void shouldReturnAnUserForAuthorizedGet() {
        final ResponseEntity<User> response = authenticatedUserTestRestTemplate.getForEntity(TEST_GET_PATH,
                User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getUsername(), is(TICKET_SERVICE_USER));
    }

    @Test
    public void shouldReturnUnauthorizedForUnauthorizedUser() {
        final ResponseEntity<User> response = testRestTemplate.getForEntity(TEST_GET_PATH, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldReturnAnUserForAuthorizedPost() {
        final ResponseEntity<User> response = authenticatedUserTestRestTemplate.postForEntity(
                TEST_POST_PATH, null, User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getUsername(), is(TICKET_SERVICE_USER));
    }

    @Test
    public void shouldReturnABadRequestForAuthorizedPostWithInvalidContent() {
        final ResponseEntity<User> response = authenticatedUserTestRestTemplate.postForEntity(
                localHostUriTemplateHandler.expand(TEST_POST_VALIDATOR_PATH), new User(), User.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
}
