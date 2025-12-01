package com.cp.danek.astroAPI.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AccessDeniedHandlerJwt accessDeniedHandler;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtTokenProvider jwtTokenProvider,
                          AuthEntryPointJwt unauthorizedHandler,
                          AccessDeniedHandlerJwt accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Настройка CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Отключение CSRF - ИСПРАВЛЕНО (используем лямбду)
                .csrf(AbstractHttpConfigurer::disable)

                // Обработка исключений
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // Настройка сессий (без состояния)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Настройка авторизации запросов
                .authorizeHttpRequests(auth -> auth
                        // Публичные endpoints (без аутентификации)
                        .requestMatchers(
                                "/api/auth/**",            // Аутентификация
                                "/api/users/create",       // Регистрация
                                "/api/test/**",           // Тестовые endpoints
                                "/swagger-ui/**",         // Swagger UI
                                "/swagger-ui.html",       // Swagger UI HTML
                                "/v3/api-docs/**",        // OpenAPI JSON
                                "/api-docs/**"            // Документация
                        ).permitAll()
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                );

        // Добавляем JWT фильтр перед стандартным фильтром аутентификации
        http.addFilterAfter(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Разрешаем все origins (для разработки)
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Разрешаемые HTTP методы
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        ));

        // Разрешаемые заголовки
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Разрешаем credentials (куки, авторизация)
        configuration.setAllowCredentials(true);

        // Время кэширования CORS префлайт запросов
        configuration.setMaxAge(3600L);

        // Экспортируемые заголовки
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}