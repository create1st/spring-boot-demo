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

import com.create.model.Role;
import com.create.model.RoleBuilder;
import com.create.model.User;
import com.create.model.UserBuilder;
import com.create.repository.RoleRepository;
import com.create.repository.UserRepository;
import com.create.security.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void bootstrapDatabase() {
        bootstrapRoleRepository();
        bootstrapUserRepository();
    }

    private void bootstrapRoleRepository() {
        Stream.of(
                createRole(Authority.ADMIN_USER),
                createRole(Authority.TICKET_SERVICE_USER)
        )
                .forEach(roleRepository::save);
    }

    private Role createRole(String name) {
        return RoleBuilder
                .aRole()
                .withName(name)
                .build();
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
        Role adminRole = getAdminRole();
        Set<Role> roles = Collections.singleton(adminRole);
        return UserBuilder
                .anUser()
                .withUsername(ADMIN_USER)
                .withPassword(password)
                .withRoles(roles)
                .build();
    }

    private Role getAdminRole() {
        return roleRepository.findByName(Authority.ADMIN_USER);
    }

    private User createTicketServiceUser() {
        String password = passwordEncoder.encode(USER_PASSWORD);
        Role adminRole = getAdminRole();
        Role ticketServiceUserRole = getTicketServiceUserRole();
        Set<Role> roles = new HashSet<>(Arrays.asList(adminRole, ticketServiceUserRole));
        return UserBuilder
                .anUser()
                .withUsername(TICKET_SERVICE_USER)
                .withPassword(password)
                .withRoles(roles)
                .build();
    }

    private Role getTicketServiceUserRole() {
        return roleRepository.findByName(Authority.TICKET_SERVICE_USER);
    }
}
