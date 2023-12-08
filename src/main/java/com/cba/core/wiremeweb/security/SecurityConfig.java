package com.cba.core.wiremeweb.security;

import com.cba.core.wiremeweb.config.JwtConfig;
import com.cba.core.wiremeweb.dto.PermissionRequestDto;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.exception.AuthEntryPoint;
import com.cba.core.wiremeweb.filter.AuthTokenVerifyFilter;
import com.cba.core.wiremeweb.filter.CustomLogoutHandler;
import com.cba.core.wiremeweb.filter.UserNamePasswordVerifyFilter;
import com.cba.core.wiremeweb.service.CustomUserDetailsService;
import com.cba.core.wiremeweb.service.PermissionService;
import com.cba.core.wiremeweb.service.RefreshTokenService;
import com.cba.core.wiremeweb.service.TokenBlacklistService;
import com.cba.core.wiremeweb.util.JwtUtil;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile(value = {"dev", "stage"})
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;
    private final PermissionService<PermissionResponseDto, PermissionRequestDto> permissionService;// autowired not worked in the context of creating new object using new key word, has to manually inject.
    private final JwtUtil jwtUtil;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserBeanUtil userBeanUtil;
    private final TokenBlacklistService tokenBlacklistService;
    private final CustomLogoutHandler customLogoutHandler;
    private final MessageSource messageSource;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
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
                    authorize.requestMatchers("/general/**").permitAll();
                    authorize.requestMatchers("/swagger-doc/**").permitAll();
                    authorize.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll();
                    authorize.requestMatchers("/actuator/**").permitAll()
                            .anyRequest().authenticated();
                });
//                .httpBasic();
//        http.authenticationProvider(authenticationProvider()); // no need, auto detect

        http.addFilter(new UserNamePasswordVerifyFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                jwtConfig, refreshTokenService, jwtUtil, encoder));
        http.addFilterAfter(new AuthTokenVerifyFilter(jwtConfig, jwtUtil, permissionService, decoder, userBeanUtil, tokenBlacklistService, messageSource), UserNamePasswordVerifyFilter.class);

        return http.build();
    }
}
