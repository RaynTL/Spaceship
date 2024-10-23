package com.example.spaceship.config.security;


import com.example.spaceship.model.Token;
import com.example.spaceship.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration class for setting up security filters and policies.
 * This class configures HTTP security for the application, including authentication,
 * authorization, and session management.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    /**
     * Configures the security filter chain for HTTP requests.
     * @param httpSecurity the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if there is an error during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .headers(httpSecurityHeadersConfigurer -> {
                httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
            })
            .authorizeHttpRequests(request -> request
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                    .permitAll()
                .requestMatchers("/auth/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout ->
                logout.logoutUrl("/auth/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                        logout(header);
                     })
                    .logoutSuccessHandler((request, response, authentication) ->SecurityContextHolder.getContext())
            );

        return httpSecurity.build();
    }

    /**
     * Handles user logout by invalidating the token.
     * @param header the Authorization header containing the token
     */
    private void logout(String header) {

        if (header == null || !header.startsWith("Bearer")) {
            throw new IllegalArgumentException("Invalid token");
        }

        final String jwtToken = header.substring(7);;
        final Token token = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        token.setExpired(true);
        token.setRevoked(true);
        tokenRepository.save(token);
    }
}
