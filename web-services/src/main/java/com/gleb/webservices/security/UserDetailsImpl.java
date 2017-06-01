package com.gleb.webservices.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gleb.dao.objects.DBUser;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private DBUser user;

    public UserDetailsImpl() {

    }

    public UserDetailsImpl(DBUser user) {
        this.user = user;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public DBUser getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String group : user.getGroups()) {
            authorities.add(new SimpleGrantedAuthority(group));
        }
        return authorities;
    }
}
