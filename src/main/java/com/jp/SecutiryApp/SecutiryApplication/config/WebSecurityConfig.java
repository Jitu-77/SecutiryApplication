package com.jp.SecutiryApp.SecutiryApplication.config;

import com.jp.SecutiryApp.SecutiryApplication.enums.Role;
import com.jp.SecutiryApp.SecutiryApplication.filter.JwtAuthFilters;
import com.jp.SecutiryApp.SecutiryApplication.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilters jwtAuthFilters;
    private final OAuth2SuccessHandler successHandler;
    private static final String[] publicRoutes ={
            "/error", "/auth/**","/home.html"
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .authorizeHttpRequests(auth -> auth

                        // NO RESTRICTION
//                        .requestMatchers("/posts", "/error", "/auth/**","/home.html").permitAll()
                        .requestMatchers(publicRoutes).permitAll()

                        //now implementing role based
//                                .requestMatchers("/posts/**").hasRole(Role.ADMIN.name())

                        //partial authorization on same route
                        .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/posts/**").hasAnyRole(Role.ADMIN.name(),Role.CREATOR.name())


                        //restricted to ADMIN ROLE
//                        .requestMatchers("/posts/**").hasAnyRole("ADMIN")

                        .anyRequest().authenticated()
                        )
                //disable csrf
                .csrf(csrfConfig -> csrfConfig.disable())

                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // registering the  custom filter
                .addFilterBefore(jwtAuthFilters, UsernamePasswordAuthenticationFilter.class)
                // for oauth2 client
                .oauth2Login(oauthConfigurer->
                        oauthConfigurer
                                .failureUrl("/login?error=true")
                                .successHandler(successHandler)
                );


                    //we can disable the form based login by commenting this one
//                 .formLogin(Customizer.withDefaults());


        return httpSecurity.build();
    };
// Now no need as we are using our own UserDetailsService---------
//    @Bean
//    UserDetailsService myInMemoryUserDetailsService() {
//        UserDetails normalUser = User
//                .withUsername("anuj")
//                .password(passwordEncoder().encode("Anuj123"))
//                .roles("USER")
//                .build();
//
//        UserDetails adminUser = User
//                .withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // removing the passwordEncoder from here as it creates circular dependency between jwtAuthFilter and User Service
    // so moving this to app config
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}