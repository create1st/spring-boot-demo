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

import com.create.model.UserBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Configuration
public class TestControllerConfiguration {
    public static final String TEST_GET_PATH = "/test-get";
    public static final String TEST_POST_PATH = "/test-post";
    public static final String TEST_POST_VALIDATOR_PATH = "/test-post-validator";

    @RestController
    public static class DummyController {

        @GetMapping(TEST_GET_PATH)
        @ResponseBody
        public com.create.model.User testGet(@AuthenticationPrincipal User user) {
            return UserBuilder
                    .anUser()
                    .withUsername(user.getUsername())
                    .build();
        }

        @PostMapping(TEST_POST_PATH)
        @ResponseBody
        public com.create.model.User testPost(@AuthenticationPrincipal User user) {
            return UserBuilder
                    .anUser()
                    .withUsername(user.getUsername())
                    .build();
        }

        @PostMapping(TEST_POST_VALIDATOR_PATH)
        @ResponseBody
        public com.create.model.User testPost(@Valid  @RequestBody com.create.model.User modelUser,
                                              @AuthenticationPrincipal User user) {
            return UserBuilder
                    .anUser()
                    .withUsername(user.getUsername())
                    .build();
        }
    }
}
