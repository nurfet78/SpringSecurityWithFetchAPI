package com.nurfet.fetchapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final SuccessUserHandler successUserHandler;

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    private final CustomUrlLogoutSuccessHandler urlLogoutSuccessHandler;

    private final PasswordEncoder passwordEncoder;

    private final CustomAccessDeniedHandler accessDeniedHandler;


    public SecurityConfig(UserDetailsService userDetailsService,
                          SuccessUserHandler successUserHandler,
                          CustomAuthenticationFailureHandler authenticationFailureHandler,
                          CustomUrlLogoutSuccessHandler urlLogoutSuccessHandler,
                          PasswordEncoder passwordEncoder,
                          CustomAccessDeniedHandler accessDeniedHandler) {

        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.urlLogoutSuccessHandler = urlLogoutSuccessHandler;
        this.passwordEncoder = passwordEncoder;
        this.accessDeniedHandler = accessDeniedHandler;
    }


    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/users/*").hasRole("ADMIN")
                        .anyRequest().authenticated()).
                exceptionHandling((exception) -> exception.accessDeniedHandler(accessDeniedHandler))

                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/")
                        .permitAll()
                        .successHandler(successUserHandler)
                        .failureHandler(authenticationFailureHandler)
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll())

                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .permitAll()
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                        .logoutSuccessHandler(urlLogoutSuccessHandler));


        return http.build();
    }
}
