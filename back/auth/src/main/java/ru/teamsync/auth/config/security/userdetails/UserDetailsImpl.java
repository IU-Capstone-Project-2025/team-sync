package ru.teamsync.auth.config.security.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDetailsImpl(
        Integer internalId,
        String email,
        List<Role> roles
) implements UserDetails {

    private static final String NO_PASSWORD = "";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return NO_PASSWORD;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
