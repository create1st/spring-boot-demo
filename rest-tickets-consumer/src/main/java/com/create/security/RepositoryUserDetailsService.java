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

package com.create.security;

import com.create.model.User;
import com.create.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepositoryUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public RepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(user);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);
    }

    private User getUser(String username) {
        return Optional
                .of(username)
                .map(userRepository::findByUsername)
                .filter(Objects::nonNull)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " does not exists"));
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
       return Stream.concat(
               getGrantedAuthoritiesForRoles(user),
               getGrantedAuthoritiesForPermissions(user)
       )
                .collect(Collectors.toList());
    }

    private Stream<SimpleGrantedAuthority> getGrantedAuthoritiesForRoles(User user) {
        return user.getRoles()
                 .stream()
                 .map(Role::toPermissionName)
                 .map(SimpleGrantedAuthority::new);
    }

    private Stream<SimpleGrantedAuthority> getGrantedAuthoritiesForPermissions(User user) {
        return user.getPermissions()
                .stream()
                .map(Permission::name)
                .map(SimpleGrantedAuthority::new);
    }
}
