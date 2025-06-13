package com.example.steam.config;

import com.example.steam.config.jwt.JwtAuthenticationFilter;
import com.example.steam.config.jwt.JwtExceptionHandler;
import com.example.steam.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final JwtExceptionHandler jwtExceptionHandler;
    private final ApplicationContext applicationContext;
    private final CorsConfig corsConfig;
    private final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html", "/actuator/**",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**","/api/v1/payment/success**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProvider, applicationContext, jwtExceptionHandler);
        http
                .cors(cors -> {
                    cors.configurationSource(corsConfig.corsFilter());
                })
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((ssmt) -> ssmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin((form) -> form.disable())
                .httpBasic((basic) -> basic.disable())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(AUTH_WHITELIST).permitAll()
//                                .requestMatchers(PUBLIC_API_LIST).permitAll()
//                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtExceptionHandler)
                        .accessDeniedHandler(jwtExceptionHandler)
                );

        return http.build();
    }
}
