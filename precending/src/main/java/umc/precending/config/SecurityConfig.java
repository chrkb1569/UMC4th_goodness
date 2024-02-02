package umc.precending.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import umc.precending.config.jwt.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final String[] swaggerPath = {"/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/error"};
    private final String AUTH_PATH = "/api/auth/**";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(
                        security -> security
                                .authenticationEntryPoint(authenticationEntryPointHandler)
                                .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(
                        security -> security
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        security -> security
                                .requestMatchers(HttpMethod.POST, AUTH_PATH).permitAll()
                                .requestMatchers(swaggerPath).permitAll()
                                .anyRequest().authenticated())
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
