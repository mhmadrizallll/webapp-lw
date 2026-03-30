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
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .headers(headers -> headers.frameOptions(frame -> frame.disable()))
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/", "/login", "/login-success", "/access-denied")
          .permitAll()
          .requestMatchers("/auth/register")
          .hasRole("ADMIN")
          .requestMatchers(
            "/informasi_it",
            "/form_it",
            "/petunjuk_it",
            "/prosedur_it",
            "/telepon_extension",
            "/daily_report",
            "/css/**",
            "/js/**",
            "/images/**",
            "/pdfs/**",
            "/pdfjs/**"
          )
          .permitAll()

          // 🔥 WAJIB tambahkan root + subpath
          .requestMatchers("/fig5", "/fig5/**")
          .hasAnyRole("FIG5", "ADMIN")

          .requestMatchers("/filw-nb", "/filw-nb/**")
          .hasAnyRole("FILW_NB", "ADMIN")

          .requestMatchers("/filw-on", "/filw-on/**")
          .hasAnyRole("FILW_ON", "ADMIN")

          .anyRequest()
          .authenticated()
      )

      // 🔥 LOGIN SUCCESS → HALAMAN TRANSISI
      .formLogin(form ->
        form
          .loginPage("/login")
          .failureUrl("/login?error=true")
          .successHandler((request, response, authentication) -> {
            String role = authentication
              .getAuthorities()
              .iterator()
              .next()
              .getAuthority();

            request.getSession().setAttribute("LOGIN_ROLE", role);

            response.sendRedirect("/login-success");
          })
          .permitAll()
      )

      // 🔥 ACCESS DENIED → HALAMAN ACCESS DENIED
      .exceptionHandling(ex ->
        ex.accessDeniedHandler((request, response, accessDeniedException) -> {
          String uri = request.getRequestURI();

          // hapus session lama
          request.getSession().invalidate();

          // buat session baru
          var newSession = request.getSession(true);

          if (uri.startsWith("/filw-nb")) {
            newSession.setAttribute("DENIED_ROLE", "FILW_NB");
          } else if (uri.startsWith("/filw-on")) {
            newSession.setAttribute("DENIED_ROLE", "FILW_ON");
          } else if (uri.startsWith("/fig5")) {
            newSession.setAttribute("DENIED_ROLE", "FIG5");
          }

          response.sendRedirect("/access-denied");
        })
      )

      .logout(logout ->
    logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/logout-success") // redirect ke halaman terpisah
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