package com.macroshop.shop.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // BCryptPasswordEncoder 빈을 정의
    // 이 PasswordEncoder는 암호화에 사용되며, 사용자의 비밀번호를 안전하게 저장하고 검증하는데 사용된다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }


    // 필요에 따라 다른 Spring Security 설정을 추가할 수 있습니다.
    // 예를 들어, 특정 HTTP 요청에 대한 보안 요구사항, CSRF 보호, 세션 관리 등을 설정할 수 있습니다.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(registry -> registry.requestMatchers("/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(config -> config.sessionFixation().changeSessionId()
                        .maximumSessions(1)
                        .expiredSessionStrategy(Object::notify)
                        .maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry()));
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll()); // 모든 요청에 대해 접근을 허용합니다.
        // 예시로 CSRF 보호를 비활성화했습니다. 실제 운영 환경에서는 보안을 위해 CSRF 보호를 유지하는 것이 좋습니다.

        return http.build();
    }


}
