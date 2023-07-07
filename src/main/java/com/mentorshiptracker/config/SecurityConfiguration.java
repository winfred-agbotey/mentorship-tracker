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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jetAuthedFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers("api/advisor/**").hasAnyRole("Administrator","Mentorship manager")
                        .requestMatchers(POST,"api/advisor/**").hasAnyAuthority("Administrator")
                        .requestMatchers("api/admin/**").hasAnyRole("Administrator")
                        .requestMatchers(POST,"api/admin/**").hasAnyAuthority("Administrator")
                        .requestMatchers(GET,"api/admin/**").hasAnyAuthority("Administrator")
                        .anyRequest()
                        .authenticated())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jetAuthedFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}