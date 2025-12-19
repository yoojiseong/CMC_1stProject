package cmc_demoproject.posts.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API 테스트 편의를 위해 비활성화

                // 1. 세션 관리 설정 (핵심)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요 시 세션 생성
                        .sessionFixation().migrateSession() // 세션 고정 공격 방지
                )

                // 2. 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN") // ADMIN만 접근
                        .requestMatchers("/login", "/signup", "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 3. 폼 로그인 설정 (세션 로그인 방식)
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login") // 로그인 처리 주소
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )

                // 4. 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }
}