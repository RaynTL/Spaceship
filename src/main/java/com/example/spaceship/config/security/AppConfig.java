package com.example.spaceship.config.security;

import com.example.spaceship.exception.user.UserNotFoundException;
import com.example.spaceship.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Application configuration class for Spring Security.
 * This class configures the security aspects, including user authentication and password encoding.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository userRepository;

    /**
     * Provides a UserDetailsService that loads user-specific data.
     * @return UserDetailsService implementation that fetches user details by email.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            final com.example.spaceship.model.User user = userRepository
                    .findByEmail(username).orElseThrow(() ->new UserNotFoundException(username));
            return User.builder().username(user.getEmail()).password(user.getPassword()).build();
        };
    }

    /**
     * Configures the AuthenticationProvider to use the UserDetailsService and password encoder.
     * @return the configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    /**
     * Provides an AuthenticationManager that manages authentication requests.
     * @param authenticationConfiguration configuration for authentication
     * @return the configured AuthenticationManager.
     * @throws Exception if authentication configuration fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder that encodes passwords using BCrypt.
     * @return the BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
