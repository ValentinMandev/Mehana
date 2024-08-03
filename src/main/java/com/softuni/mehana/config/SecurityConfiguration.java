package com.softuni.mehana.config;

import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.implementation.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/", "/about", "/contact", "/gallery", "menu",
                                                "/user/login", "/user/register", "/error").permitAll()
                                        .requestMatchers("/menu/**",
                                                "/menu",
                                                "/",
                                                "/cart/add",
                                                "/cart",
                                                "/cart/remove",
                                                "checkout",
                                                "finalize-order",
                                                "order-history").hasRole(UserRoleEnum.USER.name())
                                        .anyRequest()
                                        .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/user/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/user/login?error")
                )
                .logout(
                        logout ->
                                logout
                                        .logoutUrl("/user/logout")
                                        .logoutSuccessUrl("/")
                                        .invalidateHttpSession(true)
                )
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder
                .defaultsForSpringSecurity_v5_8();
    }

}
