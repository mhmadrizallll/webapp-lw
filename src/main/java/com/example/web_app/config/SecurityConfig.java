package com.example.web_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ✅ ROOT PUBLIC
                    .requestMatchers("/").permitAll()

                    //  PUBLIC LAIN
                    .requestMatchers("/auth/register", "/login", "/css/**", "/js/**", "/images/**").permitAll()

                    // ✅ ROLE BASED
                    .requestMatchers("/fig5/**").hasAnyRole("FIG5", "ADMIN")
                    .requestMatchers("/filw-nb/**").hasAnyRole("FILW_NB", "ADMIN")
                    .requestMatchers("/filw-on/**").hasAnyRole("FILW_ON", "ADMIN")

                    // ✅ selain itu harus login
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {

                    String role = authentication.getAuthorities()
                            .iterator().next().getAuthority();

                    if (role.equals("ROLE_ADMIN")) {
                        response.sendRedirect("/");
                    } else if (role.equals("ROLE_FIG5")) {
                        response.sendRedirect("/fig5");
                    } else if (role.equals("ROLE_FILW_NB")) {
                        response.sendRedirect("/filw-nb");
                    } else if (role.equals("ROLE_FILW_ON")) {
                        response.sendRedirect("/filw-on");
                    }
                })
                .permitAll()
            )
            // 🔥 INI BAGIAN PENTING
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, accessDeniedException) -> {

                    var auth = request.getUserPrincipal();

                    if (auth != null) {
                        String role = request.isUserInRole("ADMIN") ? "ADMIN" :
                                      request.isUserInRole("FIG5") ? "FIG5" :
                                      request.isUserInRole("FILW_NB") ? "FILW_NB" :
                                      request.isUserInRole("FILW_ON") ? "FILW_ON" :
                                      "";

                        switch (role) {
                            case "ADMIN" -> response.sendRedirect("/");
                            case "FIG5" -> response.sendRedirect("/fig5");
                            case "FILW_NB" -> response.sendRedirect("/filw-nb");
                            case "FILW_ON" -> response.sendRedirect("/filw-on");
                            default -> response.sendRedirect("/login");
                        }
                    } else {
                        response.sendRedirect("/login");
                    }
                })
            )


            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}