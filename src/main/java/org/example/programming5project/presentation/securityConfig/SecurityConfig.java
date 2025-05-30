package org.example.programming5project.presentation.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/css/**", "/webjars/**", "/js/**").permitAll()
                        .requestMatchers("/patients/**").authenticated()
                        .requestMatchers("/doctors/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/hospitals").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/doctors/delete/**").hasRole("ADMIN")
                        .requestMatchers("/doctors/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/patients/**").hasRole("ADMIN")
                        .requestMatchers("/hospitals/add/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/hospitals/*").permitAll() //for client side
                        .requestMatchers(HttpMethod.GET, "/api/hospitals/*").permitAll() //for client side

                        .requestMatchers("/hospitals/**").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        antMatcher(HttpMethod.POST, "/api/hospitals"),
                        antMatcher(HttpMethod.PATCH, "/api/hospitals/*")// for the client side
                ))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:9000")
                        .allowedMethods(
                                HttpMethod.GET.name(), HttpMethod.PATCH.name());
            }
        };
    }

}
