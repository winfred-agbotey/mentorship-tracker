package com.mentorshiptracker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.mentorshiptracker.constants.AppConstants.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("api/v1/advisor/**").hasAnyRole(ADMIN_ROLE_NAME,MANAGER_ROLE_NAME)
                        .requestMatchers(POST,"api/v1/advisor/**").hasAnyAuthority(ADMIN_PERMISSION)
                        .requestMatchers("api/v1/admin/**").hasAnyRole(ADMIN_ROLE_NAME)
                        .requestMatchers(POST,"api/v1/admin/**").hasAnyAuthority(ADMIN_PERMISSION)
                        .requestMatchers(GET,"api/v1/admin/**").hasAnyAuthority(ADMIN_PERMISSION)
                        .requestMatchers("api/v1/users/advisor/").hasAnyRole(ADMIN_ROLE_NAME)
                        .requestMatchers("api/v1/users/advisee/").hasAnyRole(ADMIN_ROLE_NAME)
                        .anyRequest()
                        .authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}