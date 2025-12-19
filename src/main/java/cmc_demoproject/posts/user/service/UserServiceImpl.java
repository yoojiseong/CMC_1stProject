package cmc_demoproject.posts.user.service;

import cmc_demoproject.posts.user.dto.RegistMemberDTO;
import cmc_demoproject.posts.user.entity.Users;
import cmc_demoproject.posts.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegistMemberDTO dto){
        if(usersRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Users user = Users.builder()
                .email(dto.getEmail())
                .userName(dto.getUserName())
                .password(encodedPassword)
                .role("USER")
                .build();
        usersRepository.save(user);
    }
}
