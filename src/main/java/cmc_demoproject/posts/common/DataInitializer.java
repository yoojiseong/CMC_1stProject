package cmc_demoproject.posts.common;

import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Log4j2
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 관리자 계정이 이미 존재하는지 확인
        if (!usersRepository.existsByMemberId("admin@naver.com")) {
            String encodedPassword = passwordEncoder.encode("admin1234");
            Users user = Users.builder()
                    .email("admin@naver.com")
                    .password(encodedPassword)
                    .userName("관리자")
                    .role("ADMIN") // 기본 권한 설정. ADMIN이 필요하면 분기 처리
                    .build();

            usersRepository.save(user);
            log.info("관리자 계정 생성 완료: admin / admin1234");
        }

        // 일반 사용자 계정이 이미 존재하는지 확인
//        if (!memberRepository.existsByMemberId("user")) {
//            Member user = Member.builder()
//                    .memberId("user")
//                    .password(passwordEncoder.encode("1234"))
//                    .email("user01@example.com")
//                    .userName("일반회원")
//                    .phone("010-1111-1111")
//                    .birthDate(LocalDate.of(1995, 5, 15))
//                    .createdAt(LocalDateTime.now())
//                    .role("ROLE_USER") // 일반 회원 권한
//                    .build();
//
//            memberRepository.save(user);
//            log.info("일반 사용자 계정 생성 완료: user / 1234");
//        }
    }
}