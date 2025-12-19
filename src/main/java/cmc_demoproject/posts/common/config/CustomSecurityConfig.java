package cmc_demoproject.posts.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    // PasswordEncoder 하나만 남김 (중복 오류 해결)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 정적 리소스 보안 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("--- 세션 기반 Security 설정 적용 ---");

        http
                .csrf(csrf -> csrf.disable()) // API 테스트 편의상 비활성화

                // 1. 세션 관리 설정 (Stateless가 아닌 세션 유지 방식)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요 시 세션 생성
                        .sessionFixation().migrateSession() // 세션 고정 공격 방지
                )

                // 2. 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI: ADMIN 권한 필요 (요구사항)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                        // 누구나 접근 가능한 경로
                        .requestMatchers("/login", "/signup", "/api/auth/**", "/css/**", "/js/**").permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 3. 폼 로그인 설정 (세션 로그인 방식)
                .formLogin(form -> form
                        .loginPage("/login")               // 로그인 페이지 URL
                        .loginProcessingUrl("/api/auth/login") // 실제 로그인 처리 URL
                        .defaultSuccessUrl("/home", true)  // 로그인 성공 시 이동
                        .permitAll()
                )

                // 4. 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}