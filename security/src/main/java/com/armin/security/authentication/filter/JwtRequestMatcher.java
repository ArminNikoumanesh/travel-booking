package com.armin.security.authentication.filter;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Path Request Matcher Class,
 * Matching Default Pattern and Processing Them in Spring Security
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Component
public class JwtRequestMatcher implements RequestMatcher {

    @Value("${rest.authenticated.matcher}")
    private List<String> authenticatedMatchers;
    @Value("${rest.authorized.matcher}")
    private List<String> authorizedMatchers;

    @Override
    public boolean matches(HttpServletRequest request) {
        List<RequestMatcher> matchers = new ArrayList<>();

        authorizedMatchers.forEach(matcher -> {
            RequestMatcher newMatcher = new AntPathRequestMatcher(matcher);
            matchers.add(newMatcher);
        });
        authenticatedMatchers.forEach(matcher -> {
            RequestMatcher newMatcher = new AntPathRequestMatcher(matcher);
            matchers.add(newMatcher);
        });

        return matchers.stream().anyMatch(matcher -> matcher.matches(request));
       /*
        RequestMatcher adminMatcher = new AntPathRequestMatcher(RestApi.MATCHER_ADMIN);
        RequestMatcher sellerMatcher = new AntPathRequestMatcher(RestApi.MATCHER_SELLER);
        RequestMatcher identifiedMatcher = new AntPathRequestMatcher(RestApi.MATCHER_IDENTIFIED);
        return identifiedMatcher.matches(request) || adminMatcher.matches(request) || sellerMatcher.matches(request);*/
    }
}
