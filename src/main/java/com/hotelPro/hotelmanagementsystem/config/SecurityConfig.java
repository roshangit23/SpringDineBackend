package com.hotelPro.hotelmanagementsystem.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotelPro.hotelmanagementsystem.filter.CompanyIdFilter;
import com.hotelPro.hotelmanagementsystem.filter.JwtTokenFilter;
import com.hotelPro.hotelmanagementsystem.service.MyUserDetailsService;
import com.hotelPro.hotelmanagementsystem.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CompanyIdFilter companyIdFilter;
    @Autowired
    private ObjectMapper objectMapper;
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(new AntPathRequestMatcher("/auth/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/refresh")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/logout")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/register-admin")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/register-dashboard-user")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/dashboard/login")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/dashboard-user/add-company/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/dashboard-user/remove-company/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/register-company")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/auth/register")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/auth/deleteUser/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/auth/updateRole/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/auth/users-by-company/**")).hasRole("ADMIN")
                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider,objectMapper), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(companyIdFilter, JwtTokenFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(Arrays.asList(
                new DaoAuthenticationProvider() {{
                    setUserDetailsService(userDetailsService);
                    setPasswordEncoder(passwordEncoder);
                }}
        ));
    }

}




