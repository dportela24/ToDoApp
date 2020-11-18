package com.dportela.toDoApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    int id;

    private String username;

    @JsonIgnore
    private String password;

    public UserDetailsImpl(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserDetailsImpl(int id, String username) {
        this.id = id;
        this.username = username;
        this.password = null;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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