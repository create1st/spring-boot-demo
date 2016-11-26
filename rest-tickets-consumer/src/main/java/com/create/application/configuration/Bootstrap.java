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

import com.create.model.User;
import com.create.model.UserBuilder;
import com.create.repository.UserRepository;
import com.create.security.Permission;
import com.create.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Configuration
public class Bootstrap {
    public static final String USER_PASSWORD = "secret";
    private static final String ADMIN_USER = "admin";
    public static final String TICKET_SERVICE_USER = "user";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void bootstrapDatabase() {
        bootstrapUserRepository();
    }

    private void bootstrapUserRepository() {
        Stream.of(
                createAdminUser(),
                createTicketServiceUser()
        )
                .forEach(userRepository::save);
    }

    private User createAdminUser() {
        String password = passwordEncoder.encode(USER_PASSWORD);
        Set<Role> roles = Collections.singleton(Role.ADMIN);
        Set<Permission> permissions = Collections.singleton(Permission.BATCH);
        return UserBuilder
                .anUser()
                .withUsername(ADMIN_USER)
                .withPassword(password)
                .withRoles(roles)
                .withPermissions(permissions)
                .build();
    }

    private User createTicketServiceUser() {
        String password = passwordEncoder.encode(USER_PASSWORD);
        Set<Role> roles = new HashSet<>(Arrays.asList(Role.ADMIN, Role.WRITER));
        Set<Permission> permissions = Permission.getAll();
        return UserBuilder
                .anUser()
                .withUsername(TICKET_SERVICE_USER)
                .withPassword(password)
                .withPermissions(permissions)
                .withRoles(roles)
                .build();
    }
}
