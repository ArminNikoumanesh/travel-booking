package com.armin.security.model.object;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * All Request turns into JwtInputAuthentication Object
 * and imported into Authentication Process
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public class JwtInputAuthentication implements Authentication {
    private final String token;

    public JwtInputAuthentication(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
