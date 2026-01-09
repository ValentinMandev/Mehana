package com.softuni.mehana.config;

import com.softuni.mehana.model.enums.UserRoleEnum;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                                        .requestMatchers("/",
                                                "/about",
                                                "/contact",
                                                "/gallery",
                                                "/menu",
                                                "/user/login",
                                                "/user/register",
                                                "/error").permitAll()

                                        .requestMatchers(
                                                "/cart",
                                                "/cart/add",
                                                "/cart/remove",
                                                "/checkout",
                                                "/order-history",
                                                "/orders/all",
                                                "/orders/{id}",
                                                "/user/edit-profile").hasRole(UserRoleEnum.USER.name())

                                        .requestMatchers(
                                                "/edit-product/{id}",
                                                "/disable-product/{id}",
                                                "/add-product"
                                                ).hasRole(UserRoleEnum.ADMIN.name())
                                        .anyRequest()
                                        .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/user/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                // Премахваме .defaultSuccessUrl("/", true) и добавяме това:
                                .successHandler((request, response, authentication) -> {
                                    String returnUrl = request.getParameter("returnUrl");
                                    if (returnUrl != null && !returnUrl.isEmpty() && !returnUrl.equals("null")) {
                                        response.sendRedirect(returnUrl);
                                    } else {
                                        response.sendRedirect("/"); // по подразбиране
                                    }
                                })
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
