package cmc_demoproject.posts.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 필터 체인 설정 (접근 권한, 로그인/로그아웃 설정 등)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Swagger UI 접근 제한: ADMIN 권한 필요 (요구사항)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")

                        // 2. 인증 없이 접근 허용할 경로
                        .requestMatchers("/signup", "/login", "/api/auth/**", "/css/**", "/js/**").permitAll()

                        // 3. 관리자 전용 경로
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/api/auth/login") // 로그인 처리 URL 명시
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecretKey")
                        .rememberMeParameter("remember-Me")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                        .userDetailsService(customUserDetailsService) // UserDetailsService 명확히 지정
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                );

        return http.build();
    }
}
