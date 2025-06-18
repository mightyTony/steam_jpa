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
            "/",  // 기본 루트
            "/swagger-ui/**", // 정적 자원 포함
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/api-docs",  // 문서 초기 로딩용
            "/v3/api-docs/**", // 세부 문서
            "/swagger-ui-custom.html",
            "/api-docs",
            "/api-docs/**",
            "/actuator/**",
            "/api/v1/auth/**",
            "/api/v1/payment/success**"
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
                                .requestMatchers(
                                        "/",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",
                                        "/webjars/**",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/actuator/**",
                                        "/api/v1/auth/**",
                                        "/api/v1/payment/success**").permitAll()
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
