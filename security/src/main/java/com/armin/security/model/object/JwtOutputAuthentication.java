package com.armin.security.model.object;

import com.armin.utility.object.UserContextDto;
import com.armin.security.statics.constants.SecurityConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * All Request turns into JwtOutputAuthentication Object
 * after Successful Authentication
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public class JwtOutputAuthentication implements Authentication {

    private final UserContextDto userContextDto;

    public JwtOutputAuthentication(UserContextDto user) {
        this.userContextDto = user;
    }

    @Override
    public Object getPrincipal() {
        return this.userContextDto;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userContextDto.getPermissionIds().stream()
                .map(eachId -> new SimpleGrantedAuthority(SecurityConstant.SIMPLE_GRANTED_AUTHORITY_PREFIX + eachId))
                .collect(Collectors.toSet());
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
