package com.mtapo.app.security;

import com.mtapo.app.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/resources/**")
                .permitAll()
                .antMatchers("/home", "/topics/**", "/activity", "/activity/get-activity/**", "/navbar/**")
                .hasAnyAuthority(Role.STUDENT, Role.TEACHER, Role.ADMIN)
                .antMatchers("/uploaded-topics/**", "/topic-requests/**")
                .hasAuthority(Role.TEACHER)
                .antMatchers("/users/**", "/activity/**", "/logs/**")
                .hasAuthority(Role.ADMIN);

        http.formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());

        http.logout().logoutSuccessUrl("/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SecurityAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SecurityAuthenticationFailureHandler();
    }
}
