package com.cba.core.wiremeweb.security;

import com.cba.core.wiremeweb.config.JwtConfig;
import com.cba.core.wiremeweb.exception.AuthEntryPoint;
import com.cba.core.wiremeweb.filter.AuthTokenVerifyFilter;
import com.cba.core.wiremeweb.filter.CustomLogoutHandler;
import com.cba.core.wiremeweb.filter.UserNamePasswordVerifyFilter;
import com.cba.core.wiremeweb.service.impl.CustomUserDetailsServiceImpl;
import com.cba.core.wiremeweb.service.impl.PermissionServiceImpl;
import com.cba.core.wiremeweb.service.impl.RefreshTokenServiceImpl;
import com.cba.core.wiremeweb.service.impl.TokenBlacklistServiceImpl;
import com.cba.core.wiremeweb.util.JwtUtils;
import com.cba.core.wiremeweb.util.UserBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile(value = {"dev", "stage"})
public class SecurityConfig {

    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final PermissionServiceImpl permissionServiceImpl;// autowired not worked in the context of creating new object using new key word, has to manually inject.
    private final JwtUtils jwtUtils;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserBean userBean;
    private final TokenBlacklistServiceImpl tokenBlacklistServiceImpl;
    private final CustomLogoutHandler customLogoutHandler;
    private final MessageSource messageSource;



    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling()
                .authenticationEntryPoint(new AuthEntryPoint())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandler)
                .and()
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/").permitAll();
                    authorize.requestMatchers("/refreshtoken").permitAll();
                    authorize.requestMatchers("/swagger-doc/**").permitAll();
                    authorize.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll();
                    authorize.requestMatchers("/actuator/**").permitAll()
                            .anyRequest().authenticated();
                });
//                .httpBasic();
//        http.authenticationProvider(authenticationProvider()); // no need, auto detect

        http.addFilter(new UserNamePasswordVerifyFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                jwtConfig, refreshTokenServiceImpl, jwtUtils, encoder));
        http.addFilterAfter(new AuthTokenVerifyFilter(jwtConfig, jwtUtils, permissionServiceImpl, decoder, userBean, tokenBlacklistServiceImpl,messageSource), UserNamePasswordVerifyFilter.class);

        return http.build();
    }
}
