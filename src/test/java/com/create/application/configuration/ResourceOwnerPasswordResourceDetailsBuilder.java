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

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;

public final class ResourceOwnerPasswordResourceDetailsBuilder {
    private String username;
    private String password;
    private String id;
    private String grantType = "unsupported";
    private String clientId;
    private String accessTokenUri;
    private List<String> scope;
    private String clientSecret;
    private AuthenticationScheme clientAuthenticationScheme = AuthenticationScheme.header;
    private String tokenName = OAuth2AccessToken.ACCESS_TOKEN;

    private ResourceOwnerPasswordResourceDetailsBuilder() {
    }

    public static ResourceOwnerPasswordResourceDetailsBuilder aResourceOwnerPasswordResourceDetails() {
        return new ResourceOwnerPasswordResourceDetailsBuilder();
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }


    public ResourceOwnerPasswordResourceDetailsBuilder withGrantTypePassword() {
        this.grantType = "password";
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withScope(List<String> scope) {
        this.scope = scope;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withClientAuthenticationScheme(AuthenticationScheme clientAuthenticationScheme) {
        this.clientAuthenticationScheme = clientAuthenticationScheme;
        return this;
    }

    public ResourceOwnerPasswordResourceDetailsBuilder withTokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    public ResourceOwnerPasswordResourceDetails build() {
        ResourceOwnerPasswordResourceDetails resourceOwnerPasswordResourceDetails =
                new ResourceOwnerPasswordResourceDetails();
        resourceOwnerPasswordResourceDetails.setUsername(username);
        resourceOwnerPasswordResourceDetails.setPassword(password);
        resourceOwnerPasswordResourceDetails.setId(id);
        resourceOwnerPasswordResourceDetails.setGrantType(grantType);
        resourceOwnerPasswordResourceDetails.setClientId(clientId);
        resourceOwnerPasswordResourceDetails.setAccessTokenUri(accessTokenUri);
        resourceOwnerPasswordResourceDetails.setScope(scope);
        resourceOwnerPasswordResourceDetails.setClientSecret(clientSecret);
        resourceOwnerPasswordResourceDetails.setClientAuthenticationScheme(clientAuthenticationScheme);
        resourceOwnerPasswordResourceDetails.setTokenName(tokenName);
        return resourceOwnerPasswordResourceDetails;
    }
}
