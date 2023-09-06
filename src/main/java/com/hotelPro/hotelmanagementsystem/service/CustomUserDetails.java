package com.hotelPro.hotelmanagementsystem.service;

import com.hotelPro.hotelmanagementsystem.model.Company;
import com.hotelPro.hotelmanagementsystem.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))

                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can add logic to check if the account is expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can add logic to check if the account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can add logic to check if the credentials are expired
    }

    @Override
    public boolean isEnabled() {
        return true; // You can add logic to check if the account is enabled
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getMobileNumber() {
        return user.getMobileNumber();
    }

    public Company getCompany() {
        return user.getCompany();
    }
}
