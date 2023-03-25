package com.example.Employeedbm.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig
{
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/assets/**","/").permitAll()
                .requestMatchers("/home","/emps/","/emps/**","/emp/**").hasRole("ADMIN")
                .requestMatchers("/home","/emps/").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
//                    .loginPage("Login.html")
//                    .usernameParameter("username")
//                    .passwordParameter("password")
//                    .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .and()
                .logout().invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout/"))
                .logoutSuccessUrl("/").permitAll()
                .and()
                .rememberMe().rememberMeParameter("remember_me").key("mySecreteKey").tokenValiditySeconds(60 * 60 * 60 * 24 * 7);

        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        List<UserDetails> listUser = new ArrayList<>();
        listUser.add(
                User.builder()
                        .username("admin")
                        .password(getPasswordEncoder().encode("123"))
                        .roles("ADMIN")
                        .build()
        );

        listUser.add(
                User.builder()
                        .username("user")
                        .password(getPasswordEncoder().encode("123"))
                        .roles("USER")
                        .build()
        );

        return new InMemoryUserDetailsManager(listUser);
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


