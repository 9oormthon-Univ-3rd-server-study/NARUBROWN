package me.na2ru2.narubrown.security.config;

import lombok.RequiredArgsConstructor;
import me.na2ru2.narubrown.oauth.service.CustomOAuthUserService;
import me.na2ru2.narubrown.security.exception.CustomAccessDeniedHandler;
import me.na2ru2.narubrown.security.exception.CustomAuthenticationEntryPoint;
import me.na2ru2.narubrown.security.filter.JwtAuthenticationFilter;
import me.na2ru2.narubrown.security.handler.OAuthSuccessHandler;
import me.na2ru2.narubrown.security.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final CustomOAuthUserService customOAuthUserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final String[] allowedURLs = {
            "/swagger-resources/**", "/swagger-ui/index.html", "/webjars/**", "/swagger/**", "/users/exception", "/v3/api-docs/**", "/swagger-ui/**",
            "/user/sign-in", "/user/sign-up", "/oauth/kakao", "/oauth/google", "/error", "/favicon.ico",
            "**exception**"
    };
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuthUserService))
                        .successHandler(oAuthSuccessHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(allowedURLs).permitAll().anyRequest().hasRole("USER"))
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
                return http.build();
    }
}
