package com.armin.security.config;

import com.armin.security.authentication.exception.JwtAuthenticationEntryPoint;
import com.armin.security.authentication.filter.IdentifiableAnonymousAuthenticationFilter;
import com.armin.security.authentication.filter.JwtAuthenticationProcessingFilter;
import com.armin.security.authentication.filter.JwtAuthenticationProvider;
import com.armin.security.authorization.exception.JwtAccessDeniedHandler;
import com.armin.security.bl.JwtService;
import com.armin.security.model.object.SecurityAccessRule;
import com.armin.utility.object.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security Configuration Class,
 * Config and Manage all Security Issues
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private IdentifiableAnonymousAuthenticationFilter identifiableAnonymousAuthenticationFilter;
    @Autowired
    private JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private Environment environment;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(jwtAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        try {
            List<SecurityAccessRule> accessRules = this.jwtService.generateAccessRule();
            http.authorizeHttpRequests(authCustomizer ->
                    accessRules.forEach(rule -> authCustomizer
                            .requestMatchers(RegexRequestMatcher.regexMatcher(rule.getHttpMethod(), rule.getUrl()))
                            .hasAnyAuthority(rule.getAccess().toArray(new String[]{}))
                    )
            );
        } catch (SystemException ignored) {

        }
        return http.authenticationManager(http
                        .getSharedObject(AuthenticationManagerBuilder.class)
                        .authenticationProvider(jwtAuthenticationProvider).build())
                .anonymous(anonymousCustomizer -> anonymousCustomizer.authenticationFilter(identifiableAnonymousAuthenticationFilter))
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/swagger**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/swagger-ui/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/swagger-resources/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/webjars/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/doc/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/v2/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/v3/api-docs/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/bus-refresh").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/api-docs/**").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers(environment.getProperty("rest.public.matcher", String[].class)).permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers(environment.getProperty("rest.authenticated.matcher", String[].class)).authenticated())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/settings").permitAll())
                .authorizeHttpRequests(authCustomizer -> authCustomizer.requestMatchers("/**").denyAll())
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(this.jwtAuthenticationEntryPoint).accessDeniedHandler(this.jwtAccessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowPrivateNetwork(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationProcessingFilter> disableAutoRegistration(JwtAuthenticationProcessingFilter filter) {
        final FilterRegistrationBean<JwtAuthenticationProcessingFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }
}
