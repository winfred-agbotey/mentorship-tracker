package com.mentorshiptracker.models.converter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mentorshiptracker.models.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class UserInfoUserDetails implements UserDetails {
    private final String username;
    private final String password;

    @JsonIgnore
    private final Collection<? extends GrantedAuthority> authorities;

    public UserInfoUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRole().getAuthorities();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public @NotNull String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
